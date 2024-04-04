package org.example.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;

public class MongoServices {
    private static MongoServices instance = null;
    private MongoClient mongoClient;
    private Datastore datastore;

    private MongoServices() {
        String mongoUrl = System.getenv("MONGO_URL");
        if (mongoUrl == null) {
            throw new RuntimeException("MONGO_URL is not set");
        }
        mongoClient = MongoClients.create(mongoUrl);
        datastore = Morphia.createDatastore(mongoClient, "yourDatabaseName", MapperOptions.builder().build());
    }

    public static MongoServices getInstance() {
        if (instance == null) {
            instance = new MongoServices();
        }
        return instance;
    }

    public Datastore getDatastore() {
        return datastore;
    }
}