package com.wat.generator.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    private final List<KafkaTemplate<byte[], byte[]>> producerList;
    private final AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    public DataGenerator(GenerateEventProducer eventProducer, ApplicationContext context) {
        this.eventProducer = eventProducer;
        this.producerList = new ArrayList<>();
        for (int i = 0; i < 128; i++) {
        	this.producerList.add(context.getBean(KafkaTemplate.class));
        }
    }

    @PostConstruct
    public void run() {
    	while (true) {
	    	for (int location = 0; location < 64; location++) {
	    		
		        List<RAW_BODY> bodyList = new ArrayList<>();
		        
		        for (int source = 1; source <= 20; source++) {
		        	String SOURCE =  "SRC" + String.format("%03d", source);
		        	
			        for (int tag = 1; tag <= 3000; tag++) { 	
			            RAW_BODY body = new RAW_BODY();
			            body.setSOURCE(SOURCE);
			            body.setTAG(SOURCE + ".T" + String.format("%04d", tag));
			            body.setVALUE("8000");
			            body.setTIMESTAMP(String.valueOf(System.currentTimeMillis()));
			            bodyList.add(body);       
			        }
			        
			        RAW raw = new RAW();
			        RAW_HEADER header = new RAW_HEADER();
			        header.setLOCATION("LOC" + String.format("%02d", location + 1));
			        raw.setHEADER(header);
			        raw.setBODY(bodyList);
			        
                    int index = counter.getAndIncrement() % producerList.size();
                    eventProducer.onData(raw, producerList.get(index));
			        
			        System.out.println("[Module] Data Generated for LOC " + location);
			        
			        try {
			        	Thread.sleep(200);
			        } catch (Exception ex) {}       
			        
		        }

	    	}
    	}
    }
}

