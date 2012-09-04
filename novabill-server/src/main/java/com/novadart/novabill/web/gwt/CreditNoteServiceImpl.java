package com.novadart.novabill.web.gwt;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.NumberOfCreditNotesPerYearQuotaReachedChecker;
import com.novadart.novabill.authorization.NumberOfInvoicesPerYearQuotaReachedChecker;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.dto.factory.CreditNoteDTOFactory;
import com.novadart.novabill.domain.dto.factory.AccountingDocumentItemDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.InvoiceValidator;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
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
	@Restrictions(checkers = {NumberOfCreditNotesPerYearQuotaReachedChecker.class})
	public Long add(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException {
		Client client = Client.findClient(creditNoteDTO.getClient().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		Business business = Business.findBusiness(creditNoteDTO.getBusiness().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(business.getId()))
			throw new DataAccessException();
		CreditNote creditNote = new CreditNote();//create new credit note
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
	@Transactional(readOnly = false)
	public void remove(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		CreditNote creditNote = CreditNote.findCreditNote(id);
		if(creditNote == null)
			throw new NoSuchObjectException();
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(creditNote.getBusiness().getId()))
			throw new DataAccessException();
		creditNote.remove(); //removing credit note
		if(Hibernate.isInitialized(creditNote.getBusiness().getCreditNotes()))
			creditNote.getBusiness().getCreditNotes().remove(creditNote);
		if(Hibernate.isInitialized(creditNote.getClient().getCreditNotes()))
			creditNote.getClient().getCreditNotes().remove(creditNote);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	public void update(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException {
		if(creditNoteDTO.getId() == null)
			throw new DataAccessException();
		Client client = Client.findClient(creditNoteDTO.getClient().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		Business business = Business.findBusiness(creditNoteDTO.getBusiness().getId());
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(business.getId()))
			throw new DataAccessException();
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
		return utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getNextCreditNoteDocumentID();
	}

	@Override
	public PageDTO<CreditNoteDTO> getAllForClientInRange(long id, int start, int length) throws NotAuthenticatedException, DataAccessException,	NoSuchObjectException, ConcurrentAccessException {
		Client client = Client.findClient(id);
		if(!utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId().equals(client.getBusiness().getId()))
			throw new DataAccessException();
		List<CreditNote> creditNotes = client.getAllCreditNotesInRange(start, length);
		List<CreditNoteDTO> creditNoteDTOs = new ArrayList<CreditNoteDTO>(creditNotes.size());
		for(CreditNote creditNote: creditNotes)
			creditNoteDTOs.add(CreditNoteDTOFactory.toDTO(creditNote));
		return new PageDTO<CreditNoteDTO>(creditNoteDTOs, start, length, Invoice.countInvocesForClient(id));
	}

}
