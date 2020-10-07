package com.cased;

import java.util.LinkedList;
import java.util.List;

import com.cased.http.CasedHeaders;
import com.cased.http.CasedHttpResponse;
import com.cased.http.Requestor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

public class ExportTest {
    @Test
    public void fetchExportThrowsExceptionIfNoApiKey() throws Exception {
        Cased cased = new Cased();
        assertThrows(Exception.class, () -> cased.Export.fetch("123"));
    }

    public void exportResourceHasARequestor() throws Exception {
        Requestor mockRequestor = mock(Requestor.class);
        assertEquals(mockRequestor, new Export(mockRequestor).requestor);
    }

    @Test
    public void exportResourceHasResourceName() {
        Requestor mockRequestor = mock(Requestor.class);
        Export Export = new Export(mockRequestor);

        assertEquals("exports", Export.getResourceName());
    }

    @Test
    public void exportFetchCallsRequestorGetCorrectly() throws Exception {
        Requestor mockRequestor = mock(Requestor.class);
        Export mockExport = new Export(mockRequestor);
        CasedHeaders headers = new CasedHeaders();

        when(mockRequestor.get("/exports/123")).thenReturn(new CasedHttpResponse("{}", headers));
        mockExport.fetch("123");
        verify(mockRequestor).get("/exports/123");
    }

    @Test
    public void exportListCallsRequestorGetCorrectly() throws Exception {
        Requestor mockRequestor = mock(Requestor.class);
        Export mockExport = new Export(mockRequestor);
        CasedHeaders headers = new CasedHeaders();
        String linkHeaderString = "<https://api.cased.com/exports?page=1>; rel=\"first\", <https://api.cased.com/exports?page=5>; rel=\"last\", <https://api.cased.com/exports?page=2>; rel=\"prev\", <https://api.cased.com/exports?page=4; rel=\"next\"";

        List<String> linkValues = new LinkedList<String>();
        linkValues.add(linkHeaderString);
        headers.put("Links", linkValues);

        when(mockRequestor.get("/exports?per_page=25")).thenReturn(new CasedHttpResponse("{}", headers));
        mockExport.list();
        verify(mockRequestor).get("/exports?per_page=25");
    }
}