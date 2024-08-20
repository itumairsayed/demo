import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventHubConfig {

    @Bean
    public EventHubConsumerAsyncClient eventHubConsumerAsyncClient() {
        return new EventHubClientBuilder()
            .connectionString("${azure.eventhub.connection-string}", "${azure.eventhub.name}")
            .consumerGroup("${azure.eventhub.consumer-group}")
            .buildAsyncConsumerClient();
    }
}
