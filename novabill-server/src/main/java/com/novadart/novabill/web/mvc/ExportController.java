package com.novadart.novabill.web.mvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novadart.novabill.annotation.Xsrf;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Logo;
import com.novadart.novabill.report.JasperReportKeyResolutionException;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.XsrfTokenService;
import com.novadart.novabill.service.export.data.DataExporter;
import com.novadart.novabill.service.export.data.ExportDataBundle;

@Controller
@RequestMapping("/private/export")
public class ExportController {

	public static final String CLIENTS_REQUEST_PARAM = "clients";
	public static final String INVOICES_REQUEST_PARAM = "invoices";
	public static final String ESTIMATIONS_REQUEST_PARAM = "estimations";
	public static final String CREDITNOTES_REQUEST_PARAM = "creditnotes";
	public static final String TRANSPORTDOCS_REQUEST_PARAM = "transportdocs";
	public static final String TOKEN_REQUEST_PARAM = "token";

	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private DataExporter dataExporter;

	@Autowired
	private ReloadableResourceBundleMessageSource messageSource;
	
	@Autowired
	private XsrfTokenService xsrfTokenService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	@Transactional(readOnly = true)
	@Xsrf(tokenRequestParam = TOKEN_REQUEST_PARAM, tokensSessionField = XsrfTokenSessionFieldNames.EXPORT_DATA_TOKENS_SESSION_FIELD)
	public void getData(
			@RequestParam(value = CLIENTS_REQUEST_PARAM, required = false) boolean clients, 
			@RequestParam(value = INVOICES_REQUEST_PARAM, required = false) boolean invoices,
			@RequestParam(value = ESTIMATIONS_REQUEST_PARAM, required = false) boolean estimations,
			@RequestParam(value = CREDITNOTES_REQUEST_PARAM, required = false) boolean creditnotes,
			@RequestParam(value = TRANSPORTDOCS_REQUEST_PARAM, required = false) boolean transportdocs,
			HttpServletResponse response, Locale locale, HttpSession session) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, JRException, JasperReportKeyResolutionException{
		Business business = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId());
		ExportDataBundle exportDataBundle = new ExportDataBundle();
		if(clients)
			exportDataBundle.setClients(business.getClients());
		if(invoices)
			exportDataBundle.setInvoices(business.getInvoices());
		if(estimations)
			exportDataBundle.setEstimations(business.getEstimations());
		if(creditnotes)
			exportDataBundle.setCreditNotes(business.getCreditNotes());
		if(transportdocs)
			exportDataBundle.setTransportDocuments(business.getTransportDocuments());
		exportDataBundle.setBusiness(business);
		if(invoices || estimations || creditnotes || transportdocs)
			exportDataBundle.setLogo(Logo.getLogoByBusinessID(business.getId())); 
		File zipFile = null;
		try{
			zipFile = dataExporter.exportData(exportDataBundle, messageSource, locale);
			response.setContentType("application/zip");
			response.setHeader ("Content-Disposition", 
					String.format("attachment; filename=\"%s.zip\"", messageSource.getMessage("export.filename", null, "data", locale)));
			response.setHeader ("Content-Length", String.valueOf(zipFile.length()));
			ServletOutputStream out = response.getOutputStream();
			IOUtils.copy(new FileInputStream(zipFile), out);
			out.flush();
			out.close();
		} finally {
			if(zipFile != null)
				zipFile.delete();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/token", method = RequestMethod.GET)
	public String getToken(HttpSession session) throws NoSuchAlgorithmException{
		return xsrfTokenService.generateToken(session, XsrfTokenSessionFieldNames.EXPORT_DATA_TOKENS_SESSION_FIELD);
	}

}
