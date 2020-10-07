package com.cased.api;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import com.cased.http.HttpResponse;
import com.cased.util.LinkHeader;

public class ResultsList implements ResultsListInterface {
    private final Map<String, List<String>> headers;
    private final Map<String, Object> jsonBody;
    private final LinkHeader links;

    public ResultsList(HttpResponse response) throws Exception {
        this.headers = response.getHeaders();

        try {
            List<String> rawLinkHeader = headers.get("Links");
            this.links = new LinkHeader(rawLinkHeader.get(0));
        } catch (Exception e) {
            // no link header
            throw new Exception("Links missing in headers; unable to build ResultsList");
        }

        this.jsonBody = new Gson().fromJson(response.getBody(), Map.class);

    }

    public List<Result> getResults() {
        List<Result> results = (List<Result>) jsonBody.get("results");
        return results;
    }

    public int getTotalCount() {
        return (int) jsonBody.get("total_count");
    }

    public int getTotalPages() {
        return (int) jsonBody.get("total_pages");
    }

    public String getFirstPage() {
        return links.getFirstPage();
    }

    public String getLastPage() {
        return links.getLastPage();
    }

    public String getPreviousPage() {
        return links.getPreviousPage();
    }

    public String getNextPage() {
        return links.getNextPage();
    }
}