package com.example.eventhub.service;


import com.azure.core.amqp.exception.AmqpException;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventProcessorClient;
import com.azure.messaging.eventhubs.EventProcessorClientBuilder;
import com.azure.messaging.eventhubs.models.ErrorContext;
import com.azure.messaging.eventhubs.models.EventContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class EventHubConsumerListener implements ServletContextListener {

    private EventProcessorClient eventProcessorClient;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("inside contextInitialized method");
        startMonitoring();
    }

    public void startMonitoring() {
        System.out.println("startMonitoring method");
        eventProcessorClient = new EventProcessorClientBuilder()
                .connectionString("Endpoint=sb://localhost;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=SAS_KEY_VALUE;UseDevelopmentEmulator=true;", "eh1")
                .consumerGroup("$Default") // or your custom consumer group
                .processEvent(this::processEvent)
                .processError(this::processError)
                .buildEventProcessorClient();

        // Start processing
        eventProcessorClient.start();
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("contextDestroyed method");
        if (eventProcessorClient != null) {
            eventProcessorClient.stop();
        }
    }

    private void processEvent(EventContext eventContext) {
        System.out.println("processEvent method");
        EventData eventData = eventContext.getEventData();
        System.out.printf("Received event: %s%n", eventData.getBodyAsString());
    }

    private void processError(ErrorContext errorContext) {
        System.out.println("processError method");
        Throwable error = errorContext.getThrowable();
        if (error instanceof AmqpException) {
            AmqpException amqpException = (AmqpException) error;
            System.err.println("AMQP Exception occurred: " + amqpException.getMessage());
            // Attempt to reconnect or handle the error
            reconnect();
        } else {
            System.err.printf("Error occurred: %s%n", error.getMessage());
        }
    }

    private void reconnect() {
        System.out.println("reconnect method");
        // Implement reconnection logic
        stopMonitoring();
        startMonitoring();
    }

    public void stopMonitoring() {
        System.out.println("stopMonitoring method");
        if (eventProcessorClient != null) {
            eventProcessorClient.stop();
        }
    }
}
