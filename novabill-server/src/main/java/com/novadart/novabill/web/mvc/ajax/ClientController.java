package com.novadart.novabill.web.mvc.ajax;

import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.service.web.ClientService;
import com.novadart.novabill.shared.client.dto.ClientAddressDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses/{businessID}/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public ClientDTO get(@PathVariable Long id) throws NoSuchObjectException, NotAuthenticatedException, DataAccessException {
        return clientService.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Long add(@PathVariable Long businessID, @RequestBody ClientDTO clientDTO) throws FreeUserAccessForbiddenException, ValidationException {
        return clientService.add(businessID, clientDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public boolean remove(@PathVariable Long businessID, @PathVariable Long id) throws NoSuchObjectException {
        return clientService.remove(businessID, id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void update(@PathVariable Long businessID, @RequestBody ClientDTO clientDTO) throws NoSuchObjectException, ValidationException {
        clientService.update(businessID, clientDTO);
    }

    @RequestMapping(value = "/{id}/addresses", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public List<ClientAddressDTO> getClientAddresses(@PathVariable Long id) throws NotAuthenticatedException, DataAccessException {
        return clientService.getClientAddresses(id);
    }

    @RequestMapping(value = "/{id}/addresses", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Long addClientAddress(@RequestBody ClientAddressDTO clientAddressDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException {
        return clientService.addClientAddress(clientAddressDTO);
    }

    @RequestMapping(value = "/{clientID}/addresses/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void removeClientAddress(@PathVariable Long clientID, @PathVariable Long id) throws NotAuthenticatedException, DataAccessException {
        clientService.removeClientAddress(clientID, id);
    }

    @RequestMapping(value = "/{clientID}/addresses/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void updateClientAddress(ClientAddressDTO clientAddressDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, NoSuchObjectException {
        clientService.updateClientAddress(clientAddressDTO);
    }
}
