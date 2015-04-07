package com.novadart.novabill.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "persistent_logins")
public class PesistentLogin {
	
	@NotNull
	@Size(max = 64)
	private String username;
	
	@Size(max = 64)
	@Id
	private String series;
	
	@Size(max = 64)
	@NotNull
	private String token;
	
	@Column(name = "last_used")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUsed;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}

}
