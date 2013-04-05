package com.novadart.novabill.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Logo;
import flexjson.JSONSerializer;
import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.DateTransformer;
import flexjson.transformer.HtmlEncoderTransformer;

@Service
public class PDFGenerator {
	
	//Important!!!
	//The DocumentType enum must be aligned with values from  DocumentType class in novabill-report/src/core.py
	public enum DocumentType{
		INVOICE, ESTIMATION, CREDIT_NOTE, TRANSPORT_DOCUMENT
	}
	
	public enum PDFGenerationCtxFields{
		contentLenght
	}

	@Value("${pdfgen.service.url}")
	private String pdfgenServiceURL;

	public static interface BeforeWriteEventHandler{
		public void beforeWriteCallback(Map<PDFGenerationCtxFields, Object> ctx);
	};
	
	private HttpPost prepareRequest(String docData, Logo logo, int docType) throws UnsupportedEncodingException{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("docData", docData));
		if(logo != null){
			params.add(new BasicNameValuePair("logoData", new String(Base64.encodeBase64(logo.getData()))));
			params.add(new BasicNameValuePair("logoWidth", logo.getWidth().toString()));
			params.add(new BasicNameValuePair("logoHeight", logo.getHeight().toString()));
		}
		params.add(new BasicNameValuePair("docType", String.valueOf(docType)));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		HttpPost post = new HttpPost(pdfgenServiceURL);
		post.setEntity(entity);
		return post;
	}
	
	public void createAndWrite(OutputStream out, AccountingDocument accountingDocument, Logo logo, 
			DocumentType docType, Boolean putWatermark, BeforeWriteEventHandler bwEvHnld) throws IOException{
		String json = new JSONSerializer().transform(new DateTransformer("dd/MM/yyyy"), Date.class)
				.transform(new HtmlEncoderTransformer(), String.class)
				.transform(new StringTransformer(), BigDecimal.class).include("accountingDocumentItems").serialize(accountingDocument).replace("'", "\\'").replace("\n", "<br/>");
		int docTypeConst = docType.ordinal();
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(prepareRequest(json, logo, docTypeConst));
		int contentLength = Integer.valueOf(response.getFirstHeader("Content-length").getValue());
		Map<PDFGenerationCtxFields, Object> ctx = new HashMap<PDFGenerationCtxFields, Object>();
		ctx.put(PDFGenerationCtxFields.contentLenght, contentLength);
		if(bwEvHnld != null)
			bwEvHnld.beforeWriteCallback(ctx);
		IOUtils.copy(response.getEntity().getContent(), out);
	    out.flush();
	}
	
	public static class StringTransformer extends AbstractTransformer{

		@Override
		public void transform(Object object) {
			getContext().writeQuoted(object.toString());
		}
		
	}
	
}
