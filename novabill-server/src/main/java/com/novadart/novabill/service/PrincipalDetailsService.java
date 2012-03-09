package com.novadart.novabill.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.PrincipalDetails;


@Service("principalDetailsService")
public class PrincipalDetailsService implements UserDetailsService{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public PrincipalDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		
		String query = "select principal from Business principal" +
				" left join fetch principal.grantedRoles" +
				" where principal.email=?1";
		List<Business> resultList = entityManager.createQuery(query, Business.class).setParameter(1, username).getResultList();
		if(resultList == null || resultList.size() == 0)
			throw new UsernameNotFoundException(String.format("Principal with username %s not found!", username));
		Business principal = resultList.get(0);
		return new PrincipalDetails(principal);
	}

}
