package com.cased.data;

import java.util.regex.Pattern;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;


public class SensitiveDataHandler {
    public String label;
    public Pattern pattern;

    public SensitiveDataHandler(final String label, final String strPattern) {
        this.label = label;
        this.pattern = Pattern.compile(strPattern);
    }

    /**
     * Given a string of data, return a List of SensitiveDataMatch objects.
     *
     */
    public List<SensitiveDataMatch> findMatches(final String data) {
        Matcher matcher = pattern.matcher(data);

        List<SensitiveDataMatch> results = new LinkedList<SensitiveDataMatch>();

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            SensitiveDataMatch match = new SensitiveDataMatch(start, end);
            results.add(match);
        }

        return results;
    }
}

