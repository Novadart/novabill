package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
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
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.TransportDocumentGwtService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-transportdocument-test-config.xml")
@Transactional
@ActiveProfiles("dev")
public class TransportDocumentServiceTest extends GWTServiceTest {
	
	@Autowired
	private TransportDocumentGwtService transportDocService;
	
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
	public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long transportDocID = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next().getId();
		TransportDocumentDTO expectedDTO = TransportDocumentDTOFactory.toDTO(TransportDocument.findTransportDocument(transportDocID), true);
		TransportDocumentDTO actualDTO = transportDocService.get(transportDocID);
		assertTrue(TestUtils.transportDocumentComparator.equal(actualDTO, expectedDTO));
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long transportDocID = Business.findBusiness(getUnathorizedBusinessID()).getTransportDocuments().iterator().next().getId();
		transportDocService.get(transportDocID);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAuthorizedTransportDocIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		transportDocService.get(null);
	}
	
	@Test
	public void getAllInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		PageDTO<TransportDocumentDTO> results = transportDocService.getAllInRange(authenticatedPrincipal.getBusiness().getId(), getYear(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		transportDocService.getAllInRange(getUnathorizedBusinessID(), getYear(), 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException{
		transportDocService.getAllInRange(null, getYear(), 0, 10);
	}
	
	@Test
	public void getAllForClientAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		List<AccountingDocumentDTO> actual = new ArrayList<AccountingDocumentDTO>(transportDocService.getAllForClient(clientID, getYear()));
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<AccountingDocumentDTO> expected = DTOUtils.toDTOList(new ArrayList(Client.findClient(clientID).getTransportDocuments()), DTOUtils.transportDocDTOConverter, false); 
		assertTrue(TestUtils.equal(expected, actual, TestUtils.transportDocumentComparator));
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		transportDocService.getAllForClient(clientID, getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNotExistTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		transportDocService.getAllForClient(-1l, getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		transportDocService.getAllForClient(null, getYear());
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		Long transportDocID = Client.findClient(clientID).getTransportDocuments().iterator().next().getId();
		transportDocService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, transportDocID);
		TransportDocument.entityManager().flush();
		assertNull(TransportDocument.findTransportDocument(transportDocID));
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		Long transportDocID = Client.findClient(clientID).getTransportDocuments().iterator().next().getId();
		transportDocService.remove(getUnathorizedBusinessID(), clientID, transportDocID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		Long transportDocID = Client.findClient(clientID).getTransportDocuments().iterator().next().getId();
		transportDocService.remove(null, clientID, transportDocID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long transportDocID = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next().getId();
		transportDocService.remove(authenticatedPrincipal.getBusiness().getId(), null, transportDocID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAauthorizedTransportDocIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		transportDocService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, null);
	}
	
	@Test
	public void getAllForClientInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		PageDTO<TransportDocumentDTO> results = transportDocService.getAllForClientInRange(clientID, getYear(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		transportDocService.getAllForClientInRange(clientID, getYear(), 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		transportDocService.getAllForClientInRange(null, getYear(), 0, 10);
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		TransportDocument expected = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		expected.setNote("Temporary note for this transport document");
		transportDocService.update(TransportDocumentDTOFactory.toDTO(expected, true));
		TransportDocument.entityManager().flush();
		TransportDocument actual = TransportDocument.findTransportDocument(expected.getId());
		assertEquals(actual.getNote(), "Temporary note for this transport document");
		
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedTransportDocNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		transportDocService.update(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedIDNull() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		TransportDocument transportDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOFactory.toDTO(transportDoc, true);
		transDocDTO.setId(null);
		transportDocService.update(transDocDTO);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOFactory.toDTO(TestUtils.createTransportDocument(authenticatedPrincipal.getBusiness().getNextTransportDocDocumentID()), true);
		transDocDTO.setClient(ClientDTOFactory.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = transportDocService.add(transDocDTO);
		TransportDocument.entityManager().flush();
		assertTrue(TestUtils.transportDocumentComparatorIgnoreID.equal(transDocDTO, TransportDocumentDTOFactory.toDTO(TransportDocument.findTransportDocument(id), true)));
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnathorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOFactory.toDTO(TestUtils.createTransportDocument(Business.findBusiness(getUnathorizedBusinessID()).getNextTransportDocDocumentID()), true);
		transDocDTO.setClient(ClientDTOFactory.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		transportDocService.add(transDocDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedTransportDocDTONull() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException{
		transportDocService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedTransportDocDTOIDNotNull() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOFactory.toDTO(TestUtils.createTransportDocument(authenticatedPrincipal.getBusiness().getNextTransportDocDocumentID()), true);
		transDocDTO.setClient(ClientDTOFactory.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		transDocDTO.setId(1l);
		transportDocService.add(transDocDTO);
	}
	
	@Test
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, AuthorizationException, InstantiationException{
		try{
			TransportDocumentDTO transDocDTO = TransportDocumentDTOFactory.toDTO(TestUtils.createInvalidTransportDocument(authenticatedPrincipal.getBusiness().getNextTransportDocDocumentID()), true);
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
