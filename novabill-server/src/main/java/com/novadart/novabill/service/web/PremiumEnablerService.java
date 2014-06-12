package com.novadart.novabill.service.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.transaction.Transactional;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.transformer.BusinessDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.ClientDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.InvoiceDTOTransformer;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.paypal.PaymentPlanDescriptor;
import com.novadart.novabill.paypal.PaymentPlansLoader;
import com.novadart.novabill.report.DocumentType;
import com.novadart.novabill.report.JRDataSourceFactory;
import com.novadart.novabill.report.JasperReportKeyResolutionException;
import com.novadart.novabill.report.JasperReportService;
import com.novadart.novabill.service.PrincipalDetailsService;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentDeltaType;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.PremiumUpgradeException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@MailMixin
@Service
public class PremiumEnablerService {

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
	private JasperReportService jasperReportService;
	
	private void makePremium(Business business){
		for(Principal principal: business.getPrincipals()){
			if(!principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM)){
				principal.getGrantedRoles().remove(RoleType.ROLE_BUSINESS_FREE);
				principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_PREMIUM);
			}
		}
	}
	
	
	private void extendNonFreeAccountExpirationTime(Business business, int numberOfMonths) {
		Long current = System.currentTimeMillis(), nonFreeAccountExpirationTime = business.getSettings().getNonFreeAccountExpirationTime();
		Long zero = nonFreeAccountExpirationTime == null || nonFreeAccountExpirationTime < current? current: nonFreeAccountExpirationTime;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(zero);
		calendar.add(Calendar.MONTH, numberOfMonths);
		business.getSettings().setNonFreeAccountExpirationTime(calendar.getTimeInMillis());
	}
	
	/* (non-Javadoc)
	 * @see com.novadart.novabill.service.web.PremiumEnablerService#enablePremiumForNMonths(com.novadart.novabill.domain.Business, int)
	 */
	public void enablePremiumForNMonths(Business business, int numberOfMonths) {
		makePremium(business);
		extendNonFreeAccountExpirationTime(business, numberOfMonths);
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
		invoice.setDocumentID(invoiceService.getNextInvoiceDocumentID());
		Date now = new Date();
		invoice.setAccountingDocumentDate(now);
		invoice.setPaymentTypeName("Rimessa Diretta");
		invoice.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
		invoice.setPaymentDeltaType(PaymentDeltaType.DAYS);
		invoice.setPaymentDueDate(now);
		invoice.setPayed(true);
		invoice.setPaymentNote("Invoice payed via PayPal");
		
		PaymentPlanDescriptor planDesc = paymentPlansLoader.getPayPalPaymentPlanDescriptor(paymentPlan);
		AccountingDocumentItem item = new AccountingDocumentItem();
		item.setPrice(planDesc.getTotalWithoutTax());
		item.setUnitOfMeasure("year");
		item.setQuantity(new BigDecimal("1.00"));
		item.setDiscount(planDesc.getDiscount());
		item.setDescription(planDesc.getItemName());
		item.setTax(planDesc.getTax());
		item.setTotal(planDesc.getTotalWithoutTax());
		invoice.getAccountingDocumentItems().add(item);
		
		invoice.setTotalBeforeTax(planDesc.getTotalWithoutTax());
		invoice.setTotal(planDesc.getTotalAfterTax());
		invoice.setTotalTax(planDesc.getTotalTax());
		
		InvoiceDTO invoiceDTO = InvoiceDTOTransformer.toDTO(invoice, true);
		invoiceDTO.setBusiness(BusinessDTOTransformer.toDTO(novadartBusiness));
		invoiceDTO.setClient(ClientDTOTransformer.toDTO(Client.findClient(clientID)));

		return invoiceService.add(invoiceDTO);
	}
	
	private void exportAndEmailInvoicePdf(Long invoiceID, Long businessID, String email) throws IOException, JRException, JasperReportKeyResolutionException{
		File invFile = null;
		try{
			invFile = File.createTempFile("premiumInvoce", ".pdf");
			jasperReportService.exportReportToPdfFile(JRDataSourceFactory.createDataSource(Invoice.findInvoice(invoiceID), businessID),
					DocumentType.INVOICE, LayoutType.TIDY, invFile.getPath());
			sendMessage(email, "Account upgraded", new HashMap<String, Object>(), "mail-templates/upgrade-notification.vm",
					IOUtils.toByteArray(new FileInputStream(invFile)), "Fattura.pdf");
		} finally {
			if(invFile != null)
				invFile.delete();
		}
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
			Long clientID = addOrUpdateClient(novadartBusiness, business, email);
			Long invoiceID = createInvoice(novadartBusiness, clientID, paymentPlan);
			exportAndEmailInvoicePdf(invoiceID, novadartBusiness.getId(), email);
		} catch (Exception e){
			throw new PremiumUpgradeException(e);
		} finally {
			clearSecurityContext();
			SecurityContextHolder.getContext().setAuthentication(auth); //restore authentication
		}
	}
	
}