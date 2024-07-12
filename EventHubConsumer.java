package com.example.eventhub_consumer.config;

import com.azure.messaging.eventhubs.*;
import com.azure.messaging.eventhubs.models.*;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class EventHubConsumer {
    private static final String connectionString = "Endpoint=sb://localhost;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=SAS_KEY_VALUE;UseDevelopmentEmulator=true;";
    private static final String eventHubName = "eh1";

    public static void main(String[] args) {
        receiveEvents().thenRun(() -> System.out.println("Finished receiving events."));
    }

    private static EventHubConsumerAsyncClient createConsumerClient(String consumerGroup) {
        return new EventHubClientBuilder()
                .connectionString(connectionString, eventHubName)
                .consumerGroup(consumerGroup)
                .buildAsyncConsumerClient();
    }

    private static CompletableFuture<Void> receiveEvents() {
        EventHubConsumerAsyncClient consumer = createConsumerClient(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        consumer.receive(false)
                .subscribe(partitionEvent -> {
                    String messageBody = new String(partitionEvent.getData().getBody(), StandardCharsets.UTF_8);
                    System.out.printf("Message received: '%s'%n", messageBody);
                }, error -> {
                    System.err.println("Error occurred while receiving events: " + error.toString());
                    countDownLatch.countDown();
                }, () -> {
                    System.out.println("Finished receiving events.");
                    countDownLatch.countDown();
                });

        try {
            countDownLatch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Receive operation was interrupted: " + e.toString());
        } finally {
            consumer.close();
        }

        return CompletableFuture.completedFuture(null);
    }
}
