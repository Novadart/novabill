package com.novadart.novabill.web.mvc;

import com.novadart.novabill.domain.Transaction;
import com.novadart.novabill.paypal.PayPalIPNHandlerService;
import com.novadart.novabill.shared.client.exception.PremiumUpgradeException;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(Urls.PUBLIC_PAYPAL_IPN_LISTENER)
public class PayPalIPNListenerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PayPalIPNListenerController.class);
	
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

	private final static String CHARSET = "charset";
    
	private boolean verifyIPN(HttpServletRequest request, String transactionID) throws URISyntaxException, ClientProtocolException, IOException{
		//passing back the message to paypal
		//Prepare 'notify-validate' command with exactly the same parameters
		Enumeration en = request.getParameterNames();
		StringBuilder cmd = new StringBuilder("cmd=_notify-validate");
		String paramName;
		String paramValue;
		while (en.hasMoreElements()) {
			paramName = (String) en.nextElement();
			paramValue = request.getParameter(paramName);
			cmd.append("&").append(paramName).append("=")
					.append(URLEncoder.encode(paramValue, request.getParameter("charset")));
		}

		//Post above command to Paypal IPN URL
		URL u = new URL(payPalUrl);
		HttpsURLConnection uc = (HttpsURLConnection) u.openConnection();
		uc.setDoOutput(true);
		uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		uc.setRequestProperty("Host", "www.paypal.com");
		PrintWriter pw = new PrintWriter(uc.getOutputStream());
		pw.println(cmd.toString());
		pw.close();

		//Read response from Paypal
		BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String responseText = in.readLine();
		in.close();

		LOGGER.info(String.format("Paypal response for transaction %s: %s", transactionID, responseText));
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
		LOGGER.info(
				String.format("IPN for transaction %s received. Params: %s", transactionID, parametersMap.toString()));
    	if(!verifyIPN(request, transactionID)){
			LOGGER.warn(String.format("Paypal verification of IPN for transaction %s failed.", transactionID));
			return;
		}
    	String email = parametersMap.get(RECEIVER_EMAIL);
    	if(email!= null && !email.equals(payPalEmail)) {//email doesn't match
			LOGGER.warn(
					String.format("Validation of IPN for transaction %s failed. Email mismatch expected: %s, actual: %s",
							transactionID, payPalEmail, email));
			return;
		}
    	Transaction transaction = null;
    	if(transactionID != null){
    		if(Transaction.findByTransactionID(transactionID).size() > 0) {
				LOGGER.info(String.format("IPN for transaction %s already processed. Discarding...", transactionID));
				return; //already processed thus ignore
			}else
    			transaction = new Transaction(transactionID).merge(); //store transaction id
    	}
		LOGGER.info(String.format("Processing IPN for transaction %s", transactionID));
    	for(PayPalIPNHandlerService ipnHandler: ipnHandlers)
    		ipnHandler.handle(transactionType, parametersMap, transaction);
    }

}
