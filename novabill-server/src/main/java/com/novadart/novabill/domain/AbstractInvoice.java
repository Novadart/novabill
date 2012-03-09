package com.novadart.novabill.domain;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
public abstract class AbstractInvoice {
	
	protected Long invoiceID;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    protected Date invoiceDate;
    
    protected Integer invoiceYear;
    
    @Type(type = "text")
    protected String note;
    
    protected BigDecimal total;
    
    protected BigDecimal totalTax;
    
    protected BigDecimal totalBeforeTax;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "invoice", orphanRemoval = true)
    @OrderBy("id")
    protected List<InvoiceItem> invoiceItems = new LinkedList<InvoiceItem>();
    
    protected static int getYear(Date date){
    	Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
    }
    
    @SuppressWarnings("unused")
	@PreUpdate
    protected void onUpdate(){
    	if(invoiceDate != null)
    		invoiceYear = getYear(invoiceDate);
    }
    
    @SuppressWarnings("unused")
    @PrePersist
    protected void onPersist(){
    	if(invoiceDate != null)
    		invoiceYear = getYear(invoiceDate);
    }

	protected void setInvoiceYear(Integer invoiceYear) {
        this.invoiceYear = invoiceYear;
    }
}
