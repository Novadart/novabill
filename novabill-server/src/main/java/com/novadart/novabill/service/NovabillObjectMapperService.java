package com.novadart.novabill.service;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Service("jacksonObjectMapper")
public class NovabillObjectMapperService extends ObjectMapper {

	private static final long serialVersionUID = 9049154060175903092L;
	
	@PostConstruct
    public void afterPropertiesSet() throws Exception {
		SimpleModule module = new SimpleModule();
		module.addSerializer(BigDecimal.class, new ToStringSerializer());
		this.registerModule(module);
    }

}
