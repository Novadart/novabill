package com.novadart.novabill.domain.security;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.novadart.novabill.domain.Business;

public class PrincipalDetails implements UserDetails {
	
	private static final long serialVersionUID = -2652502773566344511L;
	
	private Business principal;
	
	public PrincipalDetails() {}
	
	public PrincipalDetails(Business principal){
		this.principal = principal;
	}
	
	public Business getPrincipal(){
		return principal;
	}
	
	public void setPrincipal(Business principal){
		this.principal = principal;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (RoleTypes roleType : principal.getGrantedRoles())
        	authorities.add(new SimpleGrantedAuthority(roleType.name()));
        return authorities;
	}

	@Override
	public String getPassword() {
		return this.principal.getPassword();
	}

	@Override
	public String getUsername() {
		return this.principal.getEmail();
	}

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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((principal == null) ? 0 : principal.hashCode());
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
		PrincipalDetails other = (PrincipalDetails) obj;
		if (principal == null) {
			if (other.principal != null)
				return false;
		} else if (!principal.equals(other.principal))
			return false;
		return true;
	}

}
