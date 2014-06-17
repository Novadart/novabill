package com.novadart.novabill.web.mvc;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.novadart.novabill.paypal.PaymentPlansLoader;
import com.novadart.novabill.service.UtilsService;

@Controller
public class UpgradeAccountController {
	
	@Autowired
	private UtilsService utilsService;
	
	@Value("${paypal.action}")
	private String paypalAction;

	@Autowired
	private PaymentPlansLoader paymentPlans;
	

	@RequestMapping(value = Urls.PRIVATE_PREMIUM, method = RequestMethod.GET)
	@Transactional(readOnly = false)
	public String display(Model model, HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException, NoSuchAlgorithmException{
		String email = utilsService.getAuthenticatedPrincipalDetails().getUsername();
		model.addAttribute("paypalAction", paypalAction);
		model.addAttribute("hostedButtonIDOneYear", paymentPlans.getPayPalPaymentPlanDescriptors()[0].getHostedButtonID());
		model.addAttribute("hostedButtonIDTwoYears", paymentPlans.getPayPalPaymentPlanDescriptors()[1].getHostedButtonID());
		model.addAttribute("email", email);
		return "private.premium";
	}
	
}
