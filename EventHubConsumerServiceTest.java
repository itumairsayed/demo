import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.azure.messaging.eventhubs.models.EventContext;
import com.azure.messaging.eventhubs.models.PartitionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.test.publisher.TestPublisher;

import java.time.Duration;

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

    @Captor
    private ArgumentCaptor<Throwable> errorCaptor;

    private TestPublisher<PartitionEvent> testPublisher;

    @BeforeEach
    void setUp() {
        testPublisher = TestPublisher.create();
        when(consumerClient.receive(false)).thenReturn(testPublisher.flux());
    }

    @Test
    void startConsumer_ShouldStartReceivingEvents() {
        eventHubConsumerService.startConsumer();
        testPublisher.emit(new PartitionEvent("partition", new EventContext(), new EventData("test".getBytes())));

        verify(consumerClient).receive(false);
    }

    @Test
    void processEvent_ShouldProcessEvent() {
        eventHubConsumerService.startConsumer();

        PartitionEvent partitionEvent = new PartitionEvent("partition", new EventContext(), new EventData("test".getBytes()));
        testPublisher.emit(partitionEvent);

        // No explicit verification because processEvent logs to stdout.
        // You could mock System.out if needed to capture and verify output.
    }

    @Test
    void handleError_ShouldReconnectOnAmqpException() {
        eventHubConsumerService.startConsumer();

        AmqpException amqpException = mock(AmqpException.class);
        testPublisher.error(amqpException);

        verify(consumerClient, atLeast(2)).receive(false); // Initial and reconnect attempt
    }

    @Test
    void handleError_ShouldNotReconnectOnNonAmqpException() {
        eventHubConsumerService.startConsumer();

        RuntimeException runtimeException = new RuntimeException("Non-AMQP error");
        testPublisher.error(runtimeException);

        verify(consumerClient, times(1)).receive(false); // Only the initial connection attempt
    }

    @Test
    void reconnect_ShouldReconnectAfterDelay() throws InterruptedException {
        eventHubConsumerService.reconnect();

        // Simulate the passage of time in the test environment
        Thread.sleep(11000); // Sleep slightly longer than the 10-second reconnect delay

        verify(consumerClient, atLeast(2)).receive(false); // Verify that reconnect happened
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
