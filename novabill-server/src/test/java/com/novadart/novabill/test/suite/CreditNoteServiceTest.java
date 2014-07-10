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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.novadart.novabill.aspect.logging.DBLoggerAspect;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.transformer.BusinessDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.ClientDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.CreditNoteDTOTransformer;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.ClientGwtService;
import com.novadart.novabill.shared.client.facade.CreditNoteGwtService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-creditnote-test-config.xml")
@Transactional
@ActiveProfiles("dev")
public class CreditNoteServiceTest extends ServiceTest {
	
	@Autowired
	private CreditNoteGwtService creditNoteService;
	
	@Autowired
	private ClientGwtService clientService;
	
	@Test
	public void creditNoteServiceWiringTest(){
		assertNotNull(creditNoteService);
	}
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}

	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long creditNoteID = authenticatedPrincipal.getBusiness().getCreditNotes().iterator().next().getId();
		CreditNoteDTO expectedDTO = CreditNoteDTOTransformer.toDTO(CreditNote.findCreditNote(creditNoteID), true);
		CreditNoteDTO actualDTO = creditNoteService.get(creditNoteID);
		assertTrue(TestUtils.accountingDocumentComparator.equal(expectedDTO, actualDTO));
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnathorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long creditNoteID = Business.findBusiness(getUnathorizedBusinessID()).getCreditNotes().iterator().next().getId();
		creditNoteService.get(creditNoteID);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAuthorizedCreditNoteIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		creditNoteService.get(null);
	}
	
	@Test
	public void getAllInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		PageDTO<CreditNoteDTO> results = creditNoteService.getAllInRange(authenticatedPrincipal.getBusiness().getId(), getYear(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		creditNoteService.getAllInRange(getUnathorizedBusinessID(), getYear(), 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeAuthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException{
		creditNoteService.getAllInRange(null, getYear(), 0, 10);
	}
	
	@Test
	public void getAllForClientAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithCreditNotesID"));
		List<AccountingDocumentDTO> actual = new ArrayList<AccountingDocumentDTO>(creditNoteService.getAllForClient(clientID, getYear()));
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<AccountingDocumentDTO> expected = DTOUtils.toDTOList(new ArrayList(Client.findClient(clientID).getCreditNotes()), DTOUtils.creditNoteDTOConverter, false); 
		assertTrue(TestUtils.equal(expected, actual, TestUtils.accountingDocumentComparator));
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		creditNoteService.getAllForClient(clientID, getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Integer year = getYear(); 
		creditNoteService.getAllForClient(null, year);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNotExistTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		creditNoteService.getAllForClient(-1l, getYear());
	}
	
	@Test
	public void getAllForClientInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithCreditNotesID"));
		PageDTO<CreditNoteDTO> results = creditNoteService.getAllForClientInRange(clientID, getYear(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		creditNoteService.getAllForClientInRange(clientID, getYear(), 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		creditNoteService.getAllForClientInRange(null, getYear(), 0, 10);
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, JsonParseException, JsonMappingException, IOException{
		Long clientID = new Long(testProps.get("clientWithCreditNotesID"));
		Long creditNoteID = Client.findClient(clientID).getCreditNotes().iterator().next().getId();
		creditNoteService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, creditNoteID);
		CreditNote.entityManager().flush();
		assertNull(CreditNote.findCreditNote(creditNoteID));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.CREDIT_NOTE, rec.getEntityType());
		assertEquals(creditNoteID, rec.getEntityID());
		assertEquals(OperationType.DELETE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(Client.findClient(clientID).getName(), details.get(DBLoggerAspect.CLIENT_NAME));
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorized() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithCreditNotesID"));
		Long creditNoteID = Client.findClient(clientID).getCreditNotes().iterator().next().getId();
		creditNoteService.remove(getUnathorizedBusinessID(), clientID, creditNoteID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithCreditNotesID"));
		Long creditNoteID = Client.findClient(clientID).getCreditNotes().iterator().next().getId();
		creditNoteService.remove(null, clientID, creditNoteID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithCreditNotesID"));
		Long creditNoteID = Client.findClient(clientID).getCreditNotes().iterator().next().getId();
		creditNoteService.remove(authenticatedPrincipal.getBusiness().getId(), null, creditNoteID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedCreditNoteIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithCreditNotesID"));
		creditNoteService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, null);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException, JsonParseException, JsonMappingException, IOException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		CreditNoteDTO creditNoteDTO = CreditNoteDTOTransformer.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextCreditNoteDocumentID(), CreditNote.class), true);
		creditNoteDTO.setClient(ClientDTOTransformer.toDTO(client));
		creditNoteDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = creditNoteService.add(creditNoteDTO);
		CreditNote.entityManager().flush();
		assertTrue(TestUtils.accountingDocumentComparatorIgnoreID.equal(creditNoteDTO, CreditNoteDTOTransformer.toDTO(CreditNote.findCreditNote(id), true)));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.CREDIT_NOTE, rec.getEntityType());
		assertEquals(id, rec.getEntityID());
		assertEquals(OperationType.CREATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(client.getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(creditNoteDTO.getDocumentID().toString(), details.get(DBLoggerAspect.DOCUMENT_ID));
	}
	
	@Test(expected = ValidationException.class)
	public void addAuthorizedForThinClientValidationErrorTest() throws InstantiationException, IllegalAccessException, NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
		Client client = new Client();
		client.setName("John Doe");
		client.setBusiness(authenticatedPrincipal.getBusiness());
		Long clientID = clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOTransformer.toDTO(client));
		
		CreditNoteDTO credNoteDTO = CreditNoteDTOTransformer.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(), CreditNote.class), true);
		credNoteDTO.setClient(ClientDTOTransformer.toDTO(Client.findClient(clientID)));
		credNoteDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		creditNoteService.add(credNoteDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnathorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		CreditNoteDTO creditNoteDTO = CreditNoteDTOTransformer.toDTO(TestUtils.createInvOrCredNote(Business.findBusiness(getUnathorizedBusinessID()).getNextCreditNoteDocumentID(), CreditNote.class), true);
		creditNoteDTO.setClient(ClientDTOTransformer.toDTO(client));
		creditNoteDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		creditNoteService.add(creditNoteDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedCreditNoteDTONull() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException{
		creditNoteService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedCreditNoteDTOIDNotNull() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		CreditNoteDTO creditNoteDTO = CreditNoteDTOTransformer.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextCreditNoteDocumentID(), CreditNote.class), true);
		creditNoteDTO.setClient(ClientDTOTransformer.toDTO(client));
		creditNoteDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		creditNoteDTO.setId(1l);
		creditNoteService.add(creditNoteDTO);
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, JsonParseException, JsonMappingException, IOException{
		CreditNote expectedCreditNote = authenticatedPrincipal.getBusiness().getCreditNotes().iterator().next();
		expectedCreditNote.setNote("Temporary note for this credit note");
		creditNoteService.update(CreditNoteDTOTransformer.toDTO(expectedCreditNote, true));
		CreditNote.entityManager().flush();
		CreditNote actualCreditNote = CreditNote.findCreditNote(expectedCreditNote.getId());
		assertEquals(actualCreditNote.getNote(), "Temporary note for this credit note");
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.CREDIT_NOTE, rec.getEntityType());
		assertEquals(expectedCreditNote.getId(), rec.getEntityID());
		assertEquals(OperationType.UPDATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(expectedCreditNote.getClient().getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(expectedCreditNote.getDocumentID().toString(), details.get(DBLoggerAspect.DOCUMENT_ID));
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedCreditNoteNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		creditNoteService.update(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedIDNull() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		CreditNote creditNote = authenticatedPrincipal.getBusiness().getCreditNotes().iterator().next();
		CreditNoteDTO credNoteDTO = CreditNoteDTOTransformer.toDTO(creditNote, true);
		credNoteDTO.setId(null);
		creditNoteService.update(credNoteDTO);
	}
	
	@Test
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException, InstantiationException{
		try{
			CreditNoteDTO creditNoteDTO = CreditNoteDTOTransformer.toDTO(TestUtils.createInvalidInvOrCredNote(authenticatedPrincipal.getBusiness().getNextCreditNoteDocumentID(), CreditNote.class), true);
			creditNoteDTO.setClient(ClientDTOTransformer.toDTO(authenticatedPrincipal.getBusiness().getClients().iterator().next()));
			creditNoteDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
			creditNoteService.add(creditNoteDTO);
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<Field>(TestUtils.abstractInvoiceValidationFieldsMap.values());
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
