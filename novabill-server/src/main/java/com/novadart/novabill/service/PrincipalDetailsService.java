package com.novadart.novabill.service;

import com.novadart.novabill.domain.security.Principal;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Service("principalDetailsService")
public class PrincipalDetailsService implements UserDetailsService{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Principal loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		
		String query = "select principal from Principal principal" +
				" left join fetch principal.grantedRoles" +
				" where lower(principal.username)=?1";
		List<Principal> resultList = entityManager.createQuery(query, Principal.class).setParameter(1, username.toLowerCase()).getResultList();
		if(resultList == null || resultList.size() == 0)
			throw new UsernameNotFoundException(String.format("Principal with username %s not found!", username));
		return resultList.get(0);
	}

}
