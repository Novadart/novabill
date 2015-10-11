package com.novadart.novabill.service.mail.mailgun;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.Notification;
import com.novadart.novabill.service.web.InvoiceService;
import com.novadart.novabill.shared.client.dto.MailDeliveryStatus;
import com.novadart.novabill.shared.client.dto.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;

    private void createInvoiceEmailFailureNotification(Invoice invoice, String message){
        Notification notification = new Notification();
        notification.setType(NotificationType.INVOICE_EMAIL_FAILURE);
        notification.setMessage(message);
        notification.setBusiness(invoice.getBusiness());
        invoice.getBusiness().getNotifications().add(notification);
        notification.persist();
    }

    @Transactional(readOnly = false)
    private void handleEmailFailure(Long businessID, Long invoiceID) {
        Invoice invoice = Invoice.findInvoice(invoiceID);
        if(MailDeliveryStatus.READ.equals(invoice.getEmailedToClient()))
            return;
        invoiceService.setEmailedToClientStatus(businessID, invoiceID, MailDeliveryStatus.FAILURE);
        String messagePattern = messageSource.getMessage("invoices.email.failure.notification", null,
                LocaleContextHolder.getLocale());
        createInvoiceEmailFailureNotification(invoice, String.format(messagePattern,
                invoice.getDocumentID(), invoice.getClient().getName()));
    }

    @Override
    public void accept(List<Map<String, Object>> events) {
        events.stream().forEach(event -> {
            Map<String, Object> userVariables = (Map<String, Object>) event.get(USER_VARIABLES);
            if (userVariables.size() == 0)
                return;
            String customDataStr = userVariables.get(MY_CUSTOM_DATA).toString();
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String, Object> customData = mapper.readValue(customDataStr, new TypeReference<HashMap<String, Object>>() {
                });
                String hash = customData.get(HASH).toString();
                Map<String, String> variables = (Map<String, String>) customData.get(VARIABLES);
                if (integrityValidationService.isValid(variables, hash)) {
                    Long businessID = Long.valueOf(variables.get(BUSINESS_ID));
                    Long invoiceID = Long.valueOf(variables.get(INVOICE_ID));
                    String eventType = event.get(EVENT).toString();
                    if (REJECTED.equals(eventType) || FAILED.equals(eventType))
                        handleEmailFailure(businessID, invoiceID);
                }

            } catch (IOException e) {
               throw new RuntimeException(e);
            }
        });
    }
}
