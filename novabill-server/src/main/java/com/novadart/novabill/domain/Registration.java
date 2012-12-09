package com.novadart.novabill.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;


/*
 * Registration class holds the data of a account registration request.
 */
@Configurable
@Entity
public class Registration extends EmailPasswordHolder implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private boolean agreementAccepted;
	
	public static List<Registration> findRegistrations(String email, String token){
		String query = "select registration from Registration registration where registration.email = :email and registration.activationToken = :token";
		return entityManager().createQuery(query, Registration.class)
				.setParameter("email", email)
				.setParameter("token", token).getResultList();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Registration registration = new Registration();
		registration.setId(getId());
		registration.setEmail(getEmail());
		registration.setPassword(getPassword());
		registration.setConfirmPassword(getConfirmPassword());
		registration.setActivationToken(getActivationToken());
		registration.setExpirationDate(getExpirationDate());
		registration.setCreationTime(getCreationTime());
		registration.setAgreementAccepted(isAgreementAccepted());
		return registration;
	}
	
	public boolean isAgreementAccepted() {
		return agreementAccepted;
	}
	
	public void setAgreementAccepted(boolean agreementAccepted) {
		this.agreementAccepted = agreementAccepted;
	}
	
	/*
	 * Active record functionality
	 * */
	
	@PersistenceContext
    transient EntityManager entityManager;
    
    public static long countRegistrations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Registration o", Long.class).getSingleResult();
    }
    
    public static List<Registration> findAllRegistrations() {
        return entityManager().createQuery("SELECT o FROM Registration o", Registration.class).getResultList();
    }
    
    public static Registration findRegistration(Long id) {
        if (id == null) return null;
        return entityManager().find(Registration.class, id);
    }
    
    public static List<Registration> findRegistrationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Registration o", Registration.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public Registration merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Registration merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
	
	/*
	 * End of active record functionality section
	 * */

}
