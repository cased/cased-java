package com.cased.http;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.cased.Config;

public class Requestor {
    protected final Client client;

    public Requestor() {
        this.client = new OkClient();
    }

    public Requestor(final Client client) {
        this.client = client;
    }

    public HttpResponse get(final String url) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        return this.get(url, params);
    }

    public HttpResponse get(final String url, final Map<String, Object>params) throws Exception {
        final String absoluteUrl = Config.getApiBase() + url;
        final String key = Config.getPolicyKey();

        if (key == null) {
            throw new Exception("No API key provided");
        }

        return client.getRequest(absoluteUrl, key);
    }

    public HttpResponse post(final String url, final Map<String, Object> data) throws Exception {
        String absoluteUrl;
        String key;

        final Gson gson = new Gson();
        final String jsonData = gson.toJson(data);

        if (url == "/") {
            // special case for publishing
            absoluteUrl = Config.getPublishBase() + url;
            key = Config.getPublishKey();
        } else {
            absoluteUrl = Config.getApiBase() + url;
            key = Config.getPolicyKey();
        }

        if (key == null) {
            throw new Exception("No API key provided");
        }

        return client.postRequest(absoluteUrl, key, jsonData);
    }
}


