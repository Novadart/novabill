package com.novadart.novabill.web.mvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.novadart.novabill.domain.PayPalTransactionID;
import com.novadart.novabill.service.PayPalSubscriptionIPNHandlerService;


@Controller("/paypal-ipn-listener")
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
    
	private boolean verifyIPN(String queryString) throws URISyntaxException, ClientProtocolException, IOException{
    	//passing back the message to paypal
    	URIBuilder builder = new URIBuilder(payPalUrl);
    	builder.setQuery(String.format("%s=%s&%s", PARAM_NAME_CMD, PARAM_VAL_CMD, queryString));
    	HttpClient httpClient = new DefaultHttpClient();
    	HttpPost httpPost = new HttpPost(builder.build());
    	httpPost.setHeader(CONTENT_TYPE, MIME_APP_URLENC);
    	HttpResponse response = httpClient.execute(httpPost);
    	if(response.getStatusLine().getStatusCode() != 200)
    		return false;
    	InputStream is = response.getEntity().getContent();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String responseText = reader.readLine();
        is.close();
    	return responseText.equals(RESP_VERIFIED);
    	
    }
    
    @SuppressWarnings("unchecked")
	private List<NameValuePair> extractParameters(HttpServletRequest request){
    	Enumeration<String> paramNames = request.getParameterNames();
    	List<NameValuePair> parameters = new ArrayList<PayPalIPNListenerController.NameValuePair>();
    	while(paramNames.hasMoreElements()){
    		String param = paramNames.nextElement();
    		parameters.add(new NameValuePair(param, request.getParameter(param)));
    	}	
    	return parameters;
    }
    
    @RequestMapping
    public @ResponseBody void processIPN(@RequestParam("txn_type") String transactionType, @RequestParam(value = "txn_id", required = false) String transactionID,
    		HttpServletRequest request) throws URISyntaxException, ClientProtocolException, IOException{
    	List<NameValuePair> parameters = extractParameters(request);
    	if(!verifyIPN(request.getQueryString())) return;
    	if(transactionID != null && PayPalTransactionID.findByTransactionID(transactionID).size() > 0) return; //already processed thus ignore
    	subscriptionHandler.handle(transactionType, parameters);
    	if(transactionID != null)
    		new PayPalTransactionID(transactionID).merge(); //store transaction id
    }
    
    public static class NameValuePair{
    	
    	private String name;
    	
    	private String value;
    	
    	public NameValuePair(String name, String value){
    		this.name = name;
    		this.value = value;
    	}
    	
    	public NameValuePair(){}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
    	
    }

}
