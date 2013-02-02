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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.domain.dto.factory.EstimationDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.EstimationService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-estimation-test-config.xml")
@Transactional
public class EstimationServiceTest extends GWTServiceTest {
	
	@Autowired
	private EstimationService estimationService;
	
	@Resource(name = "testProps")
	private HashMap<String, String> testProps;
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}
	
	@Test
	public void estimationServiceWiringTest(){
		assertNotNull(estimationService);
	}
	
	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long estimationID = authenticatedPrincipal.getBusiness().getEstimations().iterator().next().getId();
		EstimationDTO expectedDTO = EstimationDTOFactory.toDTO(Estimation.findEstimation(estimationID));
		EstimationDTO actualDTO = estimationService.get(estimationID);
		assertTrue(TestUtils.accountingDocumentComparator.equal(actualDTO, expectedDTO));
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long estimationID = Business.findBusiness(getUnathorizedBusinessID()).getEstimations().iterator().next().getId();
		estimationService.get(estimationID);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAuthorizedEstimationIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		estimationService.get(null);
	}
	
	@Test
	public void getAllInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		PageDTO<EstimationDTO> results = estimationService.getAllInRange(authenticatedPrincipal.getBusiness().getId(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		estimationService.getAllInRange(getUnathorizedBusinessID(), 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException{
		estimationService.getAllInRange(null, 0, 10);
	}
	
	@Test
	public void getAllForClientAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		List<AccountingDocumentDTO> actual = new ArrayList<AccountingDocumentDTO>(estimationService.getAllForClient(clientID));
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<AccountingDocumentDTO> expected = DTOUtils.toDTOList(new ArrayList(Client.findClient(clientID).getEstimations()), DTOUtils.estimationDTOConverter); 
		assertTrue(TestUtils.equal(expected, actual, TestUtils.accountingDocumentComparator));
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		estimationService.getAllForClient(clientID);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNotExistTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		estimationService.getAllForClient(-1l);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		estimationService.getAllForClient(null);
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		Long estimationID = Client.findClient(clientID).getEstimations().iterator().next().getId();
		estimationService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, estimationID);
		Estimation.entityManager().flush();
		assertNull(Estimation.findEstimation(estimationID));
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		Long estimationID = Client.findClient(clientID).getEstimations().iterator().next().getId();
		estimationService.remove(getUnathorizedBusinessID(), clientID, estimationID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		Long estimationID = Client.findClient(clientID).getEstimations().iterator().next().getId();
		estimationService.remove(null, clientID, estimationID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long estimationID = authenticatedPrincipal.getBusiness().getEstimations().iterator().next().getId();
		estimationService.remove(authenticatedPrincipal.getBusiness().getId(), null, estimationID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAauthorizedEstimationIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		estimationService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, null);
	}
	
	@Test
	public void getAllForClientInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		PageDTO<EstimationDTO> results = estimationService.getAllForClientInRange(clientID, 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		estimationService.getAllForClientInRange(clientID, 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		estimationService.getAllForClientInRange(null, 0, 10);
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		Estimation expectedEstimation = authenticatedPrincipal.getBusiness().getEstimations().iterator().next();
		expectedEstimation.setNote("Temporary note for this estimation");
		estimationService.update(EstimationDTOFactory.toDTO(expectedEstimation));
		Estimation.entityManager().flush();
		Estimation actualEstimation = Estimation.findEstimation(expectedEstimation.getId());
		assertEquals(actualEstimation.getNote(), "Temporary note for this estimation");
		
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedEstimationNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		estimationService.update(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedIDNull() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		Estimation estimation = authenticatedPrincipal.getBusiness().getEstimations().iterator().next();
		EstimationDTO estDTO = EstimationDTOFactory.toDTO(estimation);
		estDTO.setId(null);
		estimationService.update(estDTO);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		EstimationDTO estDTO = EstimationDTOFactory.toDTO(TestUtils.createEstimation(authenticatedPrincipal.getBusiness().getNextEstimationDocumentID()));
		estDTO.setClient(ClientDTOFactory.toDTO(client));
		estDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = estimationService.add(estDTO);
		Estimation.entityManager().flush();
		assertTrue(TestUtils.accountingDocumentComparatorIgnoreID.equal(estDTO, EstimationDTOFactory.toDTO(Estimation.findEstimation(id))));
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnathorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		EstimationDTO estDTO = EstimationDTOFactory.toDTO(TestUtils.createEstimation(Business.findBusiness(getUnathorizedBusinessID()).getNextEstimationDocumentID()));
		estDTO.setClient(ClientDTOFactory.toDTO(client));
		estDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		estimationService.add(estDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedEstimationDTONull() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException{
		estimationService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedEstimationDTOIDNotNull() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		EstimationDTO estDTO = EstimationDTOFactory.toDTO(TestUtils.createEstimation(authenticatedPrincipal.getBusiness().getNextEstimationDocumentID()));
		estDTO.setClient(ClientDTOFactory.toDTO(client));
		estDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		estDTO.setId(1l);
		estimationService.add(estDTO);
	}
	
	@Test
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, AuthorizationException, InstantiationException{
		try{
			EstimationDTO estDTO = EstimationDTOFactory.toDTO(TestUtils.createInvalidEstimation(authenticatedPrincipal.getBusiness().getNextEstimationDocumentID()));
			estDTO.setClient(ClientDTOFactory.toDTO(authenticatedPrincipal.getBusiness().getClients().iterator().next()));
			estDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
			estimationService.add(estDTO);
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<Field>(TestUtils.estimationValidationFieldsMap.values());
			expected.remove(Field.accountingDocumentYear);
			expected.remove(Field.accountingDocumentDate);
			expected.remove(Field.documentID);
			expected.remove(Field.validTill);
			Set<Field> actual= new HashSet<Field>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertEquals(expected, actual);
		}
	}
	
}
