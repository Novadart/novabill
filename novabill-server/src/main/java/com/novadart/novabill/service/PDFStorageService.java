package com.novadart.novabill.service;

import com.google.common.base.Joiner;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.report.DocumentType;
import com.novadart.novabill.report.JRDataSourceFactory;
import com.novadart.novabill.report.JasperReportService;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Service
public class PDFStorageService {

    @Value("${pdf.storage.path}")
    private String pdfStoragePath;

    @Autowired
    private JasperReportService jasperReportService;

    @PersistenceContext
    private EntityManager entityManager;

    public static String mergePaths(String path1, String path2){
        return new File(path1, path2).toString();
    }

    private String generateDocumentPath(AccountingDocument document, DocumentType documentType,
                                        String uniquePathID) {
        String docType = documentType.name();
        String businessId = String.valueOf(document.getBusiness().getId());
        String year = String.valueOf(document.getAccountingDocumentYear());
        String docId = String.valueOf(document.getDocumentID()) +
                (document.getDocumentIDSuffix() == null? "": document.getDocumentIDSuffix());
        String filename = Joiner.on('_').skipNulls().join(new String[]{docType, businessId, year, docId, uniquePathID});
        return mergePaths(pdfStoragePath, filename) + ".pdf";
    }

    public String generateAndStorePdfForAccountingDocument(AccountingDocument document, DocumentType documentType) {
        UniquePathIDGenerationStragegy uniquePathIDGenerationStragegy = new EpochTimeUniquePathIDGenerationStrategy();
        String documentPath = generateDocumentPath(document, documentType,
                uniquePathIDGenerationStragegy.generateUniquePathID());
        JRBeanCollectionDataSource dataSource = JRDataSourceFactory.createDataSource(document,
                document.getBusiness().getId());
        jasperReportService.exportReportToPdfFile(dataSource, documentType, document.getLayoutType(), documentPath);
        return documentPath;
    }


    public void purgeOrphanPDFs(){
        String query = "select d.documentPDFPath from AccountingDocument d";
        Set<String> pdfPaths = new HashSet<>(entityManager.createQuery(query, String.class).getResultList());
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(pdfStoragePath))) {
            for (Path path : directoryStream) {
                if(!pdfPaths.contains(path.toString()))
                    Files.delete(path);
            }
        } catch (IOException ex) {}
    }

    private interface UniquePathIDGenerationStragegy {
        String generateUniquePathID();
    }

    private class EpochTimeUniquePathIDGenerationStrategy implements UniquePathIDGenerationStragegy{
        @Override
        public String generateUniquePathID() {
            return String.valueOf(System.currentTimeMillis());
        }
    }

}
