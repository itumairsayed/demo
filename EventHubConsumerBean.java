package com.example.eventhub.service;

import com.azure.core.amqp.exception.AmqpException;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.azure.messaging.eventhubs.models.PartitionContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class EventHubConsumerBean implements ServletContextListener {

    private EventHubConsumerAsyncClient consumerClient;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("inside contextInitialized");
        startMonitoring();
    }

    public void startMonitoring() {
        System.out.println("inside startMonitoring");
        String connectionString = "Endpoint=sb://localhost;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=SAS_KEY_VALUE;UseDevelopmentEmulator=true;";
        String eventHubName = "eh1";

        // Initialize the consumer client
        this.consumerClient = new EventHubClientBuilder()
                .connectionString(connectionString, eventHubName)
                .consumerGroup("$Default")
                .buildAsyncConsumerClient();

        // Start receiving events
        startReceiving();
    }

    private void startReceiving() {
        consumerClient.receive(false)
                .subscribe(
                        partitionEvent -> {
                            EventData event = partitionEvent.getData();
                            PartitionContext partitionContext = partitionEvent.getPartitionContext();
                            String contents = new String(event.getBody(), StandardCharsets.UTF_8);
                            System.out.printf("[#%s] Partition id: %s. Sequence Number: %s. Contents: '%s'%n",
                                    partitionContext.getPartitionId(), event.getSequenceNumber(), contents);
                        },
                        error -> {
                            System.err.println("Error occurred while receiving events: " + error);
                            handleProcessingError(error);
                            },
                        () -> {
                            System.out.println("Finished receiving events.");
                        }
                );
    }

    private void handleProcessingError(Throwable throwable) {
        System.out.println("inside handleProcessingError");
        if (throwable instanceof AmqpException) {
            AmqpException amqpException = (AmqpException) throwable;
            System.err.println("AMQP Exception occurred: " + amqpException.getMessage());
            // Attempt to reconnect or handle the error
            reconnect();
        } else {
            System.err.println("Unexpected error occurred: " + throwable.getMessage());
        }
    }

    private void reconnect() {
        // Implement reconnection logic
        stopMonitoring();
        startMonitoring();
    }

    public void stopMonitoring() {
        if (consumerClient != null) {
            consumerClient.close();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (consumerClient != null) {
            consumerClient.close();
        }
    }
}
