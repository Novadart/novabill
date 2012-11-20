package com.novadart.novabill.web.gwt;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.factory.AccountingDocumentItemDTOFactory;
import com.novadart.novabill.domain.dto.factory.CreditNoteDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.InvoiceValidator;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
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
	@PreAuthorize("T(com.novadart.novabill.domain.CreditNote).findCreditNote(#id)?.business?.id == principal.business.id")
	public CreditNoteDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		return CreditNoteDTOFactory.toDTO(CreditNote.findCreditNote(id));
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public PageDTO<CreditNoteDTO> getAllInRange(Long businessID, int start, int length) throws NotAuthenticatedException, ConcurrentAccessException {
		List<CreditNote> creditNotes = Business.findBusiness(businessID).getAllCreditNotesInRange(start, length); 
		List<CreditNoteDTO> creditNoteDTOs = new ArrayList<CreditNoteDTO>(creditNotes.size());
		for(CreditNote creditNote: creditNotes)
			creditNoteDTOs.add(CreditNoteDTOFactory.toDTO(creditNote));
		return new PageDTO<CreditNoteDTO>(creditNoteDTOs, start, length, CreditNote.countCreditNotes());
	}
	
	@Override
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public List<CreditNoteDTO> getAllForClient(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		List<CreditNote> creditNotes = Client.findClient(clientID).getSortedCreditNotes();
		List<CreditNoteDTO> creditNoteDTOs = new ArrayList<CreditNoteDTO>(creditNotes.size());
		for(CreditNote invoice: creditNotes)
			creditNoteDTOs.add(CreditNoteDTOFactory.toDTO(invoice));
		return creditNoteDTOs;
	}
	
	@Override
	//@Restrictions(checkers = {NumberOfCreditNotesPerYearQuotaReachedChecker.class})
	@PreAuthorize("#creditNoteDTO?.business?.id == principal.business.id and " +
				  "T(com.novadart.novabill.domain.Client).findClient(#creditNoteDTO?.client?.id)?.business?.id == principal.business.id and " +
				  "#creditNoteDTO != null and #creditNoteDTO.id == null")
	public Long add(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException {
		CreditNote creditNote = new CreditNote();//create new credit note
		CreditNoteDTOFactory.copyFromDTO(creditNote, creditNoteDTO, true);
		validator.validate(creditNote);
		Client client = Client.findClient(creditNoteDTO.getClient().getId());
		Business business = Business.findBusiness(creditNoteDTO.getBusiness().getId());
		creditNote.setClient(client);
		client.getCreditNotes().add(creditNote);
		creditNote.setBusiness(business);
		business.getCreditNotes().add(creditNote);
		creditNote.flush();
		return creditNote.getId();
	}
	
	@Override
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
				  "T(com.novadart.novabill.domain.CreditNote).findCreditNote(#creditNoteID)?.business?.id == #businessID and " +
				  "T(com.novadart.novabill.domain.CreditNote).findCreditNote(#creditNoteID)?.client?.id == #clientID")
	public void remove(Long businessID, Long clientID, Long creditNoteID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		CreditNote creditNote = CreditNote.findCreditNote(creditNoteID);
		creditNote.remove(); //removing credit note
		if(Hibernate.isInitialized(creditNote.getBusiness().getCreditNotes()))
			creditNote.getBusiness().getCreditNotes().remove(creditNote);
		if(Hibernate.isInitialized(creditNote.getClient().getCreditNotes()))
			creditNote.getClient().getCreditNotes().remove(creditNote);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#creditNoteDTO?.business?.id == principal.business.id and " +
			  	  "T(com.novadart.novabill.domain.Client).findClient(#creditNoteDTO?.client?.id)?.business?.id == principal.business.id and " +
			  	  "#creditNoteDTO?.id != null")
	public void update(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException {
		CreditNote persistedCreditNote = CreditNote.findCreditNote(creditNoteDTO.getId());
		if(persistedCreditNote == null)
			throw new NoSuchObjectException();
		CreditNoteDTOFactory.copyFromDTO(persistedCreditNote, creditNoteDTO, false);
		persistedCreditNote.getAccountingDocumentItems().clear();
		for(AccountingDocumentItemDTO itemDTO: creditNoteDTO.getItems()){
			AccountingDocumentItem item = new AccountingDocumentItem();
			AccountingDocumentItemDTOFactory.copyFromDTO(item, itemDTO);
			item.setAccountingDocument(persistedCreditNote);
			persistedCreditNote.getAccountingDocumentItems().add(item);
		}
		validator.validate(persistedCreditNote);
		
	}

	@Override
	public Long getNextInvoiceDocumentID() throws NotAuthenticatedException, ConcurrentAccessException {
		return utilsService.getAuthenticatedPrincipalDetails().getBusiness().getNextCreditNoteDocumentID();
	}

	@Override
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public PageDTO<CreditNoteDTO> getAllForClientInRange(Long clientID, int start, int length) throws NotAuthenticatedException, DataAccessException,	NoSuchObjectException, ConcurrentAccessException {
		List<CreditNote> creditNotes = Client.findClient(clientID).getAllCreditNotesInRange(start, length);
		List<CreditNoteDTO> creditNoteDTOs = new ArrayList<CreditNoteDTO>(creditNotes.size());
		for(CreditNote creditNote: creditNotes)
			creditNoteDTOs.add(CreditNoteDTOFactory.toDTO(creditNote));
		return new PageDTO<CreditNoteDTO>(creditNoteDTOs, start, length, Invoice.countInvocesForClient(clientID));
	}

}
