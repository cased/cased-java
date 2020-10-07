package com.cased;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import com.cased.data.SensitiveDataHandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

public class ConfigTest {
    @Test
    public void getPublishKeyDefaultsToNull() {
        final String key = Config.getPublishKey();
        assertEquals(null, key);
    }

    @Test
    public void getPolicyKeyDefaultsToNull() {
        final String key = Config.getPolicyKey();
        assertEquals(null, key);
    }

    @Test
    public void getApiBaseHasADefault() {
        final String key = Config.getApiBase();
        assertEquals("https://api.cased.com", key);
    }

    @Test
    public void getPublishBaseHasADefault() {
        final String key = Config.getPublishBase();
        assertEquals("https://publish.cased.com", key);
    }

    @Test
    public void apiBaseCanBeSet() {
        Config.setApiBase("https://api.test.com");
        final String key = Config.getApiBase();
        assertEquals("https://api.test.com", key);
    }

    @Test
    public void publishBaseCanBeSet() {
        Config.setPublishBase("https://publish.test.com");
        final String key = Config.getPublishBase();
        assertEquals("https://publish.test.com", key);
    }

    @Test
    public void sensitiveDataHandlersCanBeAddedAndReturned() {
        SensitiveDataHandler handler = new SensitiveDataHandler("my-label", "test");
        Config.addSensitiveDataHandler(handler);

        List<SensitiveDataHandler> expected = new LinkedList<SensitiveDataHandler>();
        expected.add(handler);

        assertEquals(expected, Config.getSensitiveDataHandlers());
    }

    @Test
    public void sensitiveDataHandlersCanBeSetAsList() {
        SensitiveDataHandler handler = new SensitiveDataHandler("my-label", "test");
        Config.addSensitiveDataHandler(handler);

        List<SensitiveDataHandler> handlers = new LinkedList<SensitiveDataHandler>();
        handlers.add(handler);
        Config.setSensitiveDataHandlers(handlers);

        assertEquals(handlers, Config.getSensitiveDataHandlers());
    }

    @Test
    public void sensitiveDataHandlersCanBeReset() {
        SensitiveDataHandler handler = new SensitiveDataHandler("my-label", "test");
        Config.addSensitiveDataHandler(handler);

        List<SensitiveDataHandler> expected1 = new LinkedList<SensitiveDataHandler>();
        expected1.add(handler);

        assertEquals(expected1, Config.getSensitiveDataHandlers());
        Config.resetSensitiveDataHandlers();

        List<SensitiveDataHandler> expected2 = new LinkedList<SensitiveDataHandler>();
        assertEquals(expected2, Config.getSensitiveDataHandlers());
    }

    @Test
    public void disablePublishingDefaultsToFalse() {
        assertFalse(Config.getDisablePublishing());
    }

    @Test
    public void disablePublishingDefaultsCanBeSet() {
        Config.setDisablePublishing(true);
        assertTrue(Config.getDisablePublishing());
    }

    @AfterEach
    public void DoAfter() {
        // reset to defaults
        Config.setPublishBase("https://publish.cased.com");
        Config.setApiBase("https://api.cased.com");
        Config.publishKey = null;
        Config.policyKey = null;
        Config.resetSensitiveDataHandlers();
        Config.setDisablePublishing(false);
    }
}
