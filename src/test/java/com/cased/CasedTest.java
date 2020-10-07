package com.cased;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CasedTest {
    @Test
    public void eventResourceCanBeAccessedThroughCased() {
        Cased cased = new Cased();
        assertEquals(Event.class, cased.Event.getClass());
    }

    @Test
    public void exportResourceCanBeAccessedThroughCased() {
        Cased cased = new Cased();
        assertEquals(Export.class, cased.Export.getClass());
    }
}