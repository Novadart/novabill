package com.novadart.novabill.web.mvc.ajax;

import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.service.web.DocumentIDClassService;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.DocumentIDClassDTO;
import com.novadart.novabill.shared.client.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses/{businessID}/docidclasses")
public class DocumentIDClassController {

    @Autowired
    private DocumentIDClassService docidclassesService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public List<DocumentIDClassDTO> getDocIDClasses(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException {
        return docidclassesService.getAll(businessID);
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public DocumentIDClassDTO add(@PathVariable Long businessID, @RequestBody DocumentIDClassDTO docIDClassDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
        Long id = docidclassesService.add(businessID, docIDClassDTO);
        DocumentIDClassDTO docIDCls = new DocumentIDClassDTO();
        docIDCls.setId(id);
        return docIDCls;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void update(@PathVariable Long businessID, @RequestBody DocumentIDClassDTO docIDClassDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
        docidclassesService.update(businessID, docIDClassDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public boolean remove(@PathVariable Long businessID, @PathVariable Long id) throws NotAuthenticatedException, DataAccessException{
        return docidclassesService.remove(businessID, id);
    }

}
