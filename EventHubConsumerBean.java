package com.example.eventhub.service;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.azure.messaging.eventhubs.EventHubConsumerClient;
import com.azure.messaging.eventhubs.models.EventPosition;
import com.azure.messaging.eventhubs.models.PartitionContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.nio.charset.StandardCharsets;

@Singleton
@Startup
public class EventHubConsumerBean {

    private EventHubConsumerAsyncClient consumerClient;

    @PostConstruct
    public void init() {
        String connectionString = "Endpoint=sb://localhost;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=SAS_KEY_VALUE;UseDevelopmentEmulator=true;";
        String eventHubName = "eh1";

        // Initialize the consumer client
        this.consumerClient = new EventHubClientBuilder()
                .connectionString(connectionString, eventHubName)
                .consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
                .buildAsyncConsumerClient();

        // Start receiving events
        startReceiving();
    }

    private void startReceiving() {

        consumerClient.receiveFromPartition("0", EventPosition.latest())
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

    @PreDestroy
    public void cleanup() {
        if (consumerClient != null) {
            consumerClient.close();
        }
    }
}
