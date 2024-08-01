package com.example.eventhub.service;


import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventProcessorClient;
import com.azure.messaging.eventhubs.EventProcessorClientBuilder;
import com.azure.messaging.eventhubs.models.ErrorContext;
import com.azure.messaging.eventhubs.models.EventContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class EventHubConsumerListener implements ServletContextListener {

    private EventProcessorClient eventProcessorClient;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        eventProcessorClient = new EventProcessorClientBuilder()
            .connectionString("<Your Event Hub Connection String>", "<Your Event Hub Name>")
            .consumerGroup("$Default") // or your custom consumer group
            .processEvent(this::processEvent)
            .processError(this::processError)
            .buildEventProcessorClient();

        // Start processing
        eventProcessorClient.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (eventProcessorClient != null) {
            eventProcessorClient.stop();
        }
    }

    private void processEvent(EventContext eventContext) {
        EventData eventData = eventContext.getEventData();
        System.out.printf("Received event: %s%n", eventData.getBodyAsString());
    }

    private void processError(ErrorContext errorContext) {
        Throwable error = errorContext.getThrowable();
        System.err.printf("Error occurred: %s%n", error.getMessage());
    }
}
