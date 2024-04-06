package org.example.controllers;

import io.javalin.Javalin;
import org.bson.types.ObjectId;
import org.example.clases.Usuario;
import org.example.services.UserServices;

import java.util.List;
import java.util.Map;

public class UserController extends BaseController{
    public UserController(Javalin app) {
        super(app);
        registerTemplates();
    }

    @Override
    public void aplicarRutas() {
        app.get("/user/register", ctx -> {
            ctx.render("/public/templates/register.html");
        });

        app.post("/user/register", ctx -> {
            String username = ctx.formParam("usuario");
            String password = ctx.formParam("password");

            Usuario existingUser = UserServices.getInstance().findByUsername(username);

            if (existingUser != null) {
                ctx.render("/public/templates/register.html", Map.of("error", "El nombre de usuario ya existe"));
            } else {
                Usuario newUser = new Usuario(new ObjectId(), username, password, false);
                UserServices.getInstance().crear(newUser);
                ctx.sessionAttribute("username", newUser);
                ctx.redirect("/");
            }
        });

        app.get("user/login", ctx -> {
            ctx.render("/public/templates/Login.html");
        });

        app.post("user/login", ctx -> {
            String username = ctx.formParam("usuario");
            String password = ctx.formParam("password");

            Usuario user = UserServices.getInstance().findByUsername(username);
            if (user != null){
                if (user.getPassword().equals(password)){
                    if (ctx.formParam("rememberMe") != null) {
                        ctx.cookie("rememberedUser", user.getUsername(),600);
                    }
                    ctx.sessionAttribute("username", user);
                    ctx.redirect("/");
                }
                else{
                    ctx.render("/public/templates/Login.html", Map.of("error", "Usuario o contraseña incorrectos"));
                }
            }
            else{
                ctx.render("/public/templates/Login.html", Map.of("error", "Usuario no existe"));
            }
        });

        app.before("/user/list", ctx -> {
            Usuario usuario = ctx.sessionAttribute("username");
            if (usuario == null || !usuario.isAdmin()) {
                ctx.redirect("/");
            }
        });

        app.get("/user/list", ctx -> {
            String pageParam = ctx.queryParam("page");
            int page = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
            List<Usuario> usuarios = UserServices.getInstance().findAll(page, 5);
            long totalUsers = UserServices.getInstance().find().count();
            int totalPages = (int) Math.ceil((double) totalUsers / 5);
            ctx.render("/public/templates/user-list.html", Map.of("usuarios", usuarios, "totalPages", totalPages, "currentPage", page));
        });

        app.get("/user/close", ctx -> {
            ctx.removeCookie("rememberedUser");
            ctx.req().getSession().invalidate();
            ctx.redirect("/");
        });
    }
}
