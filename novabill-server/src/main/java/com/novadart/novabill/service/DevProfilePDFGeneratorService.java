package com.novadart.novabill.service;

import com.novadart.novabill.domain.*;
import com.novadart.novabill.report.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

/*
 * This bean is used to create the pdfs for the accounting documents. It is instantiated only when the spring 'dev'
 * profile is active i.e. in development mode.
 */
public class DevProfilePDFGeneratorService {

    @Autowired
    private PDFStorageService pdfStorageService;

    @Scheduled(fixedDelay = 31_536_000_730l)
    @Transactional(readOnly = false)
    public void generatePDFsForDocs(){
        for(Business business: Business.findAllBusinesses()){
            for(Invoice invoice: business.getInvoices()){
                String path = pdfStorageService.generateAndStorePdfForAccountingDocument(invoice, DocumentType.INVOICE);
                invoice.setDocumentPDFPath(path);
            }
            for(Estimation estimation: business.getEstimations()){
                String path = pdfStorageService.generateAndStorePdfForAccountingDocument(estimation, DocumentType.ESTIMATION);
                estimation.setDocumentPDFPath(path);
            }
            for(CreditNote creditNote: business.getCreditNotes()){
                String path = pdfStorageService.generateAndStorePdfForAccountingDocument(creditNote, DocumentType.CREDIT_NOTE);
                creditNote.setDocumentPDFPath(path);
            }
            for(TransportDocument transportDocument: business.getTransportDocuments()){
                String path = pdfStorageService.generateAndStorePdfForAccountingDocument(transportDocument, DocumentType.TRANSPORT_DOCUMENT);
                transportDocument.setDocumentPDFPath(path);
            }
        }
    }

}
