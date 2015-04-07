package com.novadart.novabill.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

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
