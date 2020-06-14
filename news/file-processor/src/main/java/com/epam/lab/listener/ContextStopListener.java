package com.epam.lab.listener;

import com.epam.lab.reader.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ContextStopListener {

    @Autowired
    private ReaderService readerService;

    @EventListener
    public void onStopContext(ContextClosedEvent event) {
        readerService.shutdownExecutor();
    }
}
