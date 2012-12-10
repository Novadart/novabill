package com.novadart.novabill.web.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.novadart.novabill.domain.Feedback;
import com.novadart.novabill.service.UtilsService;


@Controller
@RequestMapping("/private/feedback")
public class FeedbackController {
	
	@Autowired
	private UtilsService utilsService;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String processSubmit(@RequestParam String name, @RequestParam String email, @RequestParam("issue") String category,
			@RequestParam("messsage") String message, @RequestParam String subject){
		try {
			Feedback feedback = new Feedback();
			feedback.setName(name);
			feedback.setEmail(email);
			feedback.setCategory(category);
			feedback.setMessage(message);
			feedback.setUsername(utilsService.getAuthenticatedPrincipalDetails().getUsername());
			feedback.merge();
			return "success";
		} catch (Exception e) {
			return "failure";  
		}
	}

}
