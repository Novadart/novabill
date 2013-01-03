package com.novadart.novabill.web.mvc;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.novadart.novabill.service.UtilsService;

@Controller
@RequestMapping("/concurrentAccess")
public class ConcurrentAccessController {
	
	@Autowired
	private UtilsService utilsService;
	
	@RequestMapping("/gwt")
	public void handleGWTRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//utilsService.sendException(request, response, new ConcurrentAccessException());
	}
	
	@RequestMapping
	public String handleRequest(HttpServletRequest request){
		if(utilsService.isGWTRPCCall(request))
			return "forward:/concurrentAccess/gwt";
		return "concurrentAccess";
	}

}
