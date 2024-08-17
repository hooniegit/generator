package com.wat.generator.Disruptor;

import org.springframework.kafka.core.KafkaTemplate;

import com.wat.generator.Class.RAW;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateEvent {
    private RAW raw;
    private KafkaTemplate<byte[], byte[]> producer;
    
    public void clear() {
    	this.raw = null;
    	this.producer = null;
    }
}