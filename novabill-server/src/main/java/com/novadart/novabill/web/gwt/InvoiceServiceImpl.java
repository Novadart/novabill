package com.novadart.novabill.web.gwt;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.factory.AccountingDocumentItemDTOFactory;
import com.novadart.novabill.domain.dto.factory.InvoiceDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.InvoiceValidator;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.InvoiceService;

@SuppressWarnings("serial")
public class InvoiceServiceImpl extends AbstractGwtController<InvoiceService, InvoiceServiceImpl> implements InvoiceService {

	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private InvoiceValidator validator;
	
	public InvoiceServiceImpl() {
		super(InvoiceService.class);
	}
	
	@Override
	@PreAuthorize("T(com.novadart.novabill.domain.Invoice).findInvoice(#id)?.business?.id == principal.business.id")
	public InvoiceDTO get(Long id) throws DataAccessException, NoSuchObjectException {
		return InvoiceDTOFactory.toDTO(Invoice.findInvoice(id));
	}

	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("#businessID == principal.business.id")
	public PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer start, Integer length) {
		List<Invoice> invoices = Business.findBusiness(businessID).getAllInvoicesInRange(start, length); 
		List<InvoiceDTO> invoiceDTOs = new ArrayList<InvoiceDTO>(invoices.size());
		for(Invoice invoice: invoices)
			invoiceDTOs.add(InvoiceDTOFactory.toDTO(invoice));
		return new PageDTO<InvoiceDTO>(invoiceDTOs, start, length, Invoice.countInvoices());
	}
	
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public List<InvoiceDTO> getAllForClient(Long clientID) throws DataAccessException, NoSuchObjectException {
		List<Invoice> invoices = Client.findClient(clientID).getSortedInvoices();
		List<InvoiceDTO> invoiceDTOs = new ArrayList<InvoiceDTO>(invoices.size());
		for(Invoice invoice: invoices)
			invoiceDTOs.add(InvoiceDTOFactory.toDTO(invoice));
		return invoiceDTOs;
	}
	
	@Override
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.Invoice).findInvoice(#id)?.business?.id == #businessID and " +
		  	  	  "T(com.novadart.novabill.domain.Invoice).findInvoice(#id)?.client?.id == #clientID")
	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NoSuchObjectException {
		Invoice invoice = Invoice.findInvoice(id);
		invoice.remove(); //removing invoice
		if(Hibernate.isInitialized(invoice.getBusiness().getInvoices()))
			invoice.getBusiness().getInvoices().remove(invoice);
		if(Hibernate.isInitialized(invoice.getClient().getInvoices()))
			invoice.getClient().getInvoices().remove(invoice);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	//@Restrictions(checkers = {NumberOfInvoicesPerYearQuotaReachedChecker.class})
	@PreAuthorize("#invoiceDTO?.business?.id == principal.business.id and " +
		  	  	 "T(com.novadart.novabill.domain.Client).findClient(#invoiceDTO?.client?.id)?.business?.id == principal.business.id and " +
		  	  	 "#invoiceDTO != null and #invoiceDTO.id == null")
	public Long add(InvoiceDTO invoiceDTO) throws DataAccessException, ValidationException, AuthorizationException {
		Invoice invoice = new Invoice();//create new invoice
		InvoiceDTOFactory.copyFromDTO(invoice, invoiceDTO, true);
		validator.validate(invoice);
		Client client = Client.findClient(invoiceDTO.getClient().getId());
		Business business = Business.findBusiness(invoiceDTO.getBusiness().getId());
		invoice.setClient(client);
		client.getInvoices().add(invoice);
		invoice.setBusiness(business);
		business.getInvoices().add(invoice);
		invoice.flush();
		return invoice.getId();
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#invoiceDTO?.business?.id == principal.business.id and " +
	  	  	  	 "T(com.novadart.novabill.domain.Client).findClient(#invoiceDTO?.client?.id)?.business?.id == principal.business.id and " +
	  	  	  	 "#invoiceDTO?.id != null")
	public void update(InvoiceDTO invoiceDTO) throws DataAccessException, NoSuchObjectException, ValidationException {
		Invoice persistedInvoice = Invoice.findInvoice(invoiceDTO.getId());
		if(persistedInvoice == null)
			throw new NoSuchObjectException();
		InvoiceDTOFactory.copyFromDTO(persistedInvoice, invoiceDTO, false);
		persistedInvoice.getAccountingDocumentItems().clear();
		for(AccountingDocumentItemDTO itemDTO: invoiceDTO.getItems()){
			AccountingDocumentItem item = new AccountingDocumentItem();
			AccountingDocumentItemDTOFactory.copyFromDTO(item, itemDTO);
			item.setAccountingDocument(persistedInvoice);
			persistedInvoice.getAccountingDocumentItems().add(item);
		}
		validator.validate(persistedInvoice);
	}
	
	@Override
	public Long getNextInvoiceDocumentID() {
		return utilsService.getAuthenticatedPrincipalDetails().getBusiness().getNextInvoiceDocumentID();
	}

	@Override
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public PageDTO<InvoiceDTO> getAllForClientInRange(Long clientID, Integer start, Integer length) throws DataAccessException, NoSuchObjectException {
		List<Invoice> invoices = Client.findClient(clientID).getAllInvoicesInRange(start, length);
		List<InvoiceDTO> invoiceDTOs = new ArrayList<InvoiceDTO>(invoices.size());
		for(Invoice invoice: invoices)
			invoiceDTOs.add(InvoiceDTOFactory.toDTO(invoice));
		return new PageDTO<InvoiceDTO>(invoiceDTOs, start, length, Invoice.countInvocesForClient(clientID));
	}
	
	@Override
	@Transactional(readOnly = false)
	//@Restrictions(checkers = {PremiumChecker.class})
	@PreAuthorize("principal.business.id == #businessID and " +
	  	  	  	  "T(com.novadart.novabill.domain.Invoice).findInvoice(#id)?.business?.id == #businessID and " +
	  	  	  	  "T(com.novadart.novabill.domain.Invoice).findInvoice(#id)?.client?.id == #clientID")
	public void setPayed(Long businessID, Long clientID, Long id, Boolean value) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException, AuthorizationException {
		Invoice.findInvoice(id).setPayed(value);
	}
	
}
