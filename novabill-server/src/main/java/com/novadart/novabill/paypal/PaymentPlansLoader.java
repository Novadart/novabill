package com.novadart.novabill.paypal;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class PaymentPlansLoader {
	
	private volatile PayPalPaymentPlanDescriptor[] paypalPaymentDescriptors;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@PostConstruct
	public void init() {
		try {
			InputStream in = resourceLoader.getResource("classpath:/paymentPlans.json").getInputStream();
			paypalPaymentDescriptors = new ObjectMapper().readValue(in, PayPalPaymentPlanDescriptor[].class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PayPalPaymentPlanDescriptor getPayPalPaymentPlanDescriptor(String id){
		for(PayPalPaymentPlanDescriptor descriptor: paypalPaymentDescriptors)
			if(descriptor.getItemName().equals(id))
				return descriptor;
		throw new NoSuchElementException("No such payment plan");
	}

}
