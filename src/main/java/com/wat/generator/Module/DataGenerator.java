package com.wat.generator.Module;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.wat.generator.Class.RAW;
import com.wat.generator.Class.RAW_BODY;
import com.wat.generator.Class.RAW_HEADER;
import com.wat.generator.Disruptor.GenerateEventProducer;

import jakarta.annotation.PostConstruct;

@Service
public class DataGenerator implements Runnable {
    private final GenerateEventProducer eventProducer;
    private final KafkaTemplate<byte[], byte[]> kafkaProducer;

    @Autowired
    public DataGenerator(GenerateEventProducer eventProducer, KafkaTemplate<byte[], byte[]> kafkaProducer) {
        this.eventProducer = eventProducer;
        this.kafkaProducer = kafkaProducer;
    }

    @PostConstruct
    public void run() {
    	while (true) {
	    	for (int i = 0; i < 64; i++) {
	    		
		        List<RAW_BODY> bodyList = new ArrayList<>();
		        
		        for (int source = 1; source <= 64; source++) {
		        	String SOURCE =  "SRC" + String.format("%03d", source);
			        for (int tag = 1; tag <= 1000; tag++) { 	
			            RAW_BODY body = new RAW_BODY();
			            body.setSOURCE(SOURCE);
			            body.setTAG(SOURCE + ".T" + String.format("%04d", tag));
			            body.setVALUE("1234567");
			            body.setTIMESTAMP(String.valueOf(System.currentTimeMillis()));
			            bodyList.add(body);       
			        }
		        }
		        
		        RAW raw = new RAW();
		        RAW_HEADER header = new RAW_HEADER();
		        header.setLOCATION("LOC" + String.format("%02d", i + 1));
		        raw.setHEADER(header);
		        raw.setBODY(bodyList);
		
		        eventProducer.onData(raw, kafkaProducer);
		        
		        System.out.println("[Module] Data Generated for LOC " + i);
		        
		        try {
		        	Thread.sleep(400);
		        } catch (Exception ex) {
		        	
		        }
	    	}
    	}
    }
}

