package com.cased;

import java.util.List;
import java.util.LinkedList;


import com.cased.data.SensitiveDataHandler;

public abstract class Config {
    public static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
    public static final int DEFAULT_READ_TIMEOUT = 80 * 1000;

    public static final String API_BASE = "https://api.cased.com";
    public static final String PUBLISH_BASE = "https://publish.cased.com";
    public static final String VERSION = "0.0.1";

    private static volatile String apiBase = API_BASE;
    private static volatile String publishBase = PUBLISH_BASE;

    private static volatile Boolean disablePublishing = false;

    private static volatile List<SensitiveDataHandler> sensitiveDataHandlers = new LinkedList<SensitiveDataHandler>();

    public static volatile String publishKey = null;
    public static volatile String policyKey = null;


    public static String getApiBase() {
        return apiBase;
    }

    public static void setApiBase(final String base) {
        apiBase = base;
    }

    public static String getPublishBase() {
        return publishBase;
    }

    public static void setPublishBase(final String base) {
        publishBase = base;
    }

    public static boolean getDisablePublishing() {
        return disablePublishing;
    }

    public static void setDisablePublishing(final boolean state) {
        disablePublishing = state;
    }

    public static String getPublishKey() {
        return publishKey;
    }

    public static String getPolicyKey() {
        return policyKey;
    }

    public static void addSensitiveDataHandler(SensitiveDataHandler handler) {
        sensitiveDataHandlers.add(handler);
    }

    public static void setSensitiveDataHandlers(List<SensitiveDataHandler> handlers) {
        sensitiveDataHandlers = handlers;
    }

    public static List<SensitiveDataHandler> getSensitiveDataHandlers() {
        return sensitiveDataHandlers;
    }

    public static void resetSensitiveDataHandlers() {
        sensitiveDataHandlers = new LinkedList<SensitiveDataHandler>();
    }
}