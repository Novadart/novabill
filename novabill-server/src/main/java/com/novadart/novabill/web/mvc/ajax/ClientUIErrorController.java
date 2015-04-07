package com.novadart.novabill.web.mvc.ajax;

import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.shared.client.exception.ClientUIException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/clientuierror")
public class ClientUIErrorController {
	
	@RequestMapping(method = RequestMethod.POST)
	public void postError(@RequestBody Map<String, Object> error) throws ClientUIException{
		throw new ClientUIException(error);
	}

}
