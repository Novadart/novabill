package com.novadart.novabill.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class ForgotPassword extends EmailPasswordHolder implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public ForgotPassword(){};

	public ForgotPassword(String password, String confirmPassword){
		this.password = password;
		this.confirmPassword = confirmPassword;
	}
	
	/*
	 * Active record functionality
	 * */
	
    public static long countForgotPasswords() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ForgotPassword o", Long.class).getSingleResult();
    }
    
    public static List<ForgotPassword> findAllForgotPasswords() {
        return entityManager().createQuery("SELECT o FROM ForgotPassword o", ForgotPassword.class).getResultList();
    }
    
    public static ForgotPassword findForgotPassword(Long id) {
        if (id == null) return null;
        return entityManager().find(ForgotPassword.class, id);
    }
    
    public static List<ForgotPassword> findForgotPasswordEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ForgotPassword o", ForgotPassword.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ForgotPassword merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ForgotPassword merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
	
	/*
	 * End of active record functionality section
	 * */
    
	
}
