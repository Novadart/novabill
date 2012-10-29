package com.novadart.novabill.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/*
 * Important note!
 * If fields and validation constraints are modified be sure to update the validation code. 
 */

@Configurable
@Entity
public class TransportDocument extends AccountingDocument implements Serializable {

	private static final long serialVersionUID = 9178463460405596881L;
	
	private Integer numberOfPackages;
	
	@AttributeOverrides({
		@AttributeOverride(name = "companyName", column = @Column(name = "from_company_name")),
		@AttributeOverride(name = "street", column = @Column(name = "from_street")),
		@AttributeOverride(name = "postcode", column = @Column(name = "from_postcode")),
		@AttributeOverride(name = "city", column = @Column(name = "from_city")),
		@AttributeOverride(name = "province", column = @Column(name = "from_province")),
		@AttributeOverride(name = "country", column = @Column(name = "from_country"))
	})
	@Embedded
	@Valid
	private Endpoint fromEndpoint;
	
	@AttributeOverrides({
		@AttributeOverride(name = "companyName", column = @Column(name = "to_company_name")),
		@AttributeOverride(name = "street", column = @Column(name = "to_street")),
		@AttributeOverride(name = "postcode", column = @Column(name = "to_postcode")),
		@AttributeOverride(name = "city", column = @Column(name = "to_city")),
		@AttributeOverride(name = "province", column = @Column(name = "to_province")),
		@AttributeOverride(name = "country", column = @Column(name = "to_country"))
	})
	@Embedded
	@Valid
	private Endpoint toEndpoint;
	
	private String transporter;
	
	private String transportationResponsibility;
	
	private String tradeZone;
	
	private Date transportStartDate;
	
	@ManyToOne
    protected Business business;

    @ManyToOne
    protected Client client;
    
    public static Integer countTransportDocumentsForClient(Long id){
    	return countForClient(TransportDocument.class, id);
    }
    
    /*
     *Getters and setters 
     * */
    
    public Business getBusiness() {
        return this.business;
    }
    
    public void setBusiness(Business business) {
        this.business = business;
    }
    
    public Client getClient() {
        return this.client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getNumberOfPackages() {
		return numberOfPackages;
	}

	public void setNumberOfPackages(Integer numberOfPackages) {
		this.numberOfPackages = numberOfPackages;
	}
    
    public Endpoint getFromEndpoint() {
		return fromEndpoint;
	}

	public void setFromEndpoint(Endpoint fromEndpoint) {
		this.fromEndpoint = fromEndpoint;
	}
	
	public Endpoint getToEndpoint() {
		return toEndpoint;
	}

	public void setToEndpoint(Endpoint toEndpoint) {
		this.toEndpoint = toEndpoint;
	}
	
	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getTransportationResponsibility() {
		return transportationResponsibility;
	}

	public void setTransportationResponsibility(String transportationResponsibility) {
		this.transportationResponsibility = transportationResponsibility;
	}

	public String getTradeZone() {
		return tradeZone;
	}

	public void setTradeZone(String tradeZone) {
		this.tradeZone = tradeZone;
	}
	
	public Date getTransportStartDate() {
		return transportStartDate;
	}

	public void setTransportStartDate(Date transportStartDate) {
		this.transportStartDate = transportStartDate;
	}
    
    /*
     * End of getters and setters section
     * */
    

    /*
     * Active record functionality
     * */
    
	public static long countTransportDocuments() {
        return count(TransportDocument.class);
    }
    
    public static List<TransportDocument> findAllTransportDocuments() {
        return findAll(TransportDocument.class);
    }
    
    public static TransportDocument findTransportDocument(Long id) {
        return find(TransportDocument.class, id);
    }
    
    public static List<TransportDocument> findTransportDocumentEntries(int firstResult, int maxResults) {
        return findInRange(TransportDocument.class, firstResult, maxResults);
    }
    
    @Transactional
    public TransportDocument merge() {
        return merge(this);
    }
	
    /*
     * End of active record functionality
     * */

}
