package com.novadart.novabill.test.suite;

import com.novadart.novabill.aspect.logging.DBLoggerAspect;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.transformer.BusinessDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.ClientDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.EstimationDTOTransformer;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.PDFStorageService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.facade.EstimationGwtService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-estimation-test-config.xml")
@Transactional
@DirtiesContext
@ActiveProfiles("dev")
public class EstimationServiceTest extends ServiceTest {
	
	@Autowired
	private EstimationGwtService estimationService;

	@Autowired
	private PDFStorageService pdfStorageService;
	
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
	public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long estimationID = authenticatedPrincipal.getBusiness().getEstimations().iterator().next().getId();
		EstimationDTO expectedDTO = EstimationDTOTransformer.toDTO(Estimation.findEstimation(estimationID), true);
		EstimationDTO actualDTO = estimationService.get(estimationID);
		assertTrue(TestUtils.accountingDocumentComparator.equal(actualDTO, expectedDTO));
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long estimationID = Business.findBusiness(getUnathorizedBusinessID()).getEstimations().iterator().next().getId();
		estimationService.get(estimationID);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAuthorizedEstimationIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		estimationService.get(null);
	}
	
	@Test
	public void getAllInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		PageDTO<EstimationDTO> results = estimationService.getAllInRange(authenticatedPrincipal.getBusiness().getId(), getYear(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		estimationService.getAllInRange(getUnathorizedBusinessID(), getYear(), 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		estimationService.getAllInRange(null, getYear(), 0, 10);
	}
	
	@Test
	public void getAllForClientAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		List<AccountingDocumentDTO> actual = new ArrayList<>(estimationService.getAllForClient(clientID, getYear()));
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<AccountingDocumentDTO> expected = DTOUtils.toDTOList(new ArrayList(Client.findClient(clientID).getEstimations()), DTOUtils.estimationDTOConverter, false); 
		assertTrue(TestUtils.equal(expected, actual, TestUtils.accountingDocumentComparator));
		Client.entityManager().flush();
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		estimationService.getAllForClient(clientID, getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNotExistTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		estimationService.getAllForClient(-1l, getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		estimationService.getAllForClient(null, getYear());
	}

//	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, IOException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		Long estimationID = Client.findClient(clientID).getEstimations().iterator().next().getId();
		estimationService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, estimationID);
		Estimation.entityManager().flush();
		assertNull(Estimation.findEstimation(estimationID));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.ESTIMATION, rec.getEntityType());
		assertEquals(estimationID, rec.getEntityID());
		assertEquals(OperationType.DELETE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(Client.findClient(clientID).getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(true, rec.isReferringToDeletedEntity());
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		Long estimationID = Client.findClient(clientID).getEstimations().iterator().next().getId();
		estimationService.remove(getUnathorizedBusinessID(), clientID, estimationID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		Long estimationID = Client.findClient(clientID).getEstimations().iterator().next().getId();
		estimationService.remove(null, clientID, estimationID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long estimationID = authenticatedPrincipal.getBusiness().getEstimations().iterator().next().getId();
		estimationService.remove(authenticatedPrincipal.getBusiness().getId(), null, estimationID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAauthorizedEstimationIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		estimationService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, null);
	}
	
	@Test
	public void getAllForClientInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		PageDTO<EstimationDTO> results = estimationService.getAllForClientInRange(clientID, getYear(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		estimationService.getAllForClientInRange(clientID, getYear(), 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		estimationService.getAllForClientInRange(null, getYear(), 0, 10);
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, IOException, FreeUserAccessForbiddenException {
		Estimation expectedEstimation = authenticatedPrincipal.getBusiness().getEstimations().iterator().next();
		expectedEstimation.setNote("Temporary note for this estimation");
		estimationService.update(EstimationDTOTransformer.toDTO(expectedEstimation, true));
		Estimation.entityManager().flush();
		Estimation actualEstimation = Estimation.findEstimation(expectedEstimation.getId());
		assertEquals(actualEstimation.getNote(), "Temporary note for this estimation");
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.ESTIMATION, rec.getEntityType());
		assertEquals(expectedEstimation.getId(), rec.getEntityID());
		assertEquals(OperationType.UPDATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(expectedEstimation.getClient().getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(expectedEstimation.getDocumentID().toString(), details.get(DBLoggerAspect.DOCUMENT_ID));
		assertTrue(Files.exists(FileSystems.getDefault().getPath(actualEstimation.getDocumentPath())));
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedEstimationNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException {
		estimationService.update(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedIDNull() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException {
		Estimation estimation = authenticatedPrincipal.getBusiness().getEstimations().iterator().next();
		EstimationDTO estDTO = EstimationDTOTransformer.toDTO(estimation, true);
		estDTO.setId(null);
		estimationService.update(estDTO);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException, IOException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		EstimationDTO estDTO = EstimationDTOTransformer.toDTO(TestUtils.createEstimation(authenticatedPrincipal.getBusiness().getNextEstimationDocumentID()), true);
		estDTO.setClient(ClientDTOTransformer.toDTO(client));
		estDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = estimationService.add(estDTO);
		Estimation.entityManager().flush();
		assertTrue(TestUtils.accountingDocumentComparatorIgnoreID.equal(estDTO, EstimationDTOTransformer.toDTO(Estimation.findEstimation(id), true)));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.ESTIMATION, rec.getEntityType());
		assertEquals(id, rec.getEntityID());
		assertEquals(OperationType.CREATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(client.getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(estDTO.getDocumentID().toString(), details.get(DBLoggerAspect.DOCUMENT_ID));
		assertTrue(Files.exists(FileSystems.getDefault().getPath(Estimation.findEstimation(id).getDocumentPath())));
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnathorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		EstimationDTO estDTO = EstimationDTOTransformer.toDTO(TestUtils.createEstimation(Business.findBusiness(getUnathorizedBusinessID()).getNextEstimationDocumentID()), true);
		estDTO.setClient(ClientDTOTransformer.toDTO(client));
		estDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		estimationService.add(estDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedEstimationDTONull() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException{
		estimationService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedEstimationDTOIDNotNull() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		EstimationDTO estDTO = EstimationDTOTransformer.toDTO(TestUtils.createEstimation(authenticatedPrincipal.getBusiness().getNextEstimationDocumentID()), true);
		estDTO.setClient(ClientDTOTransformer.toDTO(client));
		estDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		estDTO.setId(1l);
		estimationService.add(estDTO);
	}
	
	@Test(expected = Exception.class)
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException, InstantiationException, ValidationException {
		try{
			EstimationDTO estDTO = EstimationDTOTransformer.toDTO(TestUtils.createInvalidEstimation(authenticatedPrincipal.getBusiness().getNextEstimationDocumentID()), true);
			estDTO.setClient(ClientDTOTransformer.toDTO(authenticatedPrincipal.getBusiness().getClients().iterator().next()));
			estDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
			estimationService.add(estDTO);
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<>(TestUtils.estimationValidationFieldsMap.values());
			expected.remove(Field.accountingDocumentYear);
			expected.remove(Field.accountingDocumentDate);
			expected.remove(Field.documentID);
			expected.remove(Field.validTill);
			Set<Field> actual= new HashSet<>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertEquals(expected, actual);
			throw e;
		}
		fail();
	}


	@Test
	public void purgeOrghanCrednotesPdfsTest() throws IllegalAccessException, InstantiationException, NotAuthenticatedException, FreeUserAccessForbiddenException, DataIntegrityException, DataAccessException, ValidationException, NoSuchObjectException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		EstimationDTO estimationDTO = EstimationDTOTransformer.toDTO(TestUtils.createEstimation(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(null)), true);
		estimationDTO.setClient(ClientDTOTransformer.toDTO(client));
		estimationDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = estimationService.add(estimationDTO);
		Estimation.entityManager().flush();
		String oldPdfPath = Estimation.findEstimation(id).getDocumentPath();
		Estimation expectedEstimation = Estimation.findEstimation(id);
		expectedEstimation.setNote("Temporary note for this estimation");
		estimationService.update(EstimationDTOTransformer.toDTO(expectedEstimation, true));
		Estimation.entityManager().flush();
		String currPdfPath = Estimation.findEstimation(id).getDocumentPath();
		assertTrue(Files.exists(FileSystems.getDefault().getPath(oldPdfPath)));
		assertTrue(Files.exists(FileSystems.getDefault().getPath(currPdfPath)));
		pdfStorageService.purgeOrphanPDFs();
		assertTrue(!Files.exists(FileSystems.getDefault().getPath(oldPdfPath)));
		assertTrue(Files.exists(FileSystems.getDefault().getPath(currPdfPath)));
	}
	
}
