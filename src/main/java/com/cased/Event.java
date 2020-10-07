package com.cased;

import java.util.Map;

import com.google.gson.Gson;

import com.cased.Config;

import com.cased.api.Resource;
import com.cased.api.Result;
import com.cased.http.Requestor;


public class Event extends Resource {

    public Event(Requestor requestor) {
        super(requestor, "events");
    }

    public Map<String, Object> publish(final Map<String, Object> data) throws Exception {
        if (Config.getDisablePublishing() == true) {
            return null;
        }

        final String url = "/";
        final String res = makePostRequest(url, data).getBody();

        final Result jsonData = new Gson().fromJson(res, Result.class);
        return jsonData;
    }
}
