package com.cased.data;

public class SensitiveDataMatch {
    public final int start;
    public final int end;

    public SensitiveDataMatch(final int start, final int end) {
        this.start = start;
        this.end = end;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof SensitiveDataMatch)) {
            return false;
        }

        SensitiveDataMatch s = (SensitiveDataMatch) o;

        return ((start == s.start) && (end == s.end));

    }
}