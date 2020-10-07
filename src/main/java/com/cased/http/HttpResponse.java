package com.cased.http;

public interface HttpResponse {
    String getBody();
    CasedHeaders getHeaders();
}