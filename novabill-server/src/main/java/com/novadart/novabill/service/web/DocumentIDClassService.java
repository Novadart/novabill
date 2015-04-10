package com.novadart.novabill.service.web;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.DocumentIDClass;
import com.novadart.novabill.domain.dto.transformer.DocumentIDClassDTOTransformer;
import com.novadart.novabill.service.validator.DocumentIDClassValidator;
import com.novadart.novabill.shared.client.dto.DocumentIDClassDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DocumentIDClassService {

    @Autowired
    private DocumentIDClassValidator validator;

    @Autowired
    private BusinessService businessService;

    @PreAuthorize("#businessID == principal.business.id")
    public List<DocumentIDClassDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException {
        return businessService.getDocumentIdClasses(businessID);
    }


    @PreAuthorize("T(com.novadart.novabill.domain.DocumentIDClass).findDocumentIDClass(#id)?.business?.id == principal.business.id")
    public DocumentIDClassDTO get(Long businessID, Long id) throws NoSuchObjectException, NotAuthenticatedException, DataAccessException {
        List<DocumentIDClassDTO> documentIDClassesDTOs = businessService.getDocumentIdClasses(businessID);
        for(DocumentIDClassDTO documentIDClassDTO : documentIDClassesDTOs){
            if(documentIDClassDTO.getId().equals(id))
                return documentIDClassDTO;
        }
        throw new NoSuchObjectException();
    }

    @Transactional(readOnly = false, rollbackFor = {ValidationException.class})
    @PreAuthorize("#docIDClassDTO?.business?.id == principal.business.id and " +
            "#docIDClassDTO != null and #docIDClassDTO.id == null")
    public Long add(Long businessID, DocumentIDClassDTO docIDClassDTO) throws ValidationException {
        DocumentIDClass docIDClass = new DocumentIDClass();
        DocumentIDClassDTOTransformer.copyFromDTO(docIDClass, docIDClassDTO);
        Business business = Business.findBusiness(docIDClassDTO.getBusiness().getId());
        docIDClass.setBusiness(business);
        validator.validate(docIDClass, true);
        business.getDocumentIDClasses().add(docIDClass);
        docIDClass.setBusiness(business);
        docIDClass.persist();
        docIDClass.flush();
        return docIDClass.getId();
    }

    @Transactional(readOnly = false)
    @PreAuthorize("#businessID == principal.business.id and " +
            "T(com.novadart.novabill.domain.DocumentIDClass).findDocumentIDClass(#id)?.business?.id == #businessID")
    public void remove(Long businessID, Long id) {
        DocumentIDClass docIDClass = DocumentIDClass.findDocumentIDClass(id);
        docIDClass.remove();
        if(Hibernate.isInitialized(docIDClass.getBusiness().getDocumentIDClasses()))
            docIDClass.getBusiness().getDocumentIDClasses().remove(docIDClass);
    }

    @Transactional(readOnly = false, rollbackFor = {ValidationException.class})
    @PreAuthorize("principal.business.id == #businessID and #documentIDClassDTO?.id != null and " +
            "T(com.novadart.novabill.domain.DocumentIDClass).findDocumentIDClass(#documentIDClassDTO?.id)?.business?.id == principal.business.id")
    public void update(Long businessID, DocumentIDClassDTO documentIDClassDTO) throws NoSuchObjectException, ValidationException {
        DocumentIDClass persistedDocumentIDClass = DocumentIDClass.findDocumentIDClass(documentIDClassDTO.getId());
        if(persistedDocumentIDClass == null)
            throw new NoSuchObjectException();
        DocumentIDClass copy = persistedDocumentIDClass.shallowCopy();
        DocumentIDClassDTOTransformer.copyFromDTO(copy, documentIDClassDTO);
        validator.validate(copy, !persistedDocumentIDClass.getSuffix().equals(documentIDClassDTO.getSuffix()));
        DocumentIDClassDTOTransformer.copyFromDTO(persistedDocumentIDClass, documentIDClassDTO);
    }

}
