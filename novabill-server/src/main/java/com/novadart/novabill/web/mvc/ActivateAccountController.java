package com.novadart.novabill.web.mvc;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.domain.Registration;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.dto.PaymentDateType;

/*
 * ActivateAccountController controller method handles the activation of previously made
 * registration request. The registration data is identified by the email address and the
 * activation token. In order to activate the account the user clicks on a link sent via email
 * that leads to the activation page where the password needs to be re-entered. Upon
 * submitting the activation request data is validated for correctness and a principal and 
 * an associated business objects are created. 
 */
@Controller
@RequestMapping("/activate")
@SessionAttributes({"registration"})
public class ActivateAccountController {
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private MessageSource messageSource;
	
	private Map<Locale, PaymentType[]> paymentTypes;
	
	@PostConstruct
	public void init(){
		paymentTypes = new HashMap<Locale, PaymentType[]>();
		paymentTypes.put(Locale.ITALIAN, new PaymentType[]{
			new PaymentType(messageSource.getMessage("payment1.name", null, "Rimessa Diretta", Locale.ITALIAN),
							messageSource.getMessage("payment1.paymentNote", null, "Pagamento in Rimessa Diretta", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 0),
			new PaymentType(messageSource.getMessage("payment2.name", null, "Bonifico Bancario 30GG", Locale.ITALIAN),
							messageSource.getMessage("payment2.paymentNote", null, "Pagamento con bonifico bancario entro 30 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 30),
			new PaymentType(messageSource.getMessage("payment3.name", null, "Bonifico Bancario 60GG", Locale.ITALIAN),
							messageSource.getMessage("payment3.paymentNote", null, "Pagamento con bonifico bancario entro 60 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 60),
			new PaymentType(messageSource.getMessage("payment4.name", null, "Bonifico Bancario 90GG", Locale.ITALIAN),
							messageSource.getMessage("payment4.paymentNote", null, "Pagamento con bonifico bancario entro 90 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 90),
			new PaymentType(messageSource.getMessage("payment5.name", null, "Bonifico Bancario 30GG d.f. f.m.", Locale.ITALIAN),
							messageSource.getMessage("payment5.paymentNote", null, "Pagamento con bonifico bancario entro 30 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 30),
			new PaymentType(messageSource.getMessage("payment6.name", null, "Bonifico Bancario 60GG d.f. f.m.", Locale.ITALIAN), 
							messageSource.getMessage("payment6.paymentNote", null, "Pagamento con bonifico bancario entro 60 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 60),
			new PaymentType(messageSource.getMessage("payment7.name", null, "Bonifico Bancario 90GG d.f. f.m.", Locale.ITALIAN),
							messageSource.getMessage("payment7.paymentNote", null, "Pagamento con bonifico bancario entro 90 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 90),
		});
	}

	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(@RequestParam("email") String email, @RequestParam("token") String token, Model model) throws CloneNotSupportedException{
		if(Principal.findByUsername(email) != null) //registered user already exists
			return "invalidActivationRequest";
		for(Registration registration: Registration.findRegistrations(email, token)){
			if(registration.getExpirationDate().before(new Date())){ //expired
				registration.remove();
				continue;
			}
			model.addAttribute("registration", registration.clone());
			return "activate";
		}
		return "invalidActivationRequest";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public String processSubmit(@RequestParam("j_username") String j_username, @RequestParam("j_password") String j_password,
			@ModelAttribute("registration") Registration registration, Model model, SessionStatus status, Locale locale) throws CloneNotSupportedException{
		if(!StringUtils.equals(utilsService.hash(j_password, registration.getCreationTime()), registration.getPassword())){
			model.addAttribute("wrongPassword", true);
			return "activate";
 		}
		Principal principal = new Principal(registration);
		principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_FREE);
		Business business = new Business();
		for(PaymentType pType: paymentTypes.containsKey(locale)? paymentTypes.get(locale): paymentTypes.get(Locale.ITALIAN)){
			PaymentType paymentType = pType.clone();
			paymentType.setBusiness(business);
			business.getPaymentTypes().add(paymentType);
		}
		principal.setBusiness(business);
		business.getPrincipals().add(principal);
		business.persist();
		registration.remove();
		status.setComplete();
		return "forward:/resources/j_spring_security_check";
	}
	
}
