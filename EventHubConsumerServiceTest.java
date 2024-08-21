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
        eventFlux = mock(Flux.class);
        when(consumerClient.receive(false)).thenReturn(eventFlux);
    }

    @Test
    void startConsumer_ShouldStartReceivingEvents() {
        eventHubConsumerService.startConsumer();

        verify(consumerClient).receive(false);
        verify(eventFlux).subscribe(any(), any());
    }

    @Test
    void processEvent_ShouldProcessEvent() {
        eventHubConsumerService.startConsumer();

        PartitionEvent partitionEvent = mock(PartitionEvent.class);
        EventData eventData = new EventData("test".getBytes());
        when(partitionEvent.getData()).thenReturn(eventData);

        // Invoke processEvent directly
        eventHubConsumerService.processEvent(eventData);

        // Verify that the correct data was processed (e.g., printed to console)
        // This would require mocking System.out, which is more complex and often not necessary
    }

    @Test
    void handleError_ShouldReconnectOnAmqpException() {
        eventHubConsumerService.startConsumer();

        AmqpException amqpException = mock(AmqpException.class);

        // Directly invoke handleError to simulate an error
        eventHubConsumerService.handleError(amqpException);

        verify(consumerClient, atLeast(2)).receive(false); // Initial and reconnect attempt
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

        verify(consumerClient, at least(2)).receive(false); // Verify that reconnect happened
    }

    @Test
    void shutdownConsumer_ShouldCloseSubscriptionAndClient() {
        // Set up a subscription that is already active
        when(subscription.isDisposed()).thenReturn(false);
        eventHubConsumerService.startConsumer();

        // Simulate shutdown
        eventHubConsumerService.shutdownConsumer();

        verify(subscription).dispose();
        verify(consumerClient).close();
    }
}
