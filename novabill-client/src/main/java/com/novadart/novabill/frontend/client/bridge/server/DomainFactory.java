package com.novadart.novabill.frontend.client.bridge.server;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.novadart.novabill.shared.client.dto.IBusinessDTO;
import com.novadart.novabill.shared.client.dto.IClientDTO;
import com.novadart.novabill.shared.client.dto.IPageDTO;

public interface DomainFactory extends AutoBeanFactory {

	AutoBean<IBusinessDTO> business();
	
	AutoBean<IPageDTO<?>> page();
	
	AutoBean<IClientDTO> client();
}
