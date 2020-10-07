package com.cased.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import com.cased.Config;
import com.cased.api.Result;


public class SensitiveDataProcessorTest {
    @Test
    public void sensitiveDataProcessorCanBeConstructedWithEvent() {
        Result auditEvent = new Result();

        SensitiveDataProcessor processor = new SensitiveDataProcessor(auditEvent);
        assertEquals(auditEvent, processor.auditEvent);
    }

    @Test
    public void sensitiveDataProcessorReturnsOriginalEventIfNoSensitiveData() {
        Result auditEvent = new Result();

        SensitiveDataProcessor processor = new SensitiveDataProcessor(auditEvent);
        Result finalEvent = processor.process();
        assertEquals(auditEvent, finalEvent);
    }

    @Test
    public void sensitiveDataProcessorAddsMetadataIfSensitiveData() {
        Result auditEvent = new Result();
        auditEvent.put("username", "the name is smith");

        List<SensitiveDataHandler> handlers = new LinkedList<SensitiveDataHandler>();
        SensitiveDataHandler handler = new SensitiveDataHandler("name", "smith");
        handlers.add(handler);

        Config.setSensitiveDataHandlers(handlers);

        SensitiveDataProcessor processor = new SensitiveDataProcessor(auditEvent);
        Result originalEvent = (Result) auditEvent.clone();

        Result finalEvent = processor.process();

        // manually build the expected result without using any of the processor's methods
        Result expectedEvent = new Result();
        expectedEvent.put("username", "the name is smith");
        Map<String, Object> dotCased = new HashMap<String, Object>();
        Map<String, Object> pii = new HashMap<String, Object>();

        List<Map<String, Object>> dataList = new LinkedList<Map<String, Object>>();
        Map<String,Object> firstMatch = new HashMap<String, Object>();
        firstMatch.put("begin", 12);
        firstMatch.put("end", 17);
        firstMatch.put("label", "name");

        // continue building the nested result (with .cased and pii) from inside out,
        // eventually getting it to the right form
        dataList.add(firstMatch);
        pii.put("username", dataList);
        dotCased.put(".pii", pii);
        expectedEvent.put(".cased", dotCased);

        assertNotEquals(originalEvent, finalEvent);
        assertEquals(expectedEvent, finalEvent);

        Config.resetSensitiveDataHandlers();
    }

    @Test
    public void sensitiveDataProcessorAddsMetadataIfMultipleSensitiveDataInSameField() {
        Result auditEvent = new Result();
        auditEvent.put("username", "the name is smith and smith");

        List<SensitiveDataHandler> handlers = new LinkedList<SensitiveDataHandler>();
        SensitiveDataHandler handler = new SensitiveDataHandler("name", "smith");
        handlers.add(handler);

        Config.setSensitiveDataHandlers(handlers);

        SensitiveDataProcessor processor = new SensitiveDataProcessor(auditEvent);
        Result originalEvent = (Result) auditEvent.clone();

        Result finalEvent = processor.process();

        Result expectedEvent = new Result();
        expectedEvent.put("username", "the name is smith and smith");
        Map<String, Object> dotCased = new HashMap<String, Object>();
        Map<String, Object> pii = new HashMap<String, Object>();

        List<Map<String, Object>> dataList = new LinkedList<Map<String, Object>>();
        Map<String,Object> firstMatch = new HashMap<String, Object>();
        firstMatch.put("begin", 12);
        firstMatch.put("end", 17);
        firstMatch.put("label", "name");

        Map<String,Object> secondMatch = new HashMap<String, Object>();
        secondMatch.put("begin", 22);
        secondMatch.put("end", 27);
        secondMatch.put("label", "name");

        dataList.add(firstMatch);
        dataList.add(secondMatch);

        pii.put("username", dataList);
        dotCased.put(".pii", pii);
        expectedEvent.put(".cased", dotCased);

        assertNotEquals(originalEvent, finalEvent);
        assertEquals(expectedEvent, finalEvent);

        Config.resetSensitiveDataHandlers();
    }

    @Test
    public void sensitiveDataProcessorAddsMetadataIfSensitiveDataInMultipleFields() {
        Result auditEvent = new Result();
        auditEvent.put("username", "the name is smith");
        auditEvent.put("new_name", "the new name is also smith");

        List<SensitiveDataHandler> handlers = new LinkedList<SensitiveDataHandler>();
        SensitiveDataHandler handler = new SensitiveDataHandler("name", "smith");
        handlers.add(handler);

        Config.setSensitiveDataHandlers(handlers);

        SensitiveDataProcessor processor = new SensitiveDataProcessor(auditEvent);
        Result originalEvent = (Result) auditEvent.clone();

        Result finalEvent = processor.process();

        Result expectedEvent = new Result();
        expectedEvent.put("username", "the name is smith");
        expectedEvent.put("new_name", "the new name is also smith");

        Map<String, Object> dotCased = new HashMap<String, Object>();
        Map<String, Object> pii = new HashMap<String, Object>();

        List<Map<String, Object>> dataListOne = new LinkedList<Map<String, Object>>();
        Map<String,Object> firstMatch = new HashMap<String, Object>();
        firstMatch.put("begin", 12);
        firstMatch.put("end", 17);
        firstMatch.put("label", "name");
        dataListOne.add(firstMatch);

        List<Map<String, Object>> dataListTwo = new LinkedList<Map<String, Object>>();
        Map<String,Object> secondMatch = new HashMap<String, Object>();
        secondMatch.put("begin", 21);
        secondMatch.put("end", 26);
        secondMatch.put("label", "name");
        dataListTwo.add(secondMatch);

        pii.put("username", dataListOne);
        pii.put("new_name", dataListTwo);

        dotCased.put(".pii", pii);
        expectedEvent.put(".cased", dotCased);

        assertNotEquals(originalEvent, finalEvent);
        assertEquals(expectedEvent, finalEvent);

        Config.resetSensitiveDataHandlers();
    }

    @Test
    public void sensitiveDataProcessorAddsMetadataIfSensitiveDataInMultipleFieldsAndMultipleMatches() {
        Result auditEvent = new Result();
        auditEvent.put("username", "the name is smith and smith");
        auditEvent.put("new_name", "the new name is also smith and smith");

        List<SensitiveDataHandler> handlers = new LinkedList<SensitiveDataHandler>();
        SensitiveDataHandler handler = new SensitiveDataHandler("name", "smith");
        handlers.add(handler);

        Config.setSensitiveDataHandlers(handlers);

        SensitiveDataProcessor processor = new SensitiveDataProcessor(auditEvent);
        Result originalEvent = (Result) auditEvent.clone();

        Result finalEvent = processor.process();

        Result expectedEvent = new Result();
        expectedEvent.put("username", "the name is smith and smith");
        expectedEvent.put("new_name", "the new name is also smith and smith");

        Map<String, Object> dotCased = new HashMap<String, Object>();
        Map<String, Object> pii = new HashMap<String, Object>();

        // Build the first list, two matches that are associated the 'username' key
        List<Map<String, Object>> dataListOne = new LinkedList<Map<String, Object>>();
        Map<String,Object> firstMatch = new HashMap<String, Object>();
        firstMatch.put("begin", 12);
        firstMatch.put("end", 17);
        firstMatch.put("label", "name");

        Map<String,Object> secondMatch = new HashMap<String, Object>();
        secondMatch.put("begin", 22);
        secondMatch.put("end", 27);
        secondMatch.put("label", "name");

        // Add the matches to the list
        dataListOne.add(firstMatch);
        dataListOne.add(secondMatch);

        // Build the second list, two matches that are associated the 'new_name' key
        List<Map<String, Object>> dataListTwo = new LinkedList<Map<String, Object>>();
        Map<String,Object> thirdMatch = new HashMap<String, Object>();
        thirdMatch.put("begin", 21);
        thirdMatch.put("end", 26);
        thirdMatch.put("label", "name");

        Map<String,Object> fourthMatch = new HashMap<String, Object>();
        fourthMatch.put("begin", 31);
        fourthMatch.put("end", 36);
        fourthMatch.put("label", "name");

        // Add the matches to the list
        dataListTwo.add(thirdMatch);
        dataListTwo.add(fourthMatch);

        // Add the lists for each field
        pii.put("username", dataListOne);
        pii.put("new_name", dataListTwo);

        // Build the rest of the object
        dotCased.put(".pii", pii);
        expectedEvent.put(".cased", dotCased);

        assertNotEquals(originalEvent, finalEvent);
        assertEquals(expectedEvent, finalEvent);

        Config.resetSensitiveDataHandlers();
    }
}
