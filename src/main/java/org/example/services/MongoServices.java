package org.example.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoServices {
    private static MongoServices instance = null;
    private MongoClient mongoClient;
    private MongoDatabase database;

    private MongoServices() {
        String mongoUrl = System.getenv("MONGO_URL");
        if (mongoUrl == null) {
            throw new RuntimeException("MONGO_URL is not set");
        }
        mongoClient = MongoClients.create(mongoUrl);
        database = mongoClient.getDatabase("yourDatabaseName");
    }

    public static MongoServices getInstance() {
        if (instance == null) {
            instance = new MongoServices();
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}