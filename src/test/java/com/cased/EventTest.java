package com.cased;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.cased.Config;
import com.cased.http.CasedHeaders;
import com.cased.http.CasedHttpResponse;
import com.cased.http.Requestor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class EventTest {
    @Test
    public void publishEventThrowsExceptionIfNoApiKey() throws Exception {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("event", "foo");

        Cased cased = new Cased();
        assertThrows(Exception.class, () -> cased.Event.publish(data));
    }

    @Test
    public void fetchEventThrowsExceptionIfNoApiKey() throws Exception {
        Cased cased = new Cased();
        assertThrows(Exception.class, () -> cased.Event.fetch("123"));
    }

    public void eventResourceHasARequestor() throws Exception {
        Requestor mockRequestor = mock(Requestor.class);
        assertEquals(mockRequestor, new Event(mockRequestor).requestor);
    }

    @Test
    public void eventResourceHasResourceName() {
        Requestor mockRequestor = mock(Requestor.class);
        Event event = new Event(mockRequestor);

        assertEquals("events", event.getResourceName());
    }

    @Test
    public void eventFetchCallsRequestorGetCorrectly() throws Exception {
        Requestor mockRequestor = mock(Requestor.class);
        Event mockEvent = new Event(mockRequestor);
        CasedHeaders headers = new CasedHeaders();

        when(mockRequestor.get("/events/123")).thenReturn(new CasedHttpResponse("{}", headers));
        mockEvent.fetch("123");
        verify(mockRequestor).get("/events/123");
    }

    @Test
    public void eventListCallsRequestorGetCorrectly() throws Exception {
        Requestor mockRequestor = mock(Requestor.class);
        Event mockEvent = new Event(mockRequestor);
        CasedHeaders headers = new CasedHeaders();
        String linkHeaderString = "<https://api.cased.com/events?page=1>; rel=\"first\", <https://api.cased.com/events?page=5>; rel=\"last\", <https://api.cased.com/events?page=2>; rel=\"prev\", <https://api.cased.com/events?page=4; rel=\"next\"";

        List<String> linkValues = new LinkedList<String>();
        linkValues.add(linkHeaderString);
        headers.put("Links", linkValues);

        when(mockRequestor.get("/events?per_page=25")).thenReturn(new CasedHttpResponse("{}", headers));
        mockEvent.list();
        verify(mockRequestor).get("/events?per_page=25");
    }


    @Test
    public void eventListWithParamsCallsRequestorGetCorrectly() throws Exception {
        Requestor mockRequestor = mock(Requestor.class);
        Event mockEvent = new Event(mockRequestor);
        CasedHeaders headers = new CasedHeaders();
        String linkHeaderString = "<https://api.cased.com/events?page=1>; rel=\"first\", <https://api.cased.com/events?page=5>; rel=\"last\", <https://api.cased.com/events?page=2>; rel=\"prev\", <https://api.cased.com/events?page=4; rel=\"next\"";

        List<String> linkValues = new LinkedList<String>();
        linkValues.add(linkHeaderString);
        headers.put("Links", linkValues);

        when(mockRequestor.get("/events?per_page=10")).thenReturn(new CasedHttpResponse("{}", headers));

        Map<String, Object>params = new HashMap<String, Object>();
        params.put("limit", 10);
        mockEvent.list(params);
        verify(mockRequestor).get("/events?per_page=10");
    }

    @Test
    public void eventListWithMultipleParamsCallsRequestorGetCorrectly() throws Exception {
        Requestor mockRequestor = mock(Requestor.class);
        Event mockEvent = new Event(mockRequestor);
        CasedHeaders headers = new CasedHeaders();
        String linkHeaderString = "<https://api.cased.com/events?page=1>; rel=\"first\", <https://api.cased.com/events?page=5>; rel=\"last\", <https://api.cased.com/events?page=2>; rel=\"prev\", <https://api.cased.com/events?page=4; rel=\"next\"";

        List<String> linkValues = new LinkedList<String>();
        linkValues.add(linkHeaderString);
        headers.put("Links", linkValues);

        when(mockRequestor.get("/events?per_page=10&page=2")).thenReturn(new CasedHttpResponse("{}", headers));

        Map<String, Object>params = new HashMap<String, Object>();
        params.put("limit", 10);
        params.put("page", 2);
        mockEvent.list(params);
        verify(mockRequestor).get("/events?per_page=10&page=2");
    }


    @Test
    public void eventListWithVariablesCallsRequestorGetCorrectly() throws Exception {
        Requestor mockRequestor = mock(Requestor.class);
        Event mockEvent = new Event(mockRequestor);
        CasedHeaders headers = new CasedHeaders();
        String linkHeaderString = "<https://api.cased.com/events?page=1>; rel=\"first\", <https://api.cased.com/events?page=5>; rel=\"last\", <https://api.cased.com/events?page=2>; rel=\"prev\", <https://api.cased.com/events?page=4; rel=\"next\"";

        List<String> linkValues = new LinkedList<String>();
        linkValues.add(linkHeaderString);
        headers.put("Links", linkValues);

        when(mockRequestor.get("/events?per_page=25&variables[organization_id]=org-abc&variables[team_id]=team-123")).thenReturn(new CasedHttpResponse("{}", headers));

        Map<String, Object>params = new HashMap<String, Object>();
        Map<String, String> policyVariables = new HashMap<String, String>();
        policyVariables.put("team_id", "team-123");
        policyVariables.put("organization_id", "org-abc");

        params.put("variables", policyVariables);

        mockEvent.list(params);
        verify(mockRequestor).get("/events?per_page=25&variables[organization_id]=org-abc&variables[team_id]=team-123");
    }

    @Test
    public void eventListWithVariablesAndPaginationCallsRequestorGetCorrectly() throws Exception {
        Requestor mockRequestor = mock(Requestor.class);
        Event mockEvent = new Event(mockRequestor);
        CasedHeaders headers = new CasedHeaders();
        String linkHeaderString = "<https://api.cased.com/events?page=1>; rel=\"first\", <https://api.cased.com/events?page=5>; rel=\"last\", <https://api.cased.com/events?page=2>; rel=\"prev\", <https://api.cased.com/events?page=4; rel=\"next\"";

        List<String> linkValues = new LinkedList<String>();
        linkValues.add(linkHeaderString);
        headers.put("Links", linkValues);

        when(mockRequestor.get("/events?per_page=10&page=2&variables[organization_id]=org-abc&variables[team_id]=team-123")).thenReturn(new CasedHttpResponse("{}", headers));

        Map<String, Object>params = new HashMap<String, Object>();
        Map<String, String> policyVariables = new HashMap<String, String>();
        policyVariables.put("team_id", "team-123");
        policyVariables.put("organization_id", "org-abc");

        params.put("limit", 10);
        params.put("page", 2);
        params.put("variables", policyVariables);

        mockEvent.list(params);
        verify(mockRequestor).get("/events?per_page=10&page=2&variables[organization_id]=org-abc&variables[team_id]=team-123");
    }


    @Test
    public void eventPublishCallsRequestorPostCorrectly() throws Exception {
        Requestor mockRequestor = mock(Requestor.class);
        Event mockEvent = new Event(mockRequestor);
        CasedHeaders headers = new CasedHeaders();

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("event", "foo");

        when(mockRequestor.post("/", data)).thenReturn(new CasedHttpResponse("{}", headers));
        mockEvent.publish(data);
        verify(mockRequestor).post("/", data);
    }

    @Test
    public void eventPublishCanBeDisabled() throws Exception {
        Config.setDisablePublishing(true);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("event", "foo");

        Cased cased = new Cased();
        assertNull(cased.Event.publish(data));
    }

    @AfterEach
    public void DoAfter() {
        // reset to defaults
        Config.setPublishBase("https://publish.cased.com");
        Config.setApiBase("https://api.cased.com");
        Config.publishKey = null;
        Config.policyKey = null;
        Config.resetSensitiveDataHandlers();
        Config.setDisablePublishing(false);
    }
}