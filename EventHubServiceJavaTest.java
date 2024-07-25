package com.example.eventhub;

import com.azure.messaging.eventhubs.*;
import com.example.eventhub.service.EventHubServiceJava;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventHubServiceJavaTest {

    @Mock
    private EventHubProducerClient producerClientMock;

    @Mock
    private EventDataBatch eventDataBatchMock;
    private EventHubServiceJava eventHubService;

    private final String connectionString = "Endpoint=sb://test.servicebus.windows.net/;SharedAccessKeyName=testKey;SharedAccessKey=testAccessKey";
    private final String eventHubName = "testEventHub";

    @BeforeEach
    void setUp() {
        eventHubService = new EventHubServiceJava(producerClientMock); // Inject the mock using the new constructor
    }

    @Test
    void testSendEvent_successful() {
        String message = "Test message";

        when(producerClientMock.createBatch()).thenReturn(eventDataBatchMock);
        when(eventDataBatchMock.tryAdd(any(EventData.class))).thenReturn(true);

        eventHubService.sendEvent(message);

        verify(producerClientMock).send(eventDataBatchMock);
        verify(eventDataBatchMock).tryAdd(any(EventData.class));
    }

    @Test
    void testSendEvent_eventTooLarge() {
        String message = "Test message";

        when(producerClientMock.createBatch()).thenReturn(eventDataBatchMock);
        when(eventDataBatchMock.tryAdd(any(EventData.class))).thenReturn(false);
        when(eventDataBatchMock.getMaxSizeInBytes()).thenReturn(1024);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            eventHubService.sendEvent(message);
        });

        assertEquals("Event is too large for an empty batch. Max size: 1024", thrown.getMessage());
        verify(eventDataBatchMock).tryAdd(any(EventData.class));
    }

    @Test
    void testConstructor_initializesProducerClient() {
        EventHubServiceJava service = new EventHubServiceJava(connectionString, eventHubName);
        assertNotNull(service.producerClient);
    }
}
