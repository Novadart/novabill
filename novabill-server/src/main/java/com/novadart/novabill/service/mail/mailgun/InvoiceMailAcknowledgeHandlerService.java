package com.novadart.novabill.service.mail.mailgun;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novadart.novabill.service.web.InvoiceService;
import com.novadart.novabill.shared.client.dto.MailDeliveryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.novadart.novabill.service.mail.mailgun.MailGunService.HASH;
import static com.novadart.novabill.service.mail.mailgun.MailGunService.VARIABLES;
import static com.novadart.novabill.service.mail.mailgun.MailGunPollingService.*;

@Service
public class InvoiceMailAcknowledgeHandlerService implements MailAcknowledgeHandler {

    public static final String BUSINESS_ID = "businessId";
    public static final String INVOICE_ID = "invoiceId";

    private static final String USER_VARIABLES = "user-variables";
    private static final String MY_CUSTOM_DATA = "my-custom-data";
    private static final String EVENT = "event";

    @Autowired
    private IntegrityValidationService integrityValidationService;

    @Autowired
    private InvoiceService invoiceService;

    @Override
    public void accept(List<Map<String, Object>> events) {
        events.stream().forEach(event->{
            Map<String, Object> userVariables = (Map<String, Object>)event.get(USER_VARIABLES);
            if(userVariables.size() == 0)
                return;
            String customDataStr = userVariables.get(MY_CUSTOM_DATA).toString();
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String, Object> customData = mapper.readValue(customDataStr, new TypeReference<HashMap<String, Object>>(){});
                String hash = customData.get(HASH).toString();
                Map<String, String> variables = (Map<String, String>)customData.get(VARIABLES);
                if(integrityValidationService.isValid(variables, hash)){
                    Long businessID = Long.valueOf(variables.get(BUSINESS_ID));
                    Long invoiceID = Long.valueOf(variables.get(INVOICE_ID));
                    String eventType = event.get(EVENT).toString();
                    if(DELIVERED.equals(eventType))
                        invoiceService.setEmailedToClientStatus(businessID, invoiceID, MailDeliveryStatus.DELIVERED);
                    else if(REJECTED.equals(eventType))
                        invoiceService.setEmailedToClientStatus(businessID, invoiceID, MailDeliveryStatus.FAILURE);
                    else if(FAILDED.equals(eventType))
                        invoiceService.setEmailedToClientStatus(businessID, invoiceID, MailDeliveryStatus.FAILURE);
                }

            } catch (IOException e) {
               throw new RuntimeException(e);
            }
        });
    }
}
