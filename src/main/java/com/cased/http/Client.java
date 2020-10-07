package com.cased.http;

public interface Client {
    public abstract HttpResponse getRequest(String url, String apiKey) throws Exception;
    public abstract HttpResponse postRequest(String url, String apiKey, String body) throws Exception;
}