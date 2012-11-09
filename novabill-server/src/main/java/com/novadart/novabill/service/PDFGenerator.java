package com.novadart.novabill.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.PostConstruct;

import jep.Jep;
import jep.JepException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.novadart.novabill.domain.AccountingDocument;

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

	@Value("${path.jep}")
	private String includePath;
	
	@Value("${path.report_script}")
	private String pyInvGenScript;
	
	@Value("${path.tmpdir.invoice_generation}")
	private String invOutLocation;

	@PostConstruct
	protected void init(){
		File outDir = new File(invOutLocation);
		if(!outDir.exists())
			outDir.mkdir();
	}
	
	public static interface BeforeWriteEventHandler{
		public void beforeWriteCallback(File file);
	};
	
	public void createAndWrite(OutputStream out, AccountingDocument accountingDocument, String pathToLogo, Integer logoWidth, Integer logoHeight, 
			DocumentType docType, Boolean putWatermark, BeforeWriteEventHandler bwEvHnld) throws IOException{
		File outDir = new File(invOutLocation);
		File invFile = File.createTempFile("inv", ".pdf", outDir);
		invFile.deleteOnExit();
		String json = new JSONSerializer().transform(new DateTransformer("dd/MM/yyyy"), Date.class)
				.transform(new HtmlEncoderTransformer(), String.class)
				.transform(new StringTransformer(), BigDecimal.class).include("accountingDocumentItems").serialize(accountingDocument).replace("'", "\\'");
		
		try {
			Jep jep = new Jep(false, includePath,  Thread.currentThread().getContextClassLoader());
			jep.runScript(pyInvGenScript, Thread.currentThread().getContextClassLoader());
			jep.eval("import json");
			int docTypeConst = docType.ordinal();
			if(pathToLogo == null)
				jep.eval(String.format("create_doc('%s', json.loads('%s'), docType=%d, watermark=%s)", invFile.getAbsolutePath(), json, docTypeConst, putWatermark? "True": "False"));
			else
				jep.eval(String.format("create_doc('%s', json.loads('%s'), '%s', %d, %d, docType=%d, watermark=%s)", invFile.getAbsolutePath(), json, pathToLogo, logoWidth, logoHeight, docTypeConst, putWatermark? "True": "False"));
			jep.close();
			if(bwEvHnld != null)
				bwEvHnld.beforeWriteCallback(invFile);
			InputStream in = new FileInputStream(invFile);
			int length = 0;
		    byte[] buf = new byte[4096];
		    while ((in != null) && ((length = in.read(buf)) != -1)) {
		         out.write(buf,0,length);
		    }
		    out.flush();
			
		} catch (JepException e) {
			e.printStackTrace();
		}finally{
			invFile.delete();
		}
		
	}
	
	public static class StringTransformer extends AbstractTransformer{

		@Override
		public void transform(Object object) {
			getContext().writeQuoted(object.toString());
		}
		
	}
	
}
