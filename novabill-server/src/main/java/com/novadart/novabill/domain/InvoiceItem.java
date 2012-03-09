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
public class InvoiceItem implements Serializable {
	
	private static final long serialVersionUID = -1072295560395962907L;

	private BigDecimal price;

    @Type(type = "text")
    private String description;

    @Size(max = 255)
    private String unitOfMeasure;

    private BigDecimal tax;
    
    private BigDecimal quantity;
    
    private BigDecimal totalBeforeTax;
    
    private BigDecimal totalTax;
    
    private BigDecimal total;
    
    @ManyToOne
    private AbstractInvoice invoice;
    
}
