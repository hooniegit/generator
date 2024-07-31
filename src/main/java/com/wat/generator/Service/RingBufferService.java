package com.wat.generator.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.wat.generator.Class.RAW;
import com.wat.generator.Event.GenerateEvent;
import com.wat.generator.Handler.GenerateEventHandler;

public class RingBufferService {
    private final RingBuffer<GenerateEvent> ringBuffer;

    public RingBufferService() {
    	// [Define] LMAX Disruptor
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        Disruptor<GenerateEvent> disruptor = new Disruptor<>(
                GenerateEvent::new,
                1024,
                threadFactory,
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );

        // [Define] WorkHandler[]
        WorkHandler<GenerateEvent>[] handlers = new GenerateEventHandler[10];
        for (int i = 0; i < handlers.length; i++) {
            handlers[i] = new GenerateEventHandler();
        }

        // [Define] RingBuffer
        disruptor.handleEventsWithWorkerPool(handlers);
        disruptor.start();
        this.ringBuffer = disruptor.getRingBuffer();
    }

    // [Task] Create Event & Run Handler Tasks
    public void produce(RAW raw, KafkaTemplate<byte[], byte[]> producer) {
        ringBuffer.publishEvent((event, sequence) -> {
        	event.setRaw(raw);
        	event.setProducer(producer);
        });
    }
}
