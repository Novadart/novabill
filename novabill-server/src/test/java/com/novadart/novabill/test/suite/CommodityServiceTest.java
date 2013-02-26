package com.novadart.novabill.test.suite;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.shared.client.facade.CommodityService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-commodity-test-config.xml")
@Transactional
public class CommodityServiceTest extends GWTServiceTest {
	
	@Autowired
	private CommodityService commodityService;
	
	@Test
	public void commodityServiceWiringTest(){
		assertNotNull(commodityService);
	}

}
