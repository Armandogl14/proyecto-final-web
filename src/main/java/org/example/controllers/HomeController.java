package org.example.controllers;

import io.javalin.Javalin;
import org.example.clases.Usuario;
import org.example.services.UserServices;

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
            ctx.render("public/templates/index.html");
        });
    }
}
