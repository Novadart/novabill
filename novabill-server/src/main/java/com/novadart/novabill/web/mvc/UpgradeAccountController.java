package com.novadart.novabill.web.mvc;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.novadart.novabill.domain.UpgradeToken;
import com.novadart.novabill.paypal.PaymentPlansLoader;
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.UtilsService;

@Controller
@RequestMapping(Urls.PRIVATE_PREMIUM)
public class UpgradeAccountController {
	
	public static final String PAYLOAD_SEPARATOR = ":65536:";
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@Value("${paypal.action}")
	private String paypalAction;

	@Autowired
	private PaymentPlansLoader paymentPlans;
	

	@RequestMapping(method = RequestMethod.GET)
	@Transactional(readOnly = false)
	public String display(Model model, HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException, NoSuchAlgorithmException{
		String email = utilsService.getAuthenticatedPrincipalDetails().getUsername();
		String token = tokenGenerator.generateToken();
		UpgradeToken upgradeToken = new UpgradeToken();
		upgradeToken.setEmail(email);
		upgradeToken.setToken(token);
		upgradeToken.persist();
		String returnURL = new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
				request.getContextPath() + String.format("/private/premium/paypal-callback?email=%s&novabillToken=%s", 
						URLEncoder.encode(email, "UTF-8"), URLEncoder.encode(token, "UTF-8"))).toString();
		model.addAttribute("paypalAction", paypalAction);
		model.addAttribute("hostedButtonIDOneYear", paymentPlans.getPayPalPaymentPlanDescriptors()[0].getHostedButtonID());
		model.addAttribute("hostedButtonIDTwoYears", paymentPlans.getPayPalPaymentPlanDescriptors()[1].getHostedButtonID());
		model.addAttribute("returnUrl", returnURL);
		model.addAttribute("payload", email + PAYLOAD_SEPARATOR + token);
		return "private.premium";
	}
	
	private void handleError(String email, String message){}

	@RequestMapping("/paypal-callback")
	@Transactional(readOnly = false)
	public String handlePaypalReturn(@RequestParam("novabillToken") String returnedNovabillToken, @RequestParam("email") String email){
		List<UpgradeToken> upgradeTokens = UpgradeToken.findByEmail(email);
		if(upgradeTokens.size() == 0){
			handleError(email, "No associated tokens");
			return "private.premiumUpgradeFailure";
		}
		boolean found = false;
		for(UpgradeToken ut: upgradeTokens){
			if(ut.getToken().equals(returnedNovabillToken))
				found = true;
			ut.remove();
		}
		if(found)
			return "private.premiumUpgradeSuccess";
		handleError(email, "Token mismatch");
		return "private.premiumUpgradeFailure";
	}
	
}
