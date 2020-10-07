package com.cased.http;

public class CasedHttpResponse implements HttpResponse {
    private final String body;
    private final CasedHeaders headers;

    public CasedHttpResponse(final String body, final CasedHeaders headers) {
        this.body = body;
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public CasedHeaders getHeaders() {
        return headers;
    }
}