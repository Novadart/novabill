package com.novadart.novabill.web.gwt;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessService;

@HandleGWTServiceAccessDenied
public class BusinessServiceProxy extends AbstractGwtController implements BusinessService {
	
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("businessServiceImpl")
	private BusinessService businessService;
	
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

	public String generatePDFToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException, DataAccessException {
		return businessService.generatePDFToken();
	}

	@Override
	public String generateExportToken() throws NotAuthenticatedException, NoSuchAlgorithmException, UnsupportedEncodingException, DataAccessException {
		return businessService.generateExportToken();
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
	
	public List<ClientDTO> getClients(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getClients(businessID);
	}

	public BusinessDTO get(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.get(businessID);
	}

	@Override
	public Long updateNotesBitMask(Long notesBitMask) throws NotAuthenticatedException, DataAccessException {
		return businessService.updateNotesBitMask(notesBitMask);
	}

}
