/*
 * Copyright 2018, Zetyun StreamTau All rights reserved.
 */

package org.apache.flink.streaming.examples.access;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RtMap extends HashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1L;

    public RtMap() {
        super(64);
    }

    public RtMap(Map<String, Object> m) {
        putAll(m);
    }
}
