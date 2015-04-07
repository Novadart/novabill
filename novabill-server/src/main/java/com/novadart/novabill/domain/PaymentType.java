package com.novadart.novabill.domain;

import com.novadart.novabill.annotation.PaymentDeltaNotNull;
import com.novadart.novabill.annotation.Trimmed;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentDeltaType;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Configurable
@Entity
@PaymentDeltaNotNull
@javax.persistence.Table(name = "payment_type")
@Table(appliesTo = "payment_type",
	indexes = @Index(columnNames = "business", name = "payment_type_business_fkey_index"))
public class PaymentType {
	
	@Size(max = 255)
	@NotEmpty
	@Trimmed
	private String name;
	
	@Size(max = 1500)
	@NotNull
	private String defaultPaymentNote;
	
	@NotNull
	private PaymentDateType paymentDateGenerator;
	
	private Integer paymentDateDelta;
	
	private PaymentDeltaType paymentDeltaType;
	
	@Column(columnDefinition = "integer default 0")
	private Integer secondaryPaymentDateDelta;
	
	@JoinColumn(name = "business")
	@ManyToOne
	private Business business;
	
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "defaultPaymentType")
	private Set<Client> clients = new HashSet<Client>();
	
	@SuppressWarnings("unused")
	@PreRemove
	private void preRemove(){
		for(Client client: getClients())
			client.setDefaultPaymentType(null);
	}
	
	public PaymentType(String name, String defaultPaymentNote, PaymentDateType paymentDateGenerator, Integer paymentDateDelta, PaymentDeltaType paymentDeltaType, Integer secondaryPaymentDateDelta) {
		this.name = name;
		this.defaultPaymentNote = defaultPaymentNote;
		this.paymentDateGenerator = paymentDateGenerator;
		this.paymentDateDelta = paymentDateDelta;
		this.paymentDeltaType = paymentDeltaType;
		this.secondaryPaymentDateDelta = secondaryPaymentDateDelta;
	}
	
	public PaymentType(){
		this(null, null, null, null, null, null);
	}
	
	
	@Override
	public  PaymentType clone() throws CloneNotSupportedException {
		return new PaymentType(name, defaultPaymentNote, paymentDateGenerator, paymentDateDelta, paymentDeltaType, secondaryPaymentDateDelta);
	}


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

	public PaymentDeltaType getPaymentDeltaType() {
		return paymentDeltaType;
	}
	
	public void setPaymentDeltaType(PaymentDeltaType paymentDeltaType) {
		this.paymentDeltaType = paymentDeltaType;
	}
	
	public Integer getSecondaryPaymentDateDelta() {
		return secondaryPaymentDateDelta;
	}

	public void setSecondaryPaymentDateDelta(Integer secondaryPaymentDateDelta) {
		this.secondaryPaymentDateDelta = secondaryPaymentDateDelta;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}
	
	public Set<Client> getClients() {
		return clients;
	}

	public void setClients(Set<Client> clients) {
		this.clients = clients;
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
    
    public static List<PaymentType> findAllPaymentTypes() {
        return entityManager().createQuery("SELECT p FROM PaymentType p", PaymentType.class).getResultList();
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
    	return String.format("<id: %d, name: %s>", id, name);
    }

}
