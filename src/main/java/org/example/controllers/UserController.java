package org.example.controllers;

import io.javalin.Javalin;

public class UserController extends BaseController{
    public UserController(Javalin app) {
        super(app);
        registerTemplates();
    }

    @Override
    public void aplicarRutas() {

    }
}
