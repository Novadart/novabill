package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Transporter;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.shared.client.dto.TransporterDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.TransporterGwtService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-transporter-test-config.xml")
@Transactional
public class TransporterServiceTest extends GWTServiceTest{

	
	@Autowired
	private TransporterGwtService transporterService;
	
	@Test
	public void transporterServiceWiringTest(){
		assertNotNull(transporterService);
	}
	
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		assertEquals(transporterDesc, Transporter.findTransporter(id).getDescription());
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		transporterService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedIDNotNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException {
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		transporterDTO.setId(1l);
		transporterService.add(transporterDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnauthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		transporterService.add(transporterDTO);
	}
	
	@Test(expected = ValidationException.class)
	public void addAuthorizedValidationError1Test() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = " \t";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		transporterService.add(transporterDTO);
	}
	
	@Test(expected = ValidationException.class)
	public void addAuthorizedValidationError2Test() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		TransporterDTO transporterDTO = new TransporterDTO();
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		transporterDTO.setDescription(null);
		transporterService.add(transporterDTO);
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException {
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		Transporter.entityManager().flush();
		transporterDTO.setId(id);
		transporterDTO.setDescription("Updated transporter description");
		transporterService.update(transporterDTO);
		Transporter.entityManager().flush();
		assertEquals("Updated transporter description", Transporter.findTransporter(id).getDescription());
	}
	
	@Test(expected = DataAccessException.class)
	public void updateUnauthorizedNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		transporterService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateUnauthorizedIDNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		transporterService.update(transporterDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateUnauthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException {
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		Transporter.entityManager().flush();
		transporterDTO.setId(id);
		transporterDTO.setDescription("Updated transporter description");
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		transporterService.update(transporterDTO);
		Transporter.entityManager().flush();
		assertEquals("Updated transporter description", Transporter.findTransporter(id).getDescription());
	}
	
	@Test(expected = ValidationException.class)
	public void updateValidationErrorTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		Transporter.entityManager().flush();
		transporterDTO.setId(id);
		transporterDTO.setDescription(" ");
		transporterService.update(transporterDTO);
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		Transporter.entityManager().flush();
		transporterService.remove(authenticatedPrincipal.getBusiness().getId(), id);
		Transporter.entityManager().flush();
		assertTrue(Transporter.findTransporter(id) == null);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedBizIDNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		Transporter.entityManager().flush();
		transporterService.remove(null, id);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedIDNullTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		transporterService.remove(authenticatedPrincipal.getBusiness().getId(), null);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = transporterService.add(transporterDTO);
		Transporter.entityManager().flush();
		transporterService.remove(getUnathorizedBusinessID(), id);
	}
	
	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		TransporterDTO transporterDTO = new TransporterDTO();
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
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
		trans.setBusiness(unauthbiz);
		unauthbiz.getTransporters().add(trans);
		trans.persist();
		Transporter.entityManager().flush();
		transporterService.get(trans.getId());
	}
	
}
