package com.novadart.novabill.web.mvc;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.SubscriptionToken;
import com.novadart.novabill.domain.security.RoleTypes;
import com.novadart.novabill.service.PrincipalDetailsService;
import com.novadart.novabill.service.UtilsService;

@Controller
@RequestMapping("/private/upgrade")
public class UpgradeAccountController {
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private PrincipalDetailsService principalDetailsService;

	@RequestMapping("/send-paypal-supscription-request")
	@Transactional(readOnly = false)
	public String sendPaypalSubscriptionRequest(Model model, HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException{
		String email = utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getEmail();
		String token = "ShouldBeRandomValue";
		SubscriptionToken subcriptionToken = new SubscriptionToken();
		subcriptionToken.setEmail(email);
		subcriptionToken.setToken(token);
		subcriptionToken.persist();
		String returnURL = new URL(request.getScheme(), request.getServerName(), request.getServerPort(),
				request.getContextPath() + String.format("/private/upgrade/paypal-callback?email=%s&novabillToken=%s", 
						URLEncoder.encode(email, "UTF-8"), URLEncoder.encode(token, "UTF-8"))).toString();
		model.addAttribute("returnUrl", returnURL);
		return "paypalSubscriptionRequest";
	}
	
	@Transactional(readOnly = false)
	private void upgrade(String email, List<SubscriptionToken> subscribtionTokens){
		Business business = principalDetailsService.loadUserByUsername(email).getPrincipal();
		business.getGrantedRoles().remove(RoleTypes.ROLE_BUSINESS_FREE);
		business.getGrantedRoles().add(RoleTypes.ROLE_BUSINESS_PREMIUM);
		for(SubscriptionToken st: subscribtionTokens)
			st.remove();
	}
	
	private void handleError(String email, String message){}

	@RequestMapping("/paypal-callback")
	public String handlePaypalReturn(@RequestParam("novabillToken") String returnedNovabillToken, @RequestParam("email") String email){
		List<SubscriptionToken> subscribtionTokens = SubscriptionToken.findByEmail(email);
		if(subscribtionTokens.size() == 0){
			handleError(email, "No associated tokens");
			return "errorPath";
		}
		for(SubscriptionToken st: subscribtionTokens){
			if(st.getToken().equals(returnedNovabillToken)){
				upgrade(email, subscribtionTokens);
				return "successPath";
			}
		}
		handleError(email, "Token mismatch");
		return "errorPath";
	}
	
}
