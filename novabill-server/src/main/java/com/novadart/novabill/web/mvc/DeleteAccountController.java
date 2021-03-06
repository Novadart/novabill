package com.novadart.novabill.web.mvc;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.web.mvc.command.DeleteAccount;

@Controller
@RequestMapping(Urls.PRIVATE_DELETE_ACCOUNT)
@SessionAttributes("deleteAccount")
public class DeleteAccountController {
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private BusinessLogoController businessLogoController;
	
	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(Model model, HttpSession session) throws NoSuchAlgorithmException {
		DeleteAccount deleteAccount = new DeleteAccount();
		model.addAttribute("deleteAccount", deleteAccount);
		
		model.addAttribute("exportClientsParamName", ExportController.CLIENTS_REQUEST_PARAM);
		model.addAttribute("exportInvoicesParamName", ExportController.INVOICES_REQUEST_PARAM);
		model.addAttribute("exportEstimationsParamName", ExportController.ESTIMATIONS_REQUEST_PARAM);
		model.addAttribute("exportCreditnotesParamName", ExportController.CREDITNOTES_REQUEST_PARAM);
		model.addAttribute("exportTransportdocsParamName", ExportController.TRANSPORTDOCS_REQUEST_PARAM);
		model.addAttribute("exportTokenParamName", ExportController.TOKEN_REQUEST_PARAM);
		
		return "private.deleteAccount";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public String processSubmit(@ModelAttribute("deleteAccount") DeleteAccount deleteAccount, BindingResult result, SessionStatus status){
		validator.validate(deleteAccount, result);
		if(result.hasErrors())
			return "private.deleteAccount";
		Principal principal = Principal.findPrincipal(utilsService.getAuthenticatedPrincipalDetails().getId());
		if(!utilsService.isPasswordValid(principal.getPassword(), deleteAccount.getPassword())){
			result.rejectValue("password", "deleteAccount.wrong.password");
			return "private.deleteAccount";
		}
		businessLogoController.clearLogo(principal.getBusiness().getId());
		principal.getBusiness().remove();
		status.setComplete();
		return "forward:" + Urls.PRIVATE_LOGOUT;
	}
	
}
