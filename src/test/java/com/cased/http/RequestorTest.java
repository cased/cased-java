package com.cased.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cased.Config;
import com.cased.http.Client;
import com.cased.http.OkClient;
import com.cased.http.Requestor;

import com.google.gson.Gson;

import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;


public class RequestorTest {
    // Mocks
    Client mockClient = mock(Client.class);

    @Test
    public void defaultRequestorConstructorWithOkClient() {
        Requestor requestor = new Requestor();
        assertTrue(requestor.client instanceof OkClient);
    }

    @Test
    public void requestorConstructorWorksWithCustomClient() {
        Requestor requestor = new Requestor(mockClient);
        assertTrue(requestor.client instanceof Client);
    }

    @Test
    public void requestorGetThrowsIfNoAPIKeyIsSet() {
        Requestor requestor = new Requestor(mockClient);
        assertThrows(Exception.class, () -> requestor.get("/events"));
    }

    @Test
    public void requestorGetCallsGetRequestCorrectly() throws Exception {
        Requestor requestor = new Requestor(mockClient);
        Config.policyKey = "policy_test_123";
        requestor.get("/events");
        verify(mockClient).getRequest("https://api.cased.com/events", "policy_test_123");
    }

    @Test
    public void requestorGetCallsGetRequestCorrectlyWithCustomSettings() throws Exception {
        Requestor requestor = new Requestor(mockClient);
        Config.policyKey = "policy_test_123";
        Config.setApiBase("https://api.example.com");
        requestor.get("/events");
        verify(mockClient).getRequest("https://api.example.com/events", "policy_test_123");
    }

    @Test
    public void requestorPostCallsPostRequestCorrectly() throws Exception {
        Requestor requestor = new Requestor(mockClient);
        Config.publishKey = "publish_test_123";

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("event", "foo");
        final Gson gson = new Gson();
        final String jsonData = gson.toJson(data);

        requestor.post("/", data);
        verify(mockClient).postRequest("https://publish.cased.com/", "publish_test_123", jsonData);
    }

    @Test
    public void requestorPostCallsPostRequestCorrectlyWithCustomSettings() throws Exception {
        Requestor requestor = new Requestor(mockClient);
        Config.publishKey = "publish_test_123";

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("event", "foo");
        final Gson gson = new Gson();
        final String jsonData = gson.toJson(data);

        Config.setPublishBase("https://publish.example.com");
        requestor.post("/", data);
        verify(mockClient).postRequest("https://publish.example.com/", "publish_test_123", jsonData);
    }

    @Test
    public void requestorPostThrowsIfNoAPIKeyIsSet() {
        Map<String,Object> data = new HashMap<String,Object>();

        Requestor requestor = new Requestor(mockClient);
        assertThrows(Exception.class, () -> requestor.post("http://example.com", data));
    }

    @AfterEach
    public void DoAfter() {
        // reset to defaults
        Config.setPublishBase("https://publish.cased.com");
        Config.setApiBase("https://api.cased.com");
        Config.publishKey = null;
        Config.policyKey = null;
    }
}
