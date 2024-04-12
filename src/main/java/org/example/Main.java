package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.bson.types.ObjectId;
import org.example.clases.Usuario;
import org.example.controllers.HomeController;
import org.example.controllers.URLController;
import org.example.controllers.UserController;
import org.example.grpc.UrlServiceImpl;
import org.example.services.UserServices;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (UserServices.getInstance().findByUsername("admin") == null) {
            UserServices.getInstance().crear(new Usuario(new ObjectId(),"admin", "admin", true));
        }
        new Thread(() -> {
            try {
                Server server = ServerBuilder.forPort(50051)
                        .addService(new UrlServiceImpl())
                        .build();
                server.start();
                server.awaitTermination();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "public/templates";
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