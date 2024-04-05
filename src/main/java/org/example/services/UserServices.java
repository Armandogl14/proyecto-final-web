package org.example.services;

import org.example.clases.Usuario;

public class UserServices extends MongoServices<Usuario> {
    private static UserServices instance = null;
    private UserServices() {
        super(Usuario.class);
    }

    public static UserServices getInstance() {
        if (instance == null) {
            instance = new UserServices();
        }
        return instance;
    }
}
