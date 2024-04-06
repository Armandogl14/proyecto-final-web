package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.bson.types.ObjectId;
import org.example.clases.Usuario;
import org.example.controllers.HomeController;
import org.example.controllers.URLController;
import org.example.controllers.UserController;
import org.example.services.UserServices;

public class Main {
    public static void main(String[] args) {
        if (UserServices.getInstance().findByUsername("admin") == null) {
            UserServices.getInstance().crear(new Usuario(new ObjectId(),"admin", "admin", true));
        }
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "public";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.precompress = false;
                staticFileConfig.aliasCheck = null;
            });
        }).start(7000);

        new HomeController(app).aplicarRutas();
        new UserController(app).aplicarRutas();
        new URLController(app).aplicarRutas();
    }
}