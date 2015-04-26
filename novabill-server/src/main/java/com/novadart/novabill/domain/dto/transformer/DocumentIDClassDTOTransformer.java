package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.DocumentIDClass;
import com.novadart.novabill.shared.client.dto.DocumentIDClassDTO;

public class DocumentIDClassDTOTransformer {

    public static DocumentIDClassDTO toDTO(DocumentIDClass documentIDClass){
        if(documentIDClass == null)
            return null;
        DocumentIDClassDTO documentIDClassDTO = new DocumentIDClassDTO();
        documentIDClassDTO.setId(documentIDClass.getId());
        documentIDClassDTO.setSuffix(documentIDClass.getSuffix());
        return documentIDClassDTO;
    }

    public static void copyFromDTO(DocumentIDClass documentIDClass, DocumentIDClassDTO documentIDClassDTO){
        if(documentIDClass == null || documentIDClassDTO == null)
            return;
        documentIDClass.setSuffix(documentIDClassDTO.getSuffix());
    }

}
