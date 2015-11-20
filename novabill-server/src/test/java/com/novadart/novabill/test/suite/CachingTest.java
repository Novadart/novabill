package com.novadart.novabill.test.suite;

import com.novadart.novabill.aspect.CachingAspect;
import com.novadart.novabill.domain.*;
import com.novadart.novabill.domain.dto.transformer.*;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.service.web.DocumentIDClassService;
import com.novadart.novabill.service.web.InvoiceService;
import com.novadart.novabill.shared.client.data.PriceListConstants;
import com.novadart.novabill.shared.client.dto.*;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.facade.*;
import com.novadart.novabill.web.mvc.ajax.dto.EmailDTO;
import net.sf.ehcache.CacheManager;
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
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:caching-test-config.xml")
@Transactional
@ActiveProfiles("dev")
@DirtiesContext
public class CachingTest extends ServiceTest {
	
	@Resource(name = "testPL")
	private HashMap<String, String> testPL;
	
	@Autowired
	private BusinessGwtService businessGwtService;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private ClientGwtService clientService;
	
	@Autowired
	private InvoiceGwtService invoiceService;
	
	@Autowired
	private CreditNoteGwtService creditNoteService;
	
	@Autowired
	private EstimationGwtService estimationService;
	
	@Autowired
	private TransportDocumentGwtService transDocService;
	
	@Autowired
	private PaymentTypeGwtService paymentTypeService;
	
	@Autowired
	private CommodityGwtService commodityService;
	
	@Autowired
	private PriceListGwtService priceListService;
	
	@Autowired
	private TransporterGwtService transporterService;
	
	@Autowired
	private InvoiceService invoiceAjaxService;

	@Autowired
	private DocumentIDClassService docIDClassService;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
		cacheManager.getCache(CachingAspect.CLIENT_CACHE).flush();
		cacheManager.getCache(CachingAspect.INVOICE_CACHE).flush();
		cacheManager.getCache(CachingAspect.ESTIMATION_CACHE).flush();
		cacheManager.getCache(CachingAspect.CREDITNOTE_CACHE).flush();
		cacheManager.getCache(CachingAspect.TRANSPORTDOCUMENT_CACHE).flush();
		//cacheManager.getCache(CachingAspect.BUSINESS_CACHE).flush();
		cacheManager.getCache(CachingAspect.PAYMENTTYPE_CACHE).flush();
		cacheManager.getCache(CachingAspect.COMMODITY_CACHE).flush();
		cacheManager.getCache(CachingAspect.PRICELIST_CACHE).flush();
		cacheManager.getCache(CachingAspect.DOCSYEARS_CACHE).flush();
		cacheManager.getCache(CachingAspect.TRANSPORTER_CACHE).flush();
		cacheManager.getCache(CachingAspect.CLIENTADDRESS_CACHE).flush();
		cacheManager.getCache(CachingAspect.DOCUMENTIDCLASSES_CACHE).flush();
	}
	
	@Test
	public void businessGetTest() throws NotAuthenticatedException, DataAccessException{
		assertTrue(Business.findBusiness(authenticatedPrincipal.getBusiness().getId()) == Business.findBusiness(authenticatedPrincipal.getBusiness().getId()));
	}
	
	@Test
	public void businessUpdateTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		BusinessDTO business1 = businessGwtService.get(authenticatedPrincipal.getBusiness().getId());
		Business biz = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		biz.setName("Test name");
		businessGwtService.update(BusinessDTOTransformer.toDTO(biz));
		BusinessDTO business2 = businessGwtService.get(authenticatedPrincipal.getBusiness().getId());
		assertTrue(business1 != business2);
	}
	
	@Test
	public void clientGetAllCacheTest() throws NotAuthenticatedException, DataAccessException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		Set<ClientDTO> cachedClients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(clients.equals(cachedClients));
	}
	
	@Test
	public void clientRemoveCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		Integer count = businessGwtService.countClients(authenticatedPrincipal.getBusiness().getId());
		clientService.remove(authenticatedPrincipal.getBusiness().getId(), new Long(testProps.get("clientWithoutDocsID")));
		Set<ClientDTO> notCachedClients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!clients.equals(notCachedClients));
		Integer notCachedCount = businessGwtService.countClients(authenticatedPrincipal.getBusiness().getId());
		assertTrue(count.equals(notCachedCount + 1));
	}
	
	@Test
	public void  clientAddCacheTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		Integer count = businessGwtService.countClients(authenticatedPrincipal.getBusiness().getId());
		Client client = TestUtils.createClient();
		clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOTransformer.toDTO(client));
		HashSet<ClientDTO> notCachedClients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		Integer notCachedCount = businessGwtService.countClients(authenticatedPrincipal.getBusiness().getId());
		assertTrue(notCachedCount.equals(count + 1));
		assertTrue(!clients.equals(notCachedClients));
	}
	
	@Test
	public void getYearsCacheTest() throws NotAuthenticatedException, DataAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> invYears = businessService.getInvoceYears(businessID);
		List<Integer> credYears = businessService.getCreditNoteYears(businessID);
		List<Integer> estYears = businessService.getEstimationYears(businessID);
		List<Integer> tranYears = businessService.getTransportDocumentYears(businessID);
		assertTrue(invYears == businessService.getInvoceYears(businessID));
		assertTrue(credYears == businessService.getCreditNoteYears(businessID));
		assertTrue(estYears == businessService.getEstimationYears(businessID));
		assertTrue(tranYears == businessService.getTransportDocumentYears(businessID));
	}
	
	@Test
	public void  clientUpdateCacheTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, NoSuchObjectException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		Integer count = businessGwtService.countClients(authenticatedPrincipal.getBusiness().getId());
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		client.setName("The new name for this client");
		List<InvoiceDTO> invoices = businessService.getInvoices(authenticatedPrincipal.getBusiness().getId(), 2015);
		List<EstimationDTO> estimations = businessService.getEstimations(authenticatedPrincipal.getBusiness().getId(), 2015);
		List<CreditNoteDTO> credNotes = businessService.getCreditNotes(authenticatedPrincipal.getBusiness().getId(), 2015);
		List<TransportDocumentDTO> tranDocs = businessService.getTransportDocuments(authenticatedPrincipal.getBusiness().getId(), 2015);
		assertTrue(invoices == businessService.getInvoices(authenticatedPrincipal.getBusiness().getId(), 2015));
		assertTrue(estimations == businessService.getEstimations(authenticatedPrincipal.getBusiness().getId(), 2015));
		assertTrue(credNotes == businessService.getCreditNotes(authenticatedPrincipal.getBusiness().getId(), 2015));
		assertTrue(tranDocs == businessService.getTransportDocuments(authenticatedPrincipal.getBusiness().getId(), 2015));
		clientService.update(authenticatedPrincipal.getBusiness().getId(), ClientDTOTransformer.toDTO(client));
		Set<ClientDTO> notCachedClients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!clients.equals(notCachedClients));
		Integer cachedCount = businessGwtService.countClients(authenticatedPrincipal.getBusiness().getId());
		assertTrue(count.equals(cachedCount));
		assertTrue(invoices != businessService.getInvoices(authenticatedPrincipal.getBusiness().getId(), 2015));
		assertTrue(estimations != businessService.getEstimations(authenticatedPrincipal.getBusiness().getId(), 2015));
		assertTrue(credNotes != businessService.getCreditNotes(authenticatedPrincipal.getBusiness().getId(), 2015));
		assertTrue(tranDocs != businessService.getTransportDocuments(authenticatedPrincipal.getBusiness().getId(), 2015));
	}
	
	@Test
	public void businessGetInvoicesCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		List<InvoiceDTO> result = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		List<InvoiceDTO> cachedResult = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		assertTrue(result == cachedResult);
	}
	
	@Test
	public void invoiceRemoveCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> invYears = businessService.getInvoceYears(businessID);
		Set<InvoiceDTO> result = new HashSet<InvoiceDTO>(businessGwtService.getInvoices(businessID, getYear()));
		Integer countInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal totals = businessGwtService.getTotalsForYear(businessID, new Integer(testProps.get("year"))).getSecond();
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long id = null;
		for(Invoice inv: Client.findClient(clientID).getInvoices())
			if(inv.getAccountingDocumentYear().equals(getYear())){
				id = inv.getId();
				break;
			}
		invoiceService.remove(businessID, clientID, id);
		List<InvoiceDTO> nonCachedResult = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		Integer nonCachedCountInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal nonCachedTotals = businessGwtService.getTotalsForYear(businessID, new Integer(testProps.get("year"))).getSecond();
		assertTrue(result != nonCachedResult);
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear + 1));
		assertTrue(!totals.equals(nonCachedTotals));
		assertTrue(invYears != businessService.getInvoceYears(businessID));
	}
	
	@Test
	public void invoiceAddCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException, ParseException, DataIntegrityException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> invYears = businessService.getInvoceYears(businessID);
		Set<InvoiceDTO> result = new HashSet<>(businessGwtService.getInvoices(businessID, getYear()));
		Integer countInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal totals = businessGwtService.getTotalsForYear(businessID, new Integer(testProps.get("year"))).getSecond();
		
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOTransformer.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(null), Invoice.class), true);
		invDTO.setClient(ClientDTOTransformer.toDTO(client));
		invDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		invoiceService.add(invDTO);
		
		Set<InvoiceDTO> nonCachedResult = new HashSet<>(businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear()));
		Integer nonCachedCountInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal nonCachedTotals = businessGwtService.getTotalsForYear(businessID, new Integer(testProps.get("year"))).getSecond();
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear - 1));
		assertTrue(!totals.equals(nonCachedTotals));
		assertTrue(result.size() + 1 == nonCachedResult.size());
		assertTrue(!result.equals(nonCachedResult));
		assertTrue(invYears != businessService.getInvoceYears(businessID));
	}
	
	@Test
	public void invoiceUpdateCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException, DataIntegrityException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> invYears = businessService.getInvoceYears(businessID);
		Set<InvoiceDTO> result = new HashSet<>(businessGwtService.getInvoices(businessID, getYear()));
		Integer countInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal totals = businessGwtService.getTotalsForYear(businessID, new Integer(testProps.get("year"))).getSecond();
		
		Invoice inv = authenticatedPrincipal.getBusiness().getInvoicesForYear(getYear()).iterator().next();
		inv.setNote("Temporary note for this invoice");
		invoiceService.update(InvoiceDTOTransformer.toDTO(inv, true));
		Invoice.entityManager().flush();
		
		Set<InvoiceDTO> nonCachedResult = new HashSet<>(businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear()));
		Integer nonCachedCountInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal nonCachedTotals = businessGwtService.getTotalsForYear(businessID, new Integer(testProps.get("year"))).getSecond();
		assertTrue(!result.equals(nonCachedResult));
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear));
		assertTrue(totals.equals(nonCachedTotals));
		assertTrue(invYears == businessService.getInvoceYears(businessID));
	}
	
	@Test
	public void invoiceSetPayedCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> invYears = businessService.getInvoceYears(businessID);
		List<InvoiceDTO> result = businessGwtService.getInvoices(businessID, getYear());
		Integer countClients = businessGwtService.countClients(businessID);
		Integer countInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long id = null;
		for(Invoice inv: Client.findClient(clientID).getInvoices())
			if(inv.getAccountingDocumentYear().equals(getYear())){
				id = inv.getId();
				break;
			}
		invoiceService.setPayed(businessID, clientID, id, true);
		
		List<InvoiceDTO> nonCachedResult = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		Integer nonCachedCountClients = businessGwtService.countClients(businessID);
		Integer nonCachedCountInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		assertTrue(result != nonCachedResult);
		assertTrue(countClients.equals(nonCachedCountClients));
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear));
		assertTrue(invYears == businessService.getInvoceYears(businessID));
	}
	
	@Test
	public void invoicemarkViewedByClientCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> invYears = businessService.getInvoceYears(businessID);
		List<InvoiceDTO> result = businessGwtService.getInvoices(businessID, getYear());
		Integer countClients = businessGwtService.countClients(businessID);
		Integer countInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long id = null;
		for(Invoice inv: Client.findClient(clientID).getInvoices())
			if(inv.getAccountingDocumentYear().equals(getYear())){
				id = inv.getId();
				break;
			}
		invoiceAjaxService.markViewedByClient(businessID, id, System.currentTimeMillis());
		
		List<InvoiceDTO> nonCachedResult = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		Integer nonCachedCountClients = businessGwtService.countClients(businessID);
		Integer nonCachedCountInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		assertTrue(result != nonCachedResult);
		assertTrue(countClients.equals(nonCachedCountClients));
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear));
		assertTrue(invYears == businessService.getInvoceYears(businessID));
	}
	
	@Test
	public void invoiceEmailToClientCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException, NoSuchAlgorithmException, UnsupportedEncodingException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> invYears = businessService.getInvoceYears(businessID);
		List<InvoiceDTO> result = businessGwtService.getInvoices(businessID, getYear());
		Integer countClients = businessGwtService.countClients(businessID);
		Integer countInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		EmailDTO emailDTO = new EmailDTO();
		emailDTO.setTo("foo@bar.com");
		emailDTO.setReplyTo("foo@bar.it");
		emailDTO.setSubject("Test subject");
		emailDTO.setMessage("Test message");
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long id = null;
		for(Invoice inv: Client.findClient(clientID).getInvoices())
			if(inv.getAccountingDocumentYear().equals(getYear())){
				id = inv.getId();
				break;
			}
		invoiceAjaxService.email(businessID, id, emailDTO);
		List<InvoiceDTO> nonCachedResult = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		Integer nonCachedCountClients = businessGwtService.countClients(businessID);
		Integer nonCachedCountInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		assertTrue(result != nonCachedResult);
		assertTrue(countClients.equals(nonCachedCountClients));
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear));
		assertTrue(invYears == businessService.getInvoceYears(businessID));
	}
	
	@Test
	public void invoiceUpdateFailCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> invYears = businessService.getInvoceYears(businessID);
		List<InvoiceDTO> result = businessGwtService.getInvoices(businessID, getYear());
		Integer countClients = businessGwtService.countClients(businessID);
		Integer countInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal totals = businessGwtService.getTotalsForYear(businessID, new Integer(testProps.get("year"))).getSecond();
		
		try {
			Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
			inv.setPayed(null);
			invoiceService.update(InvoiceDTOTransformer.toDTO(inv, true));
			Invoice.entityManager().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<InvoiceDTO> nonCachedResult = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		Integer nonCachedCountClients = businessGwtService.countClients(businessID);
		Integer nonCachedCountInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal nonCachedTotals = businessGwtService.getTotalsForYear(businessID, new Integer(testProps.get("year"))).getSecond();
		assertTrue(result == nonCachedResult);
		assertTrue(countClients.equals(nonCachedCountClients));
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear));
		assertTrue(totals.equals(nonCachedTotals));
		assertTrue(invYears == businessService.getInvoceYears(businessID));
	}
	
	@Test
	public void creditNoteGetAllCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		List<CreditNoteDTO> result = businessGwtService.getCreditNotes(authenticatedPrincipal.getBusiness().getId(), getYear());
		List<CreditNoteDTO> cachedResult = businessGwtService.getCreditNotes(authenticatedPrincipal.getBusiness().getId(), getYear());
		assertTrue(result == cachedResult);
	}
	
	@Test
	public void creditNoteRemoveCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> credYears = businessService.getCreditNoteYears(businessID);
		List<CreditNoteDTO> result = businessGwtService.getCreditNotes(businessID, getYear());
		Long clientID = new Long(testProps.get("clientWithCreditNotesID"));
		Long id = Client.findClient(clientID).getCreditNotes().iterator().next().getId();
		CreditNote credNote = CreditNote.findCreditNote(id);
		creditNoteService.remove(businessID, clientID, id);
		List<CreditNoteDTO> nonCachedResult = businessGwtService.getCreditNotes(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size() + 1);
		assertTrue(credYears != businessService.getCreditNoteYears(businessID));
	}
	
	@Test
	public void creditNoteAddCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException, ParseException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> credYears = businessService.getCreditNoteYears(businessID);
		List<CreditNoteDTO> result = businessGwtService.getCreditNotes(businessID, getYear());
		
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		CreditNoteDTO credNoteDTO = CreditNoteDTOTransformer.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextCreditNoteDocumentID(), CreditNote.class), true);
		credNoteDTO.setClient(ClientDTOTransformer.toDTO(client));
		credNoteDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		creditNoteService.add(credNoteDTO);
		CreditNote.entityManager().flush();
		
		List<CreditNoteDTO> nonCachedResult = businessGwtService.getCreditNotes(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() + 1 == nonCachedResult.size());
		assertTrue(credYears != businessService.getCreditNoteYears(businessID));
	}
	
	@Test
	public void creditNoteUpdateCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> credYears = businessService.getCreditNoteYears(businessID);
		List<CreditNoteDTO> result = businessGwtService.getCreditNotes(businessID, getYear());
		
		CreditNote credNote = authenticatedPrincipal.getBusiness().getCreditNotes().iterator().next();
		credNote.setNote("Temporary note for this credit note");
		creditNoteService.update(CreditNoteDTOTransformer.toDTO(credNote, true));
		CreditNote.entityManager().flush();
		
		List<CreditNoteDTO> nonCachedResult = businessGwtService.getCreditNotes(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size());
		assertTrue(credYears == businessService.getCreditNoteYears(businessID));
	}
	
	@Test
	public void estimationGetAllCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		List<EstimationDTO> result = businessGwtService.getEstimations(authenticatedPrincipal.getBusiness().getId(), getYear());
		List<EstimationDTO> cachedResult = businessGwtService.getEstimations(authenticatedPrincipal.getBusiness().getId(), getYear());
		assertTrue(result == cachedResult);
	}
	
	@Test
	public void estimationRemoveCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> estYears = businessService.getEstimationYears(businessID);
		List<EstimationDTO> result = businessGwtService.getEstimations(businessID, getYear());
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		Long id = Client.findClient(clientID).getEstimations().iterator().next().getId();
		estimationService.remove(businessID, clientID, id);
		List<EstimationDTO> nonCachedResult = businessGwtService.getEstimations(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size() + 1);
		assertTrue(estYears != businessService.getEstimationYears(businessID));
	} 
	
	@Test
	public void estimationAddCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException, ParseException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> estYears = businessService.getEstimationYears(businessID);
		List<EstimationDTO> result = businessGwtService.getEstimations(businessID, getYear());
		
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		EstimationDTO estimationDTO = EstimationDTOTransformer.toDTO(TestUtils.createEstimation(authenticatedPrincipal.getBusiness().getNextEstimationDocumentID()), true);
		estimationDTO.setClient(ClientDTOTransformer.toDTO(client));
		estimationDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		estimationService.add(estimationDTO);
		
		List<EstimationDTO> nonCachedResult = businessGwtService.getEstimations(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() + 1 == nonCachedResult.size());
		assertTrue(estYears != businessService.getEstimationYears(businessID));
	}
	
	@Test
	public void estimationUpdateCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> estYears = businessService.getEstimationYears(businessID);
		List<EstimationDTO> result = businessGwtService.getEstimations(businessID, getYear());
		
		Estimation estimation = authenticatedPrincipal.getBusiness().getEstimations().iterator().next();
		estimation.setNote("Temporary note for this estimation");
		estimationService.update(EstimationDTOTransformer.toDTO(estimation, true));
		Estimation.entityManager().flush();
		
		List<EstimationDTO> nonCachedResult = businessGwtService.getEstimations(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size());
		assertTrue(estYears == businessService.getEstimationYears(businessID));
	}
	
	@Test
	public void transDocGetAllCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		List<TransportDocumentDTO> result = businessGwtService.getTransportDocuments(authenticatedPrincipal.getBusiness().getId(), getYear());
		List<TransportDocumentDTO> cachedResult = businessGwtService.getTransportDocuments(authenticatedPrincipal.getBusiness().getId(), getYear());
		assertTrue(result == cachedResult);
	}
	
	@Test
	public void transDocRemoveCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> tranYears = businessService.getTransportDocumentYears(businessID);
		List<TransportDocumentDTO> result = businessGwtService.getTransportDocuments(businessID, getYear());
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		Long id = Client.findClient(clientID).getTransportDocuments().iterator().next().getId();
		transDocService.remove(businessID, clientID, id);
		List<TransportDocumentDTO> nonCachedResult = businessGwtService.getTransportDocuments(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size() + 1);
		assertTrue(tranYears != businessService.getTransportDocumentYears(businessID));
	}
	
	@Test
	public void transDocAddCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException, ParseException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> tranYears = businessService.getTransportDocumentYears(businessID);
		List<TransportDocumentDTO> result = businessGwtService.getTransportDocuments(businessID, getYear());
		
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOTransformer.toDTO(TestUtils.createTransportDocument(authenticatedPrincipal.getBusiness().getNextTransportDocDocumentID()), true);
		transDocDTO.setClient(ClientDTOTransformer.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		transDocService.add(transDocDTO);
		TransportDocument.entityManager().flush();
		
		List<TransportDocumentDTO> nonCachedResult = businessGwtService.getTransportDocuments(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() + 1 == nonCachedResult.size());
		assertTrue(tranYears != businessService.getTransportDocumentYears(businessID));
	}
	
	@Test
	public void transDocUpdateCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException, DataIntegrityException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> tranYears = businessService.getTransportDocumentYears(businessID);
		List<TransportDocumentDTO> result = businessGwtService.getTransportDocuments(businessID, getYear());
		
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		transDoc.setNote("Temporary note for this transport document");
		transDocService.update(TransportDocumentDTOTransformer.toDTO(transDoc, true));
		TransportDocument.entityManager().flush();
		
		List<TransportDocumentDTO> nonCachedResult = businessGwtService.getTransportDocuments(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size());
		assertTrue(tranYears == businessService.getTransportDocumentYears(businessID));
	}

	@Test
	public void transDocSetInvoiceCacheTest() throws NotAuthenticatedException, DataAccessException, DataIntegrityException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<TransportDocumentDTO> result = businessGwtService.getTransportDocuments(businessID, getYear());
		
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		Invoice invoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		transDocService.setInvoice(authenticatedPrincipal.getBusiness().getId(), invoice.getId(), transDoc.getId());
		
		List<TransportDocumentDTO> nonCachedResult = businessGwtService.getTransportDocuments(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size());
	}
	
	@Test
	public void transDocClearInvoiceCacheTest() throws DataAccessException, NotAuthenticatedException, DataIntegrityException{
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		Invoice invoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		transDocService.setInvoice(authenticatedPrincipal.getBusiness().getId(), invoice.getId(), transDoc.getId());
		
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<TransportDocumentDTO> result = businessGwtService.getTransportDocuments(businessID, getYear());
		
		transDocService.clearInvoice(authenticatedPrincipal.getBusiness().getId(), transDoc.getId());
		
		List<TransportDocumentDTO> nonCachedResult = businessGwtService.getTransportDocuments(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size());
		
	}
	
	@Test
	public void invoiceGetAllUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		List<InvoiceDTO> result = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		boolean dataAccessException = false;
		try {
			businessGwtService.getInvoices(getUnathorizedBusinessID(), getYear());
		} catch (DataAccessException e) {
			dataAccessException = true;
		}
		List<InvoiceDTO> cachedResult = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		assertTrue(dataAccessException && result == cachedResult);
	}
	
	@Test
	public void paymentTypeGetAllCacheTest() throws NotAuthenticatedException, DataAccessException{
		Set<PaymentTypeDTO> paymentTypes = new HashSet<PaymentTypeDTO>(businessGwtService.getPaymentTypes(authenticatedPrincipal.getBusiness().getId()));
		Set<PaymentTypeDTO> cachedpaymentTypes = new HashSet<PaymentTypeDTO>(businessGwtService.getPaymentTypes(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(paymentTypes.equals(cachedpaymentTypes));
	}
	
	@Test
	public void paymentTypeRemoveCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException{
		Set<PaymentTypeDTO> paymentTypes = new HashSet<PaymentTypeDTO>(businessGwtService.getPaymentTypes(authenticatedPrincipal.getBusiness().getId()));
		Set<ClientDTO> clients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		paymentTypeService.remove(authenticatedPrincipal.getBusiness().getId(), authenticatedPrincipal.getBusiness().getPaymentTypes().iterator().next().getId());
		Set<PaymentTypeDTO> notCachedPaymentTypes = new HashSet<PaymentTypeDTO>(businessGwtService.getPaymentTypes(authenticatedPrincipal.getBusiness().getId()));
		Set<ClientDTO> nonCachedClients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!paymentTypes.equals(notCachedPaymentTypes));
		assertTrue(paymentTypes.size() == notCachedPaymentTypes.size() + 1);
		assertTrue(!clients.equals(nonCachedClients));
	}
	
	@Test
	public void  paymentTypeAddCacheTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException{
		Set<PaymentTypeDTO> paymentTypes = new HashSet<PaymentTypeDTO>(businessGwtService.getPaymentTypes(authenticatedPrincipal.getBusiness().getId()));
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOTransformer.toDTO(TestUtils.createPaymentType());
		paymentTypeDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		paymentTypeService.add(paymentTypeDTO);
		HashSet<PaymentTypeDTO> notCachedPaymentTypes = new HashSet<PaymentTypeDTO>(businessGwtService.getPaymentTypes(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(notCachedPaymentTypes.size() == paymentTypes.size() + 1);
		assertTrue(!paymentTypes.equals(notCachedPaymentTypes));
	}
	
	@Test
	public void  paymentTypeUpdateCacheTest() throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, NoSuchObjectException{
		Set<PaymentTypeDTO> paymentTypes = new HashSet<PaymentTypeDTO>(businessGwtService.getPaymentTypes(authenticatedPrincipal.getBusiness().getId()));
		PaymentType paymentType = authenticatedPrincipal.getBusiness().getPaymentTypes().iterator().next();
		paymentType.setName("Updated test payment type name");
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOTransformer.toDTO(paymentType);
		paymentTypeDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		paymentTypeService.update(paymentTypeDTO);
		Set<PaymentTypeDTO> notCachedPaymentTypes = new HashSet<PaymentTypeDTO>(businessGwtService.getPaymentTypes(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!paymentTypes.equals(notCachedPaymentTypes));
		assertTrue(paymentTypes.size() == notCachedPaymentTypes.size());
	}
	
	@Test
	public void transporterGetAllCacheTest() throws NotAuthenticatedException, DataAccessException{
		Set<TransporterDTO> transporters = new HashSet<TransporterDTO>(businessGwtService.getTransporters(authenticatedPrincipal.getBusiness().getId()));
		Set<TransporterDTO> cachedtransporters = new HashSet<TransporterDTO>(businessGwtService.getTransporters(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(transporters.equals(cachedtransporters));
	}
	
	@Test
	public void transporterRemoveCacheTest() throws NotAuthenticatedException, DataAccessException{
		Set<TransporterDTO> transporters = new HashSet<TransporterDTO>(businessGwtService.getTransporters(authenticatedPrincipal.getBusiness().getId()));
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		Long id = authenticatedPrincipal.getBusiness().getTransporters().iterator().next().getId();
		transporterService.remove(businessID, id);
		Set<TransporterDTO> nonCachedTransporters = new HashSet<TransporterDTO>(businessGwtService.getTransporters(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!transporters.equals(nonCachedTransporters));
		assertTrue(nonCachedTransporters.size() + 1 == transporters.size());
	}
	
	@Test
	public void transporterAddCacheTest() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException {
		Set<TransporterDTO> transporters = new HashSet<TransporterDTO>(businessGwtService.getTransporters(authenticatedPrincipal.getBusiness().getId()));
		TransporterDTO transporterDTO = new TransporterDTO();
		transporterDTO.setName("Jason");
		String transporterDesc = "Transporter description";
		transporterDTO.setDescription(transporterDesc);
		transporterDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		transporterService.add(transporterDTO);
		Set<TransporterDTO> nonCachedTransporters = new HashSet<TransporterDTO>(businessGwtService.getTransporters(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!transporters.equals(nonCachedTransporters));
		assertTrue(nonCachedTransporters.size() - 1 == transporters.size());
	}
	
	@Test
	public void transporterUpdateCache() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, NoSuchObjectException {
		Set<TransporterDTO> transporters = new HashSet<TransporterDTO>(businessGwtService.getTransporters(authenticatedPrincipal.getBusiness().getId()));
		TransporterDTO transDTO = TransporterDTOTransformer.toDTO(authenticatedPrincipal.getBusiness().getTransporters().iterator().next());
		transDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		transporterService.update(transDTO);
		Set<TransporterDTO> nonCachedTransporters = new HashSet<TransporterDTO>(businessGwtService.getTransporters(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!transporters.equals(nonCachedTransporters));
		assertTrue(nonCachedTransporters.size() == transporters.size());
	}

	@Test
	public void commodityGetAllCacheTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		Set<CommodityDTO> commodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		Set<CommodityDTO> cachedCommodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(commodities.equals(cachedCommodities));
	}
	
	@Test
	public void commodityAddCacheTest() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, NoSuchObjectException{
		Set<CommodityDTO> commodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		CommodityDTO commodityDTO = CommodityDTOTransformer.toDTO(TestUtils.createCommodity());
		commodityDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		TestUtils.setDefaultPrice(commodityDTO, new BigDecimal("19.95"));
		Long id = authenticatedPrincipal.getBusiness().getPriceLists().iterator().next().getId();
		PriceListDTO resultOne = priceListService.get(id);
		commodityService.add(commodityDTO);
		Commodity.entityManager().flush();
		Set<CommodityDTO> notCachedCommodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!commodities.equals(notCachedCommodities));
		assertTrue(commodities.size() + 1 == notCachedCommodities.size());
		assertTrue(resultOne != priceListService.get(id));
	}
	
	@Test
	public void commodityUpdateCacheTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		CommodityDTO commodityDTO = CommodityDTOTransformer.toDTO(TestUtils.createCommodity());
		commodityDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		TestUtils.setDefaultPrice(commodityDTO, new BigDecimal("19.95"));
		Long id = commodityService.add(commodityDTO);
		Commodity.entityManager().flush();
		Set<CommodityDTO> commodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		Commodity commodity = Commodity.findCommodity(id);
		commodity.setDescription("New description");
		commodityDTO = CommodityDTOTransformer.toDTO(commodity);
		commodityDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		
		Map<String, PriceDTO> prices = new HashMap<>();
		prices.put(PriceListConstants.DEFAULT, PriceDTOTransformer.toDTO(commodity.getPrices().iterator().next()));
		commodityDTO.setPrices(prices);

		Long plid = authenticatedPrincipal.getBusiness().getPriceLists().iterator().next().getId();
		PriceListDTO resultOne = priceListService.get(plid);
		
		commodityService.update(commodityDTO);
		Set<CommodityDTO> nonCachedCommodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!commodities.equals(nonCachedCommodities));
		assertTrue(commodities.size() == nonCachedCommodities.size());
		assertTrue(resultOne != priceListService.get(plid));
	}
	
	@Test
	public void commodityRemoveCacheTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		CommodityDTO commodityDTO = CommodityDTOTransformer.toDTO(TestUtils.createCommodity());
		commodityDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		TestUtils.setDefaultPrice(commodityDTO, new BigDecimal("19.95"));
		Long id = commodityService.add(commodityDTO);
		Commodity.entityManager().flush();
		Set<CommodityDTO> commodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		Long plid = authenticatedPrincipal.getBusiness().getPriceLists().iterator().next().getId();
		PriceListDTO resultOne = priceListService.get(plid);
		commodityService.remove(authenticatedPrincipal.getBusiness().getId(), id);
		Commodity.entityManager().flush();
		Set<CommodityDTO> nonCachedCommodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!commodities.equals(nonCachedCommodities));
		assertTrue(commodities.size() == nonCachedCommodities.size() + 1);
		assertTrue(resultOne != priceListService.get(plid));
	}
	
	@Test
	public void commodityAddOrRemovePriceTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		CommodityDTO commodityDTO = CommodityDTOTransformer.toDTO(TestUtils.createCommodity());
		commodityDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		TestUtils.setDefaultPrice(commodityDTO, new BigDecimal("19.95"));
		Long id = commodityService.add(commodityDTO);
		Commodity.entityManager().flush();
		Set<CommodityDTO> commodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		Commodity commodity = business.getCommodities().iterator().next();
		Price price = commodity.getPrices().iterator().next();
		price.setPriceValue(new BigDecimal("199.95"));
		Long plid = authenticatedPrincipal.getBusiness().getPriceLists().iterator().next().getId();
		PriceListDTO resultOne = priceListService.get(plid);
		commodityService.addOrUpdatePrice(business.getId(), PriceDTOTransformer.toDTO(price));
		Commodity.entityManager().flush();
		assertTrue(resultOne != priceListService.get(plid));
		//Set<CommodityDTO> nonCachedCommodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		//assertTrue(!commodities.equals(nonCachedCommodities));
	}
	
	@Test
	public void priceListGetCacheTest() throws NotAuthenticatedException, NoSuchObjectException, DataAccessException{
		Long id = authenticatedPrincipal.getBusiness().getPriceLists().iterator().next().getId();
		PriceListDTO priceListDTO = priceListService.get(id);
		PriceListDTO cachedPriceListDTO = priceListService.get(id);
		assertTrue(priceListDTO == cachedPriceListDTO);
	}
	
	@Test
	public void priceListGetAllCacheTest() throws NotAuthenticatedException, DataAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<PriceListDTO> result = priceListService.getAll(businessID);
		List<PriceListDTO> cachedResult = priceListService.getAll(businessID);
		assertTrue(result == cachedResult);
	}
	
	@Test
	public void priceListUpdateCacheTest() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, NoSuchObjectException{
		Long id = authenticatedPrincipal.getBusiness().getPriceLists().iterator().next().getId();
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<PriceListDTO> resultAll = priceListService.getAll(businessID);
		PriceListDTO resultOne = priceListService.get(id);
		PriceListDTO pl = PriceListDTOTransformer.toDTO(PriceList.findPriceList(id), null);
		pl.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		priceListService.update(pl);
		assertTrue(resultAll != priceListService.getAll(businessID));
		assertTrue(resultOne != priceListService.get(id));
	}
	
	
	@Test(expected = DataAccessException.class)
	public void priceListRemoveCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException, ValidationException, FreeUserAccessForbiddenException{
		PriceListDTO priceListDTO = PriceListDTOTransformer.toDTO(TestUtils.createPriceList(), null);
		priceListDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = priceListService.add(priceListDTO);
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<PriceListDTO> resultAll = priceListService.getAll(businessID);
		PriceListDTO resultOne = priceListService.get(id);
		priceListService.remove(businessID, id);
		PriceList.entityManager().flush();
		assertTrue(resultAll != priceListService.getAll(businessID));
		assertTrue(resultOne != null);
		priceListService.get(id);
	}
	
	@Test
	public void priceListCloneCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException, ValidationException, FreeUserAccessForbiddenException{
		Long id = Long.parseLong(testPL.get(authenticatedPrincipal.getUsername()));
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<PriceListDTO> resultAll = priceListService.getAll(businessID);
		assertTrue(resultAll == priceListService.getAll(businessID));
		priceListService.clonePriceList(businessID, id, "Cloned price list" + System.currentTimeMillis());
		PriceList.entityManager().flush();
		assertTrue(resultAll != priceListService.getAll(businessID));
	}
	
	@Test
	public void priceListAddCacheTest() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<PriceListDTO> resultAll = priceListService.getAll(businessID);
		PriceListDTO priceListDTO = PriceListDTOTransformer.toDTO(TestUtils.createPriceList(), null);
		priceListDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		priceListService.add(priceListDTO);
		assertTrue(resultAll != priceListService.getAll(businessID));
	}
	
	@Test
	public void priceListAddOrUpdateCacheTest() throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, ValidationException, FreeUserAccessForbiddenException{
		Long id = authenticatedPrincipal.getBusiness().getPriceLists().iterator().next().getId();
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		PriceListDTO resultOne = priceListService.get(id);
		PriceDTO priceDTO = PriceDTOTransformer.toDTO(PriceList.findPriceList(id).getPrices().iterator().next());
		commodityService.addOrUpdatePrice(businessID, priceDTO);
		assertTrue(resultOne != priceListService.get(id));
	}
	
	
	@Test
	public void docYearsGetTest() throws NotAuthenticatedException, DataAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		assertTrue(businessService.getInvoceYears(businessID) == businessService.getInvoceYears(businessID));
		assertTrue(businessService.getEstimationYears(businessID) == businessService.getEstimationYears(businessID));
		assertTrue(businessService.getTransportDocumentYears(businessID) == businessService.getTransportDocumentYears(businessID));
		assertTrue(businessService.getCreditNoteYears(businessID) == businessService.getCreditNoteYears(businessID));
	}
	
	private ClientAddress addClientAddress(Client client){
		ClientAddress clientAddress = TestUtils.createClientAddress();
		clientAddress.setClient(client);
		client.getAddresses().add(clientAddress);
		ClientAddress.entityManager().flush();
		return clientAddress;
	}
	
	@Test
	public void getClientAddressesTest() throws NotAuthenticatedException, DataAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		addClientAddress(client);
		Long clientID = client.getId();
		assertTrue(clientService.getClientAddresses(clientID) == clientService.getClientAddresses(clientID));
	}
	
	@Test
	public void addClientAddressTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException, ValidationException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		addClientAddress(client);
		Long clientID = client.getId();
		List<ClientAddressDTO> uncachedRes = clientService.getClientAddresses(clientID);
		assertTrue(uncachedRes == clientService.getClientAddresses(clientID));
		ClientAddressDTO clientAddressDTO = ClientAddressDTOTransformer.toDTO(TestUtils.createClientAddress());
		clientAddressDTO.setClient(ClientDTOTransformer.toDTO(client));
		clientService.addClientAddress(clientAddressDTO);
		assertTrue(uncachedRes != clientService.getClientAddresses(clientID));
	}
	
	@Test
	public void removeClientAddressTest() throws NotAuthenticatedException, DataAccessException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		Long clientAddrID = addClientAddress(client).getId();
		Long clientID = client.getId();
		List<ClientAddressDTO> uncachedRes = clientService.getClientAddresses(clientID);
		assertTrue(uncachedRes == clientService.getClientAddresses(clientID));
		clientService.removeClientAddress(clientID, clientAddrID);
		assertTrue(uncachedRes != clientService.getClientAddresses(clientID));
	}
	
	@Test
	public void updateClientAddressTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException, ValidationException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		Long clientAddressID = addClientAddress(client).getId();
		Long clientID = client.getId();
		List<ClientAddressDTO> uncachedRes = clientService.getClientAddresses(clientID);
		assertTrue(uncachedRes == clientService.getClientAddresses(clientID));
		ClientAddressDTO clientAddressDTO = ClientAddressDTOTransformer.toDTO(ClientAddress.findClientAddress(clientAddressID));
		clientAddressDTO.setClient(ClientDTOTransformer.toDTO(client));
		clientService.updateClientAddress(clientAddressDTO);
		assertTrue(uncachedRes != clientService.getClientAddresses(clientID));
	}


	@Test
	public void docIDClassAddCacheTest() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, NoSuchObjectException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		Set<DocumentIDClassDTO> docIDClasses = new HashSet<DocumentIDClassDTO>(businessService.getDocumentIdClasses(businessID));
		DocumentIDClassDTO docIDClassDTO = DocumentIDClassDTOTransformer.toDTO(TestUtils.createDocumentIDClass());
		docIDClassDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(authenticatedPrincipal.getBusiness().getId())));
		Long id = authenticatedPrincipal.getBusiness().getPriceLists().iterator().next().getId();
		docIDClassService.add(businessID, docIDClassDTO);
		DocumentIDClass.entityManager().flush();
		Set<DocumentIDClassDTO> notCachedDocIDClasses = new HashSet<DocumentIDClassDTO>(businessService.getDocumentIdClasses(businessID));
		assertTrue(!docIDClasses.equals(notCachedDocIDClasses));
		assertTrue(docIDClasses.size() + 1 == notCachedDocIDClasses.size());
	}

	@Test
	public void docIDClassUpdateCacheTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		DocumentIDClassDTO docIDClassDTO = DocumentIDClassDTOTransformer.toDTO(TestUtils.createDocumentIDClass());
		docIDClassDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(businessID)));
		Long id = docIDClassService.add(businessID, docIDClassDTO);
		DocumentIDClass.entityManager().flush();
		Set<DocumentIDClassDTO> docIDClasses = new HashSet<DocumentIDClassDTO>(businessService.getDocumentIdClasses(businessID));
		DocumentIDClass docIDClass = DocumentIDClass.findDocumentIDClass(id);
		docIDClass.setSuffix("bbis");
		docIDClassDTO = DocumentIDClassDTOTransformer.toDTO(docIDClass);
		docIDClassDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(businessID)));

		docIDClassService.update(businessID, docIDClassDTO);
		Set<DocumentIDClassDTO> nonCachedDocIdClasses = new HashSet<DocumentIDClassDTO>(businessService.getDocumentIdClasses(businessID));
		assertTrue(!docIDClasses.equals(nonCachedDocIdClasses));
		assertTrue(docIDClasses.size() == nonCachedDocIdClasses.size());
	}

	@Test
	public void docIDClassRemoveCacheTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		DocumentIDClassDTO docClassIdDTO = DocumentIDClassDTOTransformer.toDTO(TestUtils.createDocumentIDClass());
		docClassIdDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(businessID)));
		Long id = docIDClassService.add(businessID, docClassIdDTO);
		DocumentIDClass.entityManager().flush();
		Set<DocumentIDClassDTO> docIDClasses = new HashSet<DocumentIDClassDTO>(businessService.getDocumentIdClasses(businessID));
		docIDClassService.remove(businessID, id);
		DocumentIDClass.entityManager().flush();
		Set<DocumentIDClassDTO> nonCachedDocIDClasses = new HashSet<DocumentIDClassDTO>(businessService.getDocumentIdClasses(businessID));
		assertTrue(!docIDClasses.equals(nonCachedDocIDClasses));
		assertTrue(docIDClasses.size() == nonCachedDocIDClasses.size() + 1);
	}
	
}
