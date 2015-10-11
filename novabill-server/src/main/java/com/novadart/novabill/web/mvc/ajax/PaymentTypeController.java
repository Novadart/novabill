package com.novadart.novabill.web.mvc.ajax;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.service.web.PaymentTypeService;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses/{businessID}/paymenttypes")
public class PaymentTypeController {

    @Autowired
    private PaymentTypeService paymentTypeService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public List<PaymentTypeDTO> getAll(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException {
        return paymentTypeService.getAll(businessID);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public PaymentTypeDTO get(@PathVariable Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException {
        return paymentTypeService.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, Object> add(@RequestBody PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, DataAccessException, ValidationException {
        return ImmutableMap.of(JsonConst.VALUE, paymentTypeService.add(paymentTypeDTO));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void update(@RequestBody PaymentTypeDTO paymentTypeDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, NoSuchObjectException, DataAccessException, ValidationException {
        paymentTypeService.update(paymentTypeDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void remove(@PathVariable Long businessID, @PathVariable Long id) throws NotAuthenticatedException, DataAccessException {
        paymentTypeService.remove(businessID, id);
    }

}
