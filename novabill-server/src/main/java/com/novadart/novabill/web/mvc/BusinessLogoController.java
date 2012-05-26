package com.novadart.novabill.web.mvc;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.im4java.core.IM4JavaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.healthmarketscience.rmiio.SimpleRemoteInputStream;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.facade.LogoUploadStatus;
import com.novadart.services.shared.ImageDTO;
import com.novadart.services.shared.ImageStoreService;
import com.novadart.utils.image.ImageFormat;
import com.novadart.utils.image.ImageUtils;

@Controller
@RequestMapping("/private/businesses/logo")
public class BusinessLogoController {
	
	public static final int LOGO_SIZE_LIMIT = 1024 * 1024; // 1MB
	
	public static final ImageFormat DEFAULT_FORMAT = ImageFormat.JPEG;
	
	@Autowired
	private ImageStoreService imageStoreService;
	
	@Autowired
	private UtilsService utilsService;
	
	@ExceptionHandler(Exception.class)
	public String handleException(Exception ex, HttpServletRequest request, HttpServletResponse response){
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		return ex.getMessage();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public void getLogo(HttpServletResponse response) throws IOException{
		Business business = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId());
		if(business.getLogoId() == null){
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}
		ImageDTO logoDTO = imageStoreService.get(business.getLogoId());
		InputStream is = RemoteInputStreamClient.wrap(logoDTO.getRemoteImageDataInputStream());
		response.setContentType("image/" + logoDTO.getFormat().name().toLowerCase());
		response.setHeader ("Content-Disposition", String.format("attachment; filename=\"%s\"", logoDTO.getName()));
		IOUtils.copy(is, response.getOutputStream());
	}
	
	private File createTempFile(String extension, boolean deleteOnExit) throws IOException{
		File temp = File.createTempFile("logo", extension);
		if(deleteOnExit)
			temp.deleteOnExit();
		return temp;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String uploadLogo(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
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
		boolean acceptedFormat = false;
		for(ImageFormat format: ImageFormat.values())
			if(format.name().equalsIgnoreCase(subtype)){
				acceptedFormat = true;
				break;
			}
		if(!acceptedFormat)
			throw new IllegalArgumentException("Image type not supported");
		ImageDTO imageDTO = new ImageDTO();
		imageDTO.setName(FilenameUtils.removeExtension(FilenameUtils.getName(file.getOriginalFilename())) + "." + DEFAULT_FORMAT.name());
		imageDTO.setFormat(DEFAULT_FORMAT);
		File inFile = null, outFile = null;
		try{
			inFile = createTempFile("." + subtype, true);
			file.transferTo(inFile);
			outFile = createTempFile("." + DEFAULT_FORMAT.name(), true);
			Dimension imageDim = ImageUtils.resizeConvertImage(inFile, Integer.MAX_VALUE, Integer.MAX_VALUE, outFile);
			imageDTO.setWidth(imageDim.width);
			imageDTO.setHeight(imageDim.height);
			imageDTO.setRemoteImageDataInputStream(new SimpleRemoteInputStream(new FileInputStream(outFile)).export());
			Business business = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId());
			BigInteger oldLogoId = business.getLogoId();
			business.setLogoId(imageStoreService.put(imageDTO));
			business.flush();
			if(oldLogoId != null)//changes to business successful - remove old logo, if any
				imageStoreService.delete(oldLogoId);
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

}
