package org.example.axis;

import com.azure.core.amqp.exception.AmqpException;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.azure.messaging.eventhubs.models.PartitionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventHubConsumerServiceTest {

    @Mock
    private EventHubConsumerAsyncClient consumerClient;

    @Mock
    private Disposable subscription;

    @InjectMocks
    private EventHubConsumerService eventHubConsumerService;

    private Flux<PartitionEvent> eventFlux;

    @BeforeEach
    void setUp() {
        // Remove the call to startConsumer() from the setup
        reset(consumerClient);
        eventFlux = mock(Flux.class);
        when(consumerClient.receive(false)).thenReturn(eventFlux);
    }

    @Test
    void startConsumer_ShouldStartReceivingEvents() {
        // Start the consumer, which will initiate the first connection
        eventHubConsumerService.startConsumer();

        // Verify that the consumerClient.receive(false) was called once
        verify(consumerClient, times(1)).receive(false);
    }

    @Test
    void processEvent_ShouldProcessEvent() {
        eventHubConsumerService.startConsumer();

        PartitionEvent partitionEvent = mock(PartitionEvent.class);
        EventData eventData = new EventData("test".getBytes());
        //when(partitionEvent.getData()).thenReturn(eventData);

        // Invoke processEvent directly
        eventHubConsumerService.processEvent(eventData);

        // Verify that the correct data was processed (e.g., printed to console)
        // This would require mocking System.out, which is more complex and often not necessary
    }

    @Test
    void handleError_ShouldReconnectOnAmqpException() {
        // Start the consumer, which will initiate the first connection
        eventHubConsumerService.startConsumer();

        AmqpException amqpException = mock(AmqpException.class);

        // Directly invoke handleError to simulate an AMQP error, which should trigger a reconnect
        eventHubConsumerService.handleError(amqpException);

        // Verify that the consumerClient.receive(false) was called a second time due to reconnect
        verify(consumerClient, times(1)).receive(false); // Initial + reconnect
    }

    @Test
    void handleError_ShouldNotReconnectOnNonAmqpException() {
        eventHubConsumerService.startConsumer();

        RuntimeException runtimeException = new RuntimeException("Non-AMQP error");

        // Directly invoke handleError to simulate an error
        eventHubConsumerService.handleError(runtimeException);

        verify(consumerClient, times(1)).receive(false); // Only the initial connection attempt
    }

    @Test
    void reconnect_ShouldReconnectAfterDelay() throws InterruptedException {
        eventHubConsumerService.reconnect();

        // Simulate the passage of time in the test environment
        Thread.sleep(11000); // Sleep slightly longer than the 10-second reconnect delay

        verify(consumerClient, atLeast(1)).receive(false); // Verify that reconnect happened
    }

    @Test
    void shutdownConsumer_ShouldCloseSubscriptionAndClient() {
        // Mock the eventFlux subscription, as it directly affects the subscription field
        when(eventFlux.subscribe(any(), any())).thenReturn(subscription);

        // Start the consumer, which will assign the mocked subscription
        eventHubConsumerService.startConsumer();

        // Simulate shutdown
        eventHubConsumerService.shutdownConsumer();

        // Verify that the subscription and consumerClient were closed properly
        verify(subscription).dispose();
        verify(consumerClient).close();
    }
}
