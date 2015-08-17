package com.novadart.novabill.service;

import com.google.common.base.Joiner;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.report.DocumentType;
import com.novadart.novabill.report.JRDataSourceFactory;
import com.novadart.novabill.report.JasperReportService;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Service
public class PDFStorageService {

    private static String GZIP_EXT = ".gz";
    private static String PDF_EXT = ".pdf";
    private static String XML_EXT = ".xml";


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
        String docId = document.getExpandedDocumentId();
        String filename = Joiner.on('_').skipNulls().join(new String[]{docType, businessId, year, docId, uniquePathID});
        return mergePaths(pdfStoragePath, filename) + PDF_EXT;
    }

    @PostConstruct
    public void init() throws IOException {
        Path path = Paths.get(pdfStoragePath);
        if(!Files.exists(path))
            Files.createDirectories(path);
    }

    public String generateAndStorePdfForAccountingDocument(AccountingDocument document, DocumentType documentType) {
        UniquePathIDGenerationStragegy uniquePathIDGenerationStragegy = new EpochTimeUniquePathIDGenerationStrategy();
        String documentPath = generateDocumentPath(document, documentType,
                uniquePathIDGenerationStragegy.generateUniquePathID()) + GZIP_EXT;
        JRBeanCollectionDataSource dataSource = JRDataSourceFactory.createDataSource(document,
                document.getBusiness().getId());
        try(WritableByteChannel channel = new RandomAccessFile(documentPath, "rw").getChannel();
            GZIPOutputStream destStream = new GZIPOutputStream(Channels.newOutputStream(channel))
        ) {
            jasperReportService.exportReportToPdfFile(dataSource, documentType, document.getLayoutType(), destStream);
            return documentPath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void purgeOrphanPDFs(){
        String query = "select d.documentPath from AccountingDocument d";
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

    public static byte[] pdfFileToByteArray(String path) throws IOException {
        return handleFile(path, new Handler<byte[]>() {
            @Override
            public byte[] handle(InputStream inputStream) {
                try {
                    return IOUtils.toByteArray(inputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public static String getStoredFileExtension(String path){
        if(!path.endsWith(GZIP_EXT))
            throw new IllegalArgumentException();
        String newPath = path.substring(0, path.length() - GZIP_EXT.length());
        if(newPath.endsWith(PDF_EXT))
            return PDF_EXT;
        if(newPath.endsWith(XML_EXT))
            return XML_EXT;
        throw new IllegalArgumentException();
    }

    public static <T> T handleFile(String path, Handler<T> handler) throws IOException {
        try(ReadableByteChannel channel = new RandomAccessFile(path, "r").getChannel();
            GZIPInputStream inputStream = new GZIPInputStream(Channels.newInputStream(channel))
        ){
            return handler.handle(inputStream);
        }
    }

    public interface Handler<T> {
        T handle(InputStream inputStream);
    }

}
