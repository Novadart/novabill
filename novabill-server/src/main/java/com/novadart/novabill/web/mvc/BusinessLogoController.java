package com.novadart.novabill.web.mvc;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.im4java.core.IM4JavaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;

import com.novadart.novabill.annotation.Xsrf;
import com.novadart.novabill.domain.Logo;
import com.novadart.novabill.domain.Logo.LogoFormat;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.facade.LogoUploadStatus;
import com.novadart.utils.image.ImageUtils;
import com.novadart.utils.image.UnsupportedImageFormatException;

@Controller
@RequestMapping("/private/businesses/logo")
public class BusinessLogoController {
	
	public static final int LOGO_SIZE_LIMIT = 1024 * 1024; // 1MB
	public static final LogoFormat DEFAULT_FORMAT = LogoFormat.PNG;
	public static final String TOKEN_REQUEST_PARAM = "token";
	
	@Value("${logoThumbnail.format}")
	private String logoThumbnailFormat;
	
	@Value("${logoThumbnail.width}")
	private int logoThumbnailWidth;
	
	@Value("${logoThumbnail.height}")
	private int logoThumbnailHeight;
	
	@Value("${logoThumbnail.folder}")
	private String logoThumbnailFolder;
	
	@Value("${logoThumbnail.quality}")
	private double logoThumbnailQuality;
	
	@Autowired
	private UtilsService utilsService;
	
	private ServletContextResource noLogoImage;
	
	@PostConstruct
	public void init() throws IOException{
		File logoFolder = new File(logoThumbnailFolder);
		if(logoFolder.exists())
			FileUtils.cleanDirectory(logoFolder);
		else
			logoFolder.mkdir();
	}
	
	@Autowired
	public void setServletContext(ServletContext servletContext){
		noLogoImage = new ServletContextResource(servletContext, "/frontend_assets/img/no_logo.gif");
	}
	
	@ExceptionHandler(Exception.class)
	public String handleException(Exception ex, HttpServletRequest request, HttpServletResponse response){
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		return ex.getMessage();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public void getLogo(HttpServletResponse response) throws IOException{
		Logo logo = Logo.getLogoByBusinessID(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId());
		InputStream is = logo == null? noLogoImage.getInputStream() : new ByteArrayInputStream(logo.getData());
		response.setContentType("image/" + (logo == null? FilenameUtils.getExtension(noLogoImage.getPath()): logo.getFormat().name().toLowerCase()));
		response.setHeader ("Content-Disposition", String.format("attachment; filename=\"%s\"", logo == null? FilenameUtils.getName(noLogoImage.getPath()): logo.getName()));
		IOUtils.copy(is, response.getOutputStream());
	}
	
	private void serveLogoThumbnail(InputStream logoThumbnailIS, HttpServletResponse response, String name) throws IOException{
		response.setContentType("image/" + logoThumbnailFormat);
		response.setHeader ("Content-Disposition", String.format("attachment; filename=\"%s\"", name));
		IOUtils.copy(logoThumbnailIS, response.getOutputStream());
	}
	
	private File getThumbnailFile(Long businessID){
		return new File(logoThumbnailFolder, businessID.toString() + "." + logoThumbnailFormat);
	}
	
	@RequestMapping(value = "/thumbnail", method = RequestMethod.GET)
	@ResponseBody
	public void getLogoThumbnail(HttpServletResponse response) throws IOException, InterruptedException, IM4JavaException, UnsupportedImageFormatException{
		Long businessID = utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId();
		Logo logo = Logo.getLogoByBusinessID(businessID);
		if(logo == null)
			serveLogoThumbnail(noLogoImage.getInputStream(), response, FilenameUtils.getName(noLogoImage.getPath()));
		else{
			File logoThumbnailFile = getThumbnailFile(businessID);
			if(!logoThumbnailFile.exists()){
				File inFile = null;
				try{
					inFile = createTempFile("." + logo.getFormat().name(), true);
					IOUtils.copy(new ByteArrayInputStream(logo.getData()), new FileOutputStream(inFile));
					ImageUtils.resizeConvertImage(inFile, logoThumbnailWidth, logoThumbnailHeight, logoThumbnailFile, logoThumbnailQuality);
				}finally{
					if(inFile != null) inFile.delete();
				}
			}
			serveLogoThumbnail(new FileInputStream(logoThumbnailFile), response, FilenameUtils.getName(logoThumbnailFile.getPath()));
		}
	}
	
	private File createTempFile(String extension, boolean deleteOnExit) throws IOException{
		File temp = File.createTempFile("logo", extension);
		if(deleteOnExit)
			temp.deleteOnExit();
		return temp;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@Transactional(readOnly = false)
	public String uploadLogo(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws UnsupportedImageFormatException {
		if(!ServletFileUpload.isMultipartContent(request))
			return String.valueOf(LogoUploadStatus.ILLEGAL_REQUEST.ordinal());
		if(file == null)
			return String.valueOf(LogoUploadStatus.ILLEGAL_REQUEST.ordinal());
		if(file.getSize() > LOGO_SIZE_LIMIT)
			return String.valueOf(LogoUploadStatus.ILLEGAL_SIZE.ordinal());
		String contentType = file.getContentType(); 
		if(!contentType.startsWith("image"))
			return String.valueOf(LogoUploadStatus.ILLEGAL_PAYLOAD.ordinal());
		String subtype = contentType.substring(contentType.lastIndexOf('/') + 1);
		Long businessID = utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId();
		clearLogo(businessID);
		Logo logo = new Logo();
		logo.setName(FilenameUtils.removeExtension(FilenameUtils.getName(file.getOriginalFilename())) + "." + DEFAULT_FORMAT.name());
		logo.setFormat(DEFAULT_FORMAT);
		File inFile = null, outFile = null;
		try{
			inFile = createTempFile("." + subtype, true);
			file.transferTo(inFile);
			outFile = createTempFile("." + DEFAULT_FORMAT.name(), true);
			Dimension imageDim = ImageUtils.resizeConvertImage(inFile, Integer.MAX_VALUE, Integer.MAX_VALUE, outFile, 100.0);
			logo.setWidth(imageDim.width);
			logo.setHeight(imageDim.height);
			logo.setData(IOUtils.toByteArray(new FileInputStream(outFile)));
			logo.setBusinessID(businessID);
			logo.persist();
			ImageUtils.resizeConvertImage(inFile, logoThumbnailWidth, logoThumbnailHeight, getThumbnailFile(businessID), logoThumbnailQuality);
		} catch (IOException e) {
			return String.valueOf(LogoUploadStatus.INTERNAL_ERROR.ordinal());
		} catch (InterruptedException e) {
			return String.valueOf(LogoUploadStatus.INTERNAL_ERROR.ordinal());
		} catch (IM4JavaException e) {
			return String.valueOf(LogoUploadStatus.INTERNAL_ERROR.ordinal());
		}finally{
			if(inFile != null) inFile.delete();
			if(outFile != null) outFile.delete();
		}
		return String.valueOf(LogoUploadStatus.OK.ordinal());
	}
	
	public void clearLogo(Long businessID){
		Logo oldLogo = Logo.getLogoByBusinessID(businessID);
		if(oldLogo != null)
			oldLogo.remove();
		File thumbnailFile = getThumbnailFile(businessID);
		if(thumbnailFile.exists())
			thumbnailFile.delete();
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseBody
	@Transactional(readOnly = false)
	public void deleteLogo(){
		clearLogo(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId());
	}

}
