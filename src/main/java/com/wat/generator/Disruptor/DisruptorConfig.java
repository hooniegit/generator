package com.wat.generator.Disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DisruptorConfig {

    @Bean
    public Disruptor<GenerateEvent> disruptor() {
        int bufferSize = 1024;

        Disruptor<GenerateEvent> disruptor = new Disruptor<>(
        		GenerateEvent::new, 
        		bufferSize, 
        		DaemonThreadFactory.INSTANCE,
        		ProducerType.SINGLE,
        		new SleepingWaitStrategy());
        GenerateEventHandler handlerOne = new GenerateEventHandler();
        ClearingEventHandler handlerTwo = new ClearingEventHandler();
        
        disruptor.handleEventsWith(handlerOne)
        		 .then(handlerTwo);
        disruptor.start();
        
        handlerOne = null;
        handlerTwo = null;
        
        return disruptor;
    }

    @Bean
    public RingBuffer<GenerateEvent> ringBuffer(Disruptor<GenerateEvent> disruptor) {
    	System.out.println("[Config] Will Return Ring Buffer");
        return disruptor.getRingBuffer();
    }

    @Bean
    public GenerateEventProducer eventProducer(RingBuffer<GenerateEvent> ringBuffer) {
    	System.out.println("[Config] Will Return EventProducer");
        return new GenerateEventProducer(ringBuffer);
    }
}


