package com.novadart.novabill.domain;

import com.novadart.novabill.annotation.Trimmed;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Configurable
@Entity
@javax.persistence.Table(name = "transporter")
@Table(appliesTo = "transporter",
	indexes = @Index(columnNames = "business", name = "transporter_business_fkey_index"))
public class Transporter implements Serializable {

	private static final long serialVersionUID = -291082261687056423L;
	
	@NotEmpty
	@Trimmed
	@Size(max = 50)
	private String name;
	
	@Size(max = 255)
	@Trimmed
	@NotEmpty
	private String description;

	@JoinColumn(name = "business")
	@ManyToOne
	private Business business;
	
	/*
     *Getters and setters 
     * */
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
        EntityManager em = new Transporter().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countTransporters() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Transporter o", Long.class).getSingleResult();
    }
    
    public static List<Transporter> findAllTransporters() {
        return entityManager().createQuery("SELECT o FROM Transporter o", Transporter.class).getResultList();
    }
    
    public static Transporter findTransporter(Long id) {
        if (id == null) return null;
        return entityManager().find(Transporter.class, id);
    }
    
    public static List<Transporter> findTransporterEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Transporter o", Transporter.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
        	Transporter attached = Transporter.findTransporter(this.id);
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
    public Transporter merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Transporter merged = this.entityManager.merge(this);
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
    	return String.format("<id: %d, name: %s, desc: %s>", id, name, description);
    }

}
