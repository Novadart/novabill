package com.novadart.novabill.service.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.dto.factory.ClientDTOFactory;
import com.novadart.novabill.domain.dto.factory.CommodityDTOFactory;
import com.novadart.novabill.domain.dto.factory.LogRecordDTOFactory;
import com.novadart.novabill.domain.dto.factory.PaymentTypeDTOFactory;
import com.novadart.novabill.domain.dto.factory.PriceListDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.data.PriceListConstants;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.LogRecordDTO;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.tuple.Pair;

public abstract class BusinessServiceImpl implements BusinessService {

	@Autowired
	private SimpleValidator validator;
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private MessageSource messageSource;
	
	private Map<Locale, PaymentType[]> paymentTypes;
	
	@PostConstruct
	public void init(){
		paymentTypes = new HashMap<Locale, PaymentType[]>();
		paymentTypes.put(Locale.ITALIAN, new PaymentType[]{
			new PaymentType(messageSource.getMessage("payment1.name", null, "Rimessa Diretta", Locale.ITALIAN),
							messageSource.getMessage("payment1.paymentNote", null, "Pagamento in Rimessa Diretta", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 0, 0),
			new PaymentType(messageSource.getMessage("payment2.name", null, "Bonifico Bancario 30GG", Locale.ITALIAN),
							messageSource.getMessage("payment2.paymentNote", null, "Pagamento con bonifico bancario entro 30 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 1, 0),
			new PaymentType(messageSource.getMessage("payment3.name", null, "Bonifico Bancario 60GG", Locale.ITALIAN),
							messageSource.getMessage("payment3.paymentNote", null, "Pagamento con bonifico bancario entro 60 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 2, 0),
			new PaymentType(messageSource.getMessage("payment4.name", null, "Bonifico Bancario 90GG", Locale.ITALIAN),
							messageSource.getMessage("payment4.paymentNote", null, "Pagamento con bonifico bancario entro 90 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 3, 0),
			new PaymentType(messageSource.getMessage("payment5.name", null, "Bonifico Bancario 30GG d.f. f.m.", Locale.ITALIAN),
							messageSource.getMessage("payment5.paymentNote", null, "Pagamento con bonifico bancario entro 30 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 1, 0),
			new PaymentType(messageSource.getMessage("payment6.name", null, "Bonifico Bancario 60GG d.f. f.m.", Locale.ITALIAN), 
							messageSource.getMessage("payment6.paymentNote", null, "Pagamento con bonifico bancario entro 60 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 2, 0),
			new PaymentType(messageSource.getMessage("payment7.name", null, "Bonifico Bancario 90GG d.f. f.m.", Locale.ITALIAN),
							messageSource.getMessage("payment7.paymentNote", null, "Pagamento con bonifico bancario entro 90 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 3, 0),
			new PaymentType(messageSource.getMessage("payment8.name", null, "Ri.Ba. 30GG", Locale.ITALIAN),
							messageSource.getMessage("payment8.paymentNote", null, "Pagamento tramite ricevuta bancaria entro 30 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 1, 0),
			new PaymentType(messageSource.getMessage("payment9.name", null, "Ri.Ba. 60GG", Locale.ITALIAN),
							messageSource.getMessage("payment9.paymentNote", null, "Pagamento tramite ricevuta bancaria entro 60 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 2, 0),
			new PaymentType(messageSource.getMessage("payment10.name", null, "Ri.Ba. 90GG", Locale.ITALIAN),
							messageSource.getMessage("payment10.paymentNote", null, "Pagamento tramite ricevuta bancaria entro 90 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 3, 0),
			new PaymentType(messageSource.getMessage("payment11.name", null, "Ri.Ba. 30GG d.f. f.m.", Locale.ITALIAN),
							messageSource.getMessage("payment11.paymentNote", null, "Pagamento tramite ricevuta bancaria entro 30 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 1, 0),
			new PaymentType(messageSource.getMessage("payment12.name", null, "Ri.Ba. 60GG d.f. f.m.", Locale.ITALIAN),
							messageSource.getMessage("payment12.paymentNote", null, "Pagamento tramite ricevuta bancaria entro 60 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 2, 0),
			new PaymentType(messageSource.getMessage("payment13.name", null, "Ri.Ba. 90GG d.f. f.m.", Locale.ITALIAN),
							messageSource.getMessage("payment13.paymentNote", null, "Pagamento tramite ricevuta bancaria entro 90 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 3, 0)
		});
	}
	
	protected abstract BusinessService self();
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public BusinessStatsDTO getStats(Long businessID) throws NotAuthenticatedException, DataAccessException {
		BusinessStatsDTO stats = new BusinessStatsDTO();
		stats.setClientsCount(countClients(businessID));
		int year = Calendar.getInstance().get(Calendar.YEAR);
		stats.setInvoicesCountForYear(countInvoicesForYear(businessID, year));
		stats.setCommoditiesCount(self().getCommodities(businessID).size());
		Pair<BigDecimal, BigDecimal> totals = getTotalsForYear(businessID, year);
		stats.setTotalBeforeTaxesForYear(totals.getFirst());
		stats.setTotalAfterTaxesForYear(totals.getSecond());
		stats.setLogRecords(self().getLogRecords(businessID, 90));
		stats.setInvoiceCountsPerMonth(self().getInvoiceMonthCounts(businessID));
		return stats;
	}
	
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("#businessID == principal.business.id")
	public Integer countClients(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return self().getClients(businessID).size();
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public Integer countInvoicesForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		return self().getInvoices(businessID, year).size();
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public Pair<BigDecimal, BigDecimal> getTotalsForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		BigDecimal totalBeforeTaxes = new BigDecimal("0.0");
		BigDecimal totalAfterTaxes = new BigDecimal("0.0");
		for(InvoiceDTO invoiceDTO: self().getInvoices(businessID, year)){
			totalBeforeTaxes = totalBeforeTaxes.add(invoiceDTO.getTotalBeforeTax());
			totalAfterTaxes = totalAfterTaxes.add(invoiceDTO.getTotal());
		}
		return new Pair<>(totalBeforeTaxes.setScale(2, BigDecimal.ROUND_HALF_EVEN), totalAfterTaxes.setScale(2, BigDecimal.ROUND_HALF_EVEN));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#businessDTO?.id == principal.business.id")
	public void update(BusinessDTO businessDTO) throws DataAccessException, NoSuchObjectException, ValidationException {
		Business business = Business.findBusiness(businessDTO.getId());
		BusinessDTOFactory.copyFromDTO(business, businessDTO);
		validator.validate(business);
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<InvoiceDTO> getInvoices(Long businessID, Integer year){
		return DTOUtils.toDTOList(AccountingDocument.sortAccountingDocuments(Business.findBusiness(businessID).fetchInvoicesEagerly(year)), DTOUtils.invoiceDTOConverter, false);
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<CreditNoteDTO> getCreditNotes(Long businessID, Integer year) throws NotAuthenticatedException {
		return DTOUtils.toDTOList(AccountingDocument.sortAccountingDocuments(Business.findBusiness(businessID).fetchCreditNotesEagerly(year)), DTOUtils.creditNoteDTOConverter, false);
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<EstimationDTO> getEstimations(Long businessID, Integer year) throws NotAuthenticatedException {
		return DTOUtils.toDTOList(AccountingDocument.sortAccountingDocuments(Business.findBusiness(businessID).fetchEstimationsEagerly(year)), DTOUtils.estimationDTOConverter, false);
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<TransportDocumentDTO> getTransportDocuments(Long businessID, Integer year) throws NotAuthenticatedException {
		return DTOUtils.toDTOList(AccountingDocument.sortAccountingDocuments(Business.findBusiness(businessID).fetchTransportDocumentsEagerly(year)), DTOUtils.transportDocDTOConverter, false);
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<ClientDTO> getClients(Long businessID){
		Set<Client> clients = Business.findBusiness(businessID).getClients();
		List<ClientDTO> clientDTOs = new ArrayList<ClientDTO>(clients.size());
		for(Client client: clients)
			clientDTOs.add(ClientDTOFactory.toDTO(client));
		return clientDTOs;
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<CommodityDTO> getCommodities(Long businessID) throws NotAuthenticatedException, DataAccessException {
		Set<Commodity> commodities = Business.findBusiness(businessID).getCommodities();
		List<CommodityDTO> commodityDTOs = new ArrayList<CommodityDTO>(commodities.size());
		for(Commodity commodity: commodities)
			commodityDTOs.add(CommodityDTOFactory.toDTO(commodity));
		return commodityDTOs;
	}
	

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<PriceListDTO> getPriceLists(Long businessID) throws NotAuthenticatedException, DataAccessException {
		Set<PriceList> priceLists = Business.findBusiness(businessID).getPriceLists();
		List<PriceListDTO> priceListDTOs = new ArrayList<>(priceLists.size());
		for(PriceList priceList: priceLists)
			priceListDTOs.add(PriceListDTOFactory.toDTO(priceList, null));
		return priceListDTOs;
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<PaymentTypeDTO> getPaymentTypes(Long businessID) throws NotAuthenticatedException, DataAccessException{
		Set<PaymentType> paymentTypes = Business.findBusiness(businessID).getPaymentTypes();
		List<PaymentTypeDTO> paymentTypeDTOs = new ArrayList<PaymentTypeDTO>(paymentTypes.size());
		for(PaymentType paymentType: paymentTypes)
			paymentTypeDTOs.add(PaymentTypeDTOFactory.toDTO(paymentType));
		return paymentTypeDTOs;
	}
	
	@PreAuthorize("#businessID == principal.business.id")
	public BusinessDTO get(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return BusinessDTOFactory.toDTO(Business.findBusiness(businessID));
	}

	@Transactional(readOnly = false)
	public Long updateNotesBitMask(Long notesBitMask) throws NotAuthenticatedException, DataAccessException {
		Principal authenticatedPrincipal = Principal.findPrincipal(utilsService.getAuthenticatedPrincipalDetails().getId());
		authenticatedPrincipal.setNotesBitMask(notesBitMask);
		return authenticatedPrincipal.merge().getNotesBitMask();
	}

	@PreAuthorize("principal.business == null and #businessDTO != null and #businessDTO.id == null")
	@Transactional(readOnly = false)
	public Long add(BusinessDTO businessDTO) throws NotAuthenticatedException, AuthorizationException, ValidationException, DataAccessException, 
													com.novadart.novabill.shared.client.exception.CloneNotSupportedException {
		Business business = new Business();
		BusinessDTOFactory.copyFromDTO(business, businessDTO);
		validator.validate(business);
		Locale locale = LocaleContextHolder.getLocale();
		for(PaymentType pType: paymentTypes.containsKey(locale)? paymentTypes.get(locale): paymentTypes.get(Locale.ITALIAN)){
			PaymentType paymentType = null;
			try {
				paymentType = pType.clone();
			} catch (CloneNotSupportedException e) {
				throw new com.novadart.novabill.shared.client.exception.CloneNotSupportedException();
			}
			paymentType.setBusiness(business);
			business.getPaymentTypes().add(paymentType);
		}
		PriceList publicPriceList = new PriceList(PriceListConstants.DEFAULT);
		publicPriceList.setBusiness(business);
		business.getPriceLists().add(publicPriceList);
		Principal principal = Principal.findPrincipal(utilsService.getAuthenticatedPrincipalDetails().getId());
		principal.setBusiness(business);
		business.getPrincipals().add(principal);
		Business mergedBusiness = business.merge();
		utilsService.setBusinessForPrincipal(mergedBusiness);
		return mergedBusiness.getId();
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<Integer> getInvoceYears(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return Business.findBusiness(businessID).getInvoiceYears();
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<Integer> getCreditNoteYears(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return Business.findBusiness(businessID).getCreditNoteYears();
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<Integer> getEstimationYears(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return Business.findBusiness(businessID).getEstimationYears();
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<Integer> getTransportDocumentYears(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return Business.findBusiness(businessID).getTransportDocumentYears();
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<LogRecordDTO> getLogRecords(Long businessID, Integer numberOfDays) throws NotAuthenticatedException, DataAccessException {
		Long threshold = System.currentTimeMillis() - (numberOfDays * 24L * 60L * 60L * 1000L) ;
		List<LogRecordDTO> result = new ArrayList<>();
		for(LogRecord lg: LogRecord.fetchAllSince(businessID, threshold))
			result.add(LogRecordDTOFactory.toDTO(lg));
		return result;
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<Integer> getInvoiceMonthCounts(Long businessID) throws NotAuthenticatedException, DataAccessException {
		Calendar cal = Calendar.getInstance();
		List<Integer> counts = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		for(InvoiceDTO invoice: self().getInvoices(businessID, cal.get(Calendar.YEAR))){
			cal.setTime(invoice.getAccountingDocumentDate());
			int month = cal.get(Calendar.MONTH); 
			counts.set(month, counts.get(month) + 1);
		}
		return counts;
	}
	
}
