package com.wat.generator.Module;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;

import com.wat.generator.Class.RAW;
import com.wat.generator.Class.RAW_BODY;
import com.wat.generator.Class.RAW_HEADER;
import com.wat.generator.Service.RingBufferService;

public class DataGenerator implements Runnable {
    private final String location;
    private final RingBufferService ringBufferService;
    private final KafkaTemplate<byte[], byte[]> producer;

    public DataGenerator(String location, RingBufferService ringBufferService, KafkaTemplate<byte[], byte[]> producer) {
        this.location = location;
        this.ringBufferService = ringBufferService;
        this.producer = producer;
    }

    @Override
    public void run() {
        // [Create] List<RAW_BODY>
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
    	// [Create] RAW
        RAW raw = new RAW();
        RAW_HEADER header = new RAW_HEADER();
        header.setLOCATION(location);
        raw.setHEADER(header);
        raw.setBODY(bodyList);

        // [Transfer] RAW & KafkaProducer
        ringBufferService.produce(raw, producer);
    }
}

