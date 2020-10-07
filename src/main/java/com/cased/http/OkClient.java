package com.cased.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Headers;

import java.util.Set;
import java.util.List;

import okhttp3.MediaType;

public class OkClient implements Client {
    private OkHttpClient okClient = new OkHttpClient();
    private final MediaType jsonMediaType = MediaType.get("application/json; charset=utf-8");

    private HttpResponse casedResponseFromOkHttp(final Response response) throws Exception {
        final String body = response.body().string();
        final Headers rawHeaders = response.headers();

        final Set<String> headerNames = rawHeaders.names();
        final CasedHeaders headers = new CasedHeaders();

        // Format the headers to match the interface
        for (final String name : headerNames) {
            final List<String> headerValueList = rawHeaders.values(name);
            headers.put(name, headerValueList);
        }

        return new CasedHttpResponse(body, headers);
    }

    public HttpResponse getRequest(final String url, final String apiKey) throws Exception {
        final Request req = new Request.Builder()
            .url(url)
            .get()
            .build();

        try {
            final Response res = okClient.newCall(req).execute();
            final HttpResponse response = casedResponseFromOkHttp(res);
            return response;

        } catch (final Exception e) {
            throw e;
        }
    }

    public HttpResponse postRequest(final String url, final String apiKey, final String body) throws Exception {
        final Request req = new Request.Builder()
            .url(url)
            .post(RequestBody.create(body, jsonMediaType))
            .build();

        try {
            final Response res = okClient.newCall(req).execute();
            final HttpResponse response = casedResponseFromOkHttp(res);
            return response;

        } catch (final Exception e) {
            throw e;
        }
    }
}