package com.novadart.novabill.test.suite;

import com.novadart.novabill.aspect.logging.DBLoggerAspect;
import com.novadart.novabill.domain.*;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.transformer.BusinessDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.ClientDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.TransportDocumentDTOTransformer;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.PDFStorageService;
import com.novadart.novabill.shared.client.data.EntityType;
import com.novadart.novabill.shared.client.data.OperationType;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.facade.*;
import com.novadart.novabill.shared.client.tuple.Triple;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;
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
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:gwt-transportdocument-test-config.xml")
@Transactional
@DirtiesContext
@ActiveProfiles("dev")
public class TransportDocumentServiceTest extends ServiceTest {
	
	@Autowired
	private TransportDocumentGwtService transportDocService;
	
	@Autowired
	private InvoiceGwtService invoiceService;
	
	@Autowired
	private BusinessGwtService businessService;
	
	@Autowired
	private BatchDataFetcherGwtService batchDataFetcherService;
	
	@Autowired
	private ClientGwtService clientService;

	@Autowired
	private PDFStorageService pdfStorageService;
	
	@Override
	@Before
	public void authenticate() {
		authenticatedPrincipal = Principal.findByUsername("giordano.battilana@novadart.com");
		authenticatePrincipal(authenticatedPrincipal);
	}
	
	@Test
	public void transportDocServiceWiring(){
		assertNotNull(transportDocService);
	}
	
	@Test
	public void getAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long transportDocID = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next().getId();
		TransportDocumentDTO expectedDTO = TransportDocumentDTOTransformer.toDTO(TransportDocument.findTransportDocument(transportDocID), true);
		TransportDocumentDTO actualDTO = transportDocService.get(transportDocID);
		assertTrue(TestUtils.transportDocumentComparator.equal(actualDTO, expectedDTO));
	}
	
	@Test(expected = DataAccessException.class)
	public void getUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long transportDocID = Business.findBusiness(getUnathorizedBusinessID()).getTransportDocuments().iterator().next().getId();
		transportDocService.get(transportDocID);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAuthorizedTransportDocIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		transportDocService.get(null);
	}
	
	@Test
	public void getAllInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		PageDTO<TransportDocumentDTO> results = transportDocService.getAllInRange(authenticatedPrincipal.getBusiness().getId(), getYear(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		transportDocService.getAllInRange(getUnathorizedBusinessID(), getYear(), 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllInRangeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		transportDocService.getAllInRange(null, getYear(), 0, 10);
	}
	
	@Test
	public void getAllForClientAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		List<AccountingDocumentDTO> actual = new ArrayList<>(transportDocService.getAllForClient(clientID, getYear()));
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<AccountingDocumentDTO> expected = DTOUtils.toDTOList(new ArrayList(Client.findClient(clientID).getTransportDocuments()), DTOUtils.transportDocDTOConverter, false); 
		assertTrue(TestUtils.equal(expected, actual, TestUtils.transportDocumentComparator));
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		transportDocService.getAllForClient(clientID, getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNotExistTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		transportDocService.getAllForClient(-1l, getYear());
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		transportDocService.getAllForClient(null, getYear());
	}
	
	@Test
	public void removeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, IOException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		Long transportDocID = Client.findClient(clientID).getTransportDocuments().iterator().next().getId();
		transportDocService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, transportDocID);
		TransportDocument.entityManager().flush();
		assertNull(TransportDocument.findTransportDocument(transportDocID));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.TRANSPORT_DOCUMENT, rec.getEntityType());
		assertEquals(transportDocID, rec.getEntityID());
		assertEquals(OperationType.DELETE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(Client.findClient(clientID).getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(true, rec.isReferringToDeletedEntity());
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		Long transportDocID = Client.findClient(clientID).getTransportDocuments().iterator().next().getId();
		transportDocService.remove(getUnathorizedBusinessID(), clientID, transportDocID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeUnauthorizedBusinessIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		Long transportDocID = Client.findClient(clientID).getTransportDocuments().iterator().next().getId();
		transportDocService.remove(null, clientID, transportDocID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAuthorizedClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long transportDocID = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next().getId();
		transportDocService.remove(authenticatedPrincipal.getBusiness().getId(), null, transportDocID);
	}
	
	@Test(expected = DataAccessException.class)
	public void removeAauthorizedTransportDocIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		transportDocService.remove(authenticatedPrincipal.getBusiness().getId(), clientID, null);
	}
	
	@Test
	public void getAllForClientInRangeAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = new Long(testProps.get("clientWithTransportDocsID"));
		PageDTO<TransportDocumentDTO> results = transportDocService.getAllForClientInRange(clientID, getYear(), 0, 10);
		assertTrue(10 == results.getLength() && 0 == results.getOffset() && results.getItems().size() <= 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeUnauthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		Long clientID = Business.findBusiness(getUnathorizedBusinessID()).getClients().iterator().next().getId();
		transportDocService.getAllForClientInRange(clientID, getYear(), 0, 10);
	}
	
	@Test(expected = DataAccessException.class)
	public void getAllForClientInRangeClientIDNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		transportDocService.getAllForClientInRange(null, getYear(), 0, 10);
	}
	
	@Test
	public void updateAuthorizedTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, IOException, DataIntegrityException, FreeUserAccessForbiddenException {
		TransportDocument expected = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		expected.setNote("Temporary note for this transport document");
		transportDocService.update(TransportDocumentDTOTransformer.toDTO(expected, true));
		TransportDocument.entityManager().flush();
		TransportDocument actual = TransportDocument.findTransportDocument(expected.getId());
		assertEquals(actual.getNote(), "Temporary note for this transport document");
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.TRANSPORT_DOCUMENT, rec.getEntityType());
		assertEquals(expected.getId(), rec.getEntityID());
		assertEquals(OperationType.UPDATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(expected.getClient().getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(expected.getDocumentID().toString(), details.get(DBLoggerAspect.DOCUMENT_ID));
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedTransportDocNullTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, DataIntegrityException, FreeUserAccessForbiddenException {
		transportDocService.update(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void updateAuthorizedIDNull() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, DataIntegrityException, FreeUserAccessForbiddenException {
		TransportDocument transportDoc = Business.findBusiness(authenticatedPrincipal.getBusiness().getId()).getTransportDocuments().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOTransformer.toDTO(transportDoc, true);
		transDocDTO.setId(null);
		transportDocService.update(transDocDTO);
	}
	
	@Test(expected = DataIntegrityException.class)
	public void updateTransportDocInInvoiceTest() throws DataAccessException, NotAuthenticatedException, DataIntegrityException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException {
		TransportDocument transportDoc = Business.findBusiness(authenticatedPrincipal.getBusiness().getId()).getTransportDocuments().iterator().next();
		Long invoiceID = authenticatedPrincipal.getBusiness().getInvoices().iterator().next().getId();
		transportDocService.setInvoice(authenticatedPrincipal.getBusiness().getId(), invoiceID, transportDoc.getId());
		transportDocService.update(TransportDocumentDTOTransformer.toDTO(transportDoc, true));
	}
	
	@Test
	public void addAuthorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException, IOException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOTransformer.toDTO(TestUtils.createTransportDocument(authenticatedPrincipal.getBusiness().getNextTransportDocDocumentID()), true);
		transDocDTO.setClient(ClientDTOTransformer.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = transportDocService.add(transDocDTO);
		TransportDocument.entityManager().flush();
		assertTrue(TestUtils.transportDocumentComparatorIgnoreID.equal(transDocDTO, TransportDocumentDTOTransformer.toDTO(TransportDocument.findTransportDocument(id), true)));
		LogRecord rec = LogRecord.fetchLastN(authenticatedPrincipal.getBusiness().getId(), 1).get(0);
		assertEquals(EntityType.TRANSPORT_DOCUMENT, rec.getEntityType());
		assertEquals(id, rec.getEntityID());
		assertEquals(OperationType.CREATE, rec.getOperationType());
		Map<String, String> details = parseLogRecordDetailsJson(rec.getDetails());
		assertEquals(transDocDTO.getClient().getName(), details.get(DBLoggerAspect.CLIENT_NAME));
		assertEquals(transDocDTO.getDocumentID().toString(), details.get(DBLoggerAspect.DOCUMENT_ID));
	}
	
	@Test(expected = Exception.class)
	public void addAuthorizedForThinClientValidationErrorTest() throws InstantiationException, IllegalAccessException, NotAuthenticatedException, FreeUserAccessForbiddenException, DataAccessException, ValidationException {
		Client client = new Client();
		client.setName("John Doe");
		client.setBusiness(authenticatedPrincipal.getBusiness());
		Long clientID = clientService.add(authenticatedPrincipal.getBusiness().getId(), ClientDTOTransformer.toDTO(client));
		
		TransportDocumentDTO transDocDTO = TransportDocumentDTOTransformer.toDTO(TestUtils.createTransportDocument(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(null)), true);
		transDocDTO.setClient(ClientDTOTransformer.toDTO(Client.findClient(clientID)));
		transDocDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		try {
			transportDocService.add(transDocDTO);
		} catch (ValidationException e) {
			assertTrue(true);
			throw e;
		}
		fail();
	}
	
	@Test(expected = DataAccessException.class)
	public void addUnathorizedTest() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOTransformer.toDTO(TestUtils.createTransportDocument(Business.findBusiness(getUnathorizedBusinessID()).getNextTransportDocDocumentID()), true);
		transDocDTO.setClient(ClientDTOTransformer.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOTransformer.toDTO(Business.findBusiness(getUnathorizedBusinessID())));
		transportDocService.add(transDocDTO);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedTransportDocDTONull() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException{
		transportDocService.add(null);
	}
	
	@Test(expected = DataAccessException.class)
	public void addAuthorizedTransportDocDTOIDNotNull() throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOTransformer.toDTO(TestUtils.createTransportDocument(authenticatedPrincipal.getBusiness().getNextTransportDocDocumentID()), true);
		transDocDTO.setClient(ClientDTOTransformer.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		transDocDTO.setId(1l);
		transportDocService.add(transDocDTO);
	}
	
	@Test(expected = Exception.class)
	public void updateAuthorizedValidationFieldMappingTest() throws IllegalAccessException, InvocationTargetException, NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException, InstantiationException, ValidationException {
		try{
			TransportDocumentDTO transDocDTO = TransportDocumentDTOTransformer.toDTO(TestUtils.createInvalidTransportDocument(authenticatedPrincipal.getBusiness().getNextTransportDocDocumentID()), true);
			transDocDTO.setClient(ClientDTOTransformer.toDTO(authenticatedPrincipal.getBusiness().getClients().iterator().next()));
			transDocDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
			transportDocService.add(transDocDTO);
		}catch(ValidationException e){
			Set<Field> expected = new HashSet<>(TestUtils.transportDocValidationFieldsMap.values());
			expected.remove(Field.accountingDocumentYear);
			expected.remove(Field.accountingDocumentDate);
			expected.remove(Field.documentID);
			expected.remove(Field.transportStartDate);
			expected.remove(Field.numberOfPackages);
			Set<Field> actual= new HashSet<>();
			for(ErrorObject error: e.getErrors())
				actual.add(error.getField());
			assertEquals(expected, actual);
			throw e;
		}
		fail();
	}
	
	@Test
	public void getAllWithIDsTest() throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException, ValidationException, InstantiationException, IllegalAccessException{
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOTransformer.toDTO(TestUtils.createTransportDocument(authenticatedPrincipal.getBusiness().getNextTransportDocDocumentID()), true);
		transDocDTO.setClient(ClientDTOTransformer.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		transportDocService.add(transDocDTO);
		TransportDocument.entityManager().flush();
		Long nextInvID = invoiceService.getNextInvoiceDocumentID(null);
		List<TransportDocumentDTO> transDocDTOs = businessService.getTransportDocuments(authenticatedPrincipal.getBusiness().getId(), Calendar.getInstance().get(Calendar.YEAR));
		List<Long> ids = new ArrayList<>(transDocDTOs.size());
		for(TransportDocumentDTO dto: transDocDTOs)
			ids.add(dto.getId());
		Collections.sort(transDocDTOs, new Comparator<TransportDocumentDTO>() {
			@Override
			public int compare(TransportDocumentDTO t1, TransportDocumentDTO t2){
				return t1.getAccountingDocumentDate().compareTo(t2.getAccountingDocumentDate());
			}
		});
		Triple<Long, List<TransportDocumentDTO>, PaymentTypeDTO> pack = batchDataFetcherService.fetchNewInvoiceFromTransportDocumentsOpData(ids);
		assertEquals(nextInvID, pack.getFirst());
		assertEquals(ids.size(), pack.getSecond().size());
		for(int i=0; i<transDocDTOs.size(); ++i)
			assertEquals(transDocDTOs.get(i).getId(), pack.getSecond().get(i).getId());
		if(transDocDTOs.get(0).getClient().getDefaultPaymentTypeID() != null)
			assertNotNull(pack.getThird());
		else
			assertTrue(pack.getThird() == null);
	}
	
	@Test(expected = DataAccessException.class)
	public void setInvoiceNullArg1Test() throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException {
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		Invoice invoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		transportDocService.setInvoice(null, invoice.getId(), transDoc.getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void setInvoiceNullArg2Test() throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException {
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		transportDocService.setInvoice(authenticatedPrincipal.getBusiness().getId(), null, transDoc.getId());
	}

	@Test(expected = DataAccessException.class)
	public void setInvoiceNullArg3Test() throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException {
		Invoice invoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		transportDocService.setInvoice(authenticatedPrincipal.getBusiness().getId(), invoice.getId(), null);
	}
	
	@Test(expected = DataAccessException.class)
	public void setInvoiceUnauthorized1Test() throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException {
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		Invoice invoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		transportDocService.setInvoice(getUnathorizedBusinessID(), invoice.getId(), transDoc.getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void setInvoiceUnauthorized2Test() throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException {
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		Invoice invoice = Business.findBusiness(getUnathorizedBusinessID()).getInvoices().iterator().next();
		transportDocService.setInvoice(authenticatedPrincipal.getBusiness().getId(), invoice.getId(), transDoc.getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void setInvoiceUnauthorized3Test() throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException {
		TransportDocument transDoc = Business.findBusiness(getUnathorizedBusinessID()).getTransportDocuments().iterator().next();
		Invoice invoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		transportDocService.setInvoice(authenticatedPrincipal.getBusiness().getId(), invoice.getId(), transDoc.getId());
	}
	
	@Test
	public void setInvoiceAuthrizedTest() throws DataAccessException, NotAuthenticatedException, DataIntegrityException, NoSuchObjectException, FreeUserAccessForbiddenException {
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		Invoice invoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		transportDocService.setInvoice(authenticatedPrincipal.getBusiness().getId(), invoice.getId(), transDoc.getId());
		TransportDocument.entityManager().flush();
		assertEquals(invoice.getId(), TransportDocument.findTransportDocument(transDoc.getId()).getInvoice().getId());
		assertTrue(transportDocService.get(transDoc.getId()).getInvoice() != null);
		assertTrue(Invoice.findInvoice(invoice.getId()).getTransportDocuments().contains(TransportDocument.findTransportDocument(transDoc.getId())));
	}
	
	@Test(expected = DataAccessException.class)
	public void clearInvoiceNullArg1Test() throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException {
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		transportDocService.clearInvoice(null, transDoc.getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void clearInvoiceNullArg2Test() throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException {
		transportDocService.clearInvoice(authenticatedPrincipal.getBusiness().getId(), null);
	}
	
	@Test(expected = DataAccessException.class)
	public void clearInvoiceUnauthrorized1Test() throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException {
		TransportDocument transDoc = Business.findBusiness(authenticatedPrincipal.getBusiness().getId()).getTransportDocuments().iterator().next();
		transportDocService.clearInvoice(getUnathorizedBusinessID(), transDoc.getId());
	}
	
	@Test(expected = DataAccessException.class)
	public void clearInvoiceUnauthrized2Test() throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException {
		TransportDocument transDoc = Business.findBusiness(getUnathorizedBusinessID()).getTransportDocuments().iterator().next();
		transportDocService.clearInvoice(authenticatedPrincipal.getBusiness().getId(), transDoc.getId());
	}
	
	@Test
	public void clearInvoiceAuthorizedTest() throws DataAccessException, NotAuthenticatedException, DataIntegrityException, NoSuchObjectException, FreeUserAccessForbiddenException {
		TransportDocument transDoc = authenticatedPrincipal.getBusiness().getTransportDocuments().iterator().next();
		Invoice invoice = authenticatedPrincipal.getBusiness().getInvoices().iterator().next();
		transportDocService.setInvoice(authenticatedPrincipal.getBusiness().getId(), invoice.getId(), transDoc.getId());
		TransportDocument.entityManager().flush();
		transportDocService.clearInvoice(authenticatedPrincipal.getBusiness().getId(), transDoc.getId());
		TransportDocument.entityManager().flush();
		assertTrue(TransportDocument.findTransportDocument(transDoc.getId()).getInvoice() == null);
		assertTrue(!Invoice.findInvoice(invoice.getId()).getTransportDocuments().contains(TransportDocument.findTransportDocument(transDoc.getId())));
		assertTrue(transportDocService.get(transDoc.getId()).getInvoice() == null);
	}

	@Test
	public void purgeOrghanCrednotesPdfsTest() throws IllegalAccessException, InstantiationException, NotAuthenticatedException, FreeUserAccessForbiddenException, DataIntegrityException, DataAccessException, ValidationException, NoSuchObjectException {
		Client client = authenticatedPrincipal.getBusiness().getClients().iterator().next();
		TransportDocumentDTO transDocDTO = TransportDocumentDTOTransformer.toDTO(TestUtils.createTransportDocument(authenticatedPrincipal.getBusiness().getNextInvoiceDocumentID(null)), true);
		transDocDTO.setClient(ClientDTOTransformer.toDTO(client));
		transDocDTO.setBusiness(BusinessDTOTransformer.toDTO(authenticatedPrincipal.getBusiness()));
		Long id = transportDocService.add(transDocDTO);
		TransportDocument.entityManager().flush();
		String oldPdfPath = TransportDocument.findTransportDocument(id).getDocumentPath();
		TransportDocument expectedTransDoc = TransportDocument.findTransportDocument(id);
		expectedTransDoc.setNote("Temporary note for this estimation");
		transportDocService.update(TransportDocumentDTOTransformer.toDTO(expectedTransDoc, true));
		TransportDocument.entityManager().flush();
		String currPdfPath = TransportDocument.findTransportDocument(id).getDocumentPath();
		assertTrue(Files.exists(FileSystems.getDefault().getPath(oldPdfPath)));
		assertTrue(Files.exists(FileSystems.getDefault().getPath(currPdfPath)));
		pdfStorageService.purgeOrphanPDFs();
		assertTrue(!Files.exists(FileSystems.getDefault().getPath(oldPdfPath)));
		assertTrue(Files.exists(FileSystems.getDefault().getPath(currPdfPath)));
	}
	
}
