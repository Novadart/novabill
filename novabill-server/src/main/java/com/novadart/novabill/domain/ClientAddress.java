package com.novadart.novabill.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.Trimmed;

@Configurable
@Entity
public class ClientAddress implements Serializable {

	private static final long serialVersionUID = 122436824066012394L;

	@NotBlank
	@Trimmed
	@Size(max = 50)
	private String name;
	
	@NotBlank
	@Trimmed
	@Size(max = 255)
	private String companyName;
	
	@Size(max = 255)
	@NotBlank
	@Trimmed
	private String address;
	
	@Size(max = 10)
	@Trimmed
	private String postcode;
	
	@Size(max = 60)
	@Trimmed
	private String city;
	
//	@NotBlank
	@Size(max = 100)
	@Trimmed
	private String province;
	
	@NotBlank
	@Size(max = 3)
	@Trimmed
	private String country;

	@ManyToOne
	private Client client;
	
	/*
     * Getters and setters
     * */
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
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
        EntityManager em = new ClientAddress().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countClientAddresses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ClientAddress o", Long.class).getSingleResult();
    }
    
    public static List<ClientAddress> findAllClientAddresses() {
        return entityManager().createQuery("SELECT o FROM ClientAddress o", ClientAddress.class).getResultList();
    }
    
    public static ClientAddress findClientAddress(Long id) {
        if (id == null) return null;
        return entityManager().find(ClientAddress.class, id);
    }
    
    public static List<ClientAddress> findClientAddressEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ClientAddress o", ClientAddress.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
        	ClientAddress attached = ClientAddress.findClientAddress(this.id);
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
    public ClientAddress merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ClientAddress merged = this.entityManager.merge(this);
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
