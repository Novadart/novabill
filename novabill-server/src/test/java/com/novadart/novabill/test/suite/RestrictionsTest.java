package com.novadart.novabill.test.suite;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.transformer.*;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.SharingService;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.web.*;
import com.novadart.novabill.shared.client.data.FilteringDateType;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.web.mvc.PrivatePDFController;
import com.novadart.novabill.web.mvc.ajax.SharingPermitController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@DirtiesContext
@ActiveProfiles("dev")
public class RestrictionsTest extends ServiceTest {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private CommodityService commodityService;

	@Autowired
	private PaymentTypeService paymentTypeService;
	
	@Autowired
	private PriceListService priceListService;
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private SharingPermitService sharingPermitService;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private SharingService sharingService;
	
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
		assertNotNull(paymentTypeService);
		assertNotNull(priceListService);
		assertNotNull(sharingService);
	}
	
	//@Test
	public void addInvoiceOverQuotaTest() throws NotAuthenticatedException, ValidationException, DataAccessException, DataIntegrityException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOTransformer.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(null), Invoice.class), true);
		invDTO.setClient(ClientDTOTransformer.toDTO(client));
		invDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		boolean raised = false;
		try {
			invoiceService.add(invDTO);
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NUMBER_OF_INVOICES_QUOTA_REACHED, e.getError());
		}
		assertTrue(raised);
	}
	
	
	//@Test
	public void setInvoicePayedFreeUserTest() throws NotAuthenticatedException, NoSuchObjectException, DataAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		Long invoiceID = client.getInvoices().iterator().next().getId();
		boolean raised = false;
		try {
			invoiceService.setPayed(authenticatedPrincipal.getBusiness().getId(), client.getId(), invoiceID, true);
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NOT_PREMIUM_USER, e.getError());
		}
		assertTrue(raised);
	}
	
	//@Test
	public void addCommodityOverQuotaTest() throws NotAuthenticatedException, ValidationException, DataAccessException, NoSuchObjectException{
		BigDecimal defaultPrice = new BigDecimal("24.95");
		CommodityDTO commodityDTO = CommodityDTOTransformer.toDTO(TestUtils.createCommodity());
		TestUtils.setDefaultPrice(commodityDTO, defaultPrice);
		boolean raised = false;
		commodityDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		try {
			commodityService.add(commodityDTO);
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NUMBER_OF_COMMODITIES_QUOTA_REACHED, e.getError());
		}
		assertTrue(raised);
	}
	
	@Test
	public void addCommodityFreeUserTest() throws NotAuthenticatedException, ValidationException, DataAccessException, NoSuchObjectException{
		BigDecimal defaultPrice = new BigDecimal("24.95");
		CommodityDTO commodityDTO = CommodityDTOTransformer.toDTO(TestUtils.createCommodity());
		TestUtils.setDefaultPrice(commodityDTO, defaultPrice);
		boolean raised = false;
		commodityDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		try {
			commodityService.add(commodityDTO);
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NOT_PREMIUM_USER, e.getError());
		}
		assertTrue(raised);
	}
	
	//@Test
	public void addPaymentTypeOverQuotaTest() throws NotAuthenticatedException, ValidationException, DataAccessException{
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOTransformer.toDTO(TestUtils.createPaymentType());
		paymentTypeDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		boolean raised = false;
		try {
			paymentTypeService.add(paymentTypeDTO);
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NUMBER_OF_PAYMENTTYPES_QUOTA_REACHED, e.getError());
		}
		assertTrue(raised);
	}
	
	@Test
	public void addPriceListFreeUserTest() throws NotAuthenticatedException, ValidationException, DataAccessException{
		PriceListDTO priceListDTO = PriceListDTOTransformer.toDTO(TestUtils.createPriceList(), null);
		priceListDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		boolean raised = false;
		try {
			priceListService.add(priceListDTO);
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NOT_PREMIUM_USER, e.getError());
		}
		assertTrue(raised);
	}
	
	//@Test
	public void retrieveUnpaidInvoicesInRangeFreeUserTest() throws NotAuthenticatedException, DataAccessException {
		boolean raised = false;
		try {
			invoiceService.getAllUnpaidInDateRange(FilteringDateType.CREATION_DATE, null, null);
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NOT_PREMIUM_USER, e.getError());
		}
		assertTrue(raised);
	}
	
	//@Test
	public void genPaymentsProspectPDFFreeUserTest() throws NotAuthenticatedException, DataAccessException{
		PrivatePDFController controller = new PrivatePDFController();
		boolean raised = false;
		try {
			controller.getPaymentsProspectPaymentDueDatePDF(null, null, FilteringDateType.CREATION_DATE, "", false, mock(HttpServletResponse.class), null);
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NOT_PREMIUM_USER, e.getError());
		}
		assertTrue(raised);
	}
	
	
	//@Test
	public void getAllSharingPermitsFreeUserTest() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotAuthenticatedException, DataAccessException{
		SharingPermitController controller = SharingTest.initSharingPermitController(utilsService, sharingPermitService);
		boolean raised = false;
		try {
			controller.getAll(authenticatedPrincipal.getBusiness().getId());
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NOT_PREMIUM_USER, e.getError());
		}
		assertTrue(raised);
	}
	
	@Test
	public void addSharingPermitsFreeUserTest() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotAuthenticatedException, DataAccessException, ValidationException{
		SharingPermitController controller = SharingTest.initSharingPermitController(utilsService, sharingPermitService);
		boolean raised = false;
		try {
			controller.add(authenticatedPrincipal.getBusiness().getId(), false, null, null);
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NOT_PREMIUM_USER, e.getError());
		}
		assertTrue(raised);
	}
	
	@Test
	public void sendEmailSharingPermitsFreeUserTest() throws NotAuthenticatedException, DataAccessException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		SharingPermitController controller = SharingTest.initSharingPermitController(utilsService, sharingPermitService);
		boolean raised = false;
		try {
			controller.sendEmail(1l, null);
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NOT_PREMIUM_USER, e.getError());
		}
		assertTrue(raised);
	}
	
	//@Test
	public void removeSharingPermitsFreeUserTest() throws NotAuthenticatedException, DataAccessException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		SharingPermitController controller = SharingTest.initSharingPermitController(utilsService, sharingPermitService);
		boolean raised = false;
		try {
			controller.remove(authenticatedPrincipal.getBusiness().getId(), 1l);
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NOT_PREMIUM_USER, e.getError());
		}
		assertTrue(raised);
	}
	
	@Test
	public void setDefaultLayoutFreeUserTest() throws NotAuthenticatedException, DataAccessException{
		boolean raised = false;
		try {
			businessService.setDefaultLayout(authenticatedPrincipal.getBusiness().getId(), LayoutType.TIDY);
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NOT_PREMIUM_USER, e.getError());
		}
		assertTrue(raised);
	}

	@Test
	public void enableSharingTemporarilyAndNotifyParticipantTest() throws DataAccessException, FreeUserAccessForbiddenException, NotAuthenticatedException{
		Long id = authenticatedPrincipal.getBusiness().getId();
		logout();
		boolean raised = false;
		try {
			sharingService.enableSharingTemporarilyAndNotifyParticipant(Business.findBusiness(id), null, null, null);
		} catch (FreeUserAccessForbiddenException e) {
			raised = true;
			assertEquals(FreeUserAccessErrorType.NOT_PREMIUM_USER, e.getError());
		}
		assertTrue(raised);
	}
	
}
