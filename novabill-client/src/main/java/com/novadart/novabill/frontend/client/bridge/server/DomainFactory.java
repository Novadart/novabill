package com.novadart.novabill.frontend.client.bridge.server;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.novadart.novabill.shared.client.dto.IBusinessDTO;

public interface DomainFactory extends AutoBeanFactory {

	AutoBean<IBusinessDTO> business();
}
