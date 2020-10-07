package com.cased.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;


public class SensitiveDataRangeTest {
    @Test
    public void sensitiveDataRangeCanBeConstructedWithRelevantFields() {
        SensitiveDataRange range = new SensitiveDataRange("test-label", 3, 10);
        assertEquals("test-label", range.label);
        assertEquals(3, range.begin);
        assertEquals(10, range.end);
    }

    @Test
    public void sensitiveDataRangeCanBeConvertedToMap() {
        SensitiveDataRange range = new SensitiveDataRange("test-label", 3, 10);
        Map<String, Object> rangeMap = new HashMap<String, Object>();
        rangeMap.put("label", "test-label");
        rangeMap.put("begin", 3);
        rangeMap.put("end", 10);

        assertEquals(rangeMap, range.toMap());
    }

    @Test
    public void sensitiveDataRangesAreEqualBasedOnFields() {
        SensitiveDataRange range1 = new SensitiveDataRange("test-label", 3, 10);
        SensitiveDataRange range2 = new SensitiveDataRange("test-label", 3, 10);
        SensitiveDataRange range3 = new SensitiveDataRange("test-label", 4, 10);

        assertTrue(range1.equals(range1));
        assertTrue(range1.equals(range2));
        assertTrue(range2.equals(range1));
        assertEquals(range1, range2);
        assertNotEquals(range3, range2);
        assertNotEquals(range3, range1);
        assertFalse(range3.equals(range1));
        assertFalse(range1.equals(range3));
    }
}