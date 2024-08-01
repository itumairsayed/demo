package com.example.eventhub.service;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventProcessorClient;
import com.azure.messaging.eventhubs.EventProcessorClientBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.eventhubs.models.ErrorContext;
import com.azure.messaging.eventhubs.models.EventContext;

public class EventHubConsumer {

    private static final String CONNECTION_STRING = "<Your Event Hub Connection String>";
    private static final String EVENT_HUB_NAME = "<Your Event Hub Name>";
    private static final String CONSUMER_GROUP = "$Default"; // Default consumer group or your custom group

    public static void main(String[] args) {
        EventProcessorClient eventProcessorClient = new EventProcessorClientBuilder()
            .connectionString(CONNECTION_STRING, EVENT_HUB_NAME)
            .consumerGroup(CONSUMER_GROUP)
            .processEvent(EventHubConsumer::processEvent)
            .processError(EventHubConsumer::processError)
            .buildEventProcessorClient();

        // Start processing
        eventProcessorClient.start();

        // Add a shutdown hook to stop the client gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            eventProcessorClient.stop();
            System.out.println("EventProcessorClient stopped.");
        }));

        // Keep the application running
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Application interrupted.");
        }
    }

    private static void processEvent(EventContext eventContext) {
        EventData eventData = eventContext.getEventData();
        // Handle the event
        System.out.printf("Received event: %s%n", eventData.getBodyAsString());
    }

    private static void processError(ErrorContext errorContext) {
        // Handle the error
        Throwable error = errorContext.getThrowable();
        System.err.printf("Error occurred: %s%n", error.getMessage());
    }
}
