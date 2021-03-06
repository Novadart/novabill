package com.novadart.novabill.domain;

import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Table;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
@Configurable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@javax.persistence.Table(name = "log_record")
@Table(appliesTo = "log_record",
	indexes = @Index(columnNames = {"business", "time"}, name = "log_record_business_fkey_index"))
public class LogRecord {
	
	@NotNull
	private EntityType entityType;
	
	@NotNull
	private OperationType operationType;
	
	@NotNull
	private Long entityID;
	
	@Column(name = "time")
	@NotNull
	private Long time;
	
	@Type(type = "text")
	private String details;
	
	@Column(columnDefinition = "boolean default false")
	private boolean referringToDeletedEntity = false;
	
	@JoinColumn(name = "business")
	@ManyToOne
	private Business business;
	
	public LogRecord() {}
	
	public LogRecord(EntityType entityType, OperationType operationType, Long entityID, Long time) {
		this.entityType = entityType;
		this.operationType = operationType;
		this.entityID = entityID;
		this.time = time;
	}

	public static List<LogRecord> fetchLastN(Long businessID, Integer n){
		String sql = "select lr from LogRecord lr where lr.business.id = :bizID order by lr.time desc";
		return entityManager().createQuery(sql, LogRecord.class).setParameter("bizID", businessID).setFirstResult(0).setMaxResults(n).getResultList();
	}
	
	public static List<LogRecord> fetchAllSince(Long businessID, Long threshold){
		String sql = "select lr from LogRecord lr where lr.business.id = :bizID and lr.time > :threshold order by lr.time desc";
		TypedQuery<LogRecord> query = entityManager().createQuery(sql, LogRecord.class);
		query.setHint("org.hibernate.cacheable", true);
		return query.setParameter("bizID", businessID).setParameter("threshold", threshold).getResultList();
	}
	
	@Transactional(readOnly = false)
	public void markAsReferringToDeletedEntity(Long businessID, Long entityID, EntityType entityType, List<Long> excludeIDs) {
		String sql = "update LogRecord lr set lr.referringToDeletedEntity = true where lr.business.id = :bid and lr.entityID = :eid and lr.entityType = :et and lr.id not in (:exids)";
		entityManager().createQuery(sql).
			setParameter("bid", businessID).
			setParameter("eid", entityID).
			setParameter("et", entityType).
			setParameter("exids", excludeIDs).executeUpdate();
	}
	
	
	/*
	 * Getters and setters
	 * */
	
	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public Long getEntityID() {
		return entityID;
	}

	public void setEntityID(Long entityID) {
		this.entityID = entityID;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public Business getBusiness() {
		return business;
	}

	public boolean isReferringToDeletedEntity() {
		return referringToDeletedEntity;
	}

	public void setReferringToDeletedEntity(boolean referringToDeletedEntity) {
		this.referringToDeletedEntity = referringToDeletedEntity;
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
        EntityManager em = new LogRecord().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static List<LogRecord> findAllLogRecords() {
        return entityManager().createQuery("SELECT o FROM LogRecord o", LogRecord.class).getResultList();
    }
    
    public static LogRecord findLogRecord(Long id) {
        if (id == null) return null;
        return entityManager().find(LogRecord.class, id);
    }
    
    public static List<LogRecord> findLogRecordEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LogRecord o", LogRecord.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
        	LogRecord attached = LogRecord.findLogRecord(this.id);
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
    public LogRecord merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LogRecord merged = this.entityManager.merge(this);
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
