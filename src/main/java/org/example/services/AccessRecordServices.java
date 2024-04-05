package org.example.services;

import org.example.clases.AccessRecord;

public class AccessRecordServices extends MongoServices<AccessRecord>{
    private static AccessRecordServices instance = null;
    private AccessRecordServices() {
        super(AccessRecord.class);
    }

    public static AccessRecordServices getInstance() {
        if (instance == null) {
            instance = new AccessRecordServices();
        }
        return instance;
    }
}
