package com.wat.generator.Disruptor;

import com.lmax.disruptor.EventHandler;

public class ClearingEventHandler implements EventHandler<GenerateEvent>
{
    @Override
    public void onEvent(GenerateEvent event, long sequence, boolean endOfBatch)
    {
        event.clear(); 
    }
}
