package com.cased.api;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class ResultTest {

    @Test
    public void resultExtendsTheRightMap() {
        Result result = new Result();
        assertTrue(result instanceof HashMap);
    }
}