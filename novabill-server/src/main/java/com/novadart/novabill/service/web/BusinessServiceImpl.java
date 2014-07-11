package com.novadart.novabill.service.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.PremiumChecker;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.domain.LogRecord;
import com.novadart.novabill.domain.Notification;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.domain.Settings;
import com.novadart.novabill.domain.SharingPermit;
import com.novadart.novabill.domain.Transporter;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.transformer.BusinessDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.ClientDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.CommodityDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.LogRecordDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.NotificationDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.PaymentTypeDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.PriceListDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.SharingPermitDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.TransporterDTOTransformer;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.data.PriceListConstants;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.LogRecordDTO;
import com.novadart.novabill.shared.client.dto.NotificationDTO;
import com.novadart.novabill.shared.client.dto.PaymentDateType;
import com.novadart.novabill.shared.client.dto.PaymentDeltaType;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.dto.SharingPermitDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.dto.TransporterDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
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
	
	public static final String EMAIL_SUBJECT = "Invio Fattura n. $NumeroFattura del $DataFattura";
	
	public static final String EMAIL_TEXT = "Spettabile $NomeCliente,\n\ncon la presente trasmettiamo la nostra fattura nr. $NumeroFattura del $DataFattura in formato PDF.\nIl documento è scaricabile alla pagina web sotto indicata.\n\nCordiali saluti,\n$RagioneSocialeAzienda";
	
	@PostConstruct
	public void init(){
		paymentTypes = new HashMap<Locale, PaymentType[]>();
		paymentTypes.put(Locale.ITALIAN, new PaymentType[]{
			new PaymentType(messageSource.getMessage("payment1.name", null, "Rimessa Diretta", Locale.ITALIAN),
							messageSource.getMessage("payment1.paymentNote", null, "Pagamento in Rimessa Diretta", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 0, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType(messageSource.getMessage("payment2.name", null, "Bonifico Bancario 30GG", Locale.ITALIAN),
							messageSource.getMessage("payment2.paymentNote", null, "Pagamento con bonifico bancario entro 30 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 1, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType(messageSource.getMessage("payment3.name", null, "Bonifico Bancario 60GG", Locale.ITALIAN),
							messageSource.getMessage("payment3.paymentNote", null, "Pagamento con bonifico bancario entro 60 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 2, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType(messageSource.getMessage("payment4.name", null, "Bonifico Bancario 90GG", Locale.ITALIAN),
							messageSource.getMessage("payment4.paymentNote", null, "Pagamento con bonifico bancario entro 90 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 3, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType(messageSource.getMessage("payment5.name", null, "Bonifico Bancario 30GG d.f. f.m.", Locale.ITALIAN),
							messageSource.getMessage("payment5.paymentNote", null, "Pagamento con bonifico bancario entro 30 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 1, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType(messageSource.getMessage("payment6.name", null, "Bonifico Bancario 60GG d.f. f.m.", Locale.ITALIAN), 
							messageSource.getMessage("payment6.paymentNote", null, "Pagamento con bonifico bancario entro 60 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 2, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType(messageSource.getMessage("payment7.name", null, "Bonifico Bancario 90GG d.f. f.m.", Locale.ITALIAN),
							messageSource.getMessage("payment7.paymentNote", null, "Pagamento con bonifico bancario entro 90 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 3, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType(messageSource.getMessage("payment8.name", null, "Ri.Ba. 30GG", Locale.ITALIAN),
							messageSource.getMessage("payment8.paymentNote", null, "Pagamento tramite ricevuta bancaria entro 30 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 1, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType(messageSource.getMessage("payment9.name", null, "Ri.Ba. 60GG", Locale.ITALIAN),
							messageSource.getMessage("payment9.paymentNote", null, "Pagamento tramite ricevuta bancaria entro 60 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 2, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType(messageSource.getMessage("payment10.name", null, "Ri.Ba. 90GG", Locale.ITALIAN),
							messageSource.getMessage("payment10.paymentNote", null, "Pagamento tramite ricevuta bancaria entro 90 giorni", Locale.ITALIAN), PaymentDateType.IMMEDIATE, 3, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType(messageSource.getMessage("payment11.name", null, "Ri.Ba. 30GG d.f. f.m.", Locale.ITALIAN),
							messageSource.getMessage("payment11.paymentNote", null, "Pagamento tramite ricevuta bancaria entro 30 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 1, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType(messageSource.getMessage("payment12.name", null, "Ri.Ba. 60GG d.f. f.m.", Locale.ITALIAN),
							messageSource.getMessage("payment12.paymentNote", null, "Pagamento tramite ricevuta bancaria entro 60 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 2, PaymentDeltaType.COMMERCIAL_MONTH, 0),
			new PaymentType(messageSource.getMessage("payment13.name", null, "Ri.Ba. 90GG d.f. f.m.", Locale.ITALIAN),
							messageSource.getMessage("payment13.paymentNote", null, "Pagamento tramite ricevuta bancaria entro 90 giorni d.f. f.m.", Locale.ITALIAN), PaymentDateType.END_OF_MONTH, 3, PaymentDeltaType.COMMERCIAL_MONTH, 0)
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
		stats.setInvoiceTotalsPerMonth(self().getInvoiceMonthTotals(businessID));
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
		BusinessDTOTransformer.copyFromDTO(business, businessDTO);
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
			clientDTOs.add(ClientDTOTransformer.toDTO(client));
		return clientDTOs;
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<CommodityDTO> getCommodities(Long businessID) throws NotAuthenticatedException, DataAccessException {
		Set<Commodity> commodities = Business.findBusiness(businessID).getCommodities();
		List<CommodityDTO> commodityDTOs = new ArrayList<CommodityDTO>(commodities.size());
		for(Commodity commodity: commodities)
			commodityDTOs.add(CommodityDTOTransformer.toDTO(commodity));
		return commodityDTOs;
	}
	

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<PriceListDTO> getPriceLists(Long businessID) throws NotAuthenticatedException, DataAccessException {
		Set<PriceList> priceLists = Business.findBusiness(businessID).getPriceLists();
		List<PriceListDTO> priceListDTOs = new ArrayList<>(priceLists.size());
		for(PriceList priceList: priceLists)
			priceListDTOs.add(PriceListDTOTransformer.toDTO(priceList, null));
		return priceListDTOs;
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<PaymentTypeDTO> getPaymentTypes(Long businessID) throws NotAuthenticatedException, DataAccessException{
		Set<PaymentType> paymentTypes = Business.findBusiness(businessID).getPaymentTypes();
		List<PaymentTypeDTO> paymentTypeDTOs = new ArrayList<PaymentTypeDTO>(paymentTypes.size());
		for(PaymentType paymentType: paymentTypes)
			paymentTypeDTOs.add(PaymentTypeDTOTransformer.toDTO(paymentType));
		return paymentTypeDTOs;
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<TransporterDTO> getTransporters(Long businessID) throws NotAuthenticatedException, DataAccessException {
		Set<Transporter> transporters = Business.findBusiness(businessID).getTransporters();
		List<TransporterDTO> transporterDTOs = new ArrayList<>(transporters.size());
		for(Transporter transporter: transporters)
			transporterDTOs.add(TransporterDTOTransformer.toDTO(transporter));
		return transporterDTOs;
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<SharingPermitDTO> getSharingPermits(Long businessID) throws NotAuthenticatedException, DataAccessException {
		Set<SharingPermit> sharingPermits = Business.findBusiness(businessID).getSharingPermits();
		List<SharingPermitDTO> sharingPermitDTOs = new ArrayList<>(sharingPermits.size());
		for(SharingPermit sharingPermit: sharingPermits)
			sharingPermitDTOs.add(SharingPermitDTOTransformer.toDTO(sharingPermit));
		return sharingPermitDTOs;
	}
	
	@PreAuthorize("#businessID == principal.business.id")
	public BusinessDTO get(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return BusinessDTOTransformer.toDTO(Business.findBusiness(businessID));
	}

	private void setDefaultsForNewBusiness(Business business) {
		Settings settings = business.getSettings(); 
		settings.setDefaultLayoutType(LayoutType.DENSE);
		settings.setEmailReplyTo(StringUtils.isBlank(business.getEmail())? utilsService.getAuthenticatedPrincipalDetails().getUsername(): business.getEmail());
		settings.setEmailSubject(EMAIL_SUBJECT);
		settings.setEmailText(EMAIL_TEXT);
	}
	
	@PreAuthorize("principal.business == null and #businessDTO != null and #businessDTO.id == null")
	@Transactional(readOnly = false)
	public Long add(BusinessDTO businessDTO) throws NotAuthenticatedException, FreeUserAccessForbiddenException, ValidationException, DataAccessException, 
													com.novadart.novabill.shared.client.exception.CloneNotSupportedException {
		Business business = new Business();
		BusinessDTOTransformer.copyFromDTO(business, businessDTO);
		setDefaultsForNewBusiness(business);
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
		Long threshold = DateUtils.truncate(new Date(System.currentTimeMillis()), Calendar.HOUR).getTime() - (numberOfDays * 24L * 60L * 60L * 1000L) ;
		List<LogRecordDTO> result = new ArrayList<>();
		for(LogRecord lg: LogRecord.fetchAllSince(businessID, threshold))
			result.add(LogRecordDTOTransformer.toDTO(lg));
		return result;
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<BigDecimal> getInvoiceMonthTotals(Long businessID) throws NotAuthenticatedException, DataAccessException {
		Calendar cal = Calendar.getInstance();
		List<BigDecimal> totals = new ArrayList<>(12);
		for(int i = 0; i < 12; ++ i) totals.add(new BigDecimal("0.00"));
		for(InvoiceDTO invoice: self().getInvoices(businessID, cal.get(Calendar.YEAR))){
			cal.setTime(invoice.getAccountingDocumentDate());
			int month = cal.get(Calendar.MONTH); 
			totals.set(month, totals.get(month).add(invoice.getTotalBeforeTax()));
		}
		for(BigDecimal total: totals)
			total.setScale(2, RoundingMode.HALF_UP);
		return totals;
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	@Transactional(readOnly = false)
	@Restrictions(checkers = {PremiumChecker.class})
	public void setDefaultLayout(Long businessID, LayoutType layoutType) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Business.findBusiness(businessID).getSettings().setDefaultLayoutType(layoutType);
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<NotificationDTO> getNotifications(Long businessID) {
		List<Notification> notifications = Notification.getUnseenNotificationsForBusiness(businessID); 
		List<NotificationDTO> result = new ArrayList<>(notifications.size());
		for(Notification n: notifications)
			result.add(NotificationDTOTransformer.toDTO(n));
		return result;
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id and " +
				  "T(com.novadart.novabill.domain.Notification).findNotification(#id)?.business?.id == #businessID")
	@Transactional(readOnly = false)
	public void markNotificationAsSeen(Long businessID, Long id) {
		Notification notification = Notification.findNotification(id);
		notification.setSeen(true);
		notification.merge();
	}

}
