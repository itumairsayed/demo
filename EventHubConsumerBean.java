package com.example.eventhub.service;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.azure.messaging.eventhubs.models.PartitionContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.nio.charset.StandardCharsets;

@WebListener
public class EventHubConsumerBean implements ServletContextListener {

    private EventHubConsumerAsyncClient consumerClient;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String connectionString = "Endpoint=sb://localhost;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=SAS_KEY_VALUE;UseDevelopmentEmulator=true;";
        String eventHubName = "eh1";

        // Initialize the consumer client
        this.consumerClient = new EventHubClientBuilder()
                .connectionString(connectionString, eventHubName)
                .buildAsyncConsumerClient();

        // Start receiving events
        startReceiving();
    }

//    @PostConstruct
//    public void init() {
//        String connectionString = "Endpoint=sb://localhost;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=SAS_KEY_VALUE;UseDevelopmentEmulator=true;";
//        String eventHubName = "eh1";
//
//        // Initialize the consumer client
//        this.consumerClient = new EventHubClientBuilder()
//                .connectionString(connectionString, eventHubName)
//                .buildAsyncConsumerClient();
//
//        // Start receiving events
//        startReceiving();
//    }

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
                        },
                        () -> {
                            System.out.println("Finished receiving events.");
                        }
                );
    }

//    @PreDestroy
//    public void cleanup() {
//        if (consumerClient != null) {
//            consumerClient.close();
//        }
//    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (consumerClient != null) {
            consumerClient.close();
        }
    }
}
