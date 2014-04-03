package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.domain.dto.factory.CommodityDTOFactory;
import com.novadart.novabill.domain.dto.factory.InvoiceDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.web.CommodityService;
import com.novadart.novabill.service.web.InvoiceService;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationError;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;


@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath*:service-test-config.xml")
@Transactional
@ActiveProfiles("dev")
public class RestrictionsTest extends ServiceTest {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private CommodityService commodityService;
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("risto.gligorov@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}
	
	@Test
	public void wiringTest(){
		assertNotNull(invoiceService);
		assertNotNull(commodityService);
	}
	
	@Test
	public void addInvoiceOverQuotaTest() throws NotAuthenticatedException, ValidationException, DataAccessException, DataIntegrityException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(), Invoice.class), true);
		invDTO.setClient(ClientDTOFactory.toDTO(client));
		invDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		boolean raised = false;
		try {
			invoiceService.add(invDTO);
		} catch (AuthorizationException e) {
			raised = true;
			assertEquals(AuthorizationError.NUMBER_OF_INVOICES_QUOTA_REACHED, e.getError());
		}
		assertTrue(raised);
	}
	
	
	@Test
	public void setInvoicePayedTest() throws NotAuthenticatedException, NoSuchObjectException, DataAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		Long invoiceID = client.getInvoices().iterator().next().getId();
		boolean raised = false;
		try {
			invoiceService.setPayed(authenticatedPrincipal.getBusiness().getId(), client.getId(), invoiceID, true);
		} catch (AuthorizationException e) {
			raised = true;
			assertEquals(AuthorizationError.NOT_PREMIUM_USER, e.getError());
		}
		assertTrue(raised);
	}
	
	@Test
	public void addCommodityOverQuotaTest() throws NotAuthenticatedException, ValidationException, DataAccessException, NoSuchObjectException{
		BigDecimal defaultPrice = new BigDecimal("24.95");
		CommodityDTO commodityDTO = CommodityDTOFactory.toDTO(TestUtils.createCommodity());
		TestUtils.setDefaultPrice(commodityDTO, defaultPrice);
		boolean raised = false;
		commodityDTO.setBusiness(BusinessDTOFactory.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		try {
			commodityService.add(commodityDTO);
		} catch (AuthorizationException e) {
			raised = true;
			assertEquals(AuthorizationError.NUMBER_OF_COMMODITIES_QUOTA_REACHED, e.getError());
		}
		assertTrue(raised);
	}

}
