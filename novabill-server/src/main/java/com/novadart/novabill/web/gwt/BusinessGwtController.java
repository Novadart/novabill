package com.novadart.novabill.web.gwt;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.XsrfTokenService;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.shared.client.dto.*;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.facade.BusinessGwtService;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.web.mvc.XsrfTokenSessionFieldNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@HandleGWTServiceAccessDenied
public class BusinessGwtController extends AbstractGwtController implements BusinessGwtService {
	
	private static final long serialVersionUID = 1L;

	@Autowired
	private BusinessService businessService;
	
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
	public Integer countClients(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.countClients(businessID);
	}

	@Override
	public Integer countInvoicesForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		return businessService.countInvoicesForYear(businessID, year);
	}


	@Override
	public Pair<BigDecimal, BigDecimal> getTotalsForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		return businessService.getTotalsForYear(businessID, year);
	}

	@Override
	public void update(BusinessDTO businessDTO) throws DataAccessException, NoSuchObjectException, ValidationException, NotAuthenticatedException, DataAccessException {
		businessService.update(businessDTO);
	}

	@Override
	public String generatePDFToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException, DataAccessException {
		return URLEncoder.encode(generateToken(XsrfTokenSessionFieldNames.PDF_GENERATION_TOKENS_SESSION_FIELD), "UTF-8");
	}

	@Override
	public String generateExportToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException, DataAccessException {
		return URLEncoder.encode(generateToken(XsrfTokenSessionFieldNames.EXPORT_DATA_TOKENS_SESSION_FIELD), "UTF-8");
	}

	@Override
	public List<InvoiceDTO> getInvoices(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		return businessService.getInvoices(businessID, year);
	}

	@Override
	public List<CreditNoteDTO> getCreditNotes(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		return businessService.getCreditNotes(businessID, year);
	}

	@Override
	public List<EstimationDTO> getEstimations(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		return businessService.getEstimations(businessID, year);
	}

	@Override
	public List<TransportDocumentDTO> getTransportDocuments(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		return businessService.getTransportDocuments(businessID, year);
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
	public String generateLogoOpToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException, DataAccessException {
		return URLEncoder.encode(generateToken(XsrfTokenSessionFieldNames.BUSINESS_LOGO_TOKENS_SESSION_FIELD), "UTF-8");
	}

	@Override
	public Long add(BusinessDTO businessDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, 
			com.novadart.novabill.shared.client.exception.CloneNotSupportedException {
		return businessService.add(businessDTO);
	}

	@Override
	public List<TransporterDTO> getTransporters(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getTransporters(businessID);
	}

}
