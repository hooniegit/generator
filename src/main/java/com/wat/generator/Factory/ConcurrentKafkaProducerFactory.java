package com.wat.generator.Factory;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConcurrentKafkaProducerFactory {
    private final ConcurrentHashMap<String, KafkaTemplate<byte[], byte[]>> producerMap;
    private final KafkaTemplate<byte[], byte[]> kafkaTemplate;

    @Autowired
    public ConcurrentKafkaProducerFactory(KafkaTemplate<byte[], byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.producerMap = new ConcurrentHashMap<>();
    }

    public KafkaTemplate<byte[], byte[]> getProducer(String threadId) {
        return producerMap.computeIfAbsent(threadId, k -> kafkaTemplate);
    }
}
