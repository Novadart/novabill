package com.novadart.novabill.service.mail.mailgun;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novadart.novabill.domain.Email;
import com.novadart.novabill.service.mail.MailHandlingType;
import com.novadart.novabill.service.mail.MailSender;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Qualifier(MailGunService.QUALIFIER)
@Service
public class MailGunService implements MailSender {

    public static final String QUALIFIER = "MailGunSender";

    public static final String ACKNOWLEDGED = "ACKNOWLEDGED";

    public static final String VARIABLES = "variables";

    public static final String HASH = "hash";

    private static final String ID = "id";

    private static final String TEMP_DIR_PREFIX = "mailgun";

    @Value("${mailgun.api.key}")
    private String apiKey;

    @Value("${mailgun.api.base.url}")
    private String apiBaseUrl;

    @Autowired
    private IntegrityValidationService integrityValidationService;

    private Map<String, String> parseResponse(String jsonStr) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonStr, new TypeReference<HashMap<String, String>>(){});
    }

    private FormDataBodyPart prepareAttachment(Email email) {
        File tempDir = null;
        try {
            tempDir = Files.createTempDirectory(TEMP_DIR_PREFIX).toFile();
            tempDir.deleteOnExit();
            File file = new File(tempDir, email.getAttachmentName());
            file.deleteOnExit();
            IOUtils.copy(new ByteArrayInputStream(email.getAttachment()), new FileOutputStream(file));
            return new FormDataBodyPart("attachment", file, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(tempDir != null)
                try {
                    FileUtils.deleteDirectory(tempDir);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    @Override
    public boolean send(Email email, Consumer<String> onSuccess, Consumer<Throwable> onError) {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", apiKey));
        WebResource webResource = client.resource(apiBaseUrl + "/messages");
        FormDataMultiPart form = new FormDataMultiPart();
        form.field("from", email.getFrom());
        for(String to: email.getTo())
            form.field("to", to);
        form.field("subject", email.getSubject());
        form.field("html", email.getText());
        if(email.getAttachment() != null)
            form.bodyPart(prepareAttachment(email));
        if(!StringUtils.isEmpty(email.getReplyTo()))
            form.field("h:Reply-To", email.getReplyTo());
        if(MailHandlingType.EXTERNAL_ACKNOWLEDGED.equals(email.getHandlingType())) {
            form.field("o:tag", ACKNOWLEDGED);
            Map<String, Object> vars = new HashMap<>();
            vars.put(VARIABLES, email.getVariables());
            vars.put(HASH, integrityValidationService.produceHash(email.getVariables()));
            try {
                form.field("v:my-custom-data", new ObjectMapper().writeValueAsString(vars));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            ClientResponse response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE)
                    .post(ClientResponse.class, form);
            if(response.getStatus() != 200)
                throw new RuntimeException(String.format("Failed : HTTP error code : %d", response.getStatus()));
            String jsonStr = response.getEntity(String.class);
            String id = parseResponse(jsonStr).get(ID);
            onSuccess.accept(id);
            return true;
        } catch (Throwable t) {
            onError.accept(t);
            return false;
        }
    }

}
