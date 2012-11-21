package com.novadart.novabill.test.suite;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.domain.dto.factory.CreditNoteDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.CreditNoteService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-creditnote-test-config.xml")
@Transactional
public class CreditNoteServiceTest extends GWTServiceTest {
	
	@Autowired
	private CreditNoteService creditNoteService;
	
	@Resource(name = "testProps")
	private HashMap<String, String> testProps;
	
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
	public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long creditNoteID = authenticatedPrincipal.getBusiness().getCreditNotes().iterator().next().getId();
		CreditNoteDTO expectedDTO = CreditNoteDTOFactory.toDTO(CreditNote.findCreditNote(creditNoteID));
		CreditNoteDTO actualDTO = creditNoteService.get(creditNoteID);
		assertTrue(TestUtils.accountingDocumentComparator.equal(expectedDTO, actualDTO));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getUnathorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long creditNoteID = Business.findBusiness(getUnathorizedBusinessID()).getCreditNotes().iterator().next().getId();
		creditNoteService.get(creditNoteID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAuthorizedCreditNoteIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		creditNoteService.get(null);
	}
	
	@Test
	public void getAllInRangeAuthorizedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		PageDTO<CreditNoteDTO> results = creditNoteService.getAllInRange(authenticatedPrincipal.getBusiness().getId(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllInRangeUnauthorizedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		creditNoteService.getAllInRange(getUnathorizedBusinessID(), 0, 10);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllInRangeAuthorizedBusinessIDNullTest() throws NotAuthenticatedException, ConcurrentAccessException{
		creditNoteService.getAllInRange(null, 0, 10);
	}
	
	@Test
	public void getAllForClientAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithCreditNoteID"));
		List<AccountingDocumentDTO> actual = new ArrayList<AccountingDocumentDTO>(creditNoteService.getAllForClient(clientID));
		List<AccountingDocumentDTO> expected = TestUtils.toDTOList(new ArrayList(Client.findClient(clientID).getCreditNotes()), TestUtils.creditNoteDTOConverter); 
		assertTrue(TestUtils.equal(expected, actual, TestUtils.accountingDocumentComparator));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		creditNoteService.getAllForClient(clientID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		creditNoteService.getAllForClient(null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientAuthorizedClientIDNotExistTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		creditNoteService.getAllForClient(-1l);
	}
	
	@Test
	public void getAllForClientInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithCreditNoteID"));
		PageDTO<CreditNoteDTO> results = creditNoteService.getAllForClientInRange(clientID, 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		creditNoteService.getAllForClientInRange(clientID, 0, 10);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientInRangeClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		creditNoteService.getAllForClientInRange(null, 0, 10);
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithCreditNoteID"));
		Long creditNoteID = Client.findClient(clientID).getCreditNotes().iterator().next().getId();
		creditNoteService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, creditNoteID);
		CreditNote.entityManager().flush();
		assertNull(CreditNote.findCreditNote(creditNoteID));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeUnauthorized() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithCreditNoteID"));
		Long creditNoteID = Client.findClient(clientID).getCreditNotes().iterator().next().getId();
		creditNoteService.remove(getUnathorizedBusinessID(), clientID, creditNoteID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithCreditNoteID"));
		Long creditNoteID = Client.findClient(clientID).getCreditNotes().iterator().next().getId();
		creditNoteService.remove(null, clientID, creditNoteID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithCreditNoteID"));
		Long creditNoteID = Client.findClient(clientID).getCreditNotes().iterator().next().getId();
		creditNoteService.remove(authenticatedPrincipal.getBusiness().getId(), null, creditNoteID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeUnauthorizedCreditNoteIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithCreditNoteID"));
		creditNoteService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, null);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		CreditNoteDTO creditNoteDTO = CreditNoteDTOFactory.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextCreditNoteDocumentID(), CreditNote.class));
		creditNoteDTO.setClient(ClientDTOFactory.toDTO(client));
		creditNoteDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = creditNoteService.add(creditNoteDTO);
		CreditNote.entityManager().flush();
		assertTrue(TestUtils.accountingDocumentComparatorIgnoreID.equal(creditNoteDTO, CreditNoteDTOFactory.toDTO(CreditNote.findCreditNote(id))));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addUnathorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		CreditNoteDTO creditNoteDTO = CreditNoteDTOFactory.toDTO(TestUtils.createInvOrCredNote(Business.findBusiness(getUnathorizedBusinessID()).getNextCreditNoteDocumentID(), CreditNote.class));
		creditNoteDTO.setClient(ClientDTOFactory.toDTO(client));
		creditNoteDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		creditNoteService.add(creditNoteDTO);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addAuthorizedCreditNoteDTONull() throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException{
		creditNoteService.add(null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addAuthorizedCreditNoteDTOIDNotNull() throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		CreditNoteDTO creditNoteDTO = CreditNoteDTOFactory.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextCreditNoteDocumentID(), CreditNote.class));
		creditNoteDTO.setClient(ClientDTOFactory.toDTO(client));
		creditNoteDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		creditNoteDTO.setId(1l);
		creditNoteService.add(creditNoteDTO);
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException{
		CreditNote expectedCreditNote = authenticatedPrincipal.getBusiness().getCreditNotes().iterator().next();
		expectedCreditNote.setNote("Temporary note for this credit note");
		creditNoteService.update(CreditNoteDTOFactory.toDTO(expectedCreditNote));
		CreditNote.entityManager().flush();
		CreditNote actualCreditNote = CreditNote.findCreditNote(expectedCreditNote.getId());
		assertEquals(actualCreditNote.getNote(), "Temporary note for this credit note");
		
	}
	
	@Test(expected = AccessDeniedException.class)
	public void updateAuthorizedCreditNoteNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException{
		creditNoteService.update(null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void updateAuthorizedIDNull() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException{
		CreditNote creditNote = authenticatedPrincipal.getBusiness().getCreditNotes().iterator().next();
		CreditNoteDTO credNoteDTO = CreditNoteDTOFactory.toDTO(creditNote);
		credNoteDTO.setId(null);
		creditNoteService.update(credNoteDTO);
	}
	
	@Test
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException, AuthorizationException, InstantiationException{
		try{
			CreditNoteDTO creditNoteDTO = CreditNoteDTOFactory.toDTO(TestUtils.createInvalidIngOrCredNote(authenticatedPrincipal.getBusiness().getNextCreditNoteDocumentID(), CreditNote.class));
			creditNoteDTO.setClient(ClientDTOFactory.toDTO(authenticatedPrincipal.getBusiness().getClients().iterator().next()));
			creditNoteDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
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
