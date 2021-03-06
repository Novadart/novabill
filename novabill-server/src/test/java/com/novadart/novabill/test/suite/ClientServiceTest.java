package com.novadart.novabill.test.suite;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.novadart.novabill.aspect.logging.DBLoggerAspect;
import com.novadart.novabill.domain.*;
import com.novadart.novabill.domain.dto.transformer.ClientAddressDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.ClientDTOTransformer;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.data.PriceListConstants;
import com.novadart.novabill.shared.client.dto.ClientAddressDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.facade.BusinessGwtService;
import com.novadart.novabill.shared.client.facade.ClientGwtService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-client-test-config.xml")
@Transactional
@DirtiesContext
@ActiveProfiles("dev")
public class ClientServiceTest extends ServiceTest {
	
	@Autowired
	private ClientGwtService clientService;
	
	@Autowired
	private BusinessGwtService businessService;
	
	@Autowired
	private SimpleValidator validator;
	
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
	public void getAllAuthenticatedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Set<ClientDTO> expected = new HashSet<ClientDTO>();
		for(Client client: authenticatedPrincipal.getBusiness().getClients())
			expected.add(ClientDTOTransformer.toDTO(client));
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
	
	@Test(expected = DataAccessException.class)
	public void getAllUnauthenticatedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessService.getClients(getUnathorizedBusinessID());
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllUnauthenticatedNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		businessService.getClients(null);
	}
	
	@Test
	public void removeAuthenticatedTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, JsonParseException, JsonMappingException, IOException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithoutInvoicesID"));
		String name = Client.findClient(clientID).getName();
		boolean result = clientService.remove(authenticatedPrincipal.getBusiness().getId(), clientID);
		Client.entityManager().flush();
		assertEquals(true, result);
		assertNull(Client.findClient(clientID));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.CLIENT, rec.getEntityType());
		assertEquals(clientID, rec.getEntityID());
		assertEquals(OperationType.DELETE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(name, details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(true, rec.isReferringToDeletedEntity());
	}
	
	@Test
	public void removeAuthenticatedDataIntegrityViolationTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		boolean result = clientService.remove(authenticatedPrincipal.getBusiness().getId(), clientID);
		Client.entityManager().flush();
		assertEquals(false, result);
	}
	
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthenticatedTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		Long clientID = authenticatedPrincipal.getBusiness().getClients().iterator().next().getId();
		clientService.remove(getUnathorizedBusinessID(), clientID);
	}

	@Test(expected = DataAccessException.class)
	public void removeUnauthenticatedTestBusinessIDNull() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		Long clientID = authenticatedPrincipal.getBusiness().getClients().iterator().next().getId();
		clientService.remove(null, clientID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthenticatedTestClientIDNull() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		clientService.remove(getUnathorizedBusinessID(), null);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthenticatedNonExistingClientTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		clientService.remove(authenticatedPrincipal.getBusiness().getId(), -1l);
	}
	
	@Test
	public void getAuthenticatedTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = authenticatedPrincipal.getBusiness().getClients().iterator().next().getId();
		ClientDTO expectedDTO = ClientDTOTransformer.toDTO(Client.findClient(clientID));
		ClientDTO actualDTO = clientService.get(clientID);
		assertTrue(EqualsBuilder.reflectionEquals(expectedDTO, actualDTO, "contact") && EqualsBuilder.reflectionEquals(expectedDTO.getContact(), actualDTO.getContact(), false));
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthenticatedTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId(); 
		clientService.get(clientID);
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthenticatedClientIDNullTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, FreeUserAccessForbiddenException {
		clientService.get(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAuthenticatedNonExistingClientTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, FreeUserAccessForbiddenException {
		clientService.get(-1l);
	}
	
	@Test
	public void addAuthenticatedTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, JsonParseException, JsonMappingException, IOException{
		Client expectedClient = TestUtils.createClient();
		expectedClient.setBusiness(authenticatedPrincipal.getBusiness());
		Long clientID = clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOTransformer.toDTO(expectedClient));
		expectedClient.setId(clientID);
		Client.entityManager().flush();
		Client actualClient = Client.findClient(clientID);
		assertTrue(EqualsBuilder.reflectionEquals(expectedClient, actualClient, "addresses", "contact", "invoices",
				"estimations", "creditNotes", "transportDocuments", "business", "version", "defaultPriceList", "defaultPaymentType", "creationTime"));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.CLIENT, rec.getEntityType());
		assertEquals(clientID, rec.getEntityID());
		assertEquals(OperationType.CREATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(actualClient.getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(PriceListConstants.DEFAULT, actualClient.getDefaultPriceList().getName());
	}
	
	@Test
	public void addAuthenticatedThinClientTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, JsonParseException, JsonMappingException, IOException{
		Client expectedClient = new Client();
		expectedClient.setName("John Doe");
		expectedClient.setBusiness(authenticatedPrincipal.getBusiness());
		Long clientID = clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOTransformer.toDTO(expectedClient));
		Client.entityManager().flush();
		Client actualClient = Client.findClient(clientID);
		assertTrue(actualClient.getName().equals("John Doe"));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.CLIENT, rec.getEntityType());
		assertEquals(clientID, rec.getEntityID());
		assertEquals(OperationType.CREATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(actualClient.getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(PriceListConstants.DEFAULT, actualClient.getDefaultPriceList().getName());
	}
	
	
	@Test(expected = DataAccessException.class)
	public void addAuthenticatedNullClient() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException{
		clientService.add(authenticatedPrincipal.getBusiness().getId(), null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthenticatedNotNullClientID() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException{
		Client client = TestUtils.createClient();
		client.setId(100l);
		clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOTransformer.toDTO(client));
	}
	
	@Test(expected = Exception.class)
	public void addAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException, ValidationException {
		Client client = TestUtils.createClient();
		for(String key: validationFieldsMap.keySet()){
			BeanUtils.setProperty(client, key, StringUtils.leftPad("1", 1000, '1'));
		}
		try{
			clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOTransformer.toDTO(client));
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<Field>(validationFieldsMap.values());
			Set<Field> actual= new HashSet<Field>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertEquals(expected, actual);
			throw e;
		}
		fail();
	}
	
	@Test
	public void updateAuthenticatedTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ValidationException, JsonParseException, JsonMappingException, IOException, FreeUserAccessForbiddenException {
		Long clientID = authenticatedPrincipal.getBusiness().getClients().iterator().next().getId();
		Client expectedClient = Client.findClient(clientID);
		expectedClient.setName("Temporary name for this company");
		clientService.update(authenticatedPrincipal.getBusiness().getId(), ClientDTOTransformer.toDTO(expectedClient));
		Client.entityManager().flush();
		Client actualClient = Client.findClient(clientID);
		assertEquals(actualClient.getName(), "Temporary name for this company");
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.CLIENT, rec.getEntityType());
		assertEquals(clientID, rec.getEntityID());
		assertEquals(OperationType.UPDATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(expectedClient.getName(), details.get(DBLoggerAspect.CLIENT_NAME));
	}
	
	@Test(expected = Exception.class)
	public void updateAuthenticatedValidationErrorTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, IOException, ValidationException, FreeUserAccessForbiddenException {
		Long clientID = authenticatedPrincipal.getBusiness().getClients().iterator().next().getId();
		Client expectedClient = Client.findClient(clientID);
		expectedClient.setAddress("");
		try {
			clientService.update(authenticatedPrincipal.getBusiness().getId(), ClientDTOTransformer.toDTO(expectedClient));
		} catch (ValidationException e) {
			assertTrue(true);
			throw e;
		}
		fail();
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthenticatedClientIDNull() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException {
		clientService.update(authenticatedPrincipal.getBusiness().getId(), ClientDTOTransformer.toDTO(TestUtils.createClient()));
	}
	
	@Test(expected = Exception.class)
	public void updateAuthenticatedClientNull() throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		try {
			clientService.update(authenticatedPrincipal.getBusiness().getId(), null);
		} catch (DataAccessException e) {
			assertTrue(true);
			throw e;
		}
		fail();
	}
	
	@Test(expected = DataAccessException.class)
	public void updateUnauthenticatedClient() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		client.setName("Temporary name for this company");
		clientService.update(getUnathorizedBusinessID(), ClientDTOTransformer.toDTO(client));
	}
	
	@Test(expected = Exception.class)
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		for(String key: validationFieldsMap.keySet()){
			BeanUtils.setProperty(client, key, StringUtils.leftPad("1", 1000, '1'));
		}
		try{
			clientService.update(authenticatedPrincipal.getBusiness().getId(), ClientDTOTransformer.toDTO(client));
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<Field>(validationFieldsMap.values());
			Set<Field> actual= new HashSet<Field>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertEquals(expected, actual);
			throw e;
		}
		fail();
	}
	
	@Test
	public void updateAuthorizedDefaultPaymentTypeNotNullNullTest() throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		PaymentType paymentType = TestUtils.createPaymentType();
		client.setDefaultPaymentType(paymentType);
		paymentType.getClients().add(client);
		paymentType.persist();
		PaymentType.entityManager().flush();
		ClientDTO clientDTO = ClientDTOTransformer.toDTO(client);
		clientDTO.setDefaultPaymentTypeID(null);
		clientService.update(authenticatedPrincipal.getBusiness().getId(), clientDTO);
		assertNull(client.getDefaultPaymentType());
	}
	
	@Test
	public void updateAuthorizedDefaultPaymentTypeNotNullNotNullTest() throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		PaymentType paymentType1 = TestUtils.createPaymentType();
		client.setDefaultPaymentType(paymentType1);
		paymentType1.getClients().add(client);
		paymentType1.persist();
		PaymentType.entityManager().flush();
		PaymentType paymentType2 = TestUtils.createPaymentType();
		paymentType2.persist();
		PaymentType.entityManager().flush();
		ClientDTO clientDTO = ClientDTOTransformer.toDTO(client);
		clientDTO.setDefaultPaymentTypeID(paymentType2.getId());
		clientService.update(authenticatedPrincipal.getBusiness().getId(), clientDTO);
		assertEquals(paymentType2, client.getDefaultPaymentType());
	}
	
	@Test
	public void updateAuthorizedNullDefaultPaymentTypeNullNullTest() throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		ClientDTO clientDTO = ClientDTOTransformer.toDTO(client);
		clientDTO.setDefaultPaymentTypeID(null);
		clientService.update(authenticatedPrincipal.getBusiness().getId(), clientDTO);
		assertNull(client.getDefaultPaymentType());
	}
	
	@Test
	public void updateAuthorizedNullDefaultPaymentTypeNullNotNullTest() throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		PaymentType paymentType = TestUtils.createPaymentType();
		paymentType.persist();
		PaymentType.entityManager().flush();
		ClientDTO clientDTO = ClientDTOTransformer.toDTO(client);
		clientDTO.setDefaultPaymentTypeID(paymentType.getId());
		clientService.update(authenticatedPrincipal.getBusiness().getId(), clientDTO);
		assertEquals(paymentType, client.getDefaultPaymentType());
	}
	
	
	@Test
	public void updateAuthorizedDefaultPriceListNotNullNotNullTest() throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		PriceList priceList1 = TestUtils.createPriceList();
		client.setDefaultPriceList(priceList1);
		priceList1.getClients().add(client);
		priceList1.persist();
		PriceList.entityManager().flush();
		PriceList priceList2 = TestUtils.createPriceList();
		priceList2.persist();
		PriceList.entityManager().flush();
		ClientDTO clientDTO = ClientDTOTransformer.toDTO(client);
		clientDTO.setDefaultPriceListID(priceList2.getId());
		clientService.update(authenticatedPrincipal.getBusiness().getId(), clientDTO);
		assertEquals(priceList2, client.getDefaultPriceList());
	}
	
	@Test
	public void updateAuthorizedDefaultPriceListNullNotNullTest() throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		PriceList priceList = TestUtils.createPriceList();
		priceList.persist();
		PriceList.entityManager().flush();
		ClientDTO clientDTO = ClientDTOTransformer.toDTO(client);
		clientDTO.setDefaultPriceListID(priceList.getId());
		clientService.update(authenticatedPrincipal.getBusiness().getId(), clientDTO);
		assertEquals(priceList, client.getDefaultPriceList());
	}

	@Test
	public void updateAuthorizedDefaultDocumentIDClassNotNullNullTest() throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		DocumentIDClass docIDClass = TestUtils.createDocumentIDClass();
		client.setDefaultDocumentIDClass(docIDClass);
		docIDClass.getClients().add(client);
		docIDClass.persist();
		DocumentIDClass.entityManager().flush();
		ClientDTO clientDTO = ClientDTOTransformer.toDTO(client);
		clientDTO.setDefaultDocumentIDClassID(null);
		clientService.update(authenticatedPrincipal.getBusiness().getId(), clientDTO);
		assertNull(client.getDefaultDocumentIDClass());
	}

	@Test
	public void updateAuthorizedDefaultDocumentIDClassNotNullNotNullTest() throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		DocumentIDClass docIDClass1 = TestUtils.createDocumentIDClass();
		client.setDefaultDocumentIDClass(docIDClass1);
		docIDClass1.getClients().add(client);
		docIDClass1.persist();
		DocumentIDClass.entityManager().flush();
		DocumentIDClass docIDClass2 = TestUtils.createDocumentIDClass();
		docIDClass2.persist();
		DocumentIDClass.entityManager().flush();
		ClientDTO clientDTO = ClientDTOTransformer.toDTO(client);
		clientDTO.setDefaultDocumentIDClassID(docIDClass2.getId());
		clientService.update(authenticatedPrincipal.getBusiness().getId(), clientDTO);
		assertEquals(docIDClass2, client.getDefaultDocumentIDClass());
	}

	@Test
	public void updateAuthorizedNullDefaultDocumentIDClassNullNullTest() throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		ClientDTO clientDTO = ClientDTOTransformer.toDTO(client);
		clientDTO.setDefaultDocumentIDClassID(null);
		clientService.update(authenticatedPrincipal.getBusiness().getId(), clientDTO);
		assertNull(client.getDefaultDocumentIDClass());
	}

	@Test
	public void updateAuthorizedNullDefaultDocumentIDClassNullNotNullTest() throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		DocumentIDClass docIDClass = TestUtils.createDocumentIDClass();
		docIDClass.persist();
		DocumentIDClass.entityManager().flush();
		ClientDTO clientDTO = ClientDTOTransformer.toDTO(client);
		clientDTO.setDefaultDocumentIDClassID(docIDClass.getId());
		clientService.update(authenticatedPrincipal.getBusiness().getId(), clientDTO);
		assertEquals(docIDClass, client.getDefaultDocumentIDClass());
	}

	private ClientAddress addClientAddress(Client client) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException{
		ClientAddress clientAddress = TestUtils.createClientAddress();
		ClientAddressDTO clientAddressDTO = ClientAddressDTOTransformer.toDTO(clientAddress);
		clientAddressDTO.setClient(ClientDTOTransformer.toDTO(client));
		Long id = clientService.addClientAddress(clientAddressDTO);
		clientAddress.setId(id);
		return clientAddress;
	}
	
	@Test
	public void addClientAddressAuthorizedTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		ClientAddress clientAddress = addClientAddress(client);
		assertTrue(EqualsBuilder.reflectionEquals(ClientAddress.findClientAddress(clientAddress.getId()), clientAddress, "client", "id", "version"));
	}
	
	@Test(expected = DataAccessException.class)
	public void addClientAddressUnAuthorizedTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException{
		Client client = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next();
		addClientAddress(client);
	}
	
	@Test(expected = DataAccessException.class)
	public void addClientAddressNullTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException{
		clientService.addClientAddress(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addClientAddressIDNotNullTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		ClientAddress clientAddress = TestUtils.createClientAddress();
		ClientAddressDTO clientAddressDTO = ClientAddressDTOTransformer.toDTO(clientAddress);
		clientAddressDTO.setClient(ClientDTOTransformer.toDTO(client));
		clientAddressDTO.setId(1l);
		clientService.addClientAddress(clientAddressDTO);
	}
	
	@Test
	public void getClientAddressesAuthorizedTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		addClientAddress(client);
		List<ClientAddressDTO> addresses = clientService.getClientAddresses(client.getId());
		assertEquals(1, addresses.size());
		assertEquals(client.getAddresses().iterator().next().getId(), addresses.get(0).getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void getClientAddressesUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Client client = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next();
		clientService.getClientAddresses(client.getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void getClientAddressesNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		clientService.getClientAddresses(null);
	}
	
	@Test
	public void removeClientAddressAuthenticatedTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		ClientAddress clientAddress = addClientAddress(client);
		clientService.removeClientAddress(client.getId(), clientAddress.getId());
		assertTrue(ClientAddress.findClientAddress(clientAddress.getId()) == null);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeClientAddressNull1Test() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		clientService.removeClientAddress(client.getId(), null);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeClientAddressNull2Test() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException, ValidationException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		ClientAddress clientAddress = addClientAddress(client);
		clientService.removeClientAddress(null, clientAddress.getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void removeClientAddressUnauthorizedTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		ClientAddress clientAddress = addClientAddress(client);
		clientService.removeClientAddress(getUnathorizedBusinessID(), clientAddress.getId());
	}
	
	@Test
	public void updateClientAddressAuthorizedTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, NoSuchObjectException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		ClientAddress clientAddress = addClientAddress(client);
		clientAddress.setName("Newest administrative address");
		ClientAddressDTO clientAddressDTO = ClientAddressDTOTransformer.toDTO(clientAddress);
		clientAddressDTO.setClient(ClientDTOTransformer.toDTO(client));
		clientService.updateClientAddress(clientAddressDTO);
		assertEquals("Newest administrative address", ClientAddress.findClientAddress(clientAddress.getId()).getName());
	}
	
	@Test(expected = DataAccessException.class)
	public void updateClientAddressNullTest() throws NotAuthenticatedException, NoSuchObjectException, FreeUserAccessForbiddenException, ValidationException, DataAccessException{
		clientService.updateClientAddress(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateClientAddressAuthorizedIDNullTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, NoSuchObjectException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		ClientAddress clientAddress = addClientAddress(client);
		ClientAddressDTO clientAddressDTO = ClientAddressDTOTransformer.toDTO(clientAddress);
		clientAddressDTO.setClient(ClientDTOTransformer.toDTO(client));
		clientAddressDTO.setId(null);
		clientService.updateClientAddress(clientAddressDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateClientAddressUnauthorizedTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, NoSuchObjectException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		ClientAddress clientAddress = addClientAddress(client);
		ClientAddressDTO clientAddressDTO = ClientAddressDTOTransformer.toDTO(clientAddress);
		clientAddressDTO.setClient(ClientDTOTransformer.toDTO(Client.findClient(getUnathorizedBusinessID())));
		clientService.updateClientAddress(clientAddressDTO);
	}
	

	@Test(expected = ValidationException.class)
	public void nullTaxableFieldsAddTest() throws ValidationException{
		Client client = TestUtils.createClient();
		client.setVatID(null);
		client.setSsn(null);
		validator.validate(client);
	}
	
	@Test(expected = ValidationException.class)
	public void blankTaxableFieldsAddTest() throws ValidationException{
		Client client = TestUtils.createClient();
		client.setVatID("");
		client.setSsn("    ");
		validator.validate(client);
	}
	
}
