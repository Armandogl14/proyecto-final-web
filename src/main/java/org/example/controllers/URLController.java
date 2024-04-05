package org.example.controllers;

import io.javalin.Javalin;
import org.example.clases.URL;

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
            String originalUrl = ctx.formParam("url");
            String shortUrl = generateShortUrl(originalUrl);
            //URL url = new URL(originalUrl, shortUrl, null, true);
            //urlService.create(url);
            ctx.result(shortUrl);
        });
    }

    private String generateShortUrl(String originalUrl) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(originalUrl.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getUrlEncoder().encodeToString(hash);
            String shortUrl = encoded.substring(0, 10); // Use the first 10 characters as the short URL
            return "https://agonzalezlopez.me/" + shortUrl;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /*private String generateShortUrl(String originalUrl) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(originalUrl.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getUrlEncoder().encodeToString(hash);
            String shortUrl = encoded.substring(0, 10); // Use the first 10 characters as the short URL
            return "http://localhost:7000/" + shortUrl;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }*/
}
