package com.novadart.novabill.service.mail.mailgun;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.novadart.novabill.service.mail.mailgun.MailGunService.ACKNOWLEDGED;

@Service
public class MailGunPollingService {

    public static final String DELIVERED = "delivered";
    public static final String FAILDED = "failed";
    public static final String REJECTED = "rejected";

    private static final ZoneId EDT = TimeZone.getTimeZone("GMT-04:00").toZoneId();

    private static final String PAGING = "paging";
    private static final String NEXT = "next";
    private static final String ITEMS = "items";
    private static final String TIMESTAMP = "timestamp";

    @Value("${mailgun.api.key}")
    private String apiKey;

    @Value("${mailgun.api.base.url}")
    private String apiBaseUrl;

    @Autowired
    private List<MailAcknowledgeHandler> mailAcknowledgeHandlers;

    private final ZonedDateTime begin = ZonedDateTime.now(EDT).minusMinutes(840);

    private static final long THRESHOLD_INTERVAL = 30l;

    private volatile Optional<String> nextPageUrl = Optional.empty();

    private Map<String, Object> fetchPage(String pageUrl, boolean setStartTime) throws IOException {
        Client client = new Client();
        client.addFilter(new HTTPBasicAuthFilter("api", apiKey));
        WebResource webResource = client.resource(pageUrl);
        MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
        if(setStartTime)
            queryParams.add("begin", begin.toEpochSecond());
        queryParams.add("ascending", "yes");
        queryParams.add("tags", ACKNOWLEDGED);
        queryParams.add("event", new StringJoiner(" OR ").add(DELIVERED).add(FAILDED).add(REJECTED).toString());
        ClientResponse response = webResource.queryParams(queryParams).get(ClientResponse.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getEntity(String.class), new TypeReference<HashMap<String, Object>>(){});
    }

    private boolean isValidPage(List<Map<String, Object>> items){
        if(items.size() == 0)
            return false;
        long threshold = ZonedDateTime.now(EDT).minusMinutes(THRESHOLD_INTERVAL).toEpochSecond();
        Map<String, Object> lastItem = items.get(items.size() - 1);
        Double timestamp = (Double)lastItem.get(TIMESTAMP);
        return timestamp < threshold;
    }

    private void notifyHandlers(List<Map<String, Object>> items){
        mailAcknowledgeHandlers.stream().forEach(handler->handler.accept(items));
    }

    @Scheduled(fixedDelay = 30_000) //TODO change it back to 300_000
    public void poll() throws IOException {
        Map<String, Object> eventsPage   = fetchPage(nextPageUrl.orElse(apiBaseUrl + "/events"), !nextPageUrl.isPresent());
        List<Map<String, Object>> items = (List<Map<String, Object>>)eventsPage.get(ITEMS);
        if(isValidPage(items)){
            notifyHandlers(items);
            Map<String, Object> pagingInfo = (Map<String, Object>)eventsPage.get(PAGING);
            nextPageUrl = Optional.of(pagingInfo.get(NEXT).toString());
        }
    }



}
