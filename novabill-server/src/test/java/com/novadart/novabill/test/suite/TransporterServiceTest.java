package com.novadart.novabill.test.suite;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.novadart.novabill.aspect.logging.DBLoggerAspect;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.domain.Transporter;
import com.novadart.novabill.domain.dto.transformer.BusinessDTOTransformer;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.TransporterDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.TransporterGwtService;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-transporter-test-config.xml")
@Transactional

public class TransporterServiceTest extends ServiceTest{

	
	@Autowired
	private TransporterGwtService transporterService;
	
	@Test
	public void transporterServiceWiringTest(){
		assertNotNull(transporterService);
	}
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, JsonParseException, JsonMappingException, IOException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setName("Jason");
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		assertEquals(transporterDesc, Transporter.findTransporter(id).getDescription());
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.TRANSPORTER, rec.getEntityType());
		assertEquals(id, rec.getEntityID());
		assertEquals(OperationType.CREATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(transporterDTO.getName(), details.get(DBLoggerAspect.TRANSPORTER_NAME));
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
		transporterService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedIDNotNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException {
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		transporterDTO.setId(1l);
		transporterService.add(transporterDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnauthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		transporterService.add(transporterDTO);
	}
	
	@Test(expected = Exception.class)
	public void addAuthorizedValidationError1Test() throws NotAuthenticatedException, FreeUserAccessForbiddenException, DataAccessException, ValidationException {
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = " \t";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		try {
			transporterService.add(transporterDTO);
		} catch (ValidationException e) {
			assertTrue(true);
			throw e;
		}
		fail();
	}
	
	@Test(expected = Exception.class)
	public void addAuthorizedValidationError2Test() throws NotAuthenticatedException, FreeUserAccessForbiddenException, DataAccessException, ValidationException {
		TransporterDTO transporterDTO = new TransporterDTO();
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		transporterDTO.setDescription(null);
		try {
			transporterService.add(transporterDTO);
		} catch (ValidationException e) {
			assertTrue(true);
			throw e;
		}
		fail();
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException, JsonParseException, JsonMappingException, IOException {
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setName("Jason");
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		Transporter.entityManager().flush();
		transporterDTO.setId(id);
		transporterDTO.setDescription("Updated transporter description");
		transporterService.update(transporterDTO);
		Transporter.entityManager().flush();
		assertEquals("Updated transporter description", Transporter.findTransporter(id).getDescription());
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.TRANSPORTER, rec.getEntityType());
		assertEquals(id, rec.getEntityID());
		assertEquals(OperationType.UPDATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(transporterDTO.getName(), details.get(DBLoggerAspect.TRANSPORTER_NAME));
	}
	
	@Test(expected = DataAccessException.class)
	public void updateUnauthorizedNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
		transporterService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateUnauthorizedIDNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		transporterService.update(transporterDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateUnauthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setName("Jason");
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		Transporter.entityManager().flush();
		transporterDTO.setId(id);
		transporterDTO.setDescription("Updated transporter description");
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		transporterService.update(transporterDTO);
		Transporter.entityManager().flush();
		assertEquals("Updated transporter description", Transporter.findTransporter(id).getDescription());
	}
	
	@Test(expected = Exception.class)
	public void updateValidationErrorTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException, ValidationException {
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		Transporter.entityManager().flush();
		transporterDTO.setId(id);
		transporterDTO.setDescription(" ");
		try {
			transporterService.update(transporterDTO);
		} catch (ValidationException e) {
			assertTrue(true);
			throw e;
		}
		fail();
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, JsonParseException, JsonMappingException, IOException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setName("Jason");
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		Transporter.entityManager().flush();
		transporterService.remove(authenticatedPrincipal.getBusiness().getId(), id);
		Transporter.entityManager().flush();
		assertTrue(Transporter.findTransporter(id) == null);
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.TRANSPORTER, rec.getEntityType());
		assertEquals(id, rec.getEntityID());
		assertEquals(OperationType.DELETE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(transporterDTO.getName(), details.get(DBLoggerAspect.TRANSPORTER_NAME));
		assertEquals(true, rec.isReferringToDeletedEntity());
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedBizIDNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setName("Jason");
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		Transporter.entityManager().flush();
		transporterService.remove(null, id);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedIDNullTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
		transporterService.remove(authenticatedPrincipal.getBusiness().getId(), null);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setName("Jason");
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		Transporter.entityManager().flush();
		transporterService.remove(getUnathorizedBusinessID(), id);
	}
	
	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setName("Jason");
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		Transporter.entityManager().flush();
		TransporterDTO retrieved = transporterService.get(id);
		assertEquals(id, retrieved.getId());
		assertEquals(transporterDTO.getDescription(), retrieved.getDescription());
	}
	
	
	@Test(expected = DataAccessException.class)
	public void getIdNullTest() throws NotAuthenticatedException, NoSuchObjectException, DataAccessException{
		transporterService.get(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedTest() throws NotAuthenticatedException, NoSuchObjectException, DataAccessException{
		Business unauthbiz = Business.findBusiness(getUnathorizedBusinessID());
		Transporter trans = new Transporter();
		trans.setDescription("test transporter");
		trans.setName("Jason");
		trans.setBusiness(unauthbiz);
		unauthbiz.getTransporters().add(trans);
		trans.persist();
		Transporter.entityManager().flush();
		transporterService.get(trans.getId());
	}
	
}
