package com.novadart.novabill.test.suite;


import com.dumbster.smtp.SimpleSmtpServer;
import com.novadart.novabill.aspect.logging.DBLoggerAspect;
import com.novadart.novabill.domain.*;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.transformer.BusinessDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.ClientDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.InvoiceDTOTransformer;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.report.DocumentType;
import com.novadart.novabill.report.JRDataSourceFactory;
import com.novadart.novabill.report.JasperReportService;
import com.novadart.novabill.service.PDFStorageService;
import com.novadart.novabill.service.web.InvoiceService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.FilteringDateType;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.MailDeliveryStatus;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.facade.BusinessGwtService;
import com.novadart.novabill.shared.client.facade.ClientGwtService;
import com.novadart.novabill.shared.client.facade.InvoiceGwtService;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;
import com.novadart.novabill.web.mvc.ajax.dto.EmailDTO;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPOutputStream;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-invoice-test-config.xml")
@Transactional
@DirtiesContext
@ActiveProfiles("dev")
public class InvoiceServiceTest extends ServiceTest {
	
	@Autowired
	private InvoiceGwtService invoiceService;
	
	@Autowired
	private InvoiceService invoiceAjaxService;
	
	@Autowired
	private ClientGwtService clientService;
	
	@Autowired
	private BusinessGwtService businessService;

	@Autowired
	private PDFStorageService pdfStorageService;

	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}

	@Test
	public void invoiceServiceWiringTest(){
		assertNotNull(invoiceService);
	}

	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long invoiceID = authenticatedPrincipal.getBusiness().getInvoices().iterator().next().getId();
		InvoiceDTO expectedDTO = InvoiceDTOTransformer.toDTO(Invoice.findInvoice(invoiceID), true);
		InvoiceDTO actualDTO = invoiceService.get(invoiceID);
		assertTrue(TestUtils.accountingDocumentComparator.equal(actualDTO, expectedDTO));
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long invoiceID = Business.findBusiness(getUnathorizedBusinessID()).getInvoices().iterator().next().getId();
		invoiceService.get(invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAuthorizedInvoiceIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		invoiceService.get(null);
	}
	
	@Test
	public void getAllInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		PageDTO<InvoiceDTO> results = invoiceService.getAllInRange(authenticatedPrincipal.getBusiness().getId(), getYear(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		invoiceService.getAllInRange(getUnathorizedBusinessID(), getYear(), 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		invoiceService.getAllInRange(null, getYear(), 0, 10);
	}
	
	@Test
	public void getAllForClientAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		List<AccountingDocumentDTO> actual = new ArrayList<>(invoiceService.getAllForClient(clientID, getYear()));
		List<AccountingDocumentDTO> expected = new ArrayList<>();
		for(Invoice doc: Client.findClient(clientID).getInvoices())
			if(doc.getAccountingDocumentYear().equals(getYear()))
				expected.add(DTOUtils.invoiceDTOConverter.toDTO(doc, false));
		assertTrue(TestUtils.equal(expected, actual, TestUtils.accountingDocumentComparator));
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		invoiceService.getAllForClient(clientID, getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNotExistTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		invoiceService.getAllForClient(-1l, getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		invoiceService.getAllForClient(null, getYear());
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, IOException, DataIntegrityException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, invoiceID);
		Invoice.entityManager().flush();
		assertNull(Invoice.findInvoice(invoiceID));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.INVOICE, rec.getEntityType());
		assertEquals(invoiceID, rec.getEntityID());
		assertEquals(OperationType.DELETE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(Client.findClient(clientID).getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(true, rec.isReferringToDeletedEntity());
	}
	
	@Test
	public void removeAuthorizedFromTranDocsTest() throws InstantiationException, IllegalAccessException, NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, DataIntegrityException, NoSuchObjectException {
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next(); 
		transDoc.setInvoice(inv);
		inv.getTransportDocuments().add(transDoc);
		Invoice.entityManager().flush();
		invoiceService.remove(inv.getBusiness().getId(), inv.getClient().getId(), inv.getId());
		assertTrue(TransportDocument.findTransportDocument(transDoc.getId()).getInvoice() == null);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(getUnathorizedBusinessID(), clientID, invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(null, clientID, invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		Long invoiceID = authenticatedPrincipal.getBusiness().getInvoices().iterator().next().getId();
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), null, invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAauthorizedInvoiceIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, null);
	}
	
	@Test
	public void setPayedAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException, IOException{
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.setPayed(authenticatedPrincipal.getBusiness().getId(), clientID, invoiceID, true);
		assertTrue(Invoice.findInvoice(invoiceID).getPayed());
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.INVOICE, rec.getEntityType());
		assertEquals(invoiceID, rec.getEntityID());
		assertEquals(OperationType.SET_PAYED, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(Client.findClient(clientID).getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(Invoice.findInvoice(invoiceID).getDocumentID().toString(), details.get(DBLoggerAspect.DOCUMENT_ID));
		assertEquals("true", details.get(DBLoggerAspect.PAYED_STATUS));
	}
	
	@Test(expected = DataAccessException.class)
	public void setPayedUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(getUnathorizedBusinessID(), clientID, invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void setPayedUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		Long invoiceID = Client.findClient(clientID).getInvoices().iterator().next().getId();
		invoiceService.remove(null, clientID, invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void setPayedUnauthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		Long invoiceID = authenticatedPrincipal.getBusiness().getInvoices().iterator().next().getId();
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), null, invoiceID);
	}
	
	@Test(expected = DataAccessException.class)
	public void setPayedUnauthorizedInvoiceIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, DataIntegrityException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		invoiceService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, null);
	}
	
	@Test
	public void getAllForClientInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithInvoicesID"));
		PageDTO<InvoiceDTO> results = invoiceService.getAllForClientInRange(clientID, getYear(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		invoiceService.getAllForClientInRange(clientID, getYear(), 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		invoiceService.getAllForClientInRange(null, getYear(), 0, 10);
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, IOException, DataIntegrityException, FreeUserAccessForbiddenException {
		Invoice expectedInvoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		expectedInvoice.setNote("Temporary note for this invoice");
		invoiceService.update(InvoiceDTOTransformer.toDTO(expectedInvoice, true));
		Invoice.entityManager().flush();
		Invoice actualInvoice = Invoice.findInvoice(expectedInvoice.getId());
		assertEquals(actualInvoice.getNote(), "Temporary note for this invoice");
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.INVOICE, rec.getEntityType());
		assertEquals(expectedInvoice.getId(), rec.getEntityID());
		assertEquals(OperationType.UPDATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(expectedInvoice.getClient().getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(expectedInvoice.getDocumentID().toString(), details.get(DBLoggerAspect.DOCUMENT_ID));
		assertTrue(Files.exists(FileSystems.getDefault().getPath(actualInvoice.getDocumentPath())));
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedInvoiceNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, DataIntegrityException, FreeUserAccessForbiddenException {
		invoiceService.update(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedIDNull() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, DataIntegrityException, FreeUserAccessForbiddenException {
		Invoice invoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOTransformer.toDTO(invoice, true);
		invDTO.setId(null);
		invoiceService.update(invDTO);
	}
	
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException, IOException, DataIntegrityException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOTransformer.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(null), Invoice.class), true);
		invDTO.setClient(ClientDTOTransformer.toDTO(client));
		invDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = invoiceService.add(invDTO);
		Invoice.entityManager().flush();
		assertTrue(TestUtils.accountingDocumentComparatorIgnoreID.equal(invDTO, InvoiceDTOTransformer.toDTO(Invoice.findInvoice(id), true)));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.INVOICE, rec.getEntityType());
		assertEquals(id, rec.getEntityID());
		assertEquals(OperationType.CREATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(client.getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(invDTO.getDocumentID().toString(), details.get(DBLoggerAspect.DOCUMENT_ID));
		Invoice inv = Invoice.findInvoice(id);
		assertTrue(Files.exists(FileSystems.getDefault().getPath(inv.getDocumentPath())));
		assertEquals(MailDeliveryStatus.NOT_SENT, inv.getEmailedToClient());
	}
	
	@Test
	public void addAuthorizedFromTranDocsTest() throws InstantiationException, IllegalAccessException, NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, DataIntegrityException {
		Long transportDocID = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next().getId();
		InvoiceDTO invDTO = InvoiceDTOTransformer.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(null), Invoice.class), true);
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		invDTO.setClient(ClientDTOTransformer.toDTO(client));
		invDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		List<Long> tranDocsIDs = new ArrayList<>();
		tranDocsIDs.add(transportDocID);
		invDTO.setTransportDocumentIDs(tranDocsIDs);
		Long id = invoiceService.add(invDTO);
		Invoice.entityManager().flush();
		assertTrue(Invoice.findInvoice(id).getTransportDocuments().contains(TransportDocument.findTransportDocument(transportDocID)));
		assertEquals(Invoice.findInvoice(id).getId(), TransportDocument.findTransportDocument(transportDocID).getInvoice().getId());
	}
	
	@Test(expected = Exception.class)
	public void addAuthorizedForThinClientValidationErrorTest() throws InstantiationException, IllegalAccessException, NotAuthenticatedException, FreeUserAccessForbiddenException, DataAccessException, DataIntegrityException, ValidationException {
		Client client = new Client();
		client.setName("John Doe");
		client.setBusiness(authenticatedPrincipal.getBusiness());
		Long clientID = clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOTransformer.toDTO(client));
		
		InvoiceDTO invDTO = InvoiceDTOTransformer.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(null), Invoice.class), true);
		invDTO.setClient(ClientDTOTransformer.toDTO(Client.findClient(clientID)));
		invDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		try {
			invoiceService.add(invDTO);
		} catch (ValidationException e) {
			assertTrue(true);
			throw e;
		}
		fail();
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnathorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException, DataIntegrityException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOTransformer.toDTO(TestUtils.createInvOrCredNote(Business.findBusiness(getUnathorizedBusinessID()).getNextInvoiceDocumentID(null), Invoice.class), true);
		invDTO.setClient(ClientDTOTransformer.toDTO(client));
		invDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		invoiceService.add(invDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedInvoiceDTONull() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, DataIntegrityException{
		invoiceService.add(null);
	}
	
	@Test(expected = Exception.class)
	public void addAuthorizedInvoiceDTOIDNotNull() throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException, DataIntegrityException, DataAccessException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOTransformer.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(null), Invoice.class), true);
		invDTO.setClient(ClientDTOTransformer.toDTO(client));
		invDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		invDTO.setId(1l);
		try {
			invoiceService.add(invDTO);
		} catch (DataAccessException e) {
			assertTrue(true);
			throw e;
		}
		fail();
	}
	
	@Test(expected = Exception.class)
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException, InstantiationException, DataIntegrityException, ValidationException {
		try{
			InvoiceDTO invDTO = InvoiceDTOTransformer.toDTO(TestUtils.createInvalidInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(null), Invoice.class), true);
			invDTO.setClient(ClientDTOTransformer.toDTO(authenticatedPrincipal.getBusiness().getClients().iterator().next()));
			invDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
			invoiceService.add(invDTO);
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<>(TestUtils.invoiceValidationFieldsMap.values());
			expected.remove(Field.accountingDocumentYear);
			expected.remove(Field.accountingDocumentDate);
			expected.remove(Field.documentID);
			expected.remove(Field.payed);
			expected.remove(Field.paymentDueDate);
			Set<Field> actual= new HashSet<>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertEquals(expected, actual);
			throw e;
		}
		fail();
	}
	
	private Set<Long> invoiceIDSet(Collection<InvoiceDTO> invDTOs){
		Set<Long> ids = new HashSet<>(invDTOs.size());
		for(InvoiceDTO invDTO: invDTOs)
			ids.add(invDTO.getId());
		return ids;
	}
	
	@Test
	public void getAllUnpaidInDateRangeTest() throws NotAuthenticatedException, DataAccessException, ParseException, FreeUserAccessForbiddenException{
		Long businessID = authenticatedPrincipal.getBusiness().getId();
		Set<Long> ids2013 = invoiceIDSet(businessService.getInvoices(businessID, 2013));
		Set<Long> ids2012 = invoiceIDSet(businessService.getInvoices(businessID, 2012));
		Set<Long> ids2010 = invoiceIDSet(businessService.getInvoices(businessID, 2010));
		Set<Long> ids2009 = invoiceIDSet(businessService.getInvoices(businessID, 2009));
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		assertEquals(ids2013, invoiceIDSet(invoiceService.getAllUnpaidInDateRange(FilteringDateType.PAYMENT_DUEDATE, dateFormat.parse("01-01-2013"), dateFormat.parse("31-12-2013"))));
		assertEquals(ids2012, invoiceIDSet(invoiceService.getAllUnpaidInDateRange(FilteringDateType.PAYMENT_DUEDATE, dateFormat.parse("01-01-2012"), dateFormat.parse("31-12-2012"))));
		assertEquals(ids2010, invoiceIDSet(invoiceService.getAllUnpaidInDateRange(FilteringDateType.PAYMENT_DUEDATE, dateFormat.parse("01-01-2010"), dateFormat.parse("31-12-2010"))));
		assertEquals(ids2009, invoiceIDSet(invoiceService.getAllUnpaidInDateRange(FilteringDateType.PAYMENT_DUEDATE, dateFormat.parse("01-01-2009"), dateFormat.parse("31-12-2009"))));
		
		assertEquals(ids2013, invoiceIDSet(invoiceService.getAllUnpaidInDateRange(FilteringDateType.CREATION_DATE, dateFormat.parse("01-01-2013"), dateFormat.parse("31-12-2013"))));
		assertEquals(ids2012, invoiceIDSet(invoiceService.getAllUnpaidInDateRange(FilteringDateType.CREATION_DATE, dateFormat.parse("01-01-2012"), dateFormat.parse("31-12-2012"))));
		assertEquals(ids2010, invoiceIDSet(invoiceService.getAllUnpaidInDateRange(FilteringDateType.CREATION_DATE, dateFormat.parse("01-01-2010"), dateFormat.parse("31-12-2010"))));
		assertEquals(ids2009, invoiceIDSet(invoiceService.getAllUnpaidInDateRange(FilteringDateType.CREATION_DATE, dateFormat.parse("01-01-2009"), dateFormat.parse("31-12-2009"))));
	}
	
	@Test
	public void emailInvoiceAuthorizedTest() throws NoSuchAlgorithmException, IOException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		SimpleSmtpServer smtpServer = SimpleSmtpServer.start(2525);
		EmailDTO emailDTO = new EmailDTO();
		emailDTO.setTo("foo@bar.com");
		emailDTO.setReplyTo("foo@bar.it");
		emailDTO.setSubject("Test subject");
		emailDTO.setMessage("Test message");
		invoiceAjaxService.email(inv.getBusiness().getId(), inv.getId(), emailDTO);
		smtpServer.stop();
		String token = DocumentAccessToken.findAllDocumentAccessTokens().iterator().next().getToken();
		assertTrue(smtpServer.getReceivedEmailSize() == 1);
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.INVOICE, rec.getEntityType());
		assertEquals(inv.getId(), rec.getEntityID());
		assertEquals(OperationType.EMAIL, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(inv.getClient().getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(inv.getDocumentID().toString(), details.get(DBLoggerAspect.DOCUMENT_ID));
		assertEquals("foo@bar.it", details.get(DBLoggerAspect.REPLY_TO));
		assertEquals(1l, DocumentAccessToken.countDocumentAccessTokens());
		assertEquals(1l, DocumentAccessToken.findDocumentAccessTokens(inv.getId(), token).size());
		assertTrue(MailDeliveryStatus.SENT.equals(Invoice.findInvoice(inv.getId()).getEmailedToClient()));
	}
	
	@Test(expected = ValidationException.class)
	public void emailInvoiceInvalidParams1Test() throws NoSuchAlgorithmException, UnsupportedEncodingException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		EmailDTO emailDTO = new EmailDTO();
		invoiceAjaxService.email(inv.getBusiness().getId(), inv.getId(), emailDTO);
	}
	
	@Test(expected = ValidationException.class)
	public void emailInvoiceInvalidParams2Test() throws NoSuchAlgorithmException, UnsupportedEncodingException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		EmailDTO emailDTO = new EmailDTO();
		emailDTO.setTo("foo@bar.com");
		invoiceAjaxService.email(inv.getBusiness().getId(), inv.getId(), emailDTO);
	}
	
	@Test(expected = ValidationException.class)
	public void emailInvoiceInvalidParams3Test() throws NoSuchAlgorithmException, UnsupportedEncodingException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		EmailDTO emailDTO = new EmailDTO();
		emailDTO.setTo("foo@bar.com");
		emailDTO.setReplyTo("foo@bar.it");
		invoiceAjaxService.email(inv.getBusiness().getId(), inv.getId(), emailDTO);
	}
	
	@Test(expected = ValidationException.class)
	public void emailInvoiceInvalidParams4Test() throws NoSuchAlgorithmException, UnsupportedEncodingException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		EmailDTO emailDTO = new EmailDTO();
		emailDTO.setTo("foo@bar.com");
		emailDTO.setReplyTo("foo@bar.it");
		emailDTO.setSubject("Test subject");
		invoiceAjaxService.email(inv.getBusiness().getId(), inv.getId(), emailDTO);
	}
	
	@Test(expected = ValidationException.class)
	public void emailInvoiceInvalidParams5Test() throws NoSuchAlgorithmException, UnsupportedEncodingException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		EmailDTO emailDTO = new EmailDTO();
		emailDTO.setTo("foo-bar.com");
		emailDTO.setReplyTo("foo@bar.it");
		emailDTO.setSubject("Test subject");
		emailDTO.setMessage("Test message");
		invoiceAjaxService.email(inv.getBusiness().getId(), inv.getId(), emailDTO);
	}
	
	@Test(expected = ValidationException.class)
	public void emailInvoiceInvalidParams6Test() throws NoSuchAlgorithmException, UnsupportedEncodingException, ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		EmailDTO emailDTO = new EmailDTO();
		emailDTO.setTo("foo@bar.com");
		emailDTO.setReplyTo("foo-bar.it");
		emailDTO.setSubject("Test subject");
		emailDTO.setMessage("Test message");
		invoiceAjaxService.email(inv.getBusiness().getId(), inv.getId(), emailDTO);
	}

	@Test
	public void purgeOrghanInvoicePdfsTest() throws IllegalAccessException, InstantiationException, NotAuthenticatedException, FreeUserAccessForbiddenException, DataIntegrityException, DataAccessException, ValidationException, NoSuchObjectException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		InvoiceDTO invDTO = InvoiceDTOTransformer.toDTO(TestUtils.createInvOrCredNote(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(null), Invoice.class), true);
		invDTO.setClient(ClientDTOTransformer.toDTO(client));
		invDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = invoiceService.add(invDTO);
		Invoice.entityManager().flush();
		String oldPdfPath = Invoice.findInvoice(id).getDocumentPath();
		Invoice expectedInvoice = Invoice.findInvoice(id);
		expectedInvoice.setNote("Temporary note for this invoice");
		invoiceService.update(InvoiceDTOTransformer.toDTO(expectedInvoice, true));
		Invoice.entityManager().flush();
		String currPdfPath = Invoice.findInvoice(id).getDocumentPath();
		assertTrue(Files.exists(FileSystems.getDefault().getPath(oldPdfPath)));
		assertTrue(Files.exists(FileSystems.getDefault().getPath(currPdfPath)));
		pdfStorageService.purgeOrphanPDFs();
		assertTrue(!Files.exists(FileSystems.getDefault().getPath(oldPdfPath)));
		assertTrue(Files.exists(FileSystems.getDefault().getPath(currPdfPath)));
	}

	@Test
	public void withouldTaxPensionCompensationUpdateTest() throws DataAccessException, FreeUserAccessForbiddenException, ValidationException, DataIntegrityException, NoSuchObjectException, NotAuthenticatedException {
		Invoice expectedInvoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		BigDecimal preUpdateWitholdTax = expectedInvoice.getWitholdTaxPercentSecondLevel();
		BigDecimal preUpdatePensionContribution = expectedInvoice.getPensionContributionPercent();
		BigDecimal preUpdateWitholdTaxTotal = expectedInvoice.getWitholdTaxTotal();
		BigDecimal preUpdatePensionContributionTotal = expectedInvoice.getPensionContributionTotal();
		expectedInvoice.setWitholdTaxPercentSecondLevel(new BigDecimal("20.0"));
		expectedInvoice.setPensionContributionPercent(new BigDecimal("4.0"));
		expectedInvoice.setWitholdTaxTotal(new BigDecimal("20.0"));
		expectedInvoice.setPensionContributionTotal(new BigDecimal("4.0"));
		invoiceService.update(InvoiceDTOTransformer.toDTO(expectedInvoice, true));
		Invoice.entityManager().flush();
		Invoice actualInvoice = Invoice.findInvoice(expectedInvoice.getId());
		assertEquals(null, preUpdateWitholdTax);
		assertEquals(null, preUpdatePensionContribution);
		assertEquals(null, preUpdateWitholdTaxTotal);
		assertEquals(null, preUpdatePensionContributionTotal);
		assertEquals(new BigDecimal("20.0"), actualInvoice.getWitholdTaxPercentSecondLevel());
		assertEquals(new BigDecimal("4.0"), actualInvoice.getPensionContributionPercent());
		assertEquals(new BigDecimal("20.0"), actualInvoice.getWitholdTaxTotal());
		assertEquals(new BigDecimal("4.0"), actualInvoice.getPensionContributionTotal());
	}



	//TODO remove this test case
	@Autowired
	private JasperReportService jasperReportService;
	@Test
	public void generatePdfTest(){
		Invoice inv = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		inv.setPensionContribution(true);
		inv.setPensionContributionPercent(new BigDecimal("4.0"));
		inv.setPensionContributionTotal(new BigDecimal("4.1"));
		inv.setWitholdTax(true);
		inv.setWitholdTaxPercentFirstLevel(new BigDecimal("10.0"));
		inv.setWitholdTaxPercentSecondLevel(new BigDecimal("20.0"));
		inv.setWitholdTaxTotal(new BigDecimal("20.1"));
		inv.setSplitPayment(true);
		inv.setLayoutType(LayoutType.DENSE);
		JRBeanCollectionDataSource dataSource = JRDataSourceFactory.createDataSource(inv, inv.getBusiness().getId());
		try(WritableByteChannel channel = new RandomAccessFile("/tmp/test.pdf.gz", "rw").getChannel();
			GZIPOutputStream destStream = new GZIPOutputStream(Channels.newOutputStream(channel))
		) {
			jasperReportService.exportReportToPdfFile(dataSource, DocumentType.INVOICE, inv.getLayoutType(), destStream);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}

	}


}
