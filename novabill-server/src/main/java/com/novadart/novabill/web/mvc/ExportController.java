package com.novadart.novabill.web.mvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Logo;
import com.novadart.novabill.service.DataExporter;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.XsrfTokenService;
import com.novadart.novabill.shared.client.data.DataExportClasses;

@Controller
@RequestMapping("/private/export")
public class ExportController {

	public static final String TOKENS_SESSION_FIELD = "export.data.tokens";

	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private XsrfTokenService xsrfTokenService;

	@Autowired
	private DataExporter dataExporter;

	@Autowired
	private ReloadableResourceBundleMessageSource messageSource;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public void getData(
			@RequestParam(value = "clients", required = false) boolean clients, 
			@RequestParam(value = "invoices", required = false) boolean invoices,
			@RequestParam(value = "estimations", required = false) boolean estimations, 
			@RequestParam(value = "token", required = false) String token, 
			HttpServletResponse response, Locale locale, HttpSession session) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		if(token == null || !xsrfTokenService.verifyAndRemoveToken(token, session, TOKENS_SESSION_FIELD))
			return;
		StringBuilder fileName = new StringBuilder("Export");
		Set<DataExportClasses> classes = new HashSet<DataExportClasses>();
		if(clients) {
			classes.add(DataExportClasses.CLIENT);
			fileName.append("_clients");
		}
		if(invoices) {
			classes.add(DataExportClasses.INVOICE);
			fileName.append("_invoices");
		}
		if(estimations) {
			classes.add(DataExportClasses.ESTIMATION);
			fileName.append("_estimations");
		}
		Business business = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId());
		Logo logo = null;
		if(classes.contains(DataExportClasses.INVOICE) || classes.contains(DataExportClasses.ESTIMATION))
			logo = business.getLogo();
		File zipFile = null;
		try{
			zipFile = dataExporter.exportData(classes, business, logo, messageSource, locale);
			response.setContentType("application/octet-stream");
			response.setHeader ("Content-Disposition", 
					String.format("attachment; filename=\"%s\"", fileName.toString()+".zip"));
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

}
