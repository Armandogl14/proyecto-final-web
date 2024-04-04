package org.example.clases;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("Usuario")
public class Usuario {
    @Id
    private String username;
    private String password;
    private boolean admin;

    public Usuario(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
