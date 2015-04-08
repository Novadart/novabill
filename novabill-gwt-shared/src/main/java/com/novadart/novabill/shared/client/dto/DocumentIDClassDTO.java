package com.novadart.novabill.shared.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DocumentIDClassDTO implements IsSerializable {

    private Long id;

    private String suffix;

    private BusinessDTO business;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public BusinessDTO getBusiness() {
        return business;
    }

    public void setBusiness(BusinessDTO business) {
        this.business = business;
    }
}
