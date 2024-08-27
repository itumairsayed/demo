import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class EventDataTest {

    @Test
    public void testEventSettersAndGetters() {
        // Create a new instance of Event
        Event event = new Event();

        // Create sample data
        UUID eventId = UUID.randomUUID();
        String fDocNumber = "12345";
        String documentGUId = "doc-123";
        String transactionId = "tx-456";
        int eventSequence = 1;
        String eventDescription = "Test Event";
        String eventCode = "E001";
        String eventStatus = "Active";
        String eventDetail = "Details of the event";
        String claimNumber = "CLAIM-789";
        String documentCategory = "Category1";
        String documentName = "Document1";
        String documentStagingUrl = "http://example.com/doc";
        String documentClass = "ClassA";
        String policyNumber = "POL-321";
        String uploadGroup = "Group1";
        String tenantId = "Tenant-001";
        String receiveDate = "2024-08-27";
        boolean ocr = true;
        boolean notify = false;
        String source = "Source1";
        String docUploadStatus = "Uploaded";
        int uploadGroupSize = 5;
        String insertBy = "User1";
        LocalDateTime insertDate = LocalDateTime.now();
        String notes = "These are some notes.";

        // Set values
        event.setEventId(eventId);
        event.setFDocNumber(fDocNumber);
        event.setDocumentGUId(documentGUId);
        event.setTransactionId(transactionId);
        event.setEventSequence(eventSequence);
        event.setEventDescription(eventDescription);
        event.setEventCode(eventCode);
        event.setEventStatus(eventStatus);
        event.setEventDetail(eventDetail);
        event.setClaimNumber(claimNumber);
        event.setDocumentCategory(documentCategory);
        event.setDocumentName(documentName);
        event.setDocumentStagingUrl(documentStagingUrl);
        event.setDocumentClass(documentClass);
        event.setPolicyNumber(policyNumber);
        event.setUploadGroup(uploadGroup);
        event.setTenantId(tenantId);
        event.setReceiveDate(receiveDate);
        event.setOcr(ocr);
        event.setNotify(notify);
        event.setSource(source);
        event.setDocUploadStatus(docUploadStatus);
        event.setUploadGroupSize(uploadGroupSize);
        event.setInsertBy(insertBy);
        event.setInsertDate(insertDate);
        event.setNotes(notes);

        // Assertions
        assertEquals(eventId, event.getEventId());
        assertEquals(fDocNumber, event.getFDocNumber());
        assertEquals(documentGUId, event.getDocumentGUId());
        assertEquals(transactionId, event.getTransactionId());
        assertEquals(eventSequence, event.getEventSequence());
        assertEquals(eventDescription, event.getEventDescription());
        assertEquals(eventCode, event.getEventCode());
        assertEquals(eventStatus, event.getEventStatus());
        assertEquals(eventDetail, event.getEventDetail());
        assertEquals(claimNumber, event.getClaimNumber());
        assertEquals(documentCategory, event.getDocumentCategory());
        assertEquals(documentName, event.getDocumentName());
        assertEquals(documentStagingUrl, event.getDocumentStagingUrl());
        assertEquals(documentClass, event.getDocumentClass());
        assertEquals(policyNumber, event.getPolicyNumber());
        assertEquals(uploadGroup, event.getUploadGroup());
        assertEquals(tenantId, event.getTenantId());
        assertEquals(receiveDate, event.getReceiveDate());
        assertTrue(event.isOcr());
        assertFalse(event.isNotify());
        assertEquals(source, event.getSource());
        assertEquals(docUploadStatus, event.getDocUploadStatus());
        assertEquals(uploadGroupSize, event.getUploadGroupSize());
        assertEquals(insertBy, event.getInsertBy());
        assertEquals(insertDate, event.getInsertDate());
        assertEquals(notes, event.getNotes());
    }
}
