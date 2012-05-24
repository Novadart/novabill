package com.novadart.novabill.web.gwt;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.annotation.CheckQuotas;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.EstimationDTOFactory;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.InvoiceDTOFactory;
import com.novadart.novabill.domain.InvoiceItem;
import com.novadart.novabill.domain.InvoiceItemDTOFactory;
import com.novadart.novabill.quota.NumberOfInvoicesPerYearQuotaReachedChecker;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.InvoiceItemDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.InvalidDocumentIDException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.QuotaException;
import com.novadart.novabill.shared.client.facade.InvoiceService;

@SuppressWarnings("serial")
public class InvoiceServiceImpl extends AbstractGwtController<InvoiceService, InvoiceServiceImpl> implements InvoiceService {

	@Autowired
	private UtilsService utilsService;

	public InvoiceServiceImpl() {
		super(InvoiceService.class);
	}
	
	@Override
	public InvoiceDTO get(long id) throws DataAccessException, NoSuchObjectException {
		Invoice invoice = Invoice.findInvoice(id);
		if(invoice == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(invoice.getBusiness().getId()))
			throw new DataAccessException();
		return InvoiceDTOFactory.toDTO(invoice);
	}

	@Override
	@Transactional(readOnly = true)
	public List<InvoiceDTO> getAllInRange(int start, int length) {
		List<Invoice> invoices = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId()).getAllInvoicesInRange(start, length); 
		List<InvoiceDTO> invoiceDTOs = new ArrayList<InvoiceDTO>(invoices.size());
		for(Invoice invoice: invoices)
			invoiceDTOs.add(InvoiceDTOFactory.toDTO(invoice));
		return invoiceDTOs;
	}
	
	private List<Long> computeDocumentIDGaps(List<Long> invoiceIDs, int max){
		int size = invoiceIDs.size(); 
		if(size == 0)
			return new ArrayList<Long>();
		BitSet invoiceIDsBSet = new BitSet(invoiceIDs.get(size - 1).intValue() - 1);
		for(Long invoiceID: invoiceIDs)
			invoiceIDsBSet.flip(invoiceID.intValue() - 1);
		invoiceIDsBSet.flip(0, invoiceIDs.get(0).intValue() - 1);//flip all bits till the first invoice
		BitSet mask = new BitSet(invoiceIDsBSet.length());
		mask.flip(0, invoiceIDsBSet.length());
		invoiceIDsBSet.xor(mask);
		List<Long> gaps = new ArrayList<Long>(Math.min(invoiceIDsBSet.cardinality(), max));
		for(int i=invoiceIDsBSet.nextSetBit(0), c = 0; i >= 0 && c < max; i = invoiceIDsBSet.nextSetBit(i+1), c++)
			gaps.add(new Long(i + 1));
		return gaps;
	}

	private Long suggestInvoiceDocumentID(Business business){
		List<Long> invoiceIDs = business.getCurrentYearInvoicesDocumentIDs();
		if(invoiceIDs.size() == 0) return 1l;
		if(invoiceIDs.get(0) > 1) return 1l;
		return computeDocumentIDGaps(invoiceIDs, 1).get(0);
	}
	
	private void checkInvoiceDocumentID(Business business, Long id, Long documentID) throws InvalidDocumentIDException {
		if(documentID == null) return;
		if(id == null){
			if(business.getInvoiceByIdInYear(documentID, Calendar.getInstance().get(Calendar.YEAR)) != null)
				throw new InvalidDocumentIDException(suggestInvoiceDocumentID(business));
		}else{
			Invoice invoice = business.getInvoiceByIdInYear(documentID, Calendar.getInstance().get(Calendar.YEAR));
			if(invoice != null && !invoice.getId().equals(id))
				throw new InvalidDocumentIDException(suggestInvoiceDocumentID(business));
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<InvoiceDTO> getAllForClient(long id) throws DataAccessException, NoSuchObjectException {
		Client client = Client.findClient(id);
		if(client == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		List<Invoice> invoices = client.getSortedInvoices();
		List<InvoiceDTO> invoiceDTOs = new ArrayList<InvoiceDTO>(invoices.size());
		for(Invoice invoice: invoices)
			invoiceDTOs.add(InvoiceDTOFactory.toDTO(invoice));
		return invoiceDTOs;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void remove(Long id) throws DataAccessException, NoSuchObjectException {
		Invoice invoice = Invoice.findInvoice(id);
		if(invoice == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(invoice.getBusiness().getId()))
			throw new DataAccessException();
		invoice.remove(); //removing invoice
		if(Hibernate.isInitialized(invoice.getBusiness().getInvoices()))
			invoice.getBusiness().getInvoices().remove(invoice);
		if(Hibernate.isInitialized(invoice.getClient().getInvoices()))
			invoice.getClient().getInvoices().remove(invoice);
	}

	@Override
	@Transactional(readOnly = false)
	@CheckQuotas(checkers = {NumberOfInvoicesPerYearQuotaReachedChecker.class})
	public Long add(InvoiceDTO invoiceDTO) throws DataAccessException, InvalidDocumentIDException, QuotaException {
		Client client = Client.findClient(invoiceDTO.getClient().getId());;
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		Business business = Business.findBusiness(invoiceDTO.getBusiness().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(business.getId()))
			throw new DataAccessException();
		checkInvoiceDocumentID(business, invoiceDTO.getId(), invoiceDTO.getDocumentID());
		Invoice invoice = new Invoice();//create new invoice
		invoice.setClient(client);
		client.getInvoices().add(invoice);
		invoice.setBusiness(business);
		business.getInvoices().add(invoice);
		InvoiceDTOFactory.copyFromDTO(invoice, invoiceDTO, true);
		invoice.persist();
		invoice.flush();
		return invoice.getId();
	}

	@Override
	@Transactional(readOnly = false)
	public void update(InvoiceDTO invoiceDTO) throws DataAccessException, NoSuchObjectException, InvalidDocumentIDException {
		if(invoiceDTO.getId() == null)
			throw new DataAccessException();
		Client client = Client.findClient(invoiceDTO.getClient().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		Business business = Business.findBusiness(invoiceDTO.getBusiness().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(business.getId()))
			throw new DataAccessException();
		checkInvoiceDocumentID(business, invoiceDTO.getId(), invoiceDTO.getDocumentID());
		Invoice persistedInvoice = Invoice.findInvoice(invoiceDTO.getId());
		if(persistedInvoice == null)
			throw new NoSuchObjectException();
		InvoiceDTOFactory.copyFromDTO(persistedInvoice, invoiceDTO, false);
		persistedInvoice.getInvoiceItems().clear();
		for(InvoiceItemDTO invoiceItemDTO: invoiceDTO.getItems()){
			InvoiceItem invoiceItem = new InvoiceItem();
			InvoiceItemDTOFactory.copyFromDTO(invoiceItem, invoiceItemDTO);
			invoiceItem.setInvoice(persistedInvoice);
			persistedInvoice.getInvoiceItems().add(invoiceItem);
		}
	}
	
	@Override
	public Long getNextInvoiceDocumentID() {
		return utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getNextInvoiceDocumentID();
	}

	@Override
	public List<InvoiceDTO> getAllForClientInRange(long id, int start, int length) throws DataAccessException, NoSuchObjectException {
		Client client = Client.findClient(id);
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		List<Invoice> invoices = client.getAllInvoicesInRange(start, length);
		List<InvoiceDTO> invoiceDTOs = new ArrayList<InvoiceDTO>(invoices.size());
		for(Invoice invoice: invoices)
			invoiceDTOs.add(InvoiceDTOFactory.toDTO(invoice));
		return invoiceDTOs;
	}
	
	@Override
	@Transactional(readOnly = false)
	@CheckQuotas(checkers = {NumberOfInvoicesPerYearQuotaReachedChecker.class})
	public InvoiceDTO createFromEstimation(EstimationDTO estimationDTO) throws NotAuthenticatedException, DataAccessException, InvalidDocumentIDException, NoSuchObjectException, ConcurrentAccessException, QuotaException {
		if(estimationDTO.getId() != null){//present in DB
			Estimation estimation = Estimation.findEstimation(estimationDTO.getId());
			if(estimation == null)
				throw new NoSuchObjectException();
			estimation.remove();
		}
		InvoiceDTO invoiceDTO = EstimationDTOFactory.toInvoiceDTO(estimationDTO);
		invoiceDTO.setDocumentID(getNextInvoiceDocumentID());
		Long id = add(invoiceDTO);
		invoiceDTO.setId(id);
		return invoiceDTO;
	}

	@Override
	public List<Long> validateInvoiceDocumentID(Long documentID) throws NotAuthenticatedException, ConcurrentAccessException{
		List<Long> invoiceIDs = utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getCurrentYearInvoicesDocumentIDs();
		if(invoiceIDs.size() == 0)//first invoice
			return new ArrayList<Long>();
		invoiceIDs.add(documentID);
		return computeDocumentIDGaps(invoiceIDs, 10);
	}
	
}
