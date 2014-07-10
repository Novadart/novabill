package com.novadart.novabill.domain;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.shared.client.dto.NotificationType;

@Entity
@Configurable
public class Notification {

	@NotNull
	private NotificationType type;
	
	@NotNull
	private Long creationTime = System.currentTimeMillis();
	
	@Size(max = 256)
	private String message;

	@NotNull
	private boolean seen = false;
	
	@ManyToOne
	private Business business;
	
	public static List<Notification> getUnseenNotificationsForBusiness(Long businessID) {
		String sql = "select n from Notification n where n.seen = false and n.business.id = :businessID order by n.creationTime";
		return entityManager().createQuery(sql, Notification.class).setParameter("businessID", businessID).getResultList();
	}

	/*
	 * Getters and setters
	 * */
	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public Long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Long creationTime) {
		this.creationTime = creationTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
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
        EntityManager em = new Notification().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static List<Notification> findAllNotifications() {
        return entityManager().createQuery("SELECT o FROM Notification o", Notification.class).getResultList();
    }
    
    public static Notification findNotification(Long id) {
        if (id == null) return null;
        return entityManager().find(Notification.class, id);
    }
    
    public static List<Notification> findNotificationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Notification o", Notification.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
        	Notification attached = Notification.findNotification(this.id);
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
    public Notification merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Notification merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
	
	/*
     * End of active record functionality
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
	
	
}
