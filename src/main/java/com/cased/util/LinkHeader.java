package com.cased.util;

public class LinkHeader {
    private String firstPage;
    private String lastPage;
    private String nextPage;
    private String previousPage;

    public LinkHeader(String rawLinkHeader) {
        parseLinkHeader(rawLinkHeader);
    }

    public String getFirstPage() {
        return firstPage;
    }

    public String getLastPage() {
        return lastPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    private void parseLinkHeader(String linkHeader) {
        String[] eachLink = linkHeader.split(",");
        for (String link : eachLink) {
            String[] parts = link.split(";");
            if (parts.length < 2) {
                continue;
            }

            String url = parts[0].trim();
            url = url.replace("<", "").replace(">", "");

            String rel = parts[1].trim();
            rel = rel.replace("rel=", "").replace("\"", "");

            if (rel.equals("first")) {
                firstPage = url;
            } else if (rel.equals("last")) {
                lastPage = url;
            } else if (rel.equals("prev")) {
                previousPage = url;
            } else if (rel.equals("next")) {
                nextPage = url;
            }
        }
    }
}
