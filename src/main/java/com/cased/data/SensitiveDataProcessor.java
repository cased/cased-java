package com.cased.data;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.cased.Config;
import com.cased.api.Result;

public class SensitiveDataProcessor {
    Result auditEvent;
    List<SensitiveDataHandler> handlers;

    public SensitiveDataProcessor(final Result auditEvent) {
        this.auditEvent = auditEvent;
        this.handlers = Config.getSensitiveDataHandlers();
    }

    public Result process() {
        final Map<String, List<SensitiveDataRange>> sensitiveDataRanges = new HashMap<String, List<SensitiveDataRange>>();

        for (final SensitiveDataHandler handler : handlers) {
            final Map<String, List<SensitiveDataRange>> ranges = rangesFromEvent(handler);
            sensitiveDataRanges.putAll(ranges);
        }

        if (sensitiveDataRanges.isEmpty()) {
            // There we no sensitive data matches, so just return the original event
            return auditEvent;
        } else {
            final Result updatedAuditEvent = addRangesToEvent(sensitiveDataRanges);
            return updatedAuditEvent;
        }
    }

    protected Map<String, List<SensitiveDataRange>> rangesFromEvent(final SensitiveDataHandler handler) {
        final Map<String, List<SensitiveDataRange>> sensitiveDataRanges = new HashMap<String, List<SensitiveDataRange>>();

        for (final Map.Entry<String, Object> entry : auditEvent.entrySet()) {
            final String key = entry.getKey();
            final Object value = entry.getValue();

            final List<SensitiveDataMatch> matches = handler.findMatches(value.toString());

            for (final SensitiveDataMatch match : matches) {
                final SensitiveDataRange range = new SensitiveDataRange(handler.label, match.start, match.end);
                if (sensitiveDataRanges.get(key) == null) {
                    // The first match for this key, so create a new list of ranges for this key
                    // and add the range.
                    final List<SensitiveDataRange> rangeList = new LinkedList<SensitiveDataRange>();
                    rangeList.add(range);
                    sensitiveDataRanges.put(key, rangeList);
                } else {
                    // The key already exists, so just the range to the list
                    sensitiveDataRanges.get(key).add(range);
                }
            }
        }

        return sensitiveDataRanges;
    }

    protected Result addRangesToEvent(final Map<String, List<SensitiveDataRange>> ranges) {
        // The map (which will be serialied to JSON) that includes references to
        // all the sensitive data/PII in this audit event
        // For example:
        // {
        // "email": [{"begin": 0, "end": 19, "label": "email"}],
        // "phone": [{"begin": 0, "end": 12, "label": "phone"}],
        // }
        final Map<String, List<Map<String, Object>>> sensitiveData = new HashMap<String, List<Map<String, Object>>>();

        for (final Map.Entry<String, List<SensitiveDataRange>> entry : ranges.entrySet()) {
            final String key = entry.getKey();
            final List<SensitiveDataRange> values = entry.getValue();

            final List<Map<String, Object>> dataList = new LinkedList<Map<String, Object>>();
            for (final SensitiveDataRange value : values) {
                dataList.add(value.toMap());
            }

            sensitiveData.put(key, dataList);
        }

        if (auditEvent.get(".cased") == null) {
            // Theres .cased object yet, so create it and set it to an empty HashMap
            auditEvent.put(".cased", new HashMap<String, Object>());
        }

        // Add the data to the .cased, and return the event
        final HashMap<String, Object> dotCased = (HashMap<String, Object>) auditEvent.get(".cased");
        dotCased.put(".pii", sensitiveData);

        return auditEvent;
    }
}