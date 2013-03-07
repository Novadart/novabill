package com.novadart.novabill.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.shared.client.dto.PaymentDateType;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Configurable
@Entity
public class PaymentType {
	
	@Size(max = 255)
	@NotNull
	private String name;
	
	@Size(max = 1500)
	@NotNull
	private String defaultPaymentNote;
	
	@NotNull
	private PaymentDateType paymentDateGenerator;
	
	private Integer paymentDateDelta;
	
	@ManyToOne
	private Business business;
	
	public PaymentType(String name, String defaultPaymentNote, PaymentDateType paymentDateGenerator, Integer paymentDateDelta) {
		this.name = name;
		this.defaultPaymentNote = defaultPaymentNote;
		this.paymentDateGenerator = paymentDateGenerator;
		this.paymentDateDelta = paymentDateDelta;
	}
	
	public PaymentType(){
		this(null, null, null, null);
	}
	
	
	@Override
	public  PaymentType clone() throws CloneNotSupportedException {
		return new PaymentType(name, defaultPaymentNote, paymentDateGenerator, paymentDateDelta);
	}


	@Transient
	public static PaymentType[] PAYMENT_TYPES = new PaymentType[]{
		new PaymentType("payment1.name", "payment1.paymentNote", PaymentDateType.IMMEDIATE, 0),
		new PaymentType("payment2.name", "payment2.paymentNote", PaymentDateType.IMMEDIATE, 30),
		new PaymentType("payment3.name", "payment3.paymentNote", PaymentDateType.IMMEDIATE, 60),
		new PaymentType("payment4.name", "payment4.paymentNote", PaymentDateType.IMMEDIATE, 90),
		new PaymentType("payment5.name", "payment5.paymentNote", PaymentDateType.END_OF_MONTH, 30),
		new PaymentType("payment6.name", "payment6.paymentNote", PaymentDateType.END_OF_MONTH, 60),
		new PaymentType("payment7.name", "payment7.paymentNote", PaymentDateType.END_OF_MONTH, 90),
	};
	

	/*
     * Getters and setters
     * */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultPaymentNote() {
		return defaultPaymentNote;
	}

	public void setDefaultPaymentNote(String defaultPaymentNote) {
		this.defaultPaymentNote = defaultPaymentNote;
	}

	public PaymentDateType getPaymentDateGenerator() {
		return paymentDateGenerator;
	}

	public void setPaymentDateGenerator(PaymentDateType paymentDateGenerator) {
		this.paymentDateGenerator = paymentDateGenerator;
	}

	public Integer getPaymentDateDelta() {
		return paymentDateDelta;
	}

	public void setPaymentDateDelta(Integer paymentDateDelta) {
		this.paymentDateDelta = paymentDateDelta;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}
	
	/*
     * End of getters and setters section
     * */
	
	/*
     * Active record functionality
     * */
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new PaymentType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static PaymentType findPaymentType(Long id) {
        if (id == null) return null;
        return entityManager().find(PaymentType.class, id);
    }
    
    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
        	PaymentType attached = PaymentType.findPaymentType(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public PaymentType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        PaymentType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    /*
     * End of active record functionality section
     * */
    
    /*
     * Entity
     * */
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    /*
     * End of entity section
     * */
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
