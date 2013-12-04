package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.CacheManager;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.aspect.CachingAspect;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.domain.dto.factory.CommodityDTOFactory;
import com.novadart.novabill.domain.dto.factory.CreditNoteDTOFactory;
import com.novadart.novabill.domain.dto.factory.EstimationDTOFactory;
import com.novadart.novabill.domain.dto.factory.InvoiceDTOFactory;
import com.novadart.novabill.domain.dto.factory.PaymentTypeDTOFactory;
import com.novadart.novabill.domain.dto.factory.TransportDocumentDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessGwtService;
import com.novadart.novabill.shared.client.facade.ClientGwtService;
import com.novadart.novabill.shared.client.facade.CommodityGwtService;
import com.novadart.novabill.shared.client.facade.CreditNoteGwtService;
import com.novadart.novabill.shared.client.facade.EstimationGwtService;
import com.novadart.novabill.shared.client.facade.InvoiceGwtService;
import com.novadart.novabill.shared.client.facade.PaymentTypeGwtService;
import com.novadart.novabill.shared.client.facade.PriceListGwtService;
import com.novadart.novabill.shared.client.facade.TransportDocumentGwtService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:caching-test-config.xml")
@Transactional
@ActiveProfiles("dev")
public class CachingTest extends GWTServiceTest {
	
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
	
	private PriceListGwtService priceListService;
	
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
		cacheManager.getCache(CachingAspect.BUSINESS_CACHE).flush();
		cacheManager.getCache(CachingAspect.PAYMENTTYPE_CACHE).flush();
		cacheManager.getCache(CachingAspect.COMMODITY_CACHE).flush();
		cacheManager.getCache(CachingAspect.PRICELIST_CACHE).flush();
		cacheManager.getCache(CachingAspect.DOCSYEARS_CACHE).flush();
	}
	
	@Test
	public void businessGetTest() throws NotAuthenticatedException, DataAccessException{
		BusinessDTO business = businessGwtService.get(authenticatedPrincipal.getBusiness().getId());
		BusinessDTO cachedBusiness = businessGwtService.get(authenticatedPrincipal.getBusiness().getId());
		assertTrue(business == cachedBusiness);
	}
	
	@Test
	public void businessUpdateTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		BusinessDTO business1 = businessGwtService.get(authenticatedPrincipal.getBusiness().getId());
		Business biz = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		biz.setName("Test name");
		businessGwtService.update(BusinessDTOFactory.toDTO(biz));
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
		Long count = businessGwtService.countClients(authenticatedPrincipal.getBusiness().getId());
		clientService.remove(authenticatedPrincipal.getBusiness().getId(), new Long(testProps.get("clientWithoutDocsID")));
		Set<ClientDTO> notCachedClients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!clients.equals(notCachedClients));
		Long notCachedCount = businessGwtService.countClients(authenticatedPrincipal.getBusiness().getId());
		assertTrue(count.equals(notCachedCount + 1));
	}
	
	@Test
	public void  clientAddCacheTest() throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		Long count = businessGwtService.countClients(authenticatedPrincipal.getBusiness().getId());
		Client client = TestUtils.createClient();
		clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOFactory.toDTO(client));
		HashSet<ClientDTO> notCachedClients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		Long notCachedCount = businessGwtService.countClients(authenticatedPrincipal.getBusiness().getId());
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
	public void  clientUpdateCacheTest() throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException, NoSuchObjectException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		Long count = businessGwtService.countClients(authenticatedPrincipal.getBusiness().getId());
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		client.setName("The new name for this client");
		clientService.update(authenticatedPrincipal.getBusiness().getId(), ClientDTOFactory.toDTO(client));
		Set<ClientDTO> notCachedClients = new HashSet<ClientDTO>(businessGwtService.getClients(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!clients.equals(notCachedClients));
		Long cachedCount = businessGwtService.countClients(authenticatedPrincipal.getBusiness().getId());
		assertTrue(count.equals(cachedCount));
	}
	
	@Test
	public void businessGetInvoicesCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		List<InvoiceDTO> result = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		List<InvoiceDTO> cachedResult = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		assertTrue(result == cachedResult);
	}
	
	@Test
	public void invoiceRemoveCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> invYears = businessService.getInvoceYears(businessID);
		Set<InvoiceDTO> result = new HashSet<InvoiceDTO>(businessGwtService.getInvoices(businessID, getYear()));
		Long countInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal totals = businessGwtService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long id = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(businessID, clientID, id);
		List<InvoiceDTO> nonCachedResult = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		Long nonCachedCountInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal nonCachedTotals = businessGwtService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		assertTrue(result != nonCachedResult);
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear + 1));
		assertTrue(!totals.equals(nonCachedTotals));
		assertTrue(invYears != businessService.getInvoceYears(businessID));
	}
	
	@Test
	public void invoiceAddCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> invYears = businessService.getInvoceYears(businessID);
		Set<InvoiceDTO> result = new HashSet<InvoiceDTO>(businessGwtService.getInvoices(businessID, getYear()));
		Long countInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal totals = businessGwtService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(), Invoice.class), true);
		invDTO.setClient(ClientDTOFactory.toDTO(client));
		invDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		invoiceService.add(invDTO);
		
		Set<InvoiceDTO> nonCachedResult = new HashSet<InvoiceDTO>(businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear()));
		Long nonCachedCountInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal nonCachedTotals = businessGwtService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear - 1));
		assertTrue(!totals.equals(nonCachedTotals));
		assertTrue(result.size() + 1 == nonCachedResult.size());
		assertTrue(!result.equals(nonCachedResult));
		assertTrue(invYears != businessService.getInvoceYears(businessID));
	}
	
	@Test
	public void invoiceUpdateCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> invYears = businessService.getInvoceYears(businessID);
		Set<InvoiceDTO> result = new HashSet<InvoiceDTO>(businessGwtService.getInvoices(businessID, getYear()));
		Long countInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal totals = businessGwtService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		
		Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		inv.setNote("Temporary note for this invoice");
		invoiceService.update(InvoiceDTOFactory.toDTO(inv, true));
		Invoice.entityManager().flush();
		
		Set<InvoiceDTO> nonCachedResult = new HashSet<InvoiceDTO>(businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear()));
		Long nonCachedCountInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal nonCachedTotals = businessGwtService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		assertTrue(!result.equals(nonCachedResult));
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear));
		assertTrue(totals.equals(nonCachedTotals));
		assertTrue(invYears == businessService.getInvoceYears(businessID));
	}
	
	@Test
	public void invoiceSetPayedCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> invYears = businessService.getInvoceYears(businessID);
		List<InvoiceDTO> result = businessGwtService.getInvoices(businessID, getYear());
		Long countClients = businessGwtService.countClients(businessID);
		Long countInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long id = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.setPayed(businessID, clientID, id, true);
		
		List<InvoiceDTO> nonCachedResult = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		Long nonCachedCountClients = businessGwtService.countClients(businessID);
		Long nonCachedCountInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		assertTrue(result != nonCachedResult);
		assertTrue(countClients.equals(nonCachedCountClients));
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear));
		assertTrue(invYears == businessService.getInvoceYears(businessID));
	}
	
	@Test
	public void invoiceUpdateFailCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> invYears = businessService.getInvoceYears(businessID);
		List<InvoiceDTO> result = businessGwtService.getInvoices(businessID, getYear());
		Long countClients = businessGwtService.countClients(businessID);
		Long countInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal totals = businessGwtService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		
		try {
			Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
			inv.setNote(StringUtils.leftPad("1", 2000, '1'));
			invoiceService.update(InvoiceDTOFactory.toDTO(inv, true));
			Invoice.entityManager().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<InvoiceDTO> nonCachedResult = businessGwtService.getInvoices(authenticatedPrincipal.getBusiness().getId(), getYear());
		Long nonCachedCountClients = businessGwtService.countClients(businessID);
		Long nonCachedCountInvsYear = businessGwtService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal nonCachedTotals = businessGwtService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
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
	public void creditNoteAddCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> credYears = businessService.getCreditNoteYears(businessID);
		List<CreditNoteDTO> result = businessGwtService.getCreditNotes(businessID, getYear());
		
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		CreditNoteDTO credNoteDTO = CreditNoteDTOFactory.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextCreditNoteDocumentID(), CreditNote.class), true);
		credNoteDTO.setClient(ClientDTOFactory.toDTO(client));
		credNoteDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		creditNoteService.add(credNoteDTO);
		CreditNote.entityManager().flush();
		
		List<CreditNoteDTO> nonCachedResult = businessGwtService.getCreditNotes(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() + 1 == nonCachedResult.size());
		assertTrue(credYears != businessService.getCreditNoteYears(businessID));
	}
	
	@Test
	public void creditNoteUpdateCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> credYears = businessService.getCreditNoteYears(businessID);
		List<CreditNoteDTO> result = businessGwtService.getCreditNotes(businessID, getYear());
		
		CreditNote credNote = authenticatedPrincipal.getBusiness().getCreditNotes().iterator().next();
		credNote.setNote("Temporary note for this credit note");
		creditNoteService.update(CreditNoteDTOFactory.toDTO(credNote, true));
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
	public void estimationAddCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> estYears = businessService.getEstimationYears(businessID);
		List<EstimationDTO> result = businessGwtService.getEstimations(businessID, getYear());
		
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		EstimationDTO estimationDTO = EstimationDTOFactory.toDTO(TestUtils.createEstimation(authenticatedPrincipal.getBusiness().getNextEstimationDocumentID()), true);
		estimationDTO.setClient(ClientDTOFactory.toDTO(client));
		estimationDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		estimationService.add(estimationDTO);
		
		List<EstimationDTO> nonCachedResult = businessGwtService.getEstimations(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() + 1 == nonCachedResult.size());
		assertTrue(estYears != businessService.getEstimationYears(businessID));
	}
	
	@Test
	public void estimationUpdateCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> estYears = businessService.getEstimationYears(businessID);
		List<EstimationDTO> result = businessGwtService.getEstimations(businessID, getYear());
		
		Estimation estimation = authenticatedPrincipal.getBusiness().getEstimations().iterator().next();
		estimation.setNote("Temporary note for this estimation");
		estimationService.update(EstimationDTOFactory.toDTO(estimation, true));
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
	public void transDocAddCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> tranYears = businessService.getTransportDocumentYears(businessID);
		List<TransportDocumentDTO> result = businessGwtService.getTransportDocuments(businessID, getYear());
		
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOFactory.toDTO(TestUtils.createTransportDocument(authenticatedPrincipal.getBusiness().getNextTransportDocDocumentID()), true);
		transDocDTO.setClient(ClientDTOFactory.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		transDocService.add(transDocDTO);
		TransportDocument.entityManager().flush();
		
		List<TransportDocumentDTO> nonCachedResult = businessGwtService.getTransportDocuments(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() + 1 == nonCachedResult.size());
		assertTrue(tranYears != businessService.getTransportDocumentYears(businessID));
	}
	
	@Test
	public void transDocUpdateCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<Integer> tranYears = businessService.getTransportDocumentYears(businessID);
		List<TransportDocumentDTO> result = businessGwtService.getTransportDocuments(businessID, getYear());
		
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		transDoc.setNote("Temporary note for this transport document");
		transDocService.update(TransportDocumentDTOFactory.toDTO(transDoc, true));
		TransportDocument.entityManager().flush();
		
		List<TransportDocumentDTO> nonCachedResult = businessGwtService.getTransportDocuments(businessID, getYear());
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size());
		assertTrue(tranYears == businessService.getTransportDocumentYears(businessID));
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
	public void  paymentTypeAddCacheTest() throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException{
		Set<PaymentTypeDTO> paymentTypes = new HashSet<PaymentTypeDTO>(businessGwtService.getPaymentTypes(authenticatedPrincipal.getBusiness().getId()));
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOFactory.toDTO(TestUtils.createPaymentType());
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		paymentTypeService.add(paymentTypeDTO);
		HashSet<PaymentTypeDTO> notCachedPaymentTypes = new HashSet<PaymentTypeDTO>(businessGwtService.getPaymentTypes(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(notCachedPaymentTypes.size() == paymentTypes.size() + 1);
		assertTrue(!paymentTypes.equals(notCachedPaymentTypes));
	}
	
	@Test
	public void  paymentTypeUpdateCacheTest() throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException, NoSuchObjectException{
		Set<PaymentTypeDTO> paymentTypes = new HashSet<PaymentTypeDTO>(businessGwtService.getPaymentTypes(authenticatedPrincipal.getBusiness().getId()));
		PaymentType paymentType = authenticatedPrincipal.getBusiness().getPaymentTypes().iterator().next();
		paymentType.setName("Updated test payment type name");
		PaymentTypeDTO paymentTypeDTO = PaymentTypeDTOFactory.toDTO(paymentType);
		paymentTypeDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		paymentTypeService.update(paymentTypeDTO);
		Set<PaymentTypeDTO> notCachedPaymentTypes = new HashSet<PaymentTypeDTO>(businessGwtService.getPaymentTypes(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!paymentTypes.equals(notCachedPaymentTypes));
		assertTrue(paymentTypes.size() == notCachedPaymentTypes.size());
	}

	@Test
	public void commodityGetAllCacheTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		CommodityDTO commodityDTO = CommodityDTOFactory.toDTO(TestUtils.createCommodity());
		commodityService.add(commodityDTO);
		Commodity.entityManager().flush();
		Set<CommodityDTO> commodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		Set<CommodityDTO> cachedCommodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(commodities.equals(cachedCommodities));
	}
	
	@Test
	public void commodityAddCacheTest() throws NotAuthenticatedException, DataAccessException, ValidationException, AuthorizationException, NoSuchObjectException{
		Set<CommodityDTO> commodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		CommodityDTO commodityDTO = CommodityDTOFactory.toDTO(TestUtils.createCommodity());
		commodityService.add(commodityDTO);
		Commodity.entityManager().flush();
		Set<CommodityDTO> notCachedCommodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!commodities.equals(notCachedCommodities));
		assertTrue(commodities.size() + 1 == notCachedCommodities.size());
	}
	
	@Test
	public void commodityUpdateCacheTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		CommodityDTO commodityDTO = CommodityDTOFactory.toDTO(TestUtils.createCommodity());
		commodityDTO.setSku("12345");
		Long id = commodityService.add(commodityDTO);
		Commodity.entityManager().flush();
		Set<CommodityDTO> commodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		Commodity commodity = Commodity.findCommodity(id);
		commodity.setDescription("New description");
		commodityDTO = CommodityDTOFactory.toDTO(commodity);
		commodityService.update(commodityDTO);
		Set<CommodityDTO> nonCachedCommodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!commodities.equals(nonCachedCommodities));
		assertTrue(commodities.size() == nonCachedCommodities.size());
	}
	
	@Test
	public void commodityRemoveCacheTest() throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException{
		CommodityDTO commodityDTO = CommodityDTOFactory.toDTO(TestUtils.createCommodity());
		Long id = commodityService.add(commodityDTO);
		Commodity.entityManager().flush();
		Set<CommodityDTO> commodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		commodityService.remove(authenticatedPrincipal.getBusiness().getId(), id);
		Commodity.entityManager().flush();
		Set<CommodityDTO> nonCachedCommodities = new HashSet<CommodityDTO>(businessGwtService.getCommodities(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!commodities.equals(nonCachedCommodities));
		assertTrue(commodities.size() == nonCachedCommodities.size() + 1);
	}
	
	public void priceListGetCacheTest() throws NotAuthenticatedException, NoSuchObjectException, DataAccessException{
		Long id = authenticatedPrincipal.getBusiness().getPriceLists().iterator().next().getId();
		PriceListDTO priceListDTO = priceListService.get(id);
		PriceListDTO cachedPriceListDTO = priceListService.get(id);
		assertTrue(priceListDTO == cachedPriceListDTO);
	}
	
}
