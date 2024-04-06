package org.example.controllers;

import io.javalin.Javalin;
import org.bson.types.ObjectId;
import org.example.clases.URL;
import org.example.clases.Usuario;
import org.example.services.URLServices;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class URLController extends BaseController{
    public URLController(Javalin app) {
        super(app);
        registerTemplates();
    }

    @Override
    public void aplicarRutas() {
        app.post("/url/shorten", ctx -> {
            String originalUrl = ctx.formParam("URL");
            String shortUrl = generateShortUrl(originalUrl);
            Usuario user = ctx.sessionAttribute("username");
            URL url = new URL(new ObjectId(), originalUrl, shortUrl, user.getUsername(), true);
            URLServices.getInstance().crear(url);
            //ctx.result(shortUrl);
            ctx.redirect("/");
        });
        app.get("/{shortUrl}", ctx -> {
            String shortUrl = ctx.pathParam("shortUrl");
            URL url = URLServices.getInstance().findByShortURL(shortUrl);
            if (url != null) {
                url.setClicks(url.getClicks() + 1);
                URLServices.getInstance().update(url);
                ctx.redirect(url.getUrlViejo());
            } else {
                ctx.status(404);
            }
        });
    }

    /*private String generateShortUrl(String originalUrl) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(originalUrl.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getUrlEncoder().encodeToString(hash);
            String shortUrl = encoded.substring(0, 10); // Use the first 10 characters as the short URL
            return "https://agonzalezlopez.me/" + shortUrl;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }*/

    private String generateShortUrl(String originalUrl) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(originalUrl.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getUrlEncoder().encodeToString(hash);
            return encoded.substring(0, 10);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
