package com.novadart.novabill.paypal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

@Service
public class PaymentPlansLoader {
	
	private volatile PaymentPlanDescriptor[] paypalPaymentDescriptors;
	
	@Autowired
	private ResourceLoader resourceLoader;

	public PaymentPlanDescriptor[] getPayPalPaymentPlanDescriptors(){
		return paypalPaymentDescriptors;
	}
	
	@PostConstruct
	public void init() {
		try {
			InputStream in = resourceLoader.getResource("classpath:/paymentPlans.json").getInputStream();
			paypalPaymentDescriptors = new ObjectMapper().readValue(in, PaymentPlanDescriptor[].class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PaymentPlanDescriptor getPayPalPaymentPlanDescriptor(String id){
		for(PaymentPlanDescriptor descriptor: paypalPaymentDescriptors)
			if(descriptor.getItemName().equals(id))
				return descriptor;
		throw new NoSuchElementException("No such payment plan");
	}
	
}
