package com.novadart.novabill.web.gwt;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.NumberOfInvoicesPerYearQuotaReachedChecker;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.CreditNoteDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.InvoiceValidator;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.CreditNoteService;

@SuppressWarnings("serial")
public class CreditNoteServiceImpl extends AbstractGwtController<CreditNoteService, CreditNoteServiceImpl> implements CreditNoteService {
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private InvoiceValidator validator;

	public CreditNoteServiceImpl() {
		super(CreditNoteService.class);
	}

	@Override
	public CreditNoteDTO get(long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		CreditNote creditNote = CreditNote.findCreditNote(id);
		if(creditNote == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(creditNote.getBusiness().getId()))
			throw new DataAccessException();
		return CreditNoteDTOFactory.toDTO(creditNote);
	}

	@Override
	public PageDTO<CreditNoteDTO> getAllInRange(int start, int length) throws NotAuthenticatedException, ConcurrentAccessException {
		List<CreditNote> creditNotes = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId()).getAllCreditNotesInRange(start, length); 
		List<CreditNoteDTO> creditNoteDTOs = new ArrayList<CreditNoteDTO>(creditNotes.size());
		for(CreditNote creditNote: creditNotes)
			creditNoteDTOs.add(CreditNoteDTOFactory.toDTO(creditNote));
		return new PageDTO<CreditNoteDTO>(creditNoteDTOs, start, length, (int)CreditNote.countCreditNotes());
	}
	
	@Override
	public List<CreditNoteDTO> getAllForClient(long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		Client client = Client.findClient(id);
		if(client == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		List<CreditNote> creditNotes = client.getSortedCreditNotes();
		List<CreditNoteDTO> creditNoteDTOs = new ArrayList<CreditNoteDTO>(creditNotes.size());
		for(CreditNote invoice: creditNotes)
			creditNoteDTOs.add(CreditNoteDTOFactory.toDTO(invoice));
		return creditNoteDTOs;
	}
	
	@Override
	@Restrictions(checkers = {NumberOfInvoicesPerYearQuotaReachedChecker.class})
	public Long add(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException {
		Client client = Client.findClient(creditNoteDTO.getClient().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		Business business = Business.findBusiness(creditNoteDTO.getBusiness().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(business.getId()))
			throw new DataAccessException();
		CreditNote creditNote = new CreditNote();//create new invoice
		creditNote.setClient(client);
		client.getCreditNotes().add(creditNote);
		creditNote.setBusiness(business);
		business.getCreditNotes().add(creditNote);
		CreditNoteDTOFactory.copyFromDTO(creditNote, creditNoteDTO, true);
		validator.validate(creditNote);
		creditNote.flush();
		return creditNote.getId();
	}

	@Override
	public Long getNextInvoiceDocumentID() throws NotAuthenticatedException, ConcurrentAccessException {
		return utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getNextCreditNoteDocumentID();
	}

}
