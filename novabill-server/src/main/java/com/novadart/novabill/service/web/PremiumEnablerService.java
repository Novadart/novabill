package com.novadart.novabill.service.web;

import com.novadart.novabill.domain.*;
import com.novadart.novabill.domain.dto.transformer.BusinessDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.ClientDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.InvoiceDTOTransformer;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.paypal.PaymentPlanDescriptor;
import com.novadart.novabill.paypal.PaymentPlansLoader;
import com.novadart.novabill.service.PrincipalDetailsService;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.mail.EmailBuilder;
import com.novadart.novabill.service.mail.MailHandlingType;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.NotificationType;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentDeltaType;
import com.novadart.novabill.shared.client.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static com.novadart.novabill.service.PDFStorageService.pdfFileToByteArray;

@Service
public class PremiumEnablerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PremiumEnablerService.class);

	@Value("${novadart.username}")
	private String novadartUsername; 
	
	@Autowired
	private PrincipalDetailsService principalDetailsService;
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private PaymentPlansLoader paymentPlansLoader;
	
	@Autowired
	private SessionRegistry sessionRegistry;
	
	private void expireSessions(String username){
		Object principal = null;
		for(Object p: sessionRegistry.getAllPrincipals()){
			UserDetails ud = (UserDetails)p;
			if(ud.getUsername().equals(username))
				principal = p;
		}
		if(principal != null){
			for(SessionInformation sInfo: sessionRegistry.getAllSessions(principal, false))
				sInfo.expireNow();
				
		}
			
	}
	
	private void makePremium(Business business){
		for(Principal principal: business.getPrincipals()){
			if(!principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM)){
				principal.getGrantedRoles().clear();
				principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_PREMIUM);
			}
		}
	}
	

	private void createNotification(Business business, NotificationType type){
		Notification notification = new Notification();
		notification.setType(type);
		notification.setBusiness(business);
		business.getNotifications().add(notification);
	}
	
	private void extendNonFreeAccountExpirationTime(Business business, int numberOfMonths) {
		Long current = System.currentTimeMillis(), nonFreeAccountExpirationTime = business.getSettings().getNonFreeAccountExpirationTime();
		Long zero = nonFreeAccountExpirationTime == null || nonFreeAccountExpirationTime < current? current: nonFreeAccountExpirationTime;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(zero);
		calendar.add(Calendar.MONTH, numberOfMonths);
		business.getSettings().setNonFreeAccountExpirationTime(calendar.getTimeInMillis());
		createNotification(business, zero == nonFreeAccountExpirationTime? NotificationType.PREMIUM_EXTENSION: NotificationType.PREMIUM_UPGRADE);
	}
	
	public void enablePremiumForNMonths(Business business, int numberOfMonths) throws PremiumUpgradeException {
		try {
			makePremium(business);
			extendNonFreeAccountExpirationTime(business, numberOfMonths);
		} catch (Exception e) {
			throw new PremiumUpgradeException(e);
		}
	}

	private void setSecurityContext(){
		UserDetails userDetails = principalDetailsService.loadUserByUsername(novadartUsername);
		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	private void clearSecurityContext(){
		SecurityContextHolder.getContext().setAuthentication(null);
	}
	
	private void copyDataFromBusinessToClientDTO(Business business, Client client){
		client.setName(business.getName());
		client.setAddress(business.getAddress());
		client.setCity(business.getCity());
		client.setPostcode(business.getPostcode());
		client.setProvince(business.getProvince());
		client.setCountry(business.getCountry());
		client.setVatID(business.getVatID());
		client.setSsn(business.getSsn());
	}
	
	private Long addOrUpdateClient(Business novadartBusiness, Business clientBusiness, String email) throws FreeUserAccessForbiddenException, ValidationException, NoSuchObjectException, NotAuthenticatedException, DataAccessException{
		String vatIDOrSsn = clientBusiness.getVatID() == null? clientBusiness.getSsn(): clientBusiness.getVatID();
		Client client = novadartBusiness.findClientByVatIDOrSsn(vatIDOrSsn);
		if(client == null) { //not a client add it
			client = new Client();
			copyDataFromBusinessToClientDTO(clientBusiness, client);
			client.setEmail(email);
			return clientService.add(novadartBusiness.getId(), ClientDTOTransformer.toDTO(client));
		} else { // already client - update data
			copyDataFromBusinessToClientDTO(clientBusiness, client);
			client.setEmail(email);
			clientService.update(novadartBusiness.getId(), ClientDTOTransformer.toDTO(client));
			Client.entityManager().flush();
			return client.getId();
		}
	}
	
	private Long createInvoice(Business novadartBusiness, Long clientID, String paymentPlan) throws DataAccessException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataIntegrityException{
		Invoice invoice = new Invoice();
		invoice.setDocumentID(invoiceService.getNextInvoiceDocumentID(null));
		Date now = new Date();
		invoice.setAccountingDocumentDate(now);
		invoice.setPaymentTypeName("Rimessa Diretta");
		invoice.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
		invoice.setPaymentDeltaType(PaymentDeltaType.DAYS);
		invoice.setPaymentDueDate(now);
		invoice.setPayed(true);
		invoice.setPaymentNote("Pagato");
		invoice.setLayoutType(LayoutType.TIDY);
		
		PaymentPlanDescriptor planDesc = paymentPlansLoader.getPayPalPaymentPlanDescriptor(paymentPlan);
		AccountingDocumentItem item = new AccountingDocumentItem();
		item.setPrice(planDesc.getTotalWithoutTax());
		item.setUnitOfMeasure("");
		item.setQuantity(new BigDecimal("1.00"));
		item.setDiscount(planDesc.getDiscount());
		item.setDescription(planDesc.getItemName());
		item.setTax(planDesc.getTax());
		item.setTotalBeforeTax(planDesc.getTotalWithoutTax());
		item.setTotalTax(planDesc.getTotalTax());
		item.setTotal(planDesc.getTotalAfterTax());
		invoice.getAccountingDocumentItems().add(item);
		
		Client client = Client.findClient(clientID);
		
		invoice.setTotalBeforeTax(planDesc.getTotalWithoutTax());
		invoice.setTotal(planDesc.getTotalAfterTax());
		invoice.setTotalTax(planDesc.getTotalTax());
		invoice.setToEndpoint(new Endpoint().copy(client));
		
		InvoiceDTO invoiceDTO = InvoiceDTOTransformer.toDTO(invoice, true);
		invoiceDTO.setBusiness(BusinessDTOTransformer.toDTO(novadartBusiness));
		invoiceDTO.setClient(ClientDTOTransformer.toDTO(client));

		return invoiceService.add(invoiceDTO);
	}
	
	private void exportAndEmailInvoicePdf(Long invoiceID, Long businessID, String email) throws IOException {
		byte[] pdfBytes = pdfFileToByteArray(Invoice.findInvoice(invoiceID).getDocumentPath());
		new EmailBuilder().to(email)
				.subject("Conferma attivazione Novabill Premium")
				.template("mail-templates/upgrade-notification.vm")
				.attachment(pdfBytes)
				.attachmentName("Fattura.pdf")
				.handlingType(MailHandlingType.EXTERNAL_UNACKNOWLEDGED)
				.build().send();
	}
	
	/* (non-Javadoc)
	 * @see com.novadart.novabill.service.web.PremiumEnablerService#notifyAndInvoiceBusiness(com.novadart.novabill.domain.Business, java.lang.String)
	 */
	@Transactional
	public void notifyAndInvoiceBusiness(Business business, String paymentPlan, String email) throws PremiumUpgradeException  {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //save authentication
		try{
			setSecurityContext();
			Business novadartBusiness = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId());
			LOGGER.info(String.format("Adding business %s as client to Novadart if not already", email));
			Long clientID = addOrUpdateClient(novadartBusiness, business, email);
			LOGGER.info(String.format("Invoicing %s", email));
			Long invoiceID = createInvoice(novadartBusiness, clientID, paymentPlan);
			LOGGER.info(String.format("Emailing invoice to %s", email));
			exportAndEmailInvoicePdf(invoiceID, novadartBusiness.getId(), email);
			expireSessions(email);
		} catch (Exception e){
			throw new PremiumUpgradeException(e);
		} finally {
			clearSecurityContext();
			SecurityContextHolder.getContext().setAuthentication(auth); //restore authentication
		}
	}
	
}
