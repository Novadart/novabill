package com.novadart.novabill.service.web;

import com.novadart.novabill.domain.*;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.DTOUtils.Predicate;
import com.novadart.novabill.domain.dto.transformer.AccountingDocumentItemDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.InvoiceDTOTransformer;
import com.novadart.novabill.report.DocumentType;
import com.novadart.novabill.service.PDFStorageService;
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.mail.EmailBuilder;
import com.novadart.novabill.service.mail.MailHandlingType;
import com.novadart.novabill.service.validator.AccountingDocumentValidator;
import com.novadart.novabill.service.validator.Groups.HeavyClient;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.data.FilteringDateType;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.MailDeliveryStatus;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.web.mvc.ajax.dto.EmailDTO;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import static com.novadart.novabill.service.mail.mailgun.InvoiceMailAcknowledgeHandlerService.*;

@Service
public class InvoiceServiceImpl implements InvoiceService {

	private static final String EMAIL_TEMPLATE_LOCATION = "mail-templates/invoice-notification.vm";
	
	@Value("${invoice.pdf.url}")
	private String invoicePdfUrl;
	
	@Value("${invoice.pdf.sender}")
	private String from;
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private AccountingDocumentValidator validator;
	
	@Autowired
	private SimpleValidator simpleValidator;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private TransportDocumentService transportDocService;
	
	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private PDFStorageService pdfStorageService;
	
	@Override
	@PreAuthorize("T(com.novadart.novabill.domain.Invoice).findInvoice(#id)?.business?.id == principal.business.id")
	public InvoiceDTO get(Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException {
		Invoice invoice = Invoice.findInvoice(id);
		if(invoice == null)
			throw new NoSuchElementException();
		return InvoiceDTOTransformer.toDTO(invoice, true);
	}

	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("#businessID == principal.business.id")
	public PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException {
		List<InvoiceDTO> allInvoices = businessService.getInvoices(businessID, year);
		return new PageDTO<InvoiceDTO>(DTOUtils.range(allInvoices, start, length), start, length, new Long(allInvoices.size()));
	}

	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("#businessID == principal.business.id")
	public PageDTO<InvoiceDTO> getAllInRange(Long businessID, Integer year, String docIDSuffix, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException {
		List<InvoiceDTO> allInvoices = businessService.getInvoices(businessID, year, docIDSuffix);
		return new PageDTO<InvoiceDTO>(DTOUtils.range(allInvoices, start, length), start, length, new Long(allInvoices.size()));
	}

	private static class EqualsClientIDPredicate implements Predicate<InvoiceDTO>{
		
		private Long id;
		
		public EqualsClientIDPredicate(Long id) {
			this.id = id;
		}
		
		@Override
		public boolean isTrue(InvoiceDTO doc) {
			return doc.getClient().getId().equals(id);
		}
		
	}
	
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public List<InvoiceDTO> getAllForClient(Long clientID, Integer year) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException {
		return new ArrayList<InvoiceDTO>(DTOUtils.filter(businessService.getInvoices(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(), year), new EqualsClientIDPredicate(clientID)));
	}

	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public List<InvoiceDTO> getAllForClient(Long clientID, Integer year, String docIDSuffix) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException {
		return new ArrayList<InvoiceDTO>(DTOUtils.filter(businessService.getInvoices(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(), year, docIDSuffix), new EqualsClientIDPredicate(clientID)));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {Exception.class})
	@PreAuthorize("#businessID == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.Invoice).findInvoice(#id)?.business?.id == #businessID and " +
		  	  	  "T(com.novadart.novabill.domain.Invoice).findInvoice(#id)?.client?.id == #clientID")
	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, DataIntegrityException {
		Invoice invoice = Invoice.findInvoice(id);
		invoice.getTransportDocuments().size(); //force fetching all docs at once
		for(TransportDocument transDoc: new ArrayList<>(invoice.getTransportDocuments()))
			transportDocService.clearInvoice(businessID, transDoc.getId());
		invoice.remove(); //removing invoice
		if(Hibernate.isInitialized(invoice.getBusiness().getInvoices()))
			invoice.getBusiness().getInvoices().remove(invoice);
		if(Hibernate.isInitialized(invoice.getClient().getInvoices()))
			invoice.getClient().getInvoices().remove(invoice);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {Exception.class})
	//@Restrictions(checkers = {NumberOfInvoicesPerYearQuotaReachedChecker.class})
	@PreAuthorize("#invoiceDTO?.business?.id == principal.business.id and " +
		  	  	 "T(com.novadart.novabill.domain.Client).findClient(#invoiceDTO?.client?.id)?.business?.id == principal.business.id and " +
		  	  	 "#invoiceDTO != null and #invoiceDTO.id == null")
	public Long add(InvoiceDTO invoiceDTO) throws DataAccessException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataIntegrityException {
		Invoice invoice = new Invoice();//create new invoice
		InvoiceDTOTransformer.copyFromDTO(invoice, invoiceDTO, true);
		validator.validate(Invoice.class, invoice);
		Client client = Client.findClient(invoiceDTO.getClient().getId());
		simpleValidator.validate(client, HeavyClient.class);
		Business business = Business.findBusiness(invoiceDTO.getBusiness().getId());
		invoice.setClient(client);
		client.getInvoices().add(invoice);
		invoice.setBusiness(business);
		business.getInvoices().add(invoice);
		String docPath = pdfStorageService.generateAndStorePdfForAccountingDocument(invoice, DocumentType.INVOICE);
		invoice.setDocumentPath(docPath);
		invoice.persist();
		invoice.flush();
		Long businessID = invoiceDTO.getBusiness().getId();
		if(invoiceDTO.getTransportDocumentIDs() != null){
			for(Long transDocID: invoiceDTO.getTransportDocumentIDs())
				transportDocService.setInvoice(businessID, invoice.getId(), transDocID);
		}
		return invoice.getId();
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#invoiceDTO?.business?.id == principal.business.id and " +
	  	  	  	 "T(com.novadart.novabill.domain.Client).findClient(#invoiceDTO?.client?.id)?.business?.id == principal.business.id and " +
	  	  	  	 "#invoiceDTO?.id != null")
	public void update(InvoiceDTO invoiceDTO) throws DataAccessException, NoSuchObjectException, ValidationException, DataIntegrityException {
		Invoice persistedInvoice = Invoice.findInvoice(invoiceDTO.getId());
		if(persistedInvoice == null)
			throw new NoSuchObjectException();
		if (invoiceDTO.isCreatedFromTransportDocuments()) {
			if (!DTOUtils.compareItems(invoiceDTO, InvoiceDTOTransformer.toDTO(persistedInvoice, true), false))
				throw new DataIntegrityException();
		}
		InvoiceDTOTransformer.copyFromDTO(persistedInvoice, invoiceDTO, false);
		if (!invoiceDTO.isCreatedFromTransportDocuments()) {
			persistedInvoice.getAccountingDocumentItems().clear();
			for (AccountingDocumentItemDTO itemDTO : invoiceDTO.getItems()) {
				AccountingDocumentItem item = new AccountingDocumentItem();
				AccountingDocumentItemDTOTransformer.copyFromDTO(item, itemDTO);
				item.setAccountingDocument(persistedInvoice);
				persistedInvoice.getAccountingDocumentItems().add(item);
			}
		}
		validator.validate(Invoice.class, persistedInvoice);
		String docPath = pdfStorageService.generateAndStorePdfForAccountingDocument(persistedInvoice, DocumentType.INVOICE);
		persistedInvoice.setDocumentPath(docPath);
	}
	
	@Override
	public Long getNextInvoiceDocumentID(String suffix) {
		return utilsService.getAuthenticatedPrincipalDetails().getBusiness().getNextInvoiceDocumentID(suffix);
	}

	@Override
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public PageDTO<InvoiceDTO> getAllForClientInRange(Long clientID, Integer year, Integer start, Integer length) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException {
		List<InvoiceDTO> allInvoices = getAllForClient(clientID, year);
		return new PageDTO<InvoiceDTO>(DTOUtils.range(allInvoices, start, length), start, length, new Long(allInvoices.size()));
	}
	
	@Override
	@Transactional(readOnly = false)
	//@Restrictions(checkers = {PremiumChecker.class})
	@PreAuthorize("principal.business.id == #businessID and " +
	  	  	  	  "T(com.novadart.novabill.domain.Invoice).findInvoice(#id)?.business?.id == #businessID and " +
	  	  	  	  "T(com.novadart.novabill.domain.Invoice).findInvoice(#id)?.client?.id == #clientID")
	public void setPayed(Long businessID, Long clientID, Long id, Boolean value) throws NotAuthenticatedException, FreeUserAccessForbiddenException, DataAccessException {
		Invoice.findInvoice(id).setPayed(value);
	}
	
	@Override
	//@Restrictions(checkers = {PremiumChecker.class})
	public List<InvoiceDTO> getAllUnpaidInDateRange(FilteringDateType filteringDateType, Date startDate, Date endDate) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Business business = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId());
		List<Invoice> invoices = business.getAllUnpaidInvoicesInDateRange(filteringDateType, startDate, endDate);
		List<InvoiceDTO> invoiceDTOs = new ArrayList<>(invoices.size());
		for(Invoice inv: invoices)
			invoiceDTOs.add(InvoiceDTOTransformer.toDTO(inv, false));
		return invoiceDTOs;
	}
	
	@Override
	@PreAuthorize("principal.business.id == #businessID and " +
			      "T(com.novadart.novabill.domain.Invoice).findInvoice(#id)?.business?.id == #businessID")
	public boolean email(Long businessID, Long id, EmailDTO emailDTO) throws NoSuchAlgorithmException, UnsupportedEncodingException, ValidationException {
		simpleValidator.validate(emailDTO);
		String token = tokenGenerator.generateToken();
		String url = String.format(invoicePdfUrl, id, URLEncoder.encode(token, "UTF-8"));
		Business business = Business.findBusiness(businessID);
		String from = String.format("%s via %s", business.getName(), this.from);
		return new EmailBuilder().to(emailDTO.getTo())
				.from(from)
				.replyTo(emailDTO.getReplyTo())
				.subject(emailDTO.getSubject())
				.template(EMAIL_TEMPLATE_LOCATION)
				.templateVar("message", emailDTO.getMessage().replaceAll("\n", "<br>"))
				.templateVar("invoiceUrl", url)
				.templateVar("businessReplyTo", emailDTO.getReplyTo())
				.variable(BUSINESS_ID, String.valueOf(businessID))
				.variable(INVOICE_ID, String.valueOf(id))
				.handlingType(MailHandlingType.EXTERNAL_ACKNOWLEDGED)
				.build().send(
					messageId->{ //On success
						Invoice invoice = Invoice.findInvoice(id);
						invoice.setEmailedToClient(MailDeliveryStatus.SENT);
						invoice.merge();
						new DocumentAccessToken(id, token).persist();
					},
					throwable->{});
	}

	@Override
	public void markViewedByClient(Long businessID, Long id, Long viewingTime) {
		Invoice invoice = Invoice.findInvoice(id);
		invoice.setSeenByClientTime(System.currentTimeMillis());
		invoice.setEmailedToClient(MailDeliveryStatus.READ);
		invoice.merge();
	}

	@Override
	public void setEmailedToClientStatus(Long businessID, Long id, MailDeliveryStatus status){
		Invoice invoice = Invoice.findInvoice(id);
		invoice.setEmailedToClient(status);
		invoice.merge();
	}
	
}
