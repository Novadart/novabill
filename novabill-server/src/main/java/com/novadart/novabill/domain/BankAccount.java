package com.novadart.novabill.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class BankAccount implements Serializable {
	
	private static final long serialVersionUID = 5546509172175255602L;

	@Column(unique = true)
    @Size(max = 30)
    private String iban;

    @Size(max = 255)
    private String name;

    @ManyToOne
    private Business business;
	
}
