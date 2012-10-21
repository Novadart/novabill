package com.novadart.novabill.domain;

import javax.persistence.Embeddable;

import org.apache.solr.analysis.ASCIIFoldingFilterFactory;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;


@Embeddable
@AnalyzerDef(name = FTSNamespace.DEFAULT_CONTACT_ANALYZER,
	tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
	filters = {
		@TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
		@TokenFilterDef(factory = LowerCaseFilterFactory.class)
	})
@Analyzer(definition = FTSNamespace.DEFAULT_CONTACT_ANALYZER)
public class Contact {
	
	@Field(name = FTSNamespace.FIRST_NAME)
	private String firstName;
	
	@Field(name = FTSNamespace.LAST_NAME)
	private String lastName;
	
	private String email;
	
	private String phone;
	
	private String fax;
	
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
