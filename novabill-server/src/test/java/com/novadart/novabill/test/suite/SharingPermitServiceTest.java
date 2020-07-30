package com.novadart.novabill.test.suite;

import com.novadart.novabill.aspect.logging.DBLoggerAspect;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.domain.SharingPermit;
import com.novadart.novabill.domain.dto.transformer.BusinessDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.SharingPermitDTOTransformer;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.service.web.SharingPermitService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.SharingPermitDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@DirtiesContext
@ActiveProfiles("dev")
public class SharingPermitServiceTest extends ServiceTest {

	@Autowired
	private SharingPermitService sharingPermitService;
	
	@Autowired
	private SimpleValidator validator;
	
	@Test
	public void sharingPermitAutowiredTest(){
		assertNotNull(sharingPermitService);
		assertNotNull(validator);
	}
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}
	
	@Test
	public void addAuthorizedTest() throws ValidationException, IOException, FreeUserAccessForbiddenException, DataAccessException, NotAuthenticatedException{
		SharingPermitDTO sharingPermitDTO = SharingPermitDTOTransformer.toDTO(TestUtils.createSharingPermit());
		sharingPermitDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		Long id = sharingPermitService.add(businessID, sharingPermitDTO);
		SharingPermit.entityManager().flush();
		SharingPermitDTO persistedDTO = SharingPermitDTOTransformer.toDTO(SharingPermit.findSharingPermit(id));
		assertTrue(EqualsBuilder.reflectionEquals(sharingPermitDTO, persistedDTO, "id", "business"));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.SHARING_PERMIT, rec.getEntityType());
		assertEquals(id, rec.getEntityID());
		assertEquals(OperationType.CREATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(sharingPermitDTO.getDescription(), details.get(DBLoggerAspect.SHARING_PERMIT_DESC));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addNulltest() throws ValidationException, FreeUserAccessForbiddenException, DataAccessException, NotAuthenticatedException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		sharingPermitService.add(businessID, null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addIDNotNullTest() throws ValidationException, FreeUserAccessForbiddenException, DataAccessException, NotAuthenticatedException{
		SharingPermitDTO sharingPermitDTO = SharingPermitDTOTransformer.toDTO(TestUtils.createSharingPermit());
		sharingPermitDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		sharingPermitDTO.setId(1l);
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		sharingPermitService.add(businessID, sharingPermitDTO);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addUnauthorizedTest() throws ValidationException, FreeUserAccessForbiddenException, DataAccessException, NotAuthenticatedException{
		SharingPermitDTO sharingPermitDTO = SharingPermitDTOTransformer.toDTO(TestUtils.createSharingPermit());
		sharingPermitDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		sharingPermitService.add(businessID, sharingPermitDTO);
	}
	
	@Test
	public void removeAutorizedTest() throws IOException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		SharingPermit sharingPermit = business.getSharingPermits().iterator().next();
		sharingPermitService.remove(business.getId(), sharingPermit.getId());
		assertTrue(SharingPermit.findSharingPermit(sharingPermit.getId()) == null);
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.SHARING_PERMIT, rec.getEntityType());
		assertEquals(sharingPermit.getId(), rec.getEntityID());
		assertEquals(OperationType.DELETE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(sharingPermit.getDescription(), details.get(DBLoggerAspect.SHARING_PERMIT_DESC));
		assertEquals(true, rec.isReferringToDeletedEntity());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeIdNullTest() throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		sharingPermitService.remove(authenticatedPrincipal.getBusiness().getId(), null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeBusinessIDNullTest() throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		Long id = Business.findBusiness(authenticatedPrincipal.getBusiness().getId()).getSharingPermits().iterator().next().getId();
		sharingPermitService.remove(null, id);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeUnauthorizedTest() throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		Long id = Business.findBusiness(authenticatedPrincipal.getBusiness().getId()).getSharingPermits().iterator().next().getId();
		sharingPermitService.remove(getUnathorizedBusinessID(), id);
	}
	
	@Test
	public void getAllAuthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		SharingPermit sharingPermit = business.getSharingPermits().iterator().next();
		List<SharingPermitDTO> all = sharingPermitService.getAll(business.getId());
		assertTrue(EqualsBuilder.reflectionEquals(SharingPermitDTOTransformer.toDTO(sharingPermit), all.get(0), "id", "business"));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		sharingPermitService.getAll(null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		sharingPermitService.getAll(getUnathorizedBusinessID());
	}
	
	@Test(expected = ValidationException.class)
	public void addDuplicateSharingPermitEmailTest() throws ValidationException{
		SharingPermit persistedSharingPermit = Business.findBusiness(authenticatedPrincipal.getBusiness().getId()).getSharingPermits().iterator().next();
		SharingPermit sharingPermit = TestUtils.createSharingPermit();
		sharingPermit.setEmail(persistedSharingPermit.getEmail());
		sharingPermit.setBusiness(authenticatedPrincipal.getBusiness());
		validator.validate(sharingPermit);
	}
	
	@Test
	public void updateSharingPermitEmailTest() throws ValidationException{
		SharingPermit persistedSharingPermit = Business.findBusiness(authenticatedPrincipal.getBusiness().getId()).getSharingPermits().iterator().next();
		persistedSharingPermit.setDescription("new description");
		validator.validate(persistedSharingPermit);
		persistedSharingPermit.flush();
	}
	
}
