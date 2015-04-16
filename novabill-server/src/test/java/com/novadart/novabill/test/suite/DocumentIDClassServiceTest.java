package com.novadart.novabill.test.suite;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.novadart.novabill.aspect.logging.DBLoggerAspect;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.DocumentIDClass;
import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.domain.dto.transformer.BusinessDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.DocumentIDClassDTOTransformer;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.web.DocumentIDClassService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.DocumentIDClassDTO;
import com.novadart.novabill.shared.client.exception.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ActiveProfiles("dev")
public class DocumentIDClassServiceTest extends ServiceTest{

    @Autowired
    private DocumentIDClassService docIDClassesService;

    @Override
    @Before
    public void authenticate() {
        authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
        authenticatePrincipal(authenticatedPrincipal);
    }

    @Test
    public void docIDClassesServiceWiringTest(){
        assertNotNull(docIDClassesService);
    }

    @Test
    public void addAuthorizedTest() throws ValidationException, JsonParseException, JsonMappingException, IOException, FreeUserAccessForbiddenException, DataAccessException, NotAuthenticatedException {
        DocumentIDClassDTO docIDClassDTO = DocumentIDClassDTOTransformer.toDTO(TestUtils.createDocumentIDClass());
        docIDClassDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
        Long businessID = authenticatedPrincipal.getBusiness().getId();
        Long id = docIDClassesService.add(businessID, docIDClassDTO);
        DocumentIDClass.entityManager().flush();
        DocumentIDClassDTO persistedDTO = DocumentIDClassDTOTransformer.toDTO(DocumentIDClass.findDocumentIDClass(id));
        assertTrue(EqualsBuilder.reflectionEquals(docIDClassDTO, persistedDTO, "id", "business"));
        LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
        assertEquals(EntityType.DOCUMENT_ID_CLASS, rec.getEntityType());
        assertEquals(id, rec.getEntityID());
        assertEquals(OperationType.CREATE, rec.getOperationType());
        Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
        assertEquals(docIDClassDTO.getSuffix(), details.get(DBLoggerAspect.DOCUMENT_ID_CLASS_SUFFIX));
    }

    @Test
    public void addAuthorizedLowercasedSuffixTest() throws ValidationException, JsonParseException, JsonMappingException, IOException, FreeUserAccessForbiddenException, DataAccessException, NotAuthenticatedException {
        DocumentIDClassDTO docIDClassDTO = DocumentIDClassDTOTransformer.toDTO(TestUtils.createDocumentIDClass());
        docIDClassDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
        docIDClassDTO.setSuffix("UUID");
        Long businessID = authenticatedPrincipal.getBusiness().getId();
        Long id = docIDClassesService.add(businessID, docIDClassDTO);
        DocumentIDClass.entityManager().flush();
        DocumentIDClassDTO persistedDTO = DocumentIDClassDTOTransformer.toDTO(DocumentIDClass.findDocumentIDClass(id));
        assertEquals("uuid", persistedDTO.getSuffix());
        LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
        assertEquals(EntityType.DOCUMENT_ID_CLASS, rec.getEntityType());
        assertEquals(id, rec.getEntityID());
        assertEquals(OperationType.CREATE, rec.getOperationType());
        Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
        assertEquals(docIDClassDTO.getSuffix(), details.get(DBLoggerAspect.DOCUMENT_ID_CLASS_SUFFIX));
    }

    @Test(expected = AccessDeniedException.class)
    public void addNulltest() throws ValidationException, FreeUserAccessForbiddenException, DataAccessException, NotAuthenticatedException{
        Long businessID = authenticatedPrincipal.getBusiness().getId();
        docIDClassesService.add(businessID, null);
    }

    @Test(expected = AccessDeniedException.class)
    public void addIDNotNullTest() throws ValidationException, FreeUserAccessForbiddenException, DataAccessException, NotAuthenticatedException{
        DocumentIDClassDTO docIDClassDTO = DocumentIDClassDTOTransformer.toDTO(TestUtils.createDocumentIDClass());
        docIDClassDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
        docIDClassDTO.setId(1l);
        Long businessID = authenticatedPrincipal.getBusiness().getId();
        docIDClassesService.add(businessID, docIDClassDTO);
    }

    @Test(expected = AccessDeniedException.class)
    public void addUnauthorizedTest() throws ValidationException, FreeUserAccessForbiddenException, DataAccessException, NotAuthenticatedException{
        DocumentIDClassDTO docIDClassDTO = DocumentIDClassDTOTransformer.toDTO(TestUtils.createDocumentIDClass());
        docIDClassDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
        Long businessID = authenticatedPrincipal.getBusiness().getId();
        docIDClassesService.add(businessID, docIDClassDTO);
    }

    @Test
    public void removeAutorizedTest() throws JsonParseException, JsonMappingException, IOException{
        Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
        DocumentIDClass docIDClass = business.getDocumentIDClasses().iterator().next();
        docIDClassesService.remove(business.getId(), docIDClass.getId());
        assertTrue(DocumentIDClass.findDocumentIDClass(docIDClass.getId()) == null);
        LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
        assertEquals(EntityType.DOCUMENT_ID_CLASS, rec.getEntityType());
        assertEquals(docIDClass.getId(), rec.getEntityID());
        assertEquals(OperationType.DELETE, rec.getOperationType());
        Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
        assertEquals(docIDClass.getSuffix(), details.get(DBLoggerAspect.DOCUMENT_ID_CLASS_SUFFIX));
        assertEquals(true, rec.isReferringToDeletedEntity());
    }

    @Test
    public void removeAutorizedNullAssociatedClientsTest() throws JsonParseException, JsonMappingException, IOException{
        Business business = Business.findBusiness(authenticatedPrincipal.getBusiness().getId());
        DocumentIDClass docIDClass = business.getDocumentIDClasses().iterator().next();
        Long clientID = authenticatedPrincipal.getBusiness().getClients().iterator().next().getId();
        Client client = Client.findClient(clientID);
        docIDClass.getClients().add(client);
        client.setDefaultDocumentIDClass(docIDClass);
        Client.entityManager().flush();
        docIDClassesService.remove(business.getId(), docIDClass.getId());
        assertTrue(DocumentIDClass.findDocumentIDClass(docIDClass.getId()) == null);
        LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
        assertEquals(EntityType.DOCUMENT_ID_CLASS, rec.getEntityType());
        assertEquals(docIDClass.getId(), rec.getEntityID());
        assertEquals(OperationType.DELETE, rec.getOperationType());
        Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
        assertEquals(docIDClass.getSuffix(), details.get(DBLoggerAspect.DOCUMENT_ID_CLASS_SUFFIX));
        assertEquals(true, rec.isReferringToDeletedEntity());
        assertEquals(null, client.getDefaultDocumentIDClass());
    }

    @Test(expected = AccessDeniedException.class)
    public void removeIdNullTest(){
        docIDClassesService.remove(authenticatedPrincipal.getBusiness().getId(), null);
    }

    @Test(expected = AccessDeniedException.class)
    public void removeBusinessIDNullTest(){
        Long id = Business.findBusiness(authenticatedPrincipal.getBusiness().getId()).getDocumentIDClasses().iterator().next().getId();
        docIDClassesService.remove(null, id);
    }

    @Test(expected = AccessDeniedException.class)
    public void removeUnauthorizedTest(){
        Long id = Business.findBusiness(authenticatedPrincipal.getBusiness().getId()).getDocumentIDClasses().iterator().next().getId();
        docIDClassesService.remove(getUnathorizedBusinessID(), id);
    }

    @Test
    public void getAllTest() throws NotAuthenticatedException, DataAccessException {
        List<DocumentIDClassDTO> dtos = docIDClassesService.getAll(authenticatedPrincipal.getBusiness().getId());
        assertEquals(1, dtos.size());
    }

    @Test
    public void updateAuthenticatedTest() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ValidationException, JsonParseException, JsonMappingException, IOException{
        Long docIDClassID = authenticatedPrincipal.getBusiness().getDocumentIDClasses().iterator().next().getId();
        DocumentIDClass expectedDocIDClass = DocumentIDClass.findDocumentIDClass(docIDClassID);
        expectedDocIDClass.setSuffix("bbis");
        docIDClassesService.update(authenticatedPrincipal.getBusiness().getId(), DocumentIDClassDTOTransformer.toDTO(expectedDocIDClass));
        DocumentIDClass.entityManager().flush();
        DocumentIDClass actualDocIDClass = DocumentIDClass.findDocumentIDClass(docIDClassID);
        assertEquals(actualDocIDClass.getSuffix(), "bbis");
        LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
        assertEquals(EntityType.DOCUMENT_ID_CLASS, rec.getEntityType());
        assertEquals(docIDClassID, rec.getEntityID());
        assertEquals(OperationType.UPDATE, rec.getOperationType());
        Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
        assertEquals(expectedDocIDClass.getSuffix(), details.get(DBLoggerAspect.DOCUMENT_ID_CLASS_SUFFIX));
    }

    @Test(expected = AccessDeniedException.class)
    public void updateAuthenticatedClientIDNull() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ValidationException{
        docIDClassesService.update(authenticatedPrincipal.getBusiness().getId(), DocumentIDClassDTOTransformer.toDTO(TestUtils.createDocumentIDClass()));
    }

    @Test(expected = AccessDeniedException.class)
    public void updateAuthenticatedClientNull() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ValidationException{
        docIDClassesService.update(authenticatedPrincipal.getBusiness().getId(), null);
    }

    @Test(expected = AccessDeniedException.class)
    public void updateUnauthenticatedClient() throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, ValidationException{
        DocumentIDClass docIDClass = authenticatedPrincipal.getBusiness().getDocumentIDClasses().iterator().next();
        docIDClass.setSuffix("bbis");
        docIDClassesService.update(getUnathorizedBusinessID(), DocumentIDClassDTOTransformer.toDTO(docIDClass));
    }

    @Test(expected = ValidationException.class)
    public void duplicateSuffixTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
        Long businessID = authenticatedPrincipal.getBusiness().getId();
        DocumentIDClassDTO priceListDTO = DocumentIDClassDTOTransformer.toDTO(TestUtils.createDocumentIDClass());
        priceListDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
        docIDClassesService.add(businessID, priceListDTO);
        docIDClassesService.add(businessID, priceListDTO);
    }

    @Test
    public void safeDuplicateSuffixOnUpdateTest() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
        Long businessID = authenticatedPrincipal.getBusiness().getId();
        DocumentIDClassDTO docIDClassDTO = DocumentIDClassDTOTransformer.toDTO(TestUtils.createDocumentIDClass());
        docIDClassDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
        Long id = docIDClassesService.add(businessID, docIDClassDTO);
        docIDClassDTO.setId(id);
        docIDClassesService.update(businessID, docIDClassDTO);
    }

    @Test(expected = ValidationException.class)
    public void caseInsensitiveSuffixTest() throws ValidationException {
        DocumentIDClassDTO docIDClassDTO = DocumentIDClassDTOTransformer.toDTO(TestUtils.createDocumentIDClass());
        docIDClassDTO.setSuffix("bis");
        docIDClassDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
        Long businessID = authenticatedPrincipal.getBusiness().getId();
        docIDClassesService.add(businessID, docIDClassDTO);
        DocumentIDClass.entityManager().flush();
        docIDClassDTO = DocumentIDClassDTOTransformer.toDTO(TestUtils.createDocumentIDClass());
        docIDClassDTO.setSuffix("BIS");
        docIDClassDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
        docIDClassesService.add(businessID, docIDClassDTO);
    }

}
