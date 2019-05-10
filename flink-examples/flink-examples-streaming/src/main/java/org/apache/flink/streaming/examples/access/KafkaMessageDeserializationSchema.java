/*
 * Copyright 2018, Zetyun StreamTau All rights reserved.
 */

package org.apache.flink.streaming.examples.access;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;

public class KafkaMessageDeserializationSchema implements KeyedDeserializationSchema<RtEvent> {
    private static final KafkaMessageDeserializationSchema INSTANCE = new KafkaMessageDeserializationSchema();

    public static KafkaMessageDeserializationSchema getInstance() {
        return INSTANCE;
    }

    @Override
    public RtEvent deserialize(byte[] messageKey, byte[] message, String topic, int partition, long offset) {
        RtEvent event = new RtEvent();
        event.setHeader("key", messageKey);
        event.setHeader("topic", topic);
        event.setHeader("partition", partition);
        event.setHeader("offset", offset);
        event.setPayload(message);
        return event;
    }

    @Override
    public boolean isEndOfStream(RtEvent nextElement) {
        return false;
    }

    @Override
    public TypeInformation<RtEvent> getProducedType() {
        return TypeInformation.of(RtEvent.class);
    }
}
