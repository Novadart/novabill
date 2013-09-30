package com.novadart.novabill.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "persistent_logins")
public class PesistentLogin {
	
	@NotNull
	@Size(max = 64)
	String username;
	
	@Size(max = 64)
	@Id
	String series;
	
	@Size(max = 64)
	@NotNull
	String token;
	
	@Column(name = "last_used")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	Date lastUsed;

}
