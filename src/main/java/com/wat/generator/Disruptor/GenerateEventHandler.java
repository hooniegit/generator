package com.wat.generator.Disruptor;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

import com.google.gson.Gson;
import com.lmax.disruptor.EventHandler;
import com.wat.generator.Class.RAW;

public class GenerateEventHandler implements EventHandler<GenerateEvent> {
    @Override
    public void onEvent(GenerateEvent event, long sequence, boolean endOfBatch) {
    	RAW raw = event.getRaw();
    	KafkaTemplate<byte[], byte[]> producer = event.getProducer();
    	String TOPIC = "WAT";
    	
	    try {
	        Gson gson = new Gson();
	        String userJson = gson.toJson(raw);
	        byte[] userBytes = userJson.getBytes(StandardCharsets.UTF_8);

	        ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(TOPIC, null, userBytes);
	        
	        producer.send(record);
	        System.out.println("[Handler] Data Sent");
	    } catch (Exception e) {}
    }
}
