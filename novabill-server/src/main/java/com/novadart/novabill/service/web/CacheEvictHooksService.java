package com.novadart.novabill.service.web;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.novadart.novabill.aspect.CachingAspect;


@Service
public class CacheEvictHooksService {

	@CacheEvict(value = CachingAspect.PRICELIST_CACHE, key = "#priceListID")
	public void evictPriceList(Long priceListID){}
	
}
