package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
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
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.domain.dto.factory.CreditNoteDTOFactory;
import com.novadart.novabill.domain.dto.factory.EstimationDTOFactory;
import com.novadart.novabill.domain.dto.factory.InvoiceDTOFactory;
import com.novadart.novabill.domain.dto.factory.TransportDocumentDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.facade.ClientService;
import com.novadart.novabill.shared.client.facade.CreditNoteService;
import com.novadart.novabill.shared.client.facade.EstimationService;
import com.novadart.novabill.shared.client.facade.InvoiceService;
import com.novadart.novabill.shared.client.facade.TransportDocumentService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:caching-test-config.xml")
@Transactional
@ActiveProfiles("dev")
public class CachingTest extends GWTServiceTest {
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private CreditNoteService creditNoteService;
	
	@Autowired
	private EstimationService estimationService;
	
	@Autowired
	private TransportDocumentService transDocService;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Resource(name = "testProps")
	private HashMap<String, String> testProps;
	
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
	}
	
	@Test
	public void businessGetTest() throws NotAuthenticatedException, DataAccessException{
		BusinessDTO business = businessService.get(authenticatedPrincipal.getBusiness().getId());
		BusinessDTO cachedBusiness = businessService.get(authenticatedPrincipal.getBusiness().getId());
		assertTrue(business == cachedBusiness);
	}
	
	@Test
	public void businessUpdateTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException{
		BusinessDTO business1 = businessService.get(authenticatedPrincipal.getBusiness().getId());
		Business biz = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
		biz.setName("Test name");
		businessService.update(BusinessDTOFactory.toDTO(biz));
		BusinessDTO business2 = businessService.get(authenticatedPrincipal.getBusiness().getId());
		assertTrue(business1 != business2);
	}
	
	@Test
	public void clientGetAllCacheTest() throws NotAuthenticatedException, DataAccessException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(businessService.getClients(authenticatedPrincipal.getBusiness().getId()));
		Set<ClientDTO> cachedClients = new HashSet<ClientDTO>(businessService.getClients(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(clients.equals(cachedClients));
	}
	
	@Test
	public void clientRemoveCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(businessService.getClients(authenticatedPrincipal.getBusiness().getId()));
		Long count = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		clientService.remove(authenticatedPrincipal.getBusiness().getId(), new Long(testProps.get("clientWithoutDocsID")));
		Set<ClientDTO> notCachedClients = new HashSet<ClientDTO>(businessService.getClients(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!clients.equals(notCachedClients));
		Long notCachedCount = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		assertTrue(count.equals(notCachedCount + 1));
	}
	
	@Test
	public void  clientAddCacheTest() throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(businessService.getClients(authenticatedPrincipal.getBusiness().getId()));
		Long count = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		Client client = TestUtils.createClient();
		clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOFactory.toDTO(client));
		HashSet<ClientDTO> notCachedClients = new HashSet<ClientDTO>(businessService.getClients(authenticatedPrincipal.getBusiness().getId()));
		Long notCachedCount = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		assertTrue(notCachedCount.equals(count + 1));
		assertTrue(!clients.equals(notCachedClients));
	}
	
	@Test
	public void  clientUpdateCacheTest() throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException, NoSuchObjectException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(businessService.getClients(authenticatedPrincipal.getBusiness().getId()));
		Long count = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		client.setName("The new name for this client");
		clientService.update(authenticatedPrincipal.getBusiness().getId(), ClientDTOFactory.toDTO(client));
		Set<ClientDTO> notCachedClients = new HashSet<ClientDTO>(businessService.getClients(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!clients.equals(notCachedClients));
		Long cachedCount = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		assertTrue(count.equals(cachedCount));
	}
	
	@Test
	public void businessGetInvoicesCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		List<InvoiceDTO> result = businessService.getInvoices(authenticatedPrincipal.getBusiness().getId());
		List<InvoiceDTO> cachedResult = businessService.getInvoices(authenticatedPrincipal.getBusiness().getId());
		assertTrue(result == cachedResult);
	}
	
	@Test
	public void invoiceRemoveCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		Set<InvoiceDTO> result = new HashSet<InvoiceDTO>(businessService.getInvoices(businessID));
		Long countInvs = businessService.countInvoices(businessID);
		Long countInvsYear = businessService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal totals = businessService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long id = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(businessID, clientID, id);
		List<InvoiceDTO> nonCachedResult = businessService.getInvoices(authenticatedPrincipal.getBusiness().getId());
		Long nonCachedCountInvs = businessService.countInvoices(businessID);
		Long nonCachedCountInvsYear = businessService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal nonCachedTotals = businessService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		assertTrue(result != nonCachedResult);
		assertTrue(countInvs.equals(nonCachedCountInvs + 1));
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear + 1));
		assertTrue(!totals.equals(nonCachedTotals));
	}
	
	@Test
	public void invoiceAddCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		Set<InvoiceDTO> result = new HashSet<InvoiceDTO>(businessService.getInvoices(businessID));
		Long countInvs = businessService.countInvoices(businessID);
		Long countInvsYear = businessService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal totals = businessService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOFactory.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(), Invoice.class));
		invDTO.setClient(ClientDTOFactory.toDTO(client));
		invDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		invoiceService.add(invDTO);
		
		Set<InvoiceDTO> nonCachedResult = new HashSet<InvoiceDTO>(businessService.getInvoices(authenticatedPrincipal.getBusiness().getId()));
		Long nonCachedCountInvs = businessService.countInvoices(businessID);
		Long nonCachedCountInvsYear = businessService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal nonCachedTotals = businessService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		assertTrue(countInvs.equals(nonCachedCountInvs - 1));
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear - 1));
		assertTrue(!totals.equals(nonCachedTotals));
		assertTrue(result.size() + 1 == nonCachedResult.size());
		assertTrue(!result.equals(nonCachedResult));
	}
	
	@Test
	public void invoiceUpdateCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		Set<InvoiceDTO> result = new HashSet<InvoiceDTO>(businessService.getInvoices(businessID));
		Long countInvs = businessService.countInvoices(businessID);
		Long countInvsYear = businessService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal totals = businessService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		
		Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		inv.setNote("Temporary note for this invoice");
		invoiceService.update(InvoiceDTOFactory.toDTO(inv));
		Invoice.entityManager().flush();
		
		Set<InvoiceDTO> nonCachedResult = new HashSet<InvoiceDTO>(businessService.getInvoices(authenticatedPrincipal.getBusiness().getId()));
		Long nonCachedCountInvs = businessService.countInvoices(businessID);
		Long nonCachedCountInvsYear = businessService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal nonCachedTotals = businessService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		assertTrue(!result.equals(nonCachedResult));
		assertTrue(countInvs.equals(nonCachedCountInvs));
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear));
		assertTrue(totals.equals(nonCachedTotals));
	}
	
	@Test
	public void invoiceSetPayedCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<InvoiceDTO> result = businessService.getInvoices(businessID);
		Long countInvs = businessService.countInvoices(businessID);
		Long countClients = businessService.countClients(businessID);
		Long countInvsYear = businessService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long id = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.setPayed(businessID, clientID, id, true);
		
		List<InvoiceDTO> nonCachedResult = businessService.getInvoices(authenticatedPrincipal.getBusiness().getId());
		Long nonCachedCountInvs = businessService.countInvoices(businessID);
		Long nonCachedCountClients = businessService.countClients(businessID);
		Long nonCachedCountInvsYear = businessService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		assertTrue(result != nonCachedResult);
		assertTrue(countInvs.equals(nonCachedCountInvs));
		assertTrue(countClients.equals(nonCachedCountClients));
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear));
	}
	
	@Test
	public void invoiceUpdateFailCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<InvoiceDTO> result = businessService.getInvoices(businessID);
		Long countInvs = businessService.countInvoices(businessID);
		Long countClients = businessService.countClients(businessID);
		Long countInvsYear = businessService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal totals = businessService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		
		try {
			Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
			inv.setNote(StringUtils.leftPad("1", 2000, '1'));
			invoiceService.update(InvoiceDTOFactory.toDTO(inv));
			Invoice.entityManager().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<InvoiceDTO> nonCachedResult = businessService.getInvoices(authenticatedPrincipal.getBusiness().getId());
		Long nonCachedCountInvs = businessService.countInvoices(businessID);
		Long nonCachedCountClients = businessService.countClients(businessID);
		Long nonCachedCountInvsYear = businessService.countInvoicesForYear(businessID, new Integer(testProps.get("year")));
		BigDecimal nonCachedTotals = businessService.getTotalAfterTaxesForYear(businessID, new Integer(testProps.get("year")));
		assertTrue(result == nonCachedResult);
		assertTrue(countInvs.equals(nonCachedCountInvs));
		assertTrue(countClients.equals(nonCachedCountClients));
		assertTrue(countInvsYear.equals(nonCachedCountInvsYear));
		assertTrue(totals.equals(nonCachedTotals));
	}
	
	@Test
	public void creditNoteGetAllCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		List<CreditNoteDTO> result = businessService.getCreditNotes(authenticatedPrincipal.getBusiness().getId());
		List<CreditNoteDTO> cachedResult = businessService.getCreditNotes(authenticatedPrincipal.getBusiness().getId());
		assertTrue(result == cachedResult);
	}
	
	@Test
	public void creditNoteRemoveCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<CreditNoteDTO> result = businessService.getCreditNotes(businessID);
		Long clientID = new Long(testProps.get("clientWithCreditNotesID"));
		Long id = Client.findClient(clientID).getCreditNotes().iterator().next().getId();
		creditNoteService.remove(businessID, clientID, id);
		List<CreditNoteDTO> nonCachedResult = businessService.getCreditNotes(businessID);
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size() + 1);
	}
	
	@Test
	public void creditNoteAddCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<CreditNoteDTO> result = businessService.getCreditNotes(businessID);
		
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		CreditNoteDTO credNoteDTO = CreditNoteDTOFactory.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextCreditNoteDocumentID(), CreditNote.class));
		credNoteDTO.setClient(ClientDTOFactory.toDTO(client));
		credNoteDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		creditNoteService.add(credNoteDTO);
		CreditNote.entityManager().flush();
		
		List<CreditNoteDTO> nonCachedResult = businessService.getCreditNotes(businessID);
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() + 1 == nonCachedResult.size());
	}
	
	@Test
	public void creditNoteUpdateCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<CreditNoteDTO> result = businessService.getCreditNotes(businessID);
		
		CreditNote credNote = authenticatedPrincipal.getBusiness().getCreditNotes().iterator().next();
		credNote.setNote("Temporary note for this credit note");
		creditNoteService.update(CreditNoteDTOFactory.toDTO(credNote));
		CreditNote.entityManager().flush();
		
		List<CreditNoteDTO> nonCachedResult = businessService.getCreditNotes(businessID);
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size());
	}
	
	@Test
	public void estimationGetAllCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		List<EstimationDTO> result = businessService.getEstimations(authenticatedPrincipal.getBusiness().getId());
		List<EstimationDTO> cachedResult = businessService.getEstimations(authenticatedPrincipal.getBusiness().getId());
		assertTrue(result == cachedResult);
	}
	
	@Test
	public void estimationRemoveCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<EstimationDTO> result = businessService.getEstimations(businessID);
		Long clientID = new Long(testProps.get("clientWithEstimationsID"));
		Long id = Client.findClient(clientID).getEstimations().iterator().next().getId();
		estimationService.remove(businessID, clientID, id);
		List<EstimationDTO> nonCachedResult = businessService.getEstimations(businessID);
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size() + 1);
	} 
	
	@Test
	public void estimationAddCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<EstimationDTO> result = businessService.getEstimations(businessID);
		
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		EstimationDTO estimationDTO = EstimationDTOFactory.toDTO(TestUtils.createEstimation(authenticatedPrincipal.getBusiness().getNextEstimationDocumentID()));
		estimationDTO.setClient(ClientDTOFactory.toDTO(client));
		estimationDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		estimationService.add(estimationDTO);
		
		List<EstimationDTO> nonCachedResult = businessService.getEstimations(businessID);
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() + 1 == nonCachedResult.size());
	}
	
	@Test
	public void estimationUpdateCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<EstimationDTO> result = businessService.getEstimations(businessID);
		
		Estimation estimation = authenticatedPrincipal.getBusiness().getEstimations().iterator().next();
		estimation.setNote("Temporary note for this estimation");
		estimationService.update(EstimationDTOFactory.toDTO(estimation));
		Estimation.entityManager().flush();
		
		List<EstimationDTO> nonCachedResult = businessService.getEstimations(businessID);
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size());
	}
	
	@Test
	public void transDocGetAllCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		List<TransportDocumentDTO> result = businessService.getTransportDocuments(authenticatedPrincipal.getBusiness().getId());
		List<TransportDocumentDTO> cachedResult = businessService.getTransportDocuments(authenticatedPrincipal.getBusiness().getId());
		assertTrue(result == cachedResult);
	}
	
	@Test
	public void transDocRemoveCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<TransportDocumentDTO> result = businessService.getTransportDocuments(businessID);
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		Long id = Client.findClient(clientID).getTransportDocuments().iterator().next().getId();
		transDocService.remove(businessID, clientID, id);
		List<TransportDocumentDTO> nonCachedResult = businessService.getTransportDocuments(businessID);
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size() + 1);
	}
	
	@Test
	public void transDocAddCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<TransportDocumentDTO> result = businessService.getTransportDocuments(businessID);
		
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOFactory.toDTO(TestUtils.createTransportDocument(authenticatedPrincipal.getBusiness().getNextTransportDocDocumentID()));
		transDocDTO.setClient(ClientDTOFactory.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOFactory.toDTO(authenticatedPrincipal.getBusiness()));
		transDocService.add(transDocDTO);
		TransportDocument.entityManager().flush();
		
		List<TransportDocumentDTO> nonCachedResult = businessService.getTransportDocuments(businessID);
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() + 1 == nonCachedResult.size());
	}
	
	@Test
	public void transDocUpdateCacheTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, AuthorizationException, InstantiationException, IllegalAccessException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		List<TransportDocumentDTO> result = businessService.getTransportDocuments(businessID);
		
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		transDoc.setNote("Temporary note for this transport document");
		transDocService.update(TransportDocumentDTOFactory.toDTO(transDoc));
		TransportDocument.entityManager().flush();
		
		List<TransportDocumentDTO> nonCachedResult = businessService.getTransportDocuments(businessID);
		assertTrue(result != nonCachedResult);
		assertTrue(result.size() == nonCachedResult.size());
	}
	
	@Test
	public void invoiceGetAllUnauthorizedTest() throws NotAuthenticatedException, DataAccessException{
		List<InvoiceDTO> result = businessService.getInvoices(authenticatedPrincipal.getBusiness().getId());
		boolean dataAccessException = false;
		try {
			businessService.getInvoices(getUnathorizedBusinessID());
		} catch (DataAccessException e) {
			dataAccessException = true;
		}
		List<InvoiceDTO> cachedResult = businessService.getInvoices(authenticatedPrincipal.getBusiness().getId());
		assertTrue(dataAccessException && result == cachedResult);
	}

}
