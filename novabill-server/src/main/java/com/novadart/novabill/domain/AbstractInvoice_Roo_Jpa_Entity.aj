// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.novadart.novabill.domain;

import com.novadart.novabill.domain.AbstractInvoice;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Version;

privileged aspect AbstractInvoice_Roo_Jpa_Entity {
    
    declare @type: AbstractInvoice: @Entity;
    
    declare @type: AbstractInvoice: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long AbstractInvoice.id;
    
    @Version
    @Column(name = "version")
    private Integer AbstractInvoice.version;
    
    public Long AbstractInvoice.getId() {
        return this.id;
    }
    
    public void AbstractInvoice.setId(Long id) {
        this.id = id;
    }
    
    public Integer AbstractInvoice.getVersion() {
        return this.version;
    }
    
    public void AbstractInvoice.setVersion(Integer version) {
        this.version = version;
    }
    
}
