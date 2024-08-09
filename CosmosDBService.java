package org.example.axis;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.PartitionKey;

public class CosmosDBService {

    private final CosmosClient cosmosClient;
    private final CosmosDatabase database;
    private final CosmosContainer container;

    public CosmosDBService(String endpoint, String key, String databaseName, String containerName) {
        cosmosClient = new CosmosClientBuilder()
            .endpoint(endpoint)
            .key(key)
            .buildClient();

        database = cosmosClient.getDatabase(databaseName);
        container = database.getContainer(containerName);
    }

    public void insertItem(Object item) {
        container.createItem(item, new PartitionKey(item.hashCode()), null);
        System.out.println("Item inserted successfully.");
    }

    public void close() {
        cosmosClient.close();
    }

    public static void main(String[] args) {
        String endpoint = "<your-cosmosdb-endpoint>";
        String key = "<your-cosmosdb-key>";
        String databaseName = "YourDatabaseName";
        String containerName = "YourContainerName";

        CosmosDBService cosmosDBService = new CosmosDBService(endpoint, key, databaseName, containerName);

        // Create an item to insert
        MyItem myItem = new MyItem("1", "SampleItem");

        // Insert the item
        cosmosDBService.insertItem(myItem);

        // Close the service
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

