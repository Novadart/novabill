package com.novadart.novabill.test.suite;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.web.gwt.BusinessServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:caching-test-config.xml")
@Transactional
public class CachingTest extends GWTServiceTest {
	
	@Autowired
	private BusinessService businessService;
	
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
	
	

}
