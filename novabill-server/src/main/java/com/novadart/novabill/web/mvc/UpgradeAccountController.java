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
import org.springframework.web.bind.annotation.RequestParam;
import com.novadart.novabill.domain.SubscriptionToken;
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.UtilsService;

@Controller
@RequestMapping("/private/upgrade")
public class UpgradeAccountController {
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@Value("${paypal.action}")
	private String paypalAction;
	
	@Value("${paypal.hostedButtonID}")
	private String hostedButtonID;

	@RequestMapping("/send-paypal-supscription-request")
	@Transactional(readOnly = false)
	public String sendPaypalSubscriptionRequest(Model model, HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException, NoSuchAlgorithmException{
		String email = utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getEmail();
		String token = tokenGenerator.generateToken();
		SubscriptionToken subcriptionToken = new SubscriptionToken();
		subcriptionToken.setEmail(email);
		subcriptionToken.setToken(token);
		subcriptionToken.persist();
		String returnURL = new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
				request.getContextPath() + String.format("/private/upgrade/paypal-callback?email=%s&novabillToken=%s", 
						URLEncoder.encode(email, "UTF-8"), URLEncoder.encode(token, "UTF-8"))).toString();
		model.addAttribute("paypalAction", paypalAction);
		model.addAttribute("hostedButtonID", hostedButtonID);
		model.addAttribute("returnUrl", returnURL);
		model.addAttribute("email", email);
		return "paypalSubscriptionRequest";
	}
	
	
	
	private void handleError(String email, String message){}

	@RequestMapping("/paypal-callback")
	@Transactional(readOnly = false)
	public String handlePaypalReturn(@RequestParam("novabillToken") String returnedNovabillToken, @RequestParam("email") String email){
		List<SubscriptionToken> subscribtionTokens = SubscriptionToken.findByEmail(email);
		if(subscribtionTokens.size() == 0){
			handleError(email, "No associated tokens");
			return "premiumUpgradeFailure";
		}
		boolean found = false;
		for(SubscriptionToken st: subscribtionTokens){
			if(st.getToken().equals(returnedNovabillToken))
				found = true;
			st.remove();
		}
		if(found)
			return "premiumUpgradeSuccess";
		handleError(email, "Token mismatch");
		return "premiumUpgradeFailure";
	}
	
}
