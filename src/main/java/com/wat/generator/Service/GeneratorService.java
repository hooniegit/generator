package com.wat.generator.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wat.generator.Factory.ConcurrentKafkaProducerFactory;
import com.wat.generator.Module.DataGenerator;

@Service
public class GeneratorService {

    private final ScheduledExecutorService scheduler;
    private final RingBufferService[] ringBufferServices;
    private final ConcurrentKafkaProducerFactory producerFactory;

    @Autowired
    public GeneratorService(ConcurrentKafkaProducerFactory producerFactory) {
        this.scheduler = Executors.newScheduledThreadPool(64);
        this.ringBufferServices = new RingBufferService[64];
        this.producerFactory = producerFactory;

        for (int i = 0; i < 64; i++) {
            ringBufferServices[i] = new RingBufferService();
        }
    }

    public void startDataGeneration() {
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>");
    	
        final int[] cnt = {1};

        Runnable task = () -> {
            ThreadLocal<RingBufferService> threadLocalRingBuffer = ThreadLocal.withInitial(() -> {
                int index = ThreadLocalRandom.current().nextInt(64);
                return ringBufferServices[index];
            });
            
        	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> 2");
        	
        	try {
            for (int i = 0; i < 3; i++) {
                String location = "LOC" + String.format("%03d", i + 1);
                String threadId = String.valueOf(i);
                scheduler.submit(new DataGenerator(location, ringBufferServices[i], producerFactory.getProducer(threadId)));
            }
        	} catch (Exception ex) {
        		System.out.println(ex);
        	}
            System.out.printf("Data generation completed. : %s%n", cnt[0]);
            cnt[0] += 1;
        };
    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>");
        long initialDelay = 0;
        long period = 1;
        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
    }
}
