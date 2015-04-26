package com.novadart.novabill.aspect.logging;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.domain.DocumentIDClass;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.DocumentIDClassDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

public aspect DocumentIDClassServiceActionsAspect extends DBLoggerAspect{

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentIDClassServiceActionsAspect.class);

    @Autowired
    private UtilsService utilsService;

    pointcut add(Long businessID, DocumentIDClassDTO docIDClassDTO):
            execution(public Long com.novadart.novabill.service.web.DocumentIDClassService.add(..)) && args(businessID, docIDClassDTO);

    pointcut remove(Long businessID, Long id):
            execution(public boolean com.novadart.novabill.service.web.DocumentIDClassService.remove(..)) && args(businessID, id);

    pointcut update(Long businessID, DocumentIDClassDTO docIDClassDTO) :
            execution(public void com.novadart.novabill.service.web.DocumentIDClassService.update(..)) && args(businessID, docIDClassDTO);

    after(Long businessID, DocumentIDClassDTO docIDClassDTO) returning (Long id): add(businessID, docIDClassDTO) {
        Long time = System.currentTimeMillis();
        LOGGER.info("[{}, createDocumentIDClass, {}, businessID: {}, id: {}, dto: {}]",
                new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, id,
                        ReflectionToStringBuilder.toString(docIDClassDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
        Map<String, String> details = ImmutableMap.of(DOCUMENT_ID_CLASS_SUFFIX, docIDClassDTO.getSuffix());
        logActionInDB(businessID, EntityType.DOCUMENT_ID_CLASS, OperationType.CREATE, id, time, details);
    }

    boolean around(Long businessID, Long id): remove(businessID, id) {
        DocumentIDClass docIDClass = DocumentIDClass.findDocumentIDClass(id);
        boolean result = proceed(businessID, id);
        if(result) {
            Long time = System.currentTimeMillis();
            LOGGER.info("[{}, deleteDocumentIDClass, {}, businessID: {}, id: {}]",
                    new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID, id});
            Map<String, String> details = ImmutableMap.of(DOCUMENT_ID_CLASS_SUFFIX, docIDClass.getSuffix());
            logActionInDB(businessID, EntityType.DOCUMENT_ID_CLASS, OperationType.DELETE, id, time, details);
        }
        return result;
    }

    after(Long businessID, DocumentIDClassDTO docIDClassDTO) returning : update(businessID, docIDClassDTO){
        Long time = System.currentTimeMillis();
        LOGGER.info("[{}, updateDocumentIDClass, {}, businessID: {}, dto: {}]",
                new Object[]{utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(time), businessID,
                        ReflectionToStringBuilder.toString(docIDClassDTO, ToStringStyle.SHORT_PREFIX_STYLE)});
        Map<String, String> details = ImmutableMap.of(DOCUMENT_ID_CLASS_SUFFIX, docIDClassDTO.getSuffix());
        logActionInDB(businessID, EntityType.DOCUMENT_ID_CLASS, OperationType.UPDATE, docIDClassDTO.getId(), time, details);
    }


}
