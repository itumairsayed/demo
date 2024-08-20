import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.azure.messaging.eventhubs.models.EventPosition;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class EventHubConsumerService {

    private final EventHubConsumerAsyncClient consumerClient;

    public EventHubConsumerService(EventHubConsumerAsyncClient consumerClient) {
        this.consumerClient = consumerClient;
    }

    @PostConstruct
    public void startConsumer() {
        consumerClient.receive(false)
            .subscribe(event -> processEvent(event.getData()),
                       error -> System.err.println("Error occurred while receiving event: " + error));
    }

    private void processEvent(EventData eventData) {
        // Process the received event
        System.out.println("Received event: " + eventData.getBodyAsString());
    }

    @PreDestroy
    public void shutdownConsumer() {
        consumerClient.close();
    }
}
