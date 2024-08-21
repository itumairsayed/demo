package org.example.axis;

import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = EventHubConfig.class)
@TestPropertySource(properties = {
    "azure.eventhub.connection-string=Endpoint=sb://testnamespace.servicebus.windows.net/;SharedAccessKeyName=TestKeyName;SharedAccessKey=TestKeyValue;EntityPath=TestEventHubName",
    "azure.eventhub.name=TestEventHubName",
    "azure.eventhub.consumer-group=TestConsumerGroup"
})
class EventHubConfigTest {

    @Autowired
    private EventHubConfig eventHubConfig;

    @Test
    void testEventHubConsumerAsyncClientBeanCreation() {
        // Create the bean using the configuration class
        EventHubConsumerAsyncClient client = eventHubConfig.eventHubConsumerAsyncClient();

        // Validate that the client is not null
        assertNotNull(client, "The EventHubConsumerAsyncClient bean should not be null");
    }
}
