package com.cased.api;

import java.util.Map;

import javax.swing.plaf.synth.SynthMenuBarUI;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.cased.Event;
import com.cased.http.CasedHttpResponse;
import com.cased.http.HttpResponse;
import com.cased.http.Requestor;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MockResource extends Resource {
    public MockResource(Requestor requestor) {
        super(requestor, "testresource");
    }
}

//public static Requestor getRequestor() {
//    Requestor mockRequestor = mock(Requestor.class);
//
//   Map<String, List<String>> headers = new HashMap<String, List<String>>();
//   HttpResponse mockHttpResponse = new CasedHttpResponse("{}", headers);
//
//   try {
//       when(mockRequestor.get("/")).thenReturn(mockHttpResponse);
//    } catch (Exception e) {
        //
//    }
//
//    return mockRequestor;
//}

public class ResourceTest {
    @Test
    public void resourceHasARequestor() throws Exception {
        Requestor mockRequestor = mock(Requestor.class);
        assertEquals(mockRequestor, new MockResource(mockRequestor).requestor);
    }

    @Test
    public void resourceHasResourceName() {
        Requestor mockRequestor = mock(Requestor.class);
        Resource mockResource = new MockResource(mockRequestor);

        assertEquals("testresource", mockResource.getResourceName());
    }
}