package com.novadart.novabill.web.gwt;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.XsrfTokenService;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessGwtService;
import com.novadart.novabill.web.mvc.BusinessLogoController;
import com.novadart.novabill.web.mvc.ExportController;
import com.novadart.novabill.web.mvc.PDFController;

@HandleGWTServiceAccessDenied
public class BusinessGwtController extends AbstractGwtController implements BusinessGwtService {
	
	private static final long serialVersionUID = 1L;

	@Autowired
	private com.novadart.novabill.service.web.BusinessService businessService;
	
	@Autowired
	private XsrfTokenService xsrfTokenService;
	
	private String generateToken(String tokenSessionField) throws NoSuchAlgorithmException{
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return xsrfTokenService.generateToken(attr.getRequest().getSession(), tokenSessionField);
	}
	
	@Override
	public BusinessStatsDTO getStats(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getStats(businessID);
	}

	@Override
	public Long countClients(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.countClients(businessID);
	}

	@Override
	public Long countInvoices(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.countInvoices(businessID);
	}

	@Override
	public Long countInvoicesForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		return businessService.countInvoicesForYear(businessID, year);
	}


	@Override
	public BigDecimal getTotalAfterTaxesForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		return businessService.getTotalAfterTaxesForYear(businessID, year);
	}

	@Override
	public void update(BusinessDTO businessDTO) throws DataAccessException, NoSuchObjectException, ValidationException, NotAuthenticatedException, DataAccessException {
		businessService.update(businessDTO);
	}

	@Override
	public String generatePDFToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException, DataAccessException {
		return URLEncoder.encode(generateToken(PDFController.TOKENS_SESSION_FIELD), "UTF-8");
	}

	@Override
	public String generateExportToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException, DataAccessException {
		return URLEncoder.encode(generateToken(ExportController.TOKENS_SESSION_FIELD), "UTF-8");
	}

	@Override
	public List<InvoiceDTO> getInvoices(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getInvoices(businessID);
	}

	@Override
	public List<CreditNoteDTO> getCreditNotes(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getCreditNotes(businessID);
	}

	@Override
	public List<EstimationDTO> getEstimations(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getEstimations(businessID);
	}

	@Override
	public List<TransportDocumentDTO> getTransportDocuments(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getTransportDocuments(businessID);
	}
	
	@Override
	public List<ClientDTO> getClients(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getClients(businessID);
	}
	
	@Override
	public List<CommodityDTO> getCommodities(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getCommodities(businessID);
	}
	
	@Override
	public List<PaymentTypeDTO> getPaymentTypes(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getPaymentTypes(businessID);
	}

	@Override
	public BusinessDTO get(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.get(businessID);
	}

	@Override
	public Long updateNotesBitMask(Long notesBitMask) throws NotAuthenticatedException, DataAccessException {
		return businessService.updateNotesBitMask(notesBitMask);
	}

	@Override
	public String generateLogoOpToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException, DataAccessException {
		return URLEncoder.encode(generateToken(BusinessLogoController.TOKENS_SESSION_FIELD), "UTF-8");
	}

	@Override
	public Long add(BusinessDTO businessDTO) throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException, 
			com.novadart.novabill.shared.client.exception.CloneNotSupportedException {
		return businessService.add(businessDTO);
	}

}
