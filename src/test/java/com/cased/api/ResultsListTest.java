package com.cased.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;

import com.cased.http.CasedHeaders;
import com.cased.http.CasedHttpResponse;
import com.cased.http.HttpResponse;

import org.junit.jupiter.api.Test;

public class ResultsListTest {

    @Test
    public void resultsListCanBeCreatedWithPaginationData() throws Exception {
        CasedHeaders headers = new CasedHeaders();

        String body = "{'foo': 'bar'}";
        String linkHeaderString = "<https://api.cased.com/events?page=1>; rel=\"first\", <https://api.cased.com/events?page=5>; rel=\"last\", <https://api.cased.com/events?page=2>; rel=\"prev\", <https://api.cased.com/events?page=4; rel=\"next\"";

        List<String> linkValues = new LinkedList<String>();
        linkValues.add(linkHeaderString);
        headers.put("Links", linkValues);

        HttpResponse response = new CasedHttpResponse(body, headers);
        ResultsList resultsList = new ResultsList(response);

        assertEquals("https://api.cased.com/events?page=1", resultsList.getFirstPage());
        assertEquals("https://api.cased.com/events?page=5", resultsList.getLastPage());
        assertEquals("https://api.cased.com/events?page=4", resultsList.getNextPage());
        assertEquals("https://api.cased.com/events?page=2", resultsList.getPreviousPage());
    }

    @Test
    public void resultsListCanBeCreatedWithPaginationDataAndMoreHeaders() throws Exception {
        CasedHeaders headers = new CasedHeaders();

        String body = "{'foo': 'bar'}";
        String linkHeaderString = "<https://api.cased.com/events?page=1>; rel=\"first\", <https://api.cased.com/events?page=5>; rel=\"last\", <https://api.cased.com/events?page=2>; rel=\"prev\", <https://api.cased.com/events?page=4; rel=\"next\"";

        List<String> linkValues = new LinkedList<String>();
        linkValues.add(linkHeaderString);
        headers.put("Links", linkValues);

        // Add another header
        List<String> uaValues = new LinkedList<String>();
        uaValues.add("test-user-agent");
        headers.put("user-agent", uaValues);

        HttpResponse response = new CasedHttpResponse(body, headers);
        ResultsList resultsList = new ResultsList(response);

        assertEquals("https://api.cased.com/events?page=1", resultsList.getFirstPage());
        assertEquals("https://api.cased.com/events?page=5", resultsList.getLastPage());
        assertEquals("https://api.cased.com/events?page=4", resultsList.getNextPage());
        assertEquals("https://api.cased.com/events?page=2", resultsList.getPreviousPage());
    }

    @Test
    public void resultsListCanBeCreatedWithResults() throws Exception {
        CasedHeaders headers = new CasedHeaders();

        String body = "{'results': [{'action': 'test.event'}]}";
        String linkHeaderString = "<https://api.cased.com/events?page=1>; rel=\"first\", <https://api.cased.com/events?page=5>; rel=\"last\", <https://api.cased.com/events?page=2>; rel=\"prev\", <https://api.cased.com/events?page=4; rel=\"next\"";

        List<String> linkValues = new LinkedList<String>();
        linkValues.add(linkHeaderString);
        headers.put("Links", linkValues);

        HttpResponse response = new CasedHttpResponse(body, headers);
        ResultsList resultsList = new ResultsList(response);

        List<Map<String, Object>> expectedResults = new ArrayList<Map<String, Object>>();
        Map<String, Object> expectedData = new TreeMap<String, Object>();

        expectedData.put("action", "test.event");
        expectedResults.add(expectedData);

        assertEquals(expectedResults, resultsList.getResults());
    }

    @Test
    public void resultsListCanBeCreatedWithResultsWithMultipleActions() throws Exception {
        CasedHeaders headers = new CasedHeaders();

        String body = "{'results': [{'action': 'test.event'}, {'action': 'test.event2'} ]}";
        String linkHeaderString = "<https://api.cased.com/events?page=1>; rel=\"first\", <https://api.cased.com/events?page=5>; rel=\"last\", <https://api.cased.com/events?page=2>; rel=\"prev\", <https://api.cased.com/events?page=4; rel=\"next\"";

        List<String> linkValues = new LinkedList<String>();
        linkValues.add(linkHeaderString);
        headers.put("Links", linkValues);

        HttpResponse response = new CasedHttpResponse(body, headers);
        ResultsList resultsList = new ResultsList(response);

        List<Map<String, Object>> expectedResults = new ArrayList<Map<String, Object>>();
        Map<String, Object> expectedData1 = new TreeMap<String, Object>();
        Map<String, Object> expectedData2 = new TreeMap<String, Object>();

        expectedData1.put("action", "test.event");
        expectedData2.put("action", "test.event2");

        expectedResults.add(expectedData1);
        expectedResults.add(expectedData2);

        assertEquals(expectedResults, resultsList.getResults());
    }

    @Test
    public void resultsListCanBeCreatedWithResultsWithNestedEvents() throws Exception {
        CasedHeaders headers = new CasedHeaders();

        String body = "{'results': [{'action': 'test.event', 'user': 'jane'}]}";
        String linkHeaderString = "<https://api.cased.com/events?page=1>; rel=\"first\", <https://api.cased.com/events?page=5>; rel=\"last\", <https://api.cased.com/events?page=2>; rel=\"prev\", <https://api.cased.com/events?page=4; rel=\"next\"";

        List<String> linkValues = new LinkedList<String>();
        linkValues.add(linkHeaderString);
        headers.put("Links", linkValues);

        HttpResponse response = new CasedHttpResponse(body, headers);
        ResultsList resultsList = new ResultsList(response);

        List<Map<String, Object>> expectedResults = new ArrayList<Map<String, Object>>();
        Map<String, Object> expectedData1 = new TreeMap<String, Object>();

        expectedData1.put("action", "test.event");
        expectedData1.put("user", "jane");

        expectedResults.add(expectedData1);

        assertEquals(expectedResults, resultsList.getResults());
    }

    @Test
    public void resultsListCanBeCreatedWithResultsWithNestedLists() throws Exception {
        CasedHeaders headers = new CasedHeaders();

        String body = "{'results': [{'action': 'test.event', 'users': ['jane', 'jill']}]}";
        String linkHeaderString = "<https://api.cased.com/events?page=1>; rel=\"first\", <https://api.cased.com/events?page=5>; rel=\"last\", <https://api.cased.com/events?page=2>; rel=\"prev\", <https://api.cased.com/events?page=4; rel=\"next\"";

        List<String> linkValues = new LinkedList<String>();
        linkValues.add(linkHeaderString);
        headers.put("Links", linkValues);

        HttpResponse response = new CasedHttpResponse(body, headers);
        ResultsList resultsList = new ResultsList(response);

        List<Map<String, Object>> expectedResults = new ArrayList<Map<String, Object>>();
        Map<String, Object> expectedData1 = new TreeMap<String, Object>();

        List<String> names = new ArrayList<String>();
        names.add("jane");
        names.add("jill");

        expectedData1.put("action", "test.event");
        expectedData1.put("users", names);

        expectedResults.add(expectedData1);

        assertEquals(expectedResults, resultsList.getResults());
    }


    @Test
    public void resultsListThrowsExceptionIfLinksAreMissing() throws Exception {
        CasedHeaders headers = new CasedHeaders();

        String body = "{'foo': 'bar'}";

        List<String> values = new LinkedList<String>();
        values.add("test-user-agent");
        headers.put("user-agent", values);

        HttpResponse response = new CasedHttpResponse(body, headers);

        assertThrows(Exception.class, () -> new ResultsList(response));
    }
}