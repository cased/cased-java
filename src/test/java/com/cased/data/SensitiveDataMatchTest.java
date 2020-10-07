package com.cased.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


public class SensitiveDataMatchTest {
    @Test
    public void sensitiveDataMatchCanBeConstructedWithStartandEnd() {
        SensitiveDataMatch match = new SensitiveDataMatch(3, 10);
        assertEquals(3, match.start);
        assertEquals(10, match.end);
    }

    @Test
    public void sensitiveDataMatchesAreEqualBasedOnStartandEnd() {
        SensitiveDataMatch match1 = new SensitiveDataMatch(3, 10);
        SensitiveDataMatch match2 = new SensitiveDataMatch(3, 10);

        assertTrue(match1.equals(match1));
        assertTrue(match1.equals(match2));
        assertTrue(match2.equals(match1));
        assertEquals(match1, match2);
    }
}