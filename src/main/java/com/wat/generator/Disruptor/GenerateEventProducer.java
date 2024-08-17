package com.wat.generator.Disruptor;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;

import org.springframework.kafka.core.KafkaTemplate;

import com.wat.generator.Class.RAW;

public class GenerateEventProducer
{
    private final RingBuffer<GenerateEvent> ringBuffer;

    public GenerateEventProducer(RingBuffer<GenerateEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    private final EventTranslatorTwoArg<GenerateEvent, RAW, KafkaTemplate<byte[], byte[]>> TRANSLATOR =
        new EventTranslatorTwoArg<GenerateEvent, RAW, KafkaTemplate<byte[], byte[]>>()
        {
            @Override
            public void translateTo(GenerateEvent event, long sequence, RAW raw, KafkaTemplate<byte[], byte[]> producer)
            {
                event.setRaw(raw);
                event.setProducer(producer);
            }
        };

    public void onData(RAW raw, KafkaTemplate<byte[], byte[]> producer)
    {
        ringBuffer.publishEvent(TRANSLATOR, raw, producer);
    }
}