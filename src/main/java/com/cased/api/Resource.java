package com.cased.api;

import java.util.logging.Logger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.cased.http.Requestor;
import com.cased.Export;
import com.cased.api.Result;
import com.cased.api.ResultsList;
import com.cased.http.HttpResponse;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class Resource {
    public Requestor requestor;
    public String resourceName;

    public Resource(Requestor requestor, String resourceName) {
        this.requestor = requestor;
        this.resourceName = resourceName;
    }

    protected HttpResponse makeGetRequest(String url) throws Exception {
        return requestor.get(url);
    }

    protected HttpResponse makePostRequest(String url, Map<String, Object> data) throws Exception {
        return requestor.post(url, data);
    }

    private String pairsToParams(List<NameValuePair> pairs) {
        String params = URLEncodedUtils.format(pairs, StandardCharsets.UTF_8).replace("%5B", "[").replace("%5D", "]");
        return params;
    }

    protected String prepareParams(Map<String, Object>params) {
        List<NameValuePair> pairs = new LinkedList<NameValuePair>();

        Integer limit = (Integer) params.get("limit");
        Integer page = (Integer) params.get("page");
        String search = (String) params.get("search");
        Map<String, String> variables = (Map<String, String>) params.get("variables");

        if (limit == null) {
            limit = 25;
        }

        NameValuePair limitPair = new BasicNameValuePair("per_page", limit.toString());
        pairs.add(limitPair);

        if (page != null) {
            NameValuePair pagePair = new BasicNameValuePair("page", page.toString());
            pairs.add(pagePair);
        }

        if (search != null) {
            NameValuePair phrasePair = new BasicNameValuePair("phrase", page.toString());
            pairs.add(phrasePair);
        }

        if (variables != null) {
            for (Map.Entry<String, String> variable : variables.entrySet()) {
                String key = variable.getKey();
                String value = variable.getValue();

                String paramKey = "variables[" + key + "]";

                NameValuePair pair = new BasicNameValuePair(paramKey, value);
                pairs.add(pair);
            }
        }

        return pairsToParams(pairs);
    }

    public String getResourceName() {
        return resourceName;
    }

    public Result fetch(final String id) throws Exception {
        final String url = "/" + getResourceName() + "/" + id;
        final String res = makeGetRequest(url).getBody();

        final Result jsonData = new Gson().fromJson(res, Result.class);
        return jsonData;
    }

    public ResultsList list() throws Exception {
        Map<String, Object>params = new HashMap<String, Object>();
        return list(params);
    }

    public ResultsList list(final Map<String, Object>params) throws Exception {
        String queryParams = prepareParams(params);
        final String url = "/" + getResourceName() + "?" + queryParams;

        final HttpResponse res = makeGetRequest(url);

        final ResultsList resultsList = new ResultsList(res);
        return resultsList;
    }
}