package com.novadart.novabill.web.mvc;

import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.ChangePasswordValidator;
import com.novadart.novabill.web.mvc.command.ChangePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping(Urls.PRIVATE_CHANGE_PASSWORD)
@SessionAttributes("changePassword")
public class ChangePasswordController {
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private ChangePasswordValidator validator;
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(Model model){
		ChangePassword changePassword = new ChangePassword();
		changePassword.setEmail(utilsService.getAuthenticatedPrincipalDetails().getUsername());
		model.addAttribute("changePassword", changePassword);
		return "private.changePassword";
	}

	@Transactional(readOnly = false)
	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit(@ModelAttribute("changePassword") ChangePassword changePassword, BindingResult result, SessionStatus status){
		validator.validate(changePassword, result);
		if(result.hasErrors())
			return "private.changePassword";
		else{
			Principal principal = Principal.findPrincipal(utilsService.getAuthenticatedPrincipalDetails().getId());
			principal.setPassword(changePassword.getNewPassword());
			status.setComplete();
			return "redirect:/private/";
		}
	}

}
