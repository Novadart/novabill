package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
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

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.facade.ClientService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-client-test-config.xml")
@Transactional
public class ClientServiceTest extends GWTServiceTest {
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private BusinessService businessService;
	
	@Resource(name = "testProps")
	private HashMap<String, String> testProps;
	
	@SuppressWarnings("serial")
	private static Map<String, Field> validationFieldsMap = new HashMap<String, Field>(){{
		put("name", Field.name); put("address", Field.address); put("postcode", Field.postcode);
		put("city", Field.city); put("province", Field.province); put("country", Field.country);
		put("email", Field.email); put("phone", Field.phone); put("mobile", Field.mobile);
		put("fax", Field.fax); put("web", Field.web); put("vatID", Field.vatID); put("ssn", Field.ssn);
	}};
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}

	@Test
	public void clientServiceWiringTest(){
		assertNotNull(clientService);
	}
	
	@Test
	public void getAllAuthenticatedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		Set<ClientDTO> expected = new HashSet<ClientDTO>();
		for(Client client: authenticatedPrincipal.getBusiness().getClients())
			expected.add(ClientDTOFactory.toDTO(client));
		boolean contained = true;
		Set<ClientDTO> actual = new HashSet<ClientDTO>(businessService.getClients(authenticatedPrincipal.getBusiness().getId()));
		outer: for (ClientDTO cdto1 : actual) {
			for(ClientDTO cdto2: expected)
				if(EqualsBuilder.reflectionEquals(cdto1, cdto2, "contact") && EqualsBuilder.reflectionEquals(cdto1.getContact(), cdto2.getContact(), false))
					continue outer;
			contained = false;
			break outer;
		}
		assertTrue(contained && expected.size() == actual.size());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllUnauthenticatedTest() throws NotAuthenticatedException, ConcurrentAccessException{
		businessService.getClients(getUnathorizedBusinessID());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllUnauthenticatedNullTest() throws NotAuthenticatedException, ConcurrentAccessException{
		businessService.getClients(null);
	}
	
	@Test
	public void removeAuthenticatedTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithoutInvoicesID"));
		clientService.remove(authenticatedPrincipal.getBusiness().getId(), clientID);
		Client.entityManager().flush();
		assertNull(Client.findClient(clientID));
	}
	
	@Test(expected = DataIntegrityException.class)
	public void removeAuthenticatedDataIntegrityViolationTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, ConcurrentAccessException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		clientService.remove(authenticatedPrincipal.getBusiness().getId(), clientID);
		Client.entityManager().flush();
	}
	
	
	@Test(expected = AccessDeniedException.class)
	public void removeUnauthenticatedTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, ConcurrentAccessException{
		Long clientID = authenticatedPrincipal.getBusiness().getClients().iterator().next().getId();
		clientService.remove(getUnathorizedBusinessID(), clientID);
	}

	@Test(expected = AccessDeniedException.class)
	public void removeUnauthenticatedTestBusinessIDNull() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, ConcurrentAccessException{
		Long clientID = authenticatedPrincipal.getBusiness().getClients().iterator().next().getId();
		clientService.remove(null, clientID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeUnauthenticatedTestClientIDNull() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, ConcurrentAccessException{
		clientService.remove(getUnathorizedBusinessID(), null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeAuthenticatedNonExistingClientTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, ConcurrentAccessException{
		clientService.remove(authenticatedPrincipal.getBusiness().getId(), -1l);
	}
	
	@Test
	public void getAuthenticatedTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = authenticatedPrincipal.getBusiness().getClients().iterator().next().getId();
		ClientDTO expectedDTO = ClientDTOFactory.toDTO(Client.findClient(clientID));
		ClientDTO actualDTO = clientService.get(clientID);
		assertTrue(EqualsBuilder.reflectionEquals(expectedDTO, actualDTO, "contact") && EqualsBuilder.reflectionEquals(expectedDTO.getContact(), actualDTO.getContact(), false));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getUnauthenticatedTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException{
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId(); 
		clientService.get(clientID);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getUnauthenticatedClientIDNullTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException{
		clientService.get(null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAuthenticatedNonExistingClientTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException{
		clientService.get(-1l);
	}
	
	@Test
	public void addAuthenticatedTest() throws NotAuthenticatedException, ConcurrentAccessException, AuthorizationException, ValidationException{
		Client expectedClient = TestUtils.createClient();
		expectedClient.setBusiness(authenticatedPrincipal.getBusiness());
		Long clientID = clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOFactory.toDTO(expectedClient));
		expectedClient.setId(clientID);
		Client.entityManager().flush();
		Client actualClient = Client.findClient(clientID);
		assertTrue(EqualsBuilder.reflectionEquals(expectedClient, actualClient, "contact", "invoices", "estimations", "creditNotes", "transportDocuments", "business", "version"));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addAuthenticatedNullClient() throws NotAuthenticatedException, ConcurrentAccessException, AuthorizationException, ValidationException{
		clientService.add(authenticatedPrincipal.getBusiness().getId(), null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addAuthenticatedNotNullClientID() throws NotAuthenticatedException, ConcurrentAccessException, AuthorizationException, ValidationException{
		Client client = TestUtils.createClient();
		client.setId(100l);
		clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOFactory.toDTO(client));
	}
	
	@Test
	public void addAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException, AuthorizationException{
		Client client = TestUtils.createClient();
		for(String key: validationFieldsMap.keySet()){
			BeanUtils.setProperty(client, key, StringUtils.leftPad("1", 1000, '1'));
		}
		try{
			clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOFactory.toDTO(client));
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<Field>(validationFieldsMap.values());
			Set<Field> actual= new HashSet<Field>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertEquals(expected, actual);
		}
	}
	
	@Test
	public void updateAuthenticatedTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException, ValidationException{
		Long clientID = authenticatedPrincipal.getBusiness().getClients().iterator().next().getId();
		Client expectedClient = Client.findClient(clientID);
		expectedClient.setName("Temporary name for this company");
		clientService.update(authenticatedPrincipal.getBusiness().getId(), ClientDTOFactory.toDTO(expectedClient));
		Client.entityManager().flush();
		Client actualClient = Client.findClient(clientID);
		assertEquals(actualClient.getName(), "Temporary name for this company");
	}
	
	@Test(expected = AccessDeniedException.class)
	public void updateAuthenticatedClientIDNull() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException, ValidationException{
		clientService.update(authenticatedPrincipal.getBusiness().getId(), ClientDTOFactory.toDTO(TestUtils.createClient()));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void updateAuthenticatedClientNull() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException, ValidationException{
		clientService.update(authenticatedPrincipal.getBusiness().getId(), null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void updateUnauthenticatedClient() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ConcurrentAccessException, ValidationException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		client.setName("Temporary name for this company");
		clientService.update(getUnathorizedBusinessID(), ClientDTOFactory.toDTO(client));
	}
	
	@Test
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		for(String key: validationFieldsMap.keySet()){
			BeanUtils.setProperty(client, key, StringUtils.leftPad("1", 1000, '1'));
		}
		try{
			clientService.update(authenticatedPrincipal.getBusiness().getId(), ClientDTOFactory.toDTO(client));
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<Field>(validationFieldsMap.values());
			Set<Field> actual= new HashSet<Field>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertEquals(expected, actual);
		}
	}
	
}
