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
				request.getContextPath() + String.format("/private/upgrade/paypal-callback?email=%s&novabillToken=%s", 
						URLEncoder.encode(email, "UTF-8"), URLEncoder.encode(token, "UTF-8"))).toString();
		model.addAttribute("paypalAction", paypalAction);
		model.addAttribute("hostedButtonID", hostedButtonID);
		model.addAttribute("returnUrl", returnURL);
		model.addAttribute("email", email);
		return "upgrade";
	}
	
	private void handleError(String email, String message){}

	@RequestMapping("/paypal-callback")
	@Transactional(readOnly = false)
	public String handlePaypalReturn(@RequestParam("novabillToken") String returnedNovabillToken, @RequestParam("email") String email){
		List<UpgradeToken> upgradeTokens = UpgradeToken.findByEmail(email);
		if(upgradeTokens.size() == 0){
			handleError(email, "No associated tokens");
			return "premiumUpgradeFailure";
		}
		boolean found = false;
		for(UpgradeToken ut: upgradeTokens){
			if(ut.getToken().equals(returnedNovabillToken))
				found = true;
			ut.remove();
		}
		if(found)
			return "premiumUpgradeSuccess";
		handleError(email, "Token mismatch");
		return "premiumUpgradeFailure";
	}
	
}
