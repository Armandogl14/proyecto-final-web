package org.example.controllers;

import io.javalin.Javalin;
import org.example.clases.URL;
import org.example.clases.Usuario;
import org.example.services.URLServices;
import org.example.services.UserServices;

import java.util.List;
import java.util.Map;

public class HomeController extends BaseController{
    public HomeController(Javalin app) {
        super(app);
        registerTemplates();
    }

    @Override
    public void aplicarRutas() {
        app.before("/", ctx -> {
            if(ctx.cookie("rememberedUser") != null){
                Usuario user = UserServices.getInstance().findByUsername(ctx.cookie("rememberedUser"));
                ctx.sessionAttribute("username", user);
            }
        });

        app.get("/", ctx -> {
            List<URL> urls = URLServices.getInstance().find().stream().toList();
            Map<String, Object> model = Map.of("urls", urls);
            ctx.render("public/templates/index.html", model);
        });
    }
}
