package com.novadart.novabill.web.mvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novadart.novabill.domain.Transaction;
import com.novadart.novabill.paypal.PayPalIPNHandlerService;
import com.novadart.novabill.shared.client.exception.PremiumUpgradeException;

@Controller
@RequestMapping(Urls.PUBLIC_PAYPAL_IPN_LISTENER)
public class PayPalIPNListenerController {
	
	@Value("${paypal.url}")
	private String payPalUrl;
	
	@Value("${paypal.email}")
	private String payPalEmail;
	
	@Autowired
	private List<PayPalIPNHandlerService> ipnHandlers;
	
    private final static String PARAM_NAME_CMD = "cmd";
    
    private final static String PARAM_VAL_CMD = "_notify-validate";

    private final static String RESP_VERIFIED = "VERIFIED";
    
    private final static String RECEIVER_EMAIL = "receiver_email";
    
	private boolean verifyIPN(HttpServletRequest request) throws URISyntaxException, ClientProtocolException, IOException{
		//passing back the message to paypal
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(payPalUrl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(PARAM_NAME_CMD, PARAM_VAL_CMD)); //You need to add this parameter to tell PayPal to verify
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			String name = e.nextElement();
			String value = request.getParameter(name);
			params.add(new BasicNameValuePair(name, value));
		}
		post.setEntity(new UrlEncodedFormEntity(params, request.getCharacterEncoding() == null? "utf-8": request.getCharacterEncoding()));
		InputStream is = client.execute(post).getEntity().getContent();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String responseText = reader.readLine();
        is.close();
    	return RESP_VERIFIED.equals(responseText);
    }
    
	private Map<String, String> extractParameters(HttpServletRequest request){
    	Map<String, String> parametersMap = new HashMap<String, String>();
    	Enumeration<String> paramNames = request.getParameterNames();
    	while(paramNames.hasMoreElements()){
    		String paramName = paramNames.nextElement();
    		parametersMap.put(paramName, request.getParameter(paramName));
    	}	
    	return parametersMap;
    }
    
    @RequestMapping
    public @ResponseBody void processIPN(@RequestParam("txn_type") String transactionType, @RequestParam(value = "txn_id", required = false) String transactionID,
    		HttpServletRequest request) throws URISyntaxException, ClientProtocolException, IOException, PremiumUpgradeException{
    	Map<String, String> parametersMap = extractParameters(request);
    	if(!verifyIPN(request)) return;
    	String email = parametersMap.get(RECEIVER_EMAIL);
    	if(email!= null && !email.equals(payPalEmail))//email doesn't match
    		return;
    	Transaction transaction = null;
    	if(transactionID != null){
    		if(Transaction.findByTransactionID(transactionID).size() > 0)
    			return; //already processed thus ignore
    		else
    			transaction = new Transaction(transactionID).merge(); //store transaction id
    	}
    	for(PayPalIPNHandlerService ipnHandler: ipnHandlers)
    		ipnHandler.handle(transactionType, parametersMap, transaction);
    }

}
