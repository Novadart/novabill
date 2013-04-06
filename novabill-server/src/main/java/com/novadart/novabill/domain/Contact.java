package com.novadart.novabill.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import org.apache.solr.analysis.ASCIIFoldingFilterFactory;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.validator.constraints.Email;

import com.novadart.novabill.annotation.Trimmed;


/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Embeddable
@AnalyzerDef(name = FTSNamespace.DEFAULT_CONTACT_ANALYZER,
	tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
	filters = {
		@TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
		@TokenFilterDef(factory = LowerCaseFilterFactory.class)
	})
@Analyzer(definition = FTSNamespace.DEFAULT_CONTACT_ANALYZER)
public class Contact implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Field(name = FTSNamespace.FIRST_NAME)
	@Size(max = 50)
	@Trimmed
	private String firstName;
	
	@Field(name = FTSNamespace.LAST_NAME)
	@Size(max = 50)
	@Trimmed
	private String lastName;
	
	@Email
	@Trimmed
	private String email;
	
	@Size(max = 25)
	@Trimmed
	private String phone;
	
	@Size(max = 25)
	@Trimmed
	private String fax;
	
	@Size(max = 25)
	private String mobile;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
