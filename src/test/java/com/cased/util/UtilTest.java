package com.cased.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UtilTest {

    @Test
    public void parseLinkHeaderParsesALinkHeaderString() throws Exception {
        String linkHeaderString = "<https://api.cased.com/events?page=1>; rel=\"first\", <https://api.cased.com/events?page=5>; rel=\"last\", <https://api.cased.com/events?page=2>; rel=\"prev\", <https://api.cased.com/events?page=4; rel=\"next\"";

        LinkHeader linkHeader = new LinkHeader(linkHeaderString);

        assertEquals("https://api.cased.com/events?page=1", linkHeader.getFirstPage());
        assertEquals("https://api.cased.com/events?page=5", linkHeader.getLastPage());
        assertEquals("https://api.cased.com/events?page=2", linkHeader.getPreviousPage());
        assertEquals("https://api.cased.com/events?page=4", linkHeader.getNextPage());
    }

    @Test
    public void parseLinkHeaderParsesALinkHeaderStringWithJustFirstAndLast() throws Exception {
        String linkHeaderString = "<https://api.cased.com/events?page=1>; rel=\"first\", <https://api.cased.com/events?page=5>; rel=\"last\"";
        LinkHeader linkHeader = new LinkHeader(linkHeaderString);

        assertEquals("https://api.cased.com/events?page=1", linkHeader.getFirstPage());
        assertEquals("https://api.cased.com/events?page=5", linkHeader.getLastPage());
    }
}