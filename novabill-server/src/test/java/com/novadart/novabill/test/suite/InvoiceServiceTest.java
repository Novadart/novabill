package com.novadart.novabill.test.suite;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.aspect.logging.DBLoggerAspect;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.domain.dto.factory.InvoiceDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.ClientGwtService;
import com.novadart.novabill.shared.client.facade.InvoiceGwtService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-invoice-test-config.xml")
@Transactional
@ActiveProfiles("dev")
public class InvoiceServiceTest extends GWTServiceTest {
	
	@Autowired
	private InvoiceGwtService invoiceService;
	
	@Autowired
	private ClientGwtService clientService;
	
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

	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long invoiceID = authenticatedPrincipal.getBusiness().getInvoices().iterator().next().getId();
		InvoiceDTO expectedDTO = InvoiceDTOFactory.toDTO(Invoice.findInvoice(invoiceID), true);
		InvoiceDTO actualDTO = invoiceService.get(invoiceID);
		assertTrue(TestUtils.accountingDocumentComparator.equal(actualDTO, expectedDTO));
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long invoiceID = Business.findBusiness(getUnathorizedBusinessID()).getInvoices().iterator().next().getId();
		invoiceService.get(invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAuthorizedInvoiceIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		invoiceService.get(null);
	}
	
	@Test
	public void getAllInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		PageDTO<InvoiceDTO> results = invoiceService.getAllInRange(authenticatedPrincipal.getBusiness().getId(), getYear(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		invoiceService.getAllInRange(getUnathorizedBusinessID(), getYear(), 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException{
		invoiceService.getAllInRange(null, getYear(), 0, 10);
	}
	
	@Test
	public void getAllForClientAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		List<AccountingDocumentDTO> actual = new ArrayList<AccountingDocumentDTO>(invoiceService.getAllForClient(clientID, getYear()));
		List<AccountingDocumentDTO> expected = new ArrayList<>();
		for(Invoice doc: Client.findClient(clientID).getInvoices())
			if(doc.getAccountingDocumentYear().equals(getYear()))
				expected.add(DTOUtils.invoiceDTOConverter.toDTO(doc, false));
		assertTrue(TestUtils.equal(expected, actual, TestUtils.accountingDocumentComparator));
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		invoiceService.getAllForClient(clientID, getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNotExistTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		invoiceService.getAllForClient(-1l, getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		invoiceService.getAllForClient(null, getYear());
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, JsonParseException, JsonMappingException, IOException, DataIntegrityException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, invoiceID);
		Invoice.entityManager().flush();
		assertNull(Invoice.findInvoice(invoiceID));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.INVOICE, rec.getEntityType());
		assertEquals(invoiceID, rec.getEntityID());
		assertEquals(OperationType.DELETE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(Client.findClient(clientID).getName(), details.get(DBLoggerAspect.CLIENT_NAME));
	}
	
	@Test
	public void removeAuthorizedFromTranDocsTest() throws InstantiationException, IllegalAccessException, NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, DataIntegrityException, NoSuchObjectException {
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next(); 
		transDoc.setInvoice(inv);
		inv.getTransportDocuments().add(transDoc);
		Invoice.entityManager().flush();
		invoiceService.remove(inv.getBusiness().getId(), inv.getClient().getId(), inv.getId());
		assertTrue(TransportDocument.findTransportDocument(transDoc.getId()).getInvoice() == null);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(getUnathorizedBusinessID(), clientID, invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(null, clientID, invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException{
		Long invoiceID = authenticatedPrincipal.getBusiness().getInvoices().iterator().next().getId();
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), null, invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAauthorizedInvoiceIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, null);
	}
	
	@Test
	public void setPayedAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, AuthorizationException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.setPayed(authenticatedPrincipal.getBusiness().getId(), clientID, invoiceID, true);
		assertTrue(Invoice.findInvoice(invoiceID).getPayed());
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.INVOICE, rec.getEntityType());
		assertEquals(invoiceID, rec.getEntityID());
		assertEquals(OperationType.SET_PAYED, rec.getOperationType());
		assertTrue(rec.getDetails() == null);
	}
	
	@Test(expected = DataAccessException.class)
	public void setPayedUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(getUnathorizedBusinessID(), clientID, invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void setPayedUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(null, clientID, invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void setPayedUnauthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException{
		Long invoiceID = authenticatedPrincipal.getBusiness().getInvoices().iterator().next().getId();
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), null, invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void setPayedUnauthorizedInvoiceIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, null);
	}
	
	@Test
	public void getAllForClientInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		PageDTO<InvoiceDTO> results = invoiceService.getAllForClientInRange(clientID, getYear(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		invoiceService.getAllForClientInRange(clientID, getYear(), 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		invoiceService.getAllForClientInRange(null, getYear(), 0, 10);
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, JsonParseException, JsonMappingException, IOException{
		Invoice expectedInvoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		expectedInvoice.setNote("Temporary note for this invoice");
		invoiceService.update(InvoiceDTOFactory.toDTO(expectedInvoice, true));
		Invoice.entityManager().flush();
		Invoice actualInvoice = Invoice.findInvoice(expectedInvoice.getId());
		assertEquals(actualInvoice.getNote(), "Temporary note for this invoice");
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.INVOICE, rec.getEntityType());
		assertEquals(expectedInvoice.getId(), rec.getEntityID());
		assertEquals(OperationType.UPDATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(expectedInvoice.getClient().getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(expectedInvoice.getDocumentID().toString(), details.get(DBLoggerAspect.DOCUMENT_ID));
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedInvoiceNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		invoiceService.update(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedIDNull() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		Invoice invoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(invoice, true);
		invDTO.setId(null);
		invoiceService.update(invDTO);
	}
	
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException, JsonParseException, JsonMappingException, IOException, DataIntegrityException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(), Invoice.class), true);
		invDTO.setClient(ClientDTOFactory.toDTO(client));
		invDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = invoiceService.add(invDTO);
		Invoice.entityManager().flush();
		assertTrue(TestUtils.accountingDocumentComparatorIgnoreID.equal(invDTO, InvoiceDTOFactory.toDTO(Invoice.findInvoice(id), true)));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.INVOICE, rec.getEntityType());
		assertEquals(id, rec.getEntityID());
		assertEquals(OperationType.CREATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(client.getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(invDTO.getDocumentID().toString(), details.get(DBLoggerAspect.DOCUMENT_ID));
	}
	
	@Test
	public void addAuthorizedFromTranDocsTest() throws InstantiationException, IllegalAccessException, NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, DataIntegrityException {
		Long transportDocID = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next().getId();
		InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(), Invoice.class), true);
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		invDTO.setClient(ClientDTOFactory.toDTO(client));
		invDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		List<Long> tranDocsIDs = new ArrayList<>();
		tranDocsIDs.add(transportDocID);
		invDTO.setTransportDocumentIDs(tranDocsIDs);
		Long id = invoiceService.add(invDTO);
		Invoice.entityManager().flush();
		assertTrue(Invoice.findInvoice(id).getTransportDocuments().contains(TransportDocument.findTransportDocument(transportDocID)));
		assertEquals(Invoice.findInvoice(id).getId(), TransportDocument.findTransportDocument(transportDocID).getInvoice().getId());
	}
	
	@Test(expected = ValidationException.class)
	public void addAuthorizedForThinClientValidationErrorTest() throws InstantiationException, IllegalAccessException, NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, DataIntegrityException{
		Client client = new Client();
		client.setName("John Doe");
		client.setBusiness(authenticatedPrincipal.getBusiness());
		Long clientID = clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOFactory.toDTO(client));
		
		InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(), Invoice.class), true);
		invDTO.setClient(ClientDTOFactory.toDTO(Client.findClient(clientID)));
		invDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		invoiceService.add(invDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnathorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException, DataIntegrityException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(TestUtils.createInvOrCredNote(Business.findBusiness(getUnathorizedBusinessID()).getNextInvoiceDocumentID(), Invoice.class), true);
		invDTO.setClient(ClientDTOFactory.toDTO(client));
		invDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		invoiceService.add(invDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedInvoiceDTONull() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException, DataIntegrityException{
		invoiceService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedInvoiceDTOIDNotNull() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException, DataIntegrityException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(), Invoice.class), true);
		invDTO.setClient(ClientDTOFactory.toDTO(client));
		invDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		invDTO.setId(1l);
		invoiceService.add(invDTO);
	}
	
	@Test
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, AuthorizationException, InstantiationException, DataIntegrityException{
		try{
			InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(TestUtils.createInvalidInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(), Invoice.class), true);
			invDTO.setClient(ClientDTOFactory.toDTO(authenticatedPrincipal.getBusiness().getClients().iterator().next()));
			invDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
			invoiceService.add(invDTO);
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<Field>(TestUtils.invoiceValidationFieldsMap.values());
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
