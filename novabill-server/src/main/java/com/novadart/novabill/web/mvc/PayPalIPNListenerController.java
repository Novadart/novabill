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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.novadart.novabill.domain.PayPalTransactionID;
import com.novadart.novabill.service.PayPalSubscriptionIPNHandlerService;

@Controller
@RequestMapping("/paypal-ipn-listener")
public class PayPalIPNListenerController {
	
	@Value("${paypal.url}")
	private String payPalUrl;
	
	@Autowired
	private PayPalSubscriptionIPNHandlerService subscriptionHandler;
	
	private final static String CONTENT_TYPE = "Content-Type";
	
    private final static String MIME_APP_URLENC = "application/x-www-form-urlencoded";
    
    private final static String PARAM_NAME_CMD = "cmd";
    
    private final static String PARAM_VAL_CMD = "_notify-validate";

    private final static String RESP_VERIFIED = "VERIFIED";
    
	@SuppressWarnings("unchecked")
	private boolean verifyIPN(HttpServletRequest request) throws URISyntaxException, ClientProtocolException, IOException{
		//passing back the message to paypal
		HttpClient client = new DefaultHttpClient();
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
    
    @SuppressWarnings("unchecked")
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
    		HttpServletRequest request) throws URISyntaxException, ClientProtocolException, IOException{
    	Map<String, String> parametersMap = extractParameters(request);
    	if(!verifyIPN(request)) return;
    	if(transactionID != null){
    		if(PayPalTransactionID.findByTransactionID(transactionID).size() > 0)
    			return; //already processed thus ignore
    		else
    			new PayPalTransactionID(transactionID).merge(); //store transaction id
    	}
    	subscriptionHandler.handle(transactionType, parametersMap);
    }

}
