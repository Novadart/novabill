package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.SharingPermit;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.SharingPermitDTOFactory;
import com.novadart.novabill.service.web.SharingPermitService;
import com.novadart.novabill.shared.client.dto.SharingPermitDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ActiveProfiles("dev")
public class SharingPermitServiceTest extends ServiceTest {

	@Autowired
	private SharingPermitService sharingPermitService;
	
	@Test
	public void sharingPermitAutowiredTest(){
		assertNotNull(sharingPermitService);
	}
	
	@Test
	public void addAuthorizedTest() throws ValidationException{
		SharingPermitDTO sharingPermitDTO = SharingPermitDTOFactory.toDTO(TestUtils.createSharingPermit());
		sharingPermitDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = sharingPermitService.add(sharingPermitDTO);
		SharingPermit.entityManager().flush();
		SharingPermitDTO persistedDTO = SharingPermitDTOFactory.toDTO(SharingPermit.findSharingPermit(id));
		assertTrue(EqualsBuilder.reflectionEquals(sharingPermitDTO, persistedDTO, "id", "business"));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addNulltest() throws ValidationException{
		sharingPermitService.add(null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addIDNotNullTest() throws ValidationException{
		SharingPermitDTO sharingPermitDTO = SharingPermitDTOFactory.toDTO(TestUtils.createSharingPermit());
		sharingPermitDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		sharingPermitDTO.setId(1l);
		sharingPermitService.add(sharingPermitDTO);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addUnauthorizedTest() throws ValidationException{
		SharingPermitDTO sharingPermitDTO = SharingPermitDTOFactory.toDTO(TestUtils.createSharingPermit());
		sharingPermitDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		sharingPermitService.add(sharingPermitDTO);
	}
	
	private SharingPermit addAndPermistSharingPermit(){
		SharingPermit sharingPermit = TestUtils.createSharingPermit();
		Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		sharingPermit.setBusiness(business);
		business.getSharingPermits().add(sharingPermit);
		sharingPermit.persist();
		sharingPermit.flush();
		return sharingPermit;
	}
	
	@Test
	public void removeAutorizedTest(){
		SharingPermit sharingPermit = addAndPermistSharingPermit();
		sharingPermitService.remove(authenticatedPrincipal.getBusiness().getId(), sharingPermit.getId());
		assertTrue(SharingPermit.findSharingPermit(sharingPermit.getId()) == null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeIdNullTest(){
		sharingPermitService.remove(authenticatedPrincipal.getBusiness().getId(), null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeBusinessIDNullTest(){
		SharingPermit sharingPermit = addAndPermistSharingPermit();
		sharingPermitService.remove(null, sharingPermit.getId());
	}
	
	@Test(expected = AccessDeniedException.class)
	public void removeUnauthorizedTest(){
		SharingPermit sharingPermit = addAndPermistSharingPermit();
		sharingPermitService.remove(getUnathorizedBusinessID(), sharingPermit.getId());
	}
	
	@Test
	public void getAllAuthorizedTest() throws NotAuthenticatedException, DataAccessException{
		SharingPermit sharingPermit = addAndPermistSharingPermit();
		List<SharingPermitDTO> all = sharingPermitService.getAll(authenticatedPrincipal.getBusiness().getId());
		assertTrue(1 == all.size());
		assertTrue(EqualsBuilder.reflectionEquals(SharingPermitDTOFactory.toDTO(sharingPermit), all.get(0), "id", "business"));
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllNullTest() throws NotAuthenticatedException, DataAccessException{
		sharingPermitService.getAll(null);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void getAllUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		sharingPermitService.getAll(getUnathorizedBusinessID());
	}
	
}
