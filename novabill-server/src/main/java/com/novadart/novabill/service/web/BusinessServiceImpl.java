package com.novadart.novabill.service.web;

import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.TrialOrPremiumChecker;
import com.novadart.novabill.domain.*;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.transformer.*;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.Groups.HeavyBusiness;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.data.PriceListConstants;
import com.novadart.novabill.shared.client.dto.*;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.tuple.Pair;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.CloneNotSupportedException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public abstract class BusinessServiceImpl implements BusinessService {

	@Autowired
	private SimpleValidator validator;
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private MessageSource messageSource;
	
	private Map<Locale, PaymentType[]> paymentTypes;

	private int trialPeriodInDays;
	
	public static final String EMAIL_SUBJECT = "Invio Fattura n. $NumeroFattura del $DataFattura";
	
	public static final String EMAIL_TEXT = "Spettabile $NomeCliente,\n\ncon la presente trasmettiamo la nostra fattura nr. $NumeroFattura del $DataFattura in formato PDF.\nIl documento è scaricabile alla pagina web sotto indicata.\n\nCordiali saluti,\n$RagioneSocialeAzienda";
	
	@PostConstruct
	public void init(){
		paymentTypes = new HashMap<>();
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

	public void setTrialPeriodInDays(int trialPeriodInDays) {
		this.trialPeriodInDays = trialPeriodInDays;
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public BusinessStatsDTO getStats(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
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
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@Transactional(readOnly = true)
	@PreAuthorize("#businessID == principal.business.id")
	public Integer countClients(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return self().getClients(businessID).size();
	}
	
	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public Integer countInvoicesForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return self().getInvoices(businessID, year).size();
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public Pair<BigDecimal, BigDecimal> getTotalsForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		BigDecimal totalBeforeTaxes = new BigDecimal("0.0");
		BigDecimal totalAfterTaxes = new BigDecimal("0.0");
		for(InvoiceDTO invoiceDTO: self().getInvoices(businessID, year)){
			totalBeforeTaxes = totalBeforeTaxes.add(invoiceDTO.getTotalBeforeTax());
			totalAfterTaxes = totalAfterTaxes.add(invoiceDTO.getTotal());
		}
		return new Pair<>(totalBeforeTaxes.setScale(2, BigDecimal.ROUND_HALF_EVEN), totalAfterTaxes.setScale(2, BigDecimal.ROUND_HALF_EVEN));
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#businessDTO?.id == principal.business.id")
	public void update(BusinessDTO businessDTO) throws DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException {
		Business business = Business.findBusiness(businessDTO.getId());
		BusinessDTOTransformer.copyFromDTO(business, businessDTO);
		validator.validate(business, HeavyBusiness.class);
	}
	
	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<InvoiceDTO> getInvoices(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
		return DTOUtils.toDTOList(AccountingDocument.sortAccountingDocuments(Business.findBusiness(businessID).fetchInvoicesEagerly(year)), DTOUtils.invoiceDTOConverter, false);
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<CreditNoteDTO> getCreditNotes(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
		return DTOUtils.toDTOList(AccountingDocument.sortAccountingDocuments(Business.findBusiness(businessID).fetchCreditNotesEagerly(year)), DTOUtils.creditNoteDTOConverter, false);
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<EstimationDTO> getEstimations(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return DTOUtils.toDTOList(AccountingDocument.sortAccountingDocuments(Business.findBusiness(businessID).fetchEstimationsEagerly(year)), DTOUtils.estimationDTOConverter, false);
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<TransportDocumentDTO> getTransportDocuments(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return DTOUtils.toDTOList(AccountingDocument.sortAccountingDocuments(Business.findBusiness(businessID).fetchTransportDocumentsEagerly(year)), DTOUtils.transportDocDTOConverter, false);
	}
	
	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<ClientDTO> getClients(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
		Set<Client> clients = Business.findBusiness(businessID).getClients();
		List<ClientDTO> clientDTOs = new ArrayList<ClientDTO>(clients.size());
		for(Client client: clients)
			clientDTOs.add(ClientDTOTransformer.toDTO(client));
		return clientDTOs;
	}
	
	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<CommodityDTO> getCommodities(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Set<Commodity> commodities = Business.findBusiness(businessID).getCommodities();
		List<CommodityDTO> commodityDTOs = new ArrayList<CommodityDTO>(commodities.size());
		for(Commodity commodity: commodities)
			commodityDTOs.add(CommodityDTOTransformer.toDTO(commodity));
		return commodityDTOs;
	}
	

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<PriceListDTO> getPriceLists(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Set<PriceList> priceLists = Business.findBusiness(businessID).getPriceLists();
		List<PriceListDTO> priceListDTOs = new ArrayList<>(priceLists.size());
		for(PriceList priceList: priceLists)
			priceListDTOs.add(PriceListDTOTransformer.toDTO(priceList, null));
		return priceListDTOs;
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<PaymentTypeDTO> getPaymentTypes(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
		Set<PaymentType> paymentTypes = Business.findBusiness(businessID).getPaymentTypes();
		List<PaymentTypeDTO> paymentTypeDTOs = new ArrayList<PaymentTypeDTO>(paymentTypes.size());
		for(PaymentType paymentType: paymentTypes)
			paymentTypeDTOs.add(PaymentTypeDTOTransformer.toDTO(paymentType));
		return paymentTypeDTOs;
	}
	
	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<TransporterDTO> getTransporters(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Set<Transporter> transporters = Business.findBusiness(businessID).getTransporters();
		List<TransporterDTO> transporterDTOs = new ArrayList<>(transporters.size());
		for(Transporter transporter: transporters)
			transporterDTOs.add(TransporterDTOTransformer.toDTO(transporter));
		return transporterDTOs;
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	public List<SharingPermitDTO> getSharingPermits(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Set<SharingPermit> sharingPermits = Business.findBusiness(businessID).getSharingPermits();
		List<SharingPermitDTO> sharingPermitDTOs = new ArrayList<>(sharingPermits.size());
		for(SharingPermit sharingPermit: sharingPermits)
			sharingPermitDTOs.add(SharingPermitDTOTransformer.toDTO(sharingPermit));
		return sharingPermitDTOs;
	}
	
	@PreAuthorize("#businessID == principal.business.id")
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	public BusinessDTO get(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return BusinessDTOTransformer.toDTO(Business.findBusiness(businessID));
	}

	private void setDefaultsForNewBusiness(Business business) {
		Settings settings = business.getSettings(); 
		settings.setDefaultLayoutType(LayoutType.DENSE);
		settings.setEmailReplyTo(StringUtils.isBlank(business.getEmail())? utilsService.getAuthenticatedPrincipalDetails().getUsername(): business.getEmail());
		settings.setEmailSubject(EMAIL_SUBJECT);
		settings.setEmailText(EMAIL_TEXT);
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
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
		business.getSettings().setNonFreeAccountExpirationTime(System.currentTimeMillis() + trialPeriodInDays * 86_400_000);
		business.getPrincipals().add(principal);
		Business mergedBusiness = business.merge();
		utilsService.setBusinessForPrincipal(mergedBusiness);
		return mergedBusiness.getId();
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<Integer> getInvoceYears(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return Business.findBusiness(businessID).getInvoiceYears();
	}
	
	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<Integer> getCreditNoteYears(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return Business.findBusiness(businessID).getCreditNoteYears();
	}
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	public List<Integer> getEstimationYears(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return Business.findBusiness(businessID).getEstimationYears();
	}
	
	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<Integer> getTransportDocumentYears(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return Business.findBusiness(businessID).getTransportDocumentYears();
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<LogRecordDTO> getLogRecords(Long businessID, Integer numberOfDays) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Long threshold = DateUtils.truncate(new Date(System.currentTimeMillis()), Calendar.HOUR).getTime() - (numberOfDays * 24L * 60L * 60L * 1000L) ;
		List<LogRecordDTO> result = new ArrayList<>();
		for(LogRecord lg: LogRecord.fetchAllSince(businessID, threshold))
			result.add(LogRecordDTOTransformer.toDTO(lg));
		return result;
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<BigDecimal> getInvoiceMonthTotals(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
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
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	public void setDefaultLayout(Long businessID, LayoutType layoutType) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Business.findBusiness(businessID).getSettings().setDefaultLayoutType(layoutType);
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<NotificationDTO> getNotifications(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
		List<Notification> notifications = Notification.getUnseenNotificationsForBusiness(businessID); 
		List<NotificationDTO> result = new ArrayList<>(notifications.size());
		for(Notification n: notifications)
			result.add(NotificationDTOTransformer.toDTO(n));
		return result;
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id and " +
				  "T(com.novadart.novabill.domain.Notification).findNotification(#id)?.business?.id == #businessID")
	@Transactional(readOnly = false)
	public void markNotificationAsSeen(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Notification notification = Notification.findNotification(id);
		notification.setSeen(true);
		notification.merge();
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public List<DocumentIDClassDTO> getDocumentIdClasses(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Set<DocumentIDClass> documentIDClasses = Business.findBusiness(businessID).getDocumentIDClasses();
		List<DocumentIDClassDTO> documentIDClassDTOs = new ArrayList<>(documentIDClasses.size());
		for(DocumentIDClass documentIDClass : documentIDClasses)
			documentIDClassDTOs.add(DocumentIDClassDTOTransformer.toDTO(documentIDClass));
		return documentIDClassDTOs;
	}

	@Override
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	public List<InvoiceDTO> getInvoices(Long businessID, Integer year, final String docIDSuffix) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		List<InvoiceDTO> allInvocesForYear = self().getInvoices(businessID, year);
		Collection<InvoiceDTO> filtered = DTOUtils.filter(allInvocesForYear, new DTOUtils.Predicate<InvoiceDTO>(){
			@Override
			public boolean isTrue(InvoiceDTO doc) {
				return docIDSuffix == null? doc.getDocumentIDSuffix() == null: docIDSuffix.equalsIgnoreCase(doc.getDocumentIDSuffix());
			}
		});
		return new ArrayList<>(filtered);
	}
}
