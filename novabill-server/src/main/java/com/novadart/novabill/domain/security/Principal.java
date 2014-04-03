package com.novadart.novabill.domain.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.Hash;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Registration;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
@Configurable
@Entity
public class Principal implements UserDetails {
	
	private static final long serialVersionUID = -2652502773566344511L;
	
	@Email
	private String username;
	
	private String password;
	
	private Long creationTime = System.currentTimeMillis();
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
	private Date lastLogin;
	
	private boolean enabled = true;
	
	@NotNull
	private Long notesBitMask = 0xFFFFFFFFFFFFFFFFl;
	
	@ManyToOne
	private Business business;
	
	@ElementCollection
    private Set<RoleType> grantedRoles = new HashSet<RoleType>();
	
	public Principal() {}
	
	public Principal(Registration registration){
		username = registration.getEmail();
		password = registration.getPassword();
		creationTime = registration.getCreationTime();
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (RoleType roleType : grantedRoles)
        	authorities.add(new SimpleGrantedAuthority(roleType.name()));
        return authorities;
	}
	
	public static Principal findByUsername(String username){
    	String query = "select pd from Principal pd where pd.username = :username";
    	List<Principal> result = entityManager().createQuery(query, Principal.class).setParameter("username", username).getResultList();
    	return result.size() == 0? null: result.get(0);
    }
	
	public static List<Principal> findAllPrincipals(){
		return entityManager().createQuery("SELECT o FROM Principal o", Principal.class).getResultList();
	}

	/*
	 * Getters and setters
	 * */
	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Hash(saltMethod = "getCreationTime")
	public void setPassword(String password){
		this.password = password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public Long getCreationTime(){
		return this.creationTime;
	}
	
	public void setCreationTime(Long creationTime){
		this.creationTime = creationTime;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public Long getNotesBitMask() {
		return notesBitMask;
	}

	public void setNotesBitMask(Long notesBitMask) {
		this.notesBitMask = notesBitMask;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public Set<RoleType> getGrantedRoles() {
        return this.grantedRoles;
    }
    
    public void setGrantedRoles(Set<RoleType> grantedRoles) {
        this.grantedRoles = grantedRoles;
    }
    /*
     * End of getters and setters section
     * */
    
	/*
     * UserDetails interface methods
     * */
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	/*
     * End of UserDetails interface methods section
     * */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((creationTime == null) ? 0 : creationTime.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Principal other = (Principal) obj;
		if (creationTime == null) {
			if (other.creationTime != null)
				return false;
		} else if (!creationTime.equals(other.creationTime))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	/*
     * Active record functionality
     * */
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new Principal().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static Principal findPrincipal(Long id) {
        if (id == null) return null;
        return entityManager().find(Principal.class, id);
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
            Business attached = Business.findBusiness(this.id);
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
    public Principal merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Principal merged = this.entityManager.merge(this);
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
    
    @javax.persistence.Version
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
		return String.format("<username: %s, roles: %s>", username, grantedRoles.toString()); 
    }
	
}
