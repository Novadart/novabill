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
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.domain.dto.factory.TransportDocumentDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.TransportDocumentService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-transportdocument-test-config.xml")
@Transactional
public class TransportDocumentServiceTest extends GWTServiceTest {
	
	@Autowired
	private TransportDocumentService transportDocService;
	
	@Resource(name = "testProps")
	private HashMap<String, String> testProps;
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}
	
	@Test
	public void transportDocServiceWiring(){
		assertNotNull(transportDocService);
	}
	
	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long transportDocID = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next().getId();
		TransportDocumentDTO expectedDTO = TransportDocumentDTOFactory.toDTO(TransportDocument.findTransportDocument(transportDocID));
		TransportDocumentDTO actualDTO = transportDocService.get(transportDocID);
		assertTrue(TestUtils.transportDocumentComparator.equal(actualDTO, expectedDTO));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long transportDocID = Business.findBusiness(getUnathorizedBusinessID()).getTransportDocuments().iterator().next().getId();
		transportDocService.get(transportDocID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAuthorizedTransportDocIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		transportDocService.get(null);
	}
	
	@Test
	public void getAllInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		PageDTO<TransportDocumentDTO> results = transportDocService.getAllInRange(authenticatedPrincipal.getBusiness().getId(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllInRangeUnauthorizedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		transportDocService.getAllInRange(getUnathorizedBusinessID(), 0, 10);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllInRangeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, ConcurrentAccessException{
		transportDocService.getAllInRange(null, 0, 10);
	}
	
	@Test
	public void getAllForClientAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithTransportDocumentsID"));
		List<AccountingDocumentDTO> actual = new ArrayList<AccountingDocumentDTO>(transportDocService.getAllForClient(clientID));
		List<AccountingDocumentDTO> expected = DTOUtils.toDTOList(new ArrayList(Client.findClient(clientID).getTransportDocuments()), DTOUtils.transportDocDTOConverter); 
		assertTrue(TestUtils.equal(expected, actual, TestUtils.transportDocumentComparator));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		transportDocService.getAllForClient(clientID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientAuthorizedClientIDNotExistTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		transportDocService.getAllForClient(-1l);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		transportDocService.getAllForClient(null);
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithTransportDocumentsID"));
		Long transportDocID = Client.findClient(clientID).getTransportDocuments().iterator().next().getId();
		transportDocService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, transportDocID);
		TransportDocument.entityManager().flush();
		assertNull(TransportDocument.findTransportDocument(transportDocID));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithTransportDocumentsID"));
		Long transportDocID = Client.findClient(clientID).getTransportDocuments().iterator().next().getId();
		transportDocService.remove(getUnathorizedBusinessID(), clientID, transportDocID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithTransportDocumentsID"));
		Long transportDocID = Client.findClient(clientID).getTransportDocuments().iterator().next().getId();
		transportDocService.remove(null, clientID, transportDocID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long transportDocID = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next().getId();
		transportDocService.remove(authenticatedPrincipal.getBusiness().getId(), null, transportDocID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeAauthorizedTransportDocIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithTransportDocumentsID"));
		transportDocService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, null);
	}
	
	@Test
	public void getAllForClientInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithTransportDocumentsID"));
		PageDTO<TransportDocumentDTO> results = transportDocService.getAllForClientInRange(clientID, 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		transportDocService.getAllForClientInRange(clientID, 0, 10);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllForClientInRangeClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		transportDocService.getAllForClientInRange(null, 0, 10);
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException{
		TransportDocument expected = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		expected.setNote("Temporary note for this transport document");
		transportDocService.update(TransportDocumentDTOFactory.toDTO(expected));
		TransportDocument.entityManager().flush();
		TransportDocument actual = TransportDocument.findTransportDocument(expected.getId());
		assertEquals(actual.getNote(), "Temporary note for this transport document");
		
	}
	
	@Test(expected = AccessDeniedException.class)
	public void updateAuthorizedTransportDocNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException{
		transportDocService.update(null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void updateAuthorizedIDNull() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException{
		TransportDocument transportDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOFactory.toDTO(transportDoc);
		transDocDTO.setId(null);
		transportDocService.update(transDocDTO);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOFactory.toDTO(TestUtils.createTransportDocument(authenticatedPrincipal.getBusiness().getNextTransportDocDocumentID()));
		transDocDTO.setClient(ClientDTOFactory.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = transportDocService.add(transDocDTO);
		TransportDocument.entityManager().flush();
		assertTrue(TestUtils.transportDocumentComparatorIgnoreID.equal(transDocDTO, TransportDocumentDTOFactory.toDTO(TransportDocument.findTransportDocument(id))));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addUnathorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOFactory.toDTO(TestUtils.createTransportDocument(Business.findBusiness(getUnathorizedBusinessID()).getNextTransportDocDocumentID()));
		transDocDTO.setClient(ClientDTOFactory.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		transportDocService.add(transDocDTO);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addAuthorizedTransportDocDTONull() throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException{
		transportDocService.add(null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addAuthorizedTransportDocDTOIDNotNull() throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOFactory.toDTO(TestUtils.createTransportDocument(authenticatedPrincipal.getBusiness().getNextTransportDocDocumentID()));
		transDocDTO.setClient(ClientDTOFactory.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		transDocDTO.setId(1l);
		transportDocService.add(transDocDTO);
	}
	
	@Test
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException, AuthorizationException, InstantiationException{
		try{
			TransportDocumentDTO transDocDTO = TransportDocumentDTOFactory.toDTO(TestUtils.createInvalidTransportDocument(authenticatedPrincipal.getBusiness().getNextTransportDocDocumentID()));
			transDocDTO.setClient(ClientDTOFactory.toDTO(authenticatedPrincipal.getBusiness().getClients().iterator().next()));
			transDocDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
			transportDocService.add(transDocDTO);
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<Field>(TestUtils.transportDocValidationFieldsMap.values());
			expected.remove(Field.accountingDocumentYear);
			expected.remove(Field.accountingDocumentDate);
			expected.remove(Field.documentID);
			expected.remove(Field.transportStartDate);
			expected.remove(Field.numberOfPackages);
			Set<Field> actual= new HashSet<Field>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertEquals(expected, actual);
		}
	}

}
