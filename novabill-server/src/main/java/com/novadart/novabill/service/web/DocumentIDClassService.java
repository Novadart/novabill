package com.novadart.novabill.service.web;

import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.TrialOrPremiumChecker;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.DocumentIDClass;
import com.novadart.novabill.domain.dto.transformer.DocumentIDClassDTOTransformer;
import com.novadart.novabill.service.validator.DocumentIDClassValidator;
import com.novadart.novabill.shared.client.dto.DocumentIDClassDTO;
import com.novadart.novabill.shared.client.exception.*;
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

    @Restrictions(checkers = {TrialOrPremiumChecker.class})
    @PreAuthorize("#businessID == principal.business.id")
    public List<DocumentIDClassDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
        return businessService.getDocumentIdClasses(businessID);
    }


    @Restrictions(checkers = {TrialOrPremiumChecker.class})
    @PreAuthorize("T(com.novadart.novabill.domain.DocumentIDClass).findDocumentIDClass(#id)?.business?.id == principal.business.id")
    public DocumentIDClassDTO get(Long businessID, Long id) throws NoSuchObjectException, NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
        List<DocumentIDClassDTO> documentIDClassesDTOs = businessService.getDocumentIdClasses(businessID);
        for(DocumentIDClassDTO documentIDClassDTO : documentIDClassesDTOs){
            if(documentIDClassDTO.getId().equals(id))
                return documentIDClassDTO;
        }
        throw new NoSuchObjectException();
    }

    @Restrictions(checkers = {TrialOrPremiumChecker.class})
    @Transactional(readOnly = false, rollbackFor = {ValidationException.class})
    @PreAuthorize("#docIDClassDTO?.business?.id == principal.business.id and " +
            "#docIDClassDTO != null and #docIDClassDTO.id == null")
    public Long add(Long businessID, DocumentIDClassDTO docIDClassDTO) throws ValidationException, NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
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

    @Restrictions(checkers = {TrialOrPremiumChecker.class})
    @Transactional(readOnly = false)
    @PreAuthorize("#businessID == principal.business.id and " +
            "T(com.novadart.novabill.domain.DocumentIDClass).findDocumentIDClass(#id)?.business?.id == #businessID")
    public boolean remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
        DocumentIDClass docIDClass = DocumentIDClass.findDocumentIDClass(id);
        if(docIDClass.hasInvoices())
            return false;
        for(Client client: docIDClass.getClients())
            client.setDefaultDocumentIDClass(null);
        docIDClass.remove();
        if(Hibernate.isInitialized(docIDClass.getBusiness().getDocumentIDClasses()))
            docIDClass.getBusiness().getDocumentIDClasses().remove(docIDClass);
        return true;
    }

    @Restrictions(checkers = {TrialOrPremiumChecker.class})
    @Transactional(readOnly = false, rollbackFor = {ValidationException.class})
    @PreAuthorize("principal.business.id == #businessID and #documentIDClassDTO?.id != null and " +
            "T(com.novadart.novabill.domain.DocumentIDClass).findDocumentIDClass(#documentIDClassDTO?.id)?.business?.id == principal.business.id")
    public void update(Long businessID, DocumentIDClassDTO documentIDClassDTO) throws NoSuchObjectException, ValidationException, DataAccessException, FreeUserAccessForbiddenException, NotAuthenticatedException {
        DocumentIDClass persistedDocumentIDClass = DocumentIDClass.findDocumentIDClass(documentIDClassDTO.getId());
        if(persistedDocumentIDClass == null)
            throw new NoSuchObjectException();
        DocumentIDClass copy = persistedDocumentIDClass.shallowCopy();
        DocumentIDClassDTOTransformer.copyFromDTO(copy, documentIDClassDTO);
        validator.validate(copy, !persistedDocumentIDClass.getSuffix().equalsIgnoreCase(documentIDClassDTO.getSuffix()));
        DocumentIDClassDTOTransformer.copyFromDTO(persistedDocumentIDClass, documentIDClassDTO);
    }

}
