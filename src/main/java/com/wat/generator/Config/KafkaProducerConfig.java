package com.wat.generator.Config;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {
    
    @Autowired
    private KafkaProperties kafkaProperties;
    
    
    @Autowired
    private SslBundles sslBundles;
    
    @Bean
    public ProducerFactory<byte[], byte[]> producerFactory() {
        Map<String, Object> props = kafkaProperties.buildProducerProperties(sslBundles);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 10485760);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<byte[], byte[]> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
