package com.novadart.novabill.test.suite;


import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.domain.dto.factory.InvoiceDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.PaymentType;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.InvoiceService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-invoice-test-config.xml")
@Transactional
public class InvoiceServiceTest extends GWTServiceTest {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Resource(name = "testProps")
	private HashMap<String, String> testProps;
	
	@SuppressWarnings("serial")
	private static Map<String, Field> validationFieldsMap = new HashMap<String, Field>(){{
		//Accounting doc
		put("documentID", Field.documentID); put("accountingDocumentDate", Field.accountingDocumentDate);
		put("accountingDocumentYear", Field.accountingDocumentYear); put("note", Field.note); put("paymentNote", Field.paymentNote);
		put("total", Field.total); put("totalTax", Field.totalTax); put("totalBeforeTax", Field.totalBeforeTax);
		
		//Accounting doc item
		put("accountingDocumentItems_description", Field.accountingDocumentItems_description); 
		put("accountingDocumentItems_unitOfMeasure", Field.accountingDocumentItems_unitOfMeasure); 
		put("accountingDocumentItems_tax", Field.accountingDocumentItems_tax);
		put("accountingDocumentItems_quantity", Field.accountingDocumentItems_quantity);
		put("accountingDocumentItems_totalBeforeTax", Field.accountingDocumentItems_totalBeforeTax);
		put("accountingDocumentItems_totalTax", Field.accountingDocumentItems_totalTax);
		put("accountingDocumentItems_total", Field.accountingDocumentItems_total);
		put("accountingDocumentItems_price", Field.accountingDocumentItems_price);
		
		//Invoice
		put("paymentType", Field.paymentType); put("paymentDueDate", Field.paymentDueDate); put("payed", Field.payed);
	}};
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}

	@Test
	public void invoiceServiceWiringTest(){
		assertNotNull(invoiceService);
	}

	private boolean equalInvoiceDTOs(InvoiceDTO lhs, InvoiceDTO rhs, boolean ignoreid){
		if(lhs.getItems().size() != rhs.getItems().size())
			return false;
		boolean itemsEqual = true;
		for(int i = 0; i < lhs.getItems().size(); ++i){
			if(ignoreid && !EqualsBuilder.reflectionEquals(lhs.getItems().get(i), rhs.getItems().get(i), "id")){
				itemsEqual = false;
				break;
			}
			if(!ignoreid && !EqualsBuilder.reflectionEquals(lhs.getItems().get(i), rhs.getItems().get(i), false)){
				itemsEqual = false;
				break;
			}
		}
		return (ignoreid? EqualsBuilder.reflectionEquals(lhs, rhs, "items", "client", "business", "id"):
						  EqualsBuilder.reflectionEquals(lhs, rhs, "items", "client", "business"))&& itemsEqual;
	}
	
	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long invoiceID = authenticatedPrincipal.getBusiness().getInvoices().iterator().next().getId();
		InvoiceDTO expectedDTO = InvoiceDTOFactory.toDTO(Invoice.findInvoice(invoiceID));
		InvoiceDTO actualDTO = invoiceService.get(invoiceID);
		assertTrue(equalInvoiceDTOs(expectedDTO, actualDTO, false));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long invoiceID = Business.findBusiness(getUnathorizedBusinessID()).getInvoices().iterator().next().getId();
		invoiceService.get(invoiceID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		invoiceService.get(null);
	}
	
	@Test
	public void getAllInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		PageDTO<InvoiceDTO> results = invoiceService.getAllInRange(authenticatedPrincipal.getBusiness().getId(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllInRangeUnauthorizedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		invoiceService.getAllInRange(getUnathorizedBusinessID(), 0, 10);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllInRangeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, ConcurrentAccessException{
		invoiceService.getAllInRange(null, 0, 10);
	}
	
	@Test
	public void getAllForClientAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		List<InvoiceDTO> invoiceDTOs = invoiceService.getAllForClient(clientID);
		boolean contained = true;
		outer: for(Invoice invoice: Client.findClient(clientID).getInvoices()){
			InvoiceDTO idto1 = InvoiceDTOFactory.toDTO(invoice);
			for(InvoiceDTO idto2: invoiceDTOs)
				if(equalInvoiceDTOs(idto1, idto2, false))
					continue outer;
			contained = false;
			break outer;
		}
		assertTrue(contained && Client.findClient(clientID).getInvoices().size() == invoiceDTOs.size());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		invoiceService.getAllForClient(clientID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientAuthorizedClientIDNotExistTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		invoiceService.getAllForClient(-1l);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		invoiceService.getAllForClient(null);
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, invoiceID);
		Invoice.entityManager().flush();
		assertNull(Invoice.findInvoice(invoiceID));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(getUnathorizedBusinessID(), clientID, invoiceID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(null, clientID, invoiceID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeUnauthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long invoiceID = authenticatedPrincipal.getBusiness().getInvoices().iterator().next().getId();
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), null, invoiceID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeUnauthorizedInvoiceIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, null);
	}
	
	@Test
	public void setPayedAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException, AuthorizationException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.setPayed(authenticatedPrincipal.getBusiness().getId(), clientID, invoiceID, true);
		assertTrue(Invoice.findInvoice(invoiceID).getPayed());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void setPayedUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(getUnathorizedBusinessID(), clientID, invoiceID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void setPayedUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(null, clientID, invoiceID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void setPayedUnauthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long invoiceID = authenticatedPrincipal.getBusiness().getInvoices().iterator().next().getId();
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), null, invoiceID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void setPayedUnauthorizedInvoiceIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, null);
	}
	
	@Test
	public void getAllForClientInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		PageDTO<InvoiceDTO> results = invoiceService.getAllForClientInRange(clientID, 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		invoiceService.getAllForClientInRange(clientID, 0, 10);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientInRangeClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		invoiceService.getAllForClientInRange(null, 0, 10);
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException{
		Invoice expectedInvoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		expectedInvoice.setNote("Temporary note for this invoice");
		invoiceService.update(InvoiceDTOFactory.toDTO(expectedInvoice));
		Invoice.entityManager().flush();
		Invoice actualInvoice = Invoice.findInvoice(expectedInvoice.getId());
		assertEquals(actualInvoice.getNote(), "Temporary note for this invoice");
		
	}
	
	@Test(expected = AccessDeniedException.class)
	public void updateAuthorizedInvoiceNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException{
		invoiceService.update(null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void updateAuthorizedIDNull() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException{
		Invoice invoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(invoice);
		invDTO.setId(null);
		invoiceService.update(invDTO);
	}
	
	private Invoice createInvoice(Long documentID){
		Invoice inv = new Invoice();
		inv.setAccountingDocumentDate(new Date());
		inv.setDocumentID(documentID);
		inv.setNote("");
		inv.setPaymentNote("");
		inv.setTotal(new BigDecimal("121.0"));
		inv.setTotalBeforeTax(new BigDecimal("100.0"));
		inv.setTotalTax(new BigDecimal("21.0"));
		inv.setPayed(false);
		inv.setPaymentDueDate(new Date());
		inv.setPaymentType(PaymentType.CASH);
		AccountingDocumentItem item = new AccountingDocumentItem();
		item.setDescription("description");
		item.setPrice(new BigDecimal("100.0"));
		item.setQuantity(new BigDecimal("1.0"));
		item.setTax(new BigDecimal("21.0"));
		item.setTotal(new BigDecimal("121.0"));
		item.setTotalBeforeTax(new BigDecimal("100.0"));
		item.setTotalTax(new BigDecimal("21.0"));
		item.setUnitOfMeasure("piece");
		item.setAccountingDocument(inv);
		inv.getAccountingDocumentItems().add(item);
		return inv;
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(createInvoice(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID()));
		invDTO.setClient(ClientDTOFactory.toDTO(client));
		invDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = invoiceService.add(invDTO);
		invDTO.setId(id);
		Invoice.entityManager().flush();
		assertTrue(equalInvoiceDTOs(invDTO, InvoiceDTOFactory.toDTO(Invoice.findInvoice(id)), true));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addUnathorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(createInvoice(Business.findBusiness(getUnathorizedBusinessID()).getNextInvoiceDocumentID()));
		invDTO.setClient(ClientDTOFactory.toDTO(client));
		invDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		invoiceService.add(invDTO);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addAuthorizedInvoiceDTONull() throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException{
		invoiceService.add(null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addAuthorizedInvoiceDTOIDNotNull() throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(createInvoice(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID()));
		invDTO.setClient(ClientDTOFactory.toDTO(client));
		invDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		invDTO.setId(1l);
		invoiceService.add(invDTO);
	}
	
	private Invoice createInvalidInvoice(Long documentID){
		Invoice inv = new Invoice();
		inv.setAccountingDocumentDate(new Date());
		inv.setDocumentID(documentID);
		inv.setNote(StringUtils.leftPad("1", 2000, '1'));
		inv.setPaymentNote(StringUtils.leftPad("1", 2000, '1'));
		inv.setTotal(null);
		inv.setTotalBeforeTax(null);
		inv.setTotalTax(null);
		inv.setPayed(false);
		inv.setPaymentDueDate(null);
		inv.setPaymentType(null);
		AccountingDocumentItem item = new AccountingDocumentItem();
		item.setDescription(StringUtils.leftPad("1", 1000, '1'));
		item.setPrice(null);
		item.setQuantity(null);
		item.setTax(null);
		item.setTotal(null);
		item.setTotalBeforeTax(null);
		item.setTotalTax(null);
		item.setUnitOfMeasure(StringUtils.leftPad("1", 1000, '1'));
		item.setAccountingDocument(inv);
		inv.getAccountingDocumentItems().add(item);
		return inv;
	}
	
	@Test
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException, AuthorizationException{
		try{
			InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(createInvalidInvoice(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID()));
			invDTO.setClient(ClientDTOFactory.toDTO(authenticatedPrincipal.getBusiness().getClients().iterator().next()));
			invDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
			invoiceService.add(invDTO);
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<Field>(validationFieldsMap.values());
			expected.remove(Field.accountingDocumentYear);
			expected.remove(Field.accountingDocumentDate);
			expected.remove(Field.documentID);
			expected.remove(Field.payed);
			expected.remove(Field.paymentDueDate);
			Set<Field> actual= new HashSet<Field>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertEquals(expected, actual);
		}
	}
	
}
