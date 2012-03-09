package com.novadart.novabill.domain;

import java.io.Serializable;
import javax.persistence.ManyToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Estimation extends AbstractInvoice implements Serializable {
	
	private static final long serialVersionUID = 8020837636815686509L;

	@ManyToOne
    protected Business business;

    @ManyToOne
    protected Client client;
	
}
