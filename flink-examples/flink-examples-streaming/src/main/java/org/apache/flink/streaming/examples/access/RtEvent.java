/*
 * Copyright 2018, Zetyun StreamTau All rights reserved.
 */

package org.apache.flink.streaming.examples.access;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class RtEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private long eventCommingTime;
    private long eventUpdateTime;
    private long eventSinkTime;

    private String traceId;

    private final Map<String, Object> headers = new HashMap<>();

    private byte[] payload;

    private RtMap metricMap;

    public RtEvent() {
        this.eventCommingTime = System.currentTimeMillis();
        this.metricMap = new RtMap();
    }

    public RtEvent(long eventCommingTime) {
        this.eventCommingTime = eventCommingTime;
        this.metricMap = new RtMap();
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP = new HashMap<>();

    static {
        PRIMITIVE_WRAPPER_MAP.put(Boolean.TYPE, Boolean.class);
        PRIMITIVE_WRAPPER_MAP.put(Byte.TYPE, Byte.class);
        PRIMITIVE_WRAPPER_MAP.put(Character.TYPE, Character.class);
        PRIMITIVE_WRAPPER_MAP.put(Short.TYPE, Short.class);
        PRIMITIVE_WRAPPER_MAP.put(Integer.TYPE, Integer.class);
        PRIMITIVE_WRAPPER_MAP.put(Long.TYPE, Long.class);
        PRIMITIVE_WRAPPER_MAP.put(Double.TYPE, Double.class);
        PRIMITIVE_WRAPPER_MAP.put(Float.TYPE, Float.class);
        PRIMITIVE_WRAPPER_MAP.put(Void.TYPE, Void.TYPE);
    }

    @SuppressWarnings("unchecked")
    private static  <T> Class<T> primitiveToWrapper(Class<?> type) {
        return (Class) PRIMITIVE_WRAPPER_MAP.getOrDefault(type, type);
    }

    public Object getHeader(String name) {
        return headers.get(name);
    }

    public Object getHeader(String name, Object defaultValue) {
        return headers.getOrDefault(name, defaultValue);
    }

    public Object getHeader(String name, Supplier<Object> defaultValueSupplier) {
        return Optional.ofNullable(headers.get(name)).orElseGet(defaultValueSupplier);
    }

    public <T> T getHeader(String name, Class<T> type) {
        type = primitiveToWrapper(type);
        return Optional.ofNullable(getHeader(name)).map(type::cast).orElse(null);
    }

    public <T> T getHeader(String name, Class<T> type, T defaultValue) {
        type = primitiveToWrapper(type);
        return Optional.ofNullable(getHeader(name)).map(type::cast).orElse(defaultValue);
    }

    public <T> T getHeader(String name, Class<T> type, Supplier<T> defaultValueSupplier) {
        type = primitiveToWrapper(type);
        return Optional.ofNullable(getHeader(name)).map(type::cast).orElseGet(defaultValueSupplier);
    }

    public void setHeader(String name, Object value) {
        headers.put(name, value);
    }

    public Object removeHeader(String name) {
        return headers.remove(name);
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void clearHeaders() {
        headers.clear();
    }

    public void clearValue() {
        metricMap.clear();
    }

    public void resetMetricMap() {
        metricMap = new RtMap();
    }

    /**
     * Creates event from ingest time and payload.
     *
     * @param ingestTime ingest time
     * @param payload payload
     * @return event
     */
    public static RtEvent payloadOf(long ingestTime, byte[] payload) {
        RtEvent event = new RtEvent(ingestTime);
        event.setPayload(payload);
        return event;
    }

    public static RtEvent payloadOf(byte[] payload) {
        return payloadOf(System.currentTimeMillis(), payload);
    }

    /**
     * Deprecated function, keep for binary compatibility.
     *
     * @deprecated in favor of {@link #payloadOf(byte[])}
     * @param object object
     * @return event
     */
    @Deprecated
    public static RtEvent payloadOf(Object object) {
        return payloadOf((byte[])object);
    }

    public long getEventCommingTime() {
        return eventCommingTime;
    }

    public void setEventCommingTime(long eventCommingTime) {
        this.eventCommingTime = eventCommingTime;
    }

    public long getEventUpdateTime() {
        return eventUpdateTime;
    }

    public void setEventUpdateTime(long eventUpdateTime) {
        this.eventUpdateTime = eventUpdateTime;
    }

    public long getEventSinkTime() {
        return eventSinkTime;
    }

    public void setEventSinkTime(long eventSinkTime) {
        this.eventSinkTime = eventSinkTime;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    /**
     * Deprecated function, keep for binary compatibility.
     *
     * @deprecated in favor of {@link #payloadOf(byte[])}
     * @param payload payload object, must be byte array
     * @return this event
     */
    @Deprecated
    public RtEvent setPayload(Object payload) {
        setPayload((byte[])payload);
        return this;
    }

    public RtMap getMetricMap() {
        return metricMap;
    }

    public void setMetricMap(RtMap metricMap) {
        this.metricMap = metricMap;
    }

    /**
     * set value.
     * @param key  key
     * @param value  value
     */
    public void setValue(String key, Object value) {
        if (this.metricMap == null) {
            this.metricMap = new RtMap();
        }
        this.metricMap.put(key, value);
    }

    /**
     * get value.
     * @param key key
     * @return  value object
     */
    public Object getValue(String key) {
        if (this.metricMap != null) {
            return this.metricMap.get(key);
        } else {
            return null;
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or defaultValue
     * if this record contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     */
    public Object getValueOrDefault(String key, Object defaultValue) {
        if (metricMap == null) {
            return defaultValue;
        }
        return this.metricMap.getOrDefault(key, defaultValue);
    }

    /**
     * remove value.
     * @param key key
     * @return removed value
     */
    public Object removeValue(String key) {
        if (this.metricMap != null) {
            return this.metricMap.remove(key);
        } else {
            return null;
        }
    }

    private void createMap() {
        if (metricMap == null) {
            metricMap = new RtMap();
        }
    }

    /**
     * Copy InputEvent to this.
     * @param inputEvent inputEvent
     */
    public void copyFrom(final RtEvent inputEvent) {
        this.eventCommingTime = inputEvent.getEventCommingTime();
        this.eventUpdateTime = inputEvent.getEventCommingTime();
        this.eventSinkTime = inputEvent.getEventSinkTime();
        this.traceId = inputEvent.traceId;
        this.headers.clear();
        this.headers.putAll(inputEvent.headers);
        this.metricMap = Optional.ofNullable(inputEvent.metricMap).map(RtMap::new).orElse(null);
    }

    /**
     * Clear holding data and copy all data from data as holding data.
     *
     * @param data record data to copy from
     */
    public void copyFrom(Map<String, ?> data) {
        createMap();
        metricMap.clear();
        metricMap.putAll(data);
    }

    public void putAll(Map<String, Object> data) {
        createMap();
        metricMap.putAll(data);
    }

    public void putAll(RtEvent data) {
        createMap();
        metricMap.putAll(data.getMetricMap());
    }

    /**
     * check input object equal to this or not.
     * @param input inputObj
     * @return ture or false
     */
    public boolean isEqual(Object input) {
        if (this == input) {
            return true;
        } else if (this.getClass() != input.getClass()) {
            return false;
        } else {
            RtEvent inputEvent = (RtEvent) input;

            if (this.eventCommingTime != inputEvent.eventCommingTime) {
                return false;
            } else {
                return Objects.equals(inputEvent.getMetricMap(), getMetricMap());
            }
        }
    }

    @Override
    public String toString() {
        return this.metricMap.toString();
    }
}
