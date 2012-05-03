package com.novadart.novabill.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import jep.Jep;
import jep.JepException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.novadart.novabill.domain.AbstractInvoice;
import flexjson.JSONSerializer;
import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.DateTransformer;
import flexjson.transformer.HtmlEncoderTransformer;

@Service
public class PDFGenerator {
	
	public enum DocumentType{
		INVOICE, ESTIMATION
	}

	@Value("${path.jep}")
	private String includePath;
	
	@Value("${path.report_script}")
	private String pyInvGenScript;
	
	@Value("${path.tmpdir.invoice_generation}")
	private String invOutLocation;
	
	private void checkCreateOutputLocation(File file){
		if(!file.exists())
			file.mkdir();
	}
	
	public static interface BeforeWriteEventHandler{
		public void beforeWriteCallback(File file);
	};
	
	public void createAndWrite(OutputStream out, AbstractInvoice invoice, String pathToLogo, Integer logoWidth, Integer logoHeight, 
			DocumentType docType, BeforeWriteEventHandler bwEvHnld) throws IOException{
		File outDir = new File(invOutLocation);
		checkCreateOutputLocation(outDir);
		File invFile = File.createTempFile("inv", ".pdf", outDir);
		invFile.deleteOnExit();
		String json = new JSONSerializer().transform(new DateTransformer("dd/MM/yyyy"), Date.class)
				.transform(new HtmlEncoderTransformer(), String.class)
				.transform(new StringTransformer(), BigDecimal.class).include("invoiceItems").serialize(invoice).replace("'", "\\'");
		
		try {
			Jep jep = new Jep(false, includePath,  Thread.currentThread().getContextClassLoader());
			jep.runScript(pyInvGenScript, Thread.currentThread().getContextClassLoader());
			jep.eval("import json");
			int docTypeConst = docType == DocumentType.INVOICE? 0: 1;
			if(pathToLogo == null)
				jep.eval(String.format("create_invoice('%s', json.loads('%s'), docType=%d)", invFile.getAbsolutePath(), json, docTypeConst));
			else
				jep.eval(String.format("create_invoice('%s', json.loads('%s'), '%s', %d, %d, %d)", invFile.getAbsolutePath(), json, pathToLogo, logoWidth, logoHeight, docTypeConst));
			jep.close();
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
