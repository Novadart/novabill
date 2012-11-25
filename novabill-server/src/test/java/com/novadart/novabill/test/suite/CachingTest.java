package com.novadart.novabill.test.suite;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.facade.ClientService;
import com.novadart.novabill.web.gwt.BusinessServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:caching-test-config.xml")
@Transactional
public class CachingTest extends GWTServiceTest {
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private ClientService clientService;
	
	@Resource(name = "testProps")
	private HashMap<String, String> testProps;
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}
	
	@Test
	public void businessGetStatsCacheTest() throws NotAuthenticatedException, ConcurrentAccessException{
		BusinessStatsDTO stats = businessService.getStats(authenticatedPrincipal.getBusiness().getId());
		BusinessStatsDTO cachedStats = businessService.getStats(authenticatedPrincipal.getBusiness().getId());
		assertTrue(stats == cachedStats); //needs to be the same object fetched from the cache
	}
	
	@Test
	public void businessCountClientsCacheTest() throws NotAuthenticatedException, ConcurrentAccessException{
		Long count = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		Long cachedCount = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		assertTrue(count == cachedCount); //needs to be the same object fetched from the cache
	}
	
	@Test
	public void businessCountInvoicesCacheTest() throws NotAuthenticatedException, ConcurrentAccessException{
		Long count = businessService.countInvoices(authenticatedPrincipal.getBusiness().getId());
		Long cachedCount = businessService.countInvoices(authenticatedPrincipal.getBusiness().getId());
		assertTrue(count == cachedCount); //needs to be the same object fetched from the cache
	}
	
	@Test
	public void businessCountInvoicesForYearCacheTest() throws NotAuthenticatedException, ConcurrentAccessException{
		Long count = businessService.countInvoicesForYear(authenticatedPrincipal.getBusiness().getId(), 2012);
		Long cachedCount = businessService.countInvoicesForYear(authenticatedPrincipal.getBusiness().getId(), 2012);
		assertTrue(count == cachedCount); //needs to be the same object fetched from the cache
	}
	
	@Test
	public void clientGetAllCacheTest() throws NotAuthenticatedException, ConcurrentAccessException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(clientService.getAll(authenticatedPrincipal.getBusiness().getId()));
		Set<ClientDTO> cachedClients = new HashSet<ClientDTO>(clientService.getAll(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(clients.equals(cachedClients));
	}
	
	@Test
	public void clientRemoveCacheTest() throws NotAuthenticatedException, ConcurrentAccessException, DataAccessException, NoSuchObjectException, DataIntegrityException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(clientService.getAll(authenticatedPrincipal.getBusiness().getId()));
		Long count = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		clientService.remove(authenticatedPrincipal.getBusiness().getId(), new Long(testProps.get("clientWithoutDocsID")));
		Set<ClientDTO> notCachedClients = new HashSet<ClientDTO>(clientService.getAll(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!clients.equals(notCachedClients));
		Long notCachedCount = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		assertTrue(count != notCachedCount);
	}
	
	public void  clientAddCacheTest() throws NotAuthenticatedException, ConcurrentAccessException, AuthorizationException, ValidationException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(clientService.getAll(authenticatedPrincipal.getBusiness().getId()));
		Long count = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		Client client = TestUtils.createClient();
		client.setBusiness(authenticatedPrincipal.getBusiness());
		clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOFactory.toDTO(client));
		Set<ClientDTO> notCachedClients = new HashSet<ClientDTO>(clientService.getAll(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!clients.equals(notCachedClients));
		Long notCachedCount = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		assertTrue(count != notCachedCount);
	}
	
	public void  clientUpdateCacheTest() throws NotAuthenticatedException, ConcurrentAccessException, AuthorizationException, ValidationException, DataAccessException, NoSuchObjectException{
		Set<ClientDTO> clients = new HashSet<ClientDTO>(clientService.getAll(authenticatedPrincipal.getBusiness().getId()));
		Long count = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		client.setName("The new name for this client");
		clientService.update(authenticatedPrincipal.getBusiness().getId(), ClientDTOFactory.toDTO(client));
		Set<ClientDTO> notCachedClients = new HashSet<ClientDTO>(clientService.getAll(authenticatedPrincipal.getBusiness().getId()));
		assertTrue(!clients.equals(notCachedClients));
		Long cachedCount = businessService.countClients(authenticatedPrincipal.getBusiness().getId());
		assertTrue(count != cachedCount);
	}

}
