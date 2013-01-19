package com.novadart.novabill.web.gwt;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.XsrfTokenService;
import com.novadart.novabill.service.validator.TaxableEntityValidator;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.web.mvc.ExportController;
import com.novadart.novabill.web.mvc.PDFController;

public abstract class BusinessServiceImpl implements BusinessService {

	@Autowired
	private TaxableEntityValidator validator;
	
	@Autowired
	private XsrfTokenService xsrfTokenService;
	
	@Autowired
	private UtilsService utilsService;
	
	protected abstract BusinessService self();
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public BusinessStatsDTO getStats(Long businessID) throws NotAuthenticatedException, DataAccessException {
		BusinessStatsDTO stats = new BusinessStatsDTO();
		stats.setClientsCount(countClients(businessID));
		int year = Calendar.getInstance().get(Calendar.YEAR);
		stats.setInvoicesCountForYear(countInvoicesForYear(businessID, year));
		stats.setTotalAfterTaxesForYear(getTotalAfterTaxesForYear(businessID, year));
		return stats;
	}
	
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("#businessID == principal.business.id")
	public Long countClients(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return new Long(self().getClients(businessID).size());
	}
	
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("#businessID == principal.business.id")
	public Long countInvoices(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return new Long(self().getInvoices(businessID).size());
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public Long countInvoicesForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		return new Long(DTOUtils.filter(self().getInvoices(businessID), new DTOUtils.EqualsYearPredicate<InvoiceDTO>(year)).size());
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public BigDecimal getTotalAfterTaxesForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		BigDecimal totalAfterTaxes = new BigDecimal("0.0");
		for(InvoiceDTO invoiceDTO: DTOUtils.filter(self().getInvoices(businessID), new DTOUtils.EqualsYearPredicate<InvoiceDTO>(year)))
			totalAfterTaxes = totalAfterTaxes.add(invoiceDTO.getTotal());
		return totalAfterTaxes.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#businessDTO?.id == principal.business.id")
	public void update(BusinessDTO businessDTO) throws DataAccessException, NoSuchObjectException, ValidationException {
		Business business = Business.findBusiness(businessDTO.getId());
		BusinessDTOFactory.copyFromDTO(business, businessDTO);
		validator.validate(business);
	}
	
	private String generateToken(String tokenSessionField) throws NoSuchAlgorithmException{
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return xsrfTokenService.generateToken(attr.getRequest().getSession(), tokenSessionField);
	}
	
	@Override
	public String generatePDFToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException {
		return URLEncoder.encode(generateToken(PDFController.TOKENS_SESSION_FIELD), "UTF-8");
	}

	@Override
	public String generateExportToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException {
		return URLEncoder.encode(generateToken(ExportController.TOKENS_SESSION_FIELD), "UTF-8");
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<InvoiceDTO> getInvoices(Long businessID){
		return DTOUtils.toDTOList(AccountingDocument.sortAccountingDocuments(Business.findBusiness(businessID).fetchInvoicesEagerly()), DTOUtils.invoiceDTOConverter);
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<CreditNoteDTO> getCreditNotes(Long businessID) throws NotAuthenticatedException {
		return DTOUtils.toDTOList(AccountingDocument.sortAccountingDocuments(Business.findBusiness(businessID).fetchCreditNotesEagerly()), DTOUtils.creditNoteDTOConverter);
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<EstimationDTO> getEstimations(Long businessID) throws NotAuthenticatedException {
		return DTOUtils.toDTOList(AccountingDocument.sortAccountingDocuments(Business.findBusiness(businessID).fetchEstimationsEagerly()), DTOUtils.estimationDTOConverter);
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<TransportDocumentDTO> getTransportDocuments(Long businessID) throws NotAuthenticatedException {
		return DTOUtils.toDTOList(AccountingDocument.sortAccountingDocuments(Business.findBusiness(businessID).fetchTransportDocumentsEagerly()), DTOUtils.transportDocDTOConverter);
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<ClientDTO> getClients(Long businessID){
		Set<Client> clients = Business.findBusiness(businessID).getClients();
		List<ClientDTO> clientDTOs = new ArrayList<ClientDTO>(clients.size());
		for(Client client: clients)
			clientDTOs.add(ClientDTOFactory.toDTO(client));
		return clientDTOs;
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public BusinessDTO get(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return BusinessDTOFactory.toDTO(Business.findBusiness(businessID));
	}

	@Override
	@Transactional(readOnly = false)
	public Long updateNotesBitMask(Long notesBitMask) throws NotAuthenticatedException, DataAccessException {
		Principal authenticatedPrincipal = Principal.findPrincipal(utilsService.getAuthenticatedPrincipalDetails().getId());
		authenticatedPrincipal.setNotesBitMask(notesBitMask);
		return authenticatedPrincipal.merge().getNotesBitMask();
	}

}
