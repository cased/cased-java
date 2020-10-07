package com.cased.data;

import java.util.regex.Pattern;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


public class SensitiveDataHandlerTest {
    @Test
    public void sensitiveDataHandlerCanBeConstructedWithLabel() {
        SensitiveDataHandler handler = new SensitiveDataHandler("my-label", "");
        assertEquals("my-label", handler.label);
    }

    @Test
    public void sensitiveDataHandlerCreatesPatternFromString() {
        SensitiveDataHandler handler = new SensitiveDataHandler("my-label", "test");
        Pattern pattern = Pattern.compile("test");
        assertEquals(pattern.pattern(), handler.pattern.pattern());
    }

    @Test
    public void sensitiveDataHandlerCanFindMatches() {
        SensitiveDataHandler handler = new SensitiveDataHandler("my-label", "test");
        String data = "regex test";
        List<SensitiveDataMatch> matches = handler.findMatches(data);

        List<SensitiveDataMatch> expected = new LinkedList<SensitiveDataMatch>();
        SensitiveDataMatch match = new SensitiveDataMatch(6, 10);
        expected.add(match);

        assertEquals(1, matches.size());

        assertEquals(expected, matches);
        assertEquals(expected.get(0).start, matches.get(0).start);
        assertEquals(expected.get(0).end, matches.get(0).end);
    }

    @Test
    public void sensitiveDataHandlerCanFindMultipleMatches() {
        SensitiveDataHandler handler = new SensitiveDataHandler("my-label", "test");
        String data = "regex test test";
        List<SensitiveDataMatch> matches = handler.findMatches(data);

        List<SensitiveDataMatch> expected = new LinkedList<SensitiveDataMatch>();
        SensitiveDataMatch match1 = new SensitiveDataMatch(6, 10);
        SensitiveDataMatch match2 = new SensitiveDataMatch(11, 15);

        expected.add(match1);
        expected.add(match2);

        assertEquals(2, matches.size());

        assertEquals(expected, matches);

        assertEquals(expected.get(0).start, matches.get(0).start);
        assertEquals(expected.get(0).end, matches.get(0).end);

        assertEquals(expected.get(1).start, matches.get(1).start);
        assertEquals(expected.get(1).end, matches.get(1).end);
    }

    @Test
    public void sensitiveDataHandlerReturnEmptyListWhenNoMatches() {
        SensitiveDataHandler handler = new SensitiveDataHandler("my-label", "test");
        String data = "nothing here";
        List<SensitiveDataMatch> matches = handler.findMatches(data);

        List<SensitiveDataMatch> expected = new LinkedList<SensitiveDataMatch>();
        assertEquals(expected, matches);
    }
}
