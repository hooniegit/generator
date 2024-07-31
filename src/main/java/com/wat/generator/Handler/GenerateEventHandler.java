package com.wat.generator.Handler;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;

import com.google.gson.Gson;
import com.lmax.disruptor.WorkHandler;
import com.wat.generator.Class.RAW;
import com.wat.generator.Event.GenerateEvent;

public class GenerateEventHandler implements WorkHandler<GenerateEvent> {
    @Override
    public void onEvent(GenerateEvent event) {
    	// [Get] RAW & KafkaProducer
    	RAW raw = event.getRaw();
    	KafkaTemplate<byte[], byte[]> producer = event.getProducer();
    	
    	// [Set] Kafka Topic
    	String TOPIC = "DemoTopic64";
    	
	    try {
	    	// [Serialize] RAW to JSON
	        Gson gson = new Gson();
	        String userJson = gson.toJson(raw);
	        byte[] userBytes = userJson.getBytes(StandardCharsets.UTF_8);

	        // [Create] ProducerRecord
	        ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(TOPIC, null, userBytes);
	        
	        // [Send] ProducerRecord to Apache Kafka Server
	        producer.send(record);
//	        producer.send(record, new Callback() {
//	            @Override
//	            public void onCompletion(RecordMetadata metadata, Exception exception) {
//	                if (exception == null) {
//	                } else {
//	                    exception.printStackTrace();
//	                }
//	            }
//	        });
	    } catch (Exception e) {
	    }
    }
}
