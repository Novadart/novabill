package com.novadart.novabill.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;


@Configurable
@Entity
public class DocumentAccessToken {

	private Long documentDBID;
	
	private String token;

	public DocumentAccessToken() {}

	public DocumentAccessToken(Long documentDBID, String token) {
		this.documentDBID = documentDBID;
		this.token = token;
	}
	
	public static List<DocumentAccessToken> findDocumentAccessTokens(Long id, String token){
		String sql = "select o from DocumentAccessToken o where o.documentDBID = :docID and token = :token";
		return entityManager().createQuery(sql, DocumentAccessToken.class)
				.setParameter("docID", id)
				.setParameter("token", token).getResultList();
	}

	/*
	 * Getters and setters
	 * */
	public Long getDocumentDBID() {
		return documentDBID;
	}

	public void setDocumentDBID(Long documentDBID) {
		this.documentDBID = documentDBID;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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
        EntityManager em = new DocumentAccessToken().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static DocumentAccessToken findDocumentAccessToken(Long id) {
        if (id == null) return null;
        return entityManager().find(DocumentAccessToken.class, id);
    }
    
    public static long countDocumentAccessTokens() {
        return entityManager().createQuery("SELECT COUNT(o) FROM DocumentAccessToken o", Long.class).getSingleResult();
    }
    
    public static List<DocumentAccessToken> findAllDocumentAccessTokens() {
        return entityManager().createQuery("SELECT o FROM DocumentAccessToken o", DocumentAccessToken.class).getResultList();
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
        	DocumentAccessToken attached = DocumentAccessToken.findDocumentAccessToken(this.id);
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
    
	/*
	 * End of active record functionality section
	 * */
	
	
	/*
     * Entity
     * */
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected Long id;
    
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
