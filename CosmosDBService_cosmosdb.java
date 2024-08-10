package org.example.axis;

import com.microsoft.azure.cosmosdb.*;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import rx.Observable;

import java.util.Collections;

public class CosmosDBService_cosmosdb {

    private static final String COSMOS_DB_URI = "https://<your-account>.documents.azure.com:443/";
    private static final String COSMOS_DB_KEY = "<your-account-key>";
    private static final String DATABASE_ID = "YourDatabaseName";
    private static final String COLLECTION_ID = "YourCollectionName";

    private AsyncDocumentClient asyncDocumentClient;
    private DocumentCollection collection;

    public CosmosDBService_cosmosdb() {
        this.asyncDocumentClient = new AsyncDocumentClient.Builder()
                .withServiceEndpoint(COSMOS_DB_URI)
                .withMasterKeyOrResourceToken(COSMOS_DB_KEY)
                .withConnectionPolicy(ConnectionPolicy.GetDefault())
                .withConsistencyLevel(ConsistencyLevel.Session)
                .build();

        createDatabaseIfNotExists(DATABASE_ID);
        createCollectionIfNotExists(DATABASE_ID, COLLECTION_ID);
    }

    public void insertItemAsync(Object item) {
        rx.Observable<ResourceResponse<Document>> createDocumentObservable =
                asyncDocumentClient.createDocument(collection.getSelfLink(), item, new RequestOptions(), true);

        createDocumentObservable.subscribe(
                response -> System.out.println("Item inserted successfully: " + response.getResource().getId()),
                error -> System.err.println("Error inserting item: " + error.getMessage())
        );
    }

    private void createDatabaseIfNotExists(String databaseId) {
        Database database = new Database();
        database.setId(databaseId);

        rx.Observable<ResourceResponse<Database>> createDatabaseObservable =
                asyncDocumentClient.createDatabase(database, new RequestOptions());

        createDatabaseObservable.subscribe(
                response -> System.out.println("Database created: " + response.getResource().getId()),
                error -> {
                    if (error instanceof DocumentClientException && ((DocumentClientException) error).getStatusCode() == 409) {
                        System.out.println("Database already exists.");
                    } else {
                        error.printStackTrace();
                    }
                }
        );
    }

    private void createCollectionIfNotExists(String databaseId, String collectionId) {
        DocumentCollection collectionInfo = new DocumentCollection();
        collectionInfo.setId(collectionId);

        // Set the partition key if necessary
        PartitionKeyDefinition partitionKeyDef = new PartitionKeyDefinition();
        partitionKeyDef.setPaths(Collections.singletonList("/yourPartitionKey"));
        collectionInfo.setPartitionKey(partitionKeyDef);

        Observable<ResourceResponse<DocumentCollection>> createCollectionObservable =
                asyncDocumentClient.createCollection("/dbs/" + databaseId, collectionInfo, new RequestOptions());

        createCollectionObservable.subscribe(
                response -> {
                    this.collection = response.getResource();
                    System.out.println("Collection created: " + this.collection.getId());
                },
                error -> {
                    if (error instanceof DocumentClientException && ((DocumentClientException) error).getStatusCode() == 409) {
                        System.out.println("Collection already exists.");
                    } else {
                        error.printStackTrace();
                    }
                }
        );
    }

    public void close() {
        asyncDocumentClient.close();
    }

    public static void main(String[] args) {
        CosmosDBService_cosmosdb cosmosDBService = new CosmosDBService_cosmosdb();

        // Example item to insert
        MyItem item = new MyItem("1", "SampleItem");

        // Insert the item asynchronously
        cosmosDBService.insertItemAsync(item);

        // Close the client after some time to allow async operations to complete
        try {
            Thread.sleep(5000);  // Adjust the time as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cosmosDBService.close();
    }
}

class MyItem {
    private String id;
    private String name;

    public MyItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters
}
