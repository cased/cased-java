package com.cased.data;

import java.util.HashMap;
import java.util.Map;

public class SensitiveDataRange {
    public final String label;
    public final int begin;
    public final int end;

    public SensitiveDataRange(final String label, final int begin, final int end) {
        this.label = label;
        this.begin = begin;
        this.end = end;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("label", label);
        map.put("begin", begin);
        map.put("end", end);

        return map;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof SensitiveDataRange)) {
            return false;
        }

        SensitiveDataRange s = (SensitiveDataRange) o;

        return ((label == s.label) && (begin == s.begin) && (end == s.end));
    }
}