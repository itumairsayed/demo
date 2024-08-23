package org.example.axis;

import com.azure.core.amqp.exception.AmqpException;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class EventHubConsumerService {

    private final EventHubConsumerAsyncClient consumerClient;
    private Disposable subscription;

    public EventHubConsumerService(EventHubConsumerAsyncClient consumerClient) {
        this.consumerClient = consumerClient;
    }

    @PostConstruct
    public void startConsumer() {
        connect();
    }

    public void connect() {
        subscription = consumerClient.receive(false)
            .subscribe(
                event -> processEvent(event.getData()),
                error -> handleError(error));
    }

    public void processEvent(EventData eventData) {
        // Process the received event
        System.out.println("Received event: " + eventData.getBodyAsString());
    }

    public void handleError(Throwable error) {
        System.err.println("Error occurred while receiving event: " + error.getMessage());

        if (error instanceof AmqpException) {
            System.err.println("AMQP exception, attempting to reconnect...");
            reconnect();
        }
    }

    public void reconnect() {
        // Dispose of the current subscription
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }

        // Reconnect after a short delay
        Flux.interval(Duration.ofSeconds(10))
            .take(1)
            .subscribe(i -> connect());
    }

    @PreDestroy
    public void shutdownConsumer() {
        // Properly close the consumer client when the application shuts down
        if (subscription != null) {
            subscription.dispose();
        }
        consumerClient.close();
    }
}
