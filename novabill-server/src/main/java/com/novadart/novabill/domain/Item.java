package com.novadart.novabill.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Item implements Serializable {
	
	private static final long serialVersionUID = 4265058605330997015L;

	@Size(max = 255)
    private String name;

    private BigDecimal price;

    @Type(type = "text")
    private String description;

    @Size(max = 255)
    private String unitOfMeasure;
    
    private BigDecimal tax;

    @ManyToOne
    private Business business;
	
}
