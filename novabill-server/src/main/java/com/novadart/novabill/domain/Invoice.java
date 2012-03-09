package com.novadart.novabill.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.novadart.novabill.shared.client.dto.PaymentType;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Invoice extends AbstractInvoice implements Serializable {
	
	private static final long serialVersionUID = 3369941491294470750L;

    @Type(type = "text")
    private String paymentNote;
    
    private PaymentType paymentType;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date paymentDueDate;
    
    @ManyToOne
    protected Business business;

    @ManyToOne
    @NotNull
    protected Client client;
    
}
