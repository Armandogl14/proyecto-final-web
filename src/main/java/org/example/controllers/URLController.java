package org.example.controllers;

import eu.bitwalker.useragentutils.UserAgent;
import io.javalin.Javalin;
import org.bson.types.ObjectId;
import org.example.clases.AccessRecord;
import org.example.clases.URL;
import org.example.clases.Usuario;
import org.example.services.AccessRecordServices;
import org.example.services.URLServices;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

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
                AccessRecord accessRecord = new AccessRecord();
                accessRecord.setAccessTime(LocalDateTime.now());
                accessRecord.setBrowser(getBrowserName(ctx.userAgent()));
                accessRecord.setIpAddress(ctx.ip());
                accessRecord.setOperatingSystemPlatform(getOperatingSystem(ctx.userAgent()));
                accessRecord.setUrl(url.getUrlNuevo());

                AccessRecordServices.getInstance().crear(accessRecord);
                ctx.redirect(url.getUrlViejo());
            } else {
                ctx.status(404);
            }
        });

        app.get("/url/resume/{urlNuevo}", ctx -> {
            String shortUrl = ctx.pathParam("urlNuevo");
            System.out.println(shortUrl);
            List<AccessRecord> accessRecords = AccessRecordServices.getInstance().findByURL(shortUrl);
            for (AccessRecord accessRecord : accessRecords) {
                System.out.println(accessRecord.getAccessTime() + " " + accessRecord.getBrowser() + " " + accessRecord.getIpAddress() + " " + accessRecord.getOperatingSystemPlatform() + " " + accessRecord.getUrl());
            }
            Map<String, Object> model = new HashMap<>();
            model.put("accessRecords", accessRecords);
            model.put("urlNuevo", shortUrl);
            ctx.render("/public/templates/resume.html", model);
        });

        app.before("/url/borrar/{urlNuevo}", ctx -> {
            Usuario usuario = ctx.sessionAttribute("username");
            if (usuario == null || !usuario.isAdmin()) {
                ctx.redirect("/");
            }
        });

        app.post("/url/borrar/{urlNuevo}", ctx -> {
            String shortUrl = ctx.pathParam("urlNuevo");
            URLServices.getInstance().deleteByShortURL(shortUrl);
            ctx.redirect("/");
        });
//        =================================REST services=================================================================
        // (a) Listado de las URL publicadas por un usuario incluyendo las estadísticas asociadas.
        app.get("/url/api-list", ctx -> {
//            Usuario usuario = ctx.sessionAttribute("username");
//            System.out.println(usuario.getUsername());
//            String username = usuario.getUsername();
//            List<URL> urls = URLServices.getInstance().findByUsername(username);
            List<URL> urls = URLServices.getInstance().find().stream().toList();
            Map<String, Object> model = new HashMap<>();
            model.put("urls", urls);
//            model.put("accessRecords", accessRecords);
            ctx.json(model);
        });
        app.get("/url/api-acess", ctx -> {
            List<URL> urls = URLServices.getInstance().find().stream().toList();
            List<AccessRecord> accessRecords = new ArrayList<>(); // Inicializa como lista vacía
            for (URL url : urls) {
                List<AccessRecord> records = AccessRecordServices.getInstance().findByURL(url.getUrlNuevo());
                if (!records.isEmpty()) {
                    AccessRecord accessRecord = records.get(0);
                    accessRecords.add(accessRecord);
                    System.out.println(accessRecord.getAccessTime() + " " + accessRecord.getBrowser() + " " + accessRecord.getIpAddress() + " " + accessRecord.getOperatingSystemPlatform() + " " + accessRecord.getUrl());
                }
            }
            Map<String, Object> model = new HashMap<>();
            if(accessRecords.isEmpty()){
                ctx.status(404);
                System.out.println("No hay registros de acceso");
            }else {
                model.put("accessRecords", accessRecords);
                //ctx.json(model);
                System.out.println("Registros de acceso");
            }

        });


// (b) Creación de registro de URL para un usuario retornando la estructura básica
        app.post("/url/crear", ctx -> {
            String originalUrl = ctx.formParam("URL");
            String shortUrl = generateShortUrl(originalUrl);
            Usuario user = ctx.sessionAttribute("username");
            URL url = new URL(new ObjectId(), originalUrl, shortUrl, user.getUsername(), true);
            URLServices.getInstance().crear(url);
            AccessRecord accessRecord = new AccessRecord();
            accessRecord.setAccessTime(LocalDateTime.now());
            accessRecord.setBrowser(getBrowserName(ctx.userAgent()));
            accessRecord.setIpAddress(ctx.ip());
            accessRecord.setOperatingSystemPlatform(getOperatingSystem(ctx.userAgent()));
            accessRecord.setUrl(url.getUrlNuevo());
            AccessRecordServices.getInstance().crear(accessRecord);
            //String siteImageBase64 = getSiteImageBase64(originalUrl);
            Map<String, Object> model = new HashMap<>();
            model.put("url", url);
            model.put("accessRecord", accessRecord);
            //model.put("siteImageBase64", siteImageBase64);
            ctx.json(model);
        });

    }
    private String generateShortUrl(String originalUrl) {
        try {
            String originalUrlWithTimestamp = originalUrl + System.currentTimeMillis();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(originalUrlWithTimestamp.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getUrlEncoder().encodeToString(hash);
            return encoded.substring(0, 10);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String getOperatingSystem(String userAgent) {
        if (userAgent.toLowerCase().contains("windows")) {
            return "Windows";
        } else if (userAgent.toLowerCase().contains("mac")) {
            return "Mac";
        } else if (userAgent.toLowerCase().contains("x11")) {
            return "Unix";
        } else if (userAgent.toLowerCase().contains("android")) {
            return "Android";
        } else if (userAgent.toLowerCase().contains("iphone")) {
            return "IPhone";
        } else {
            return "UnKnown";
        }
    }

    private String getBrowserName(String userAgentString) {
        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgentString);
        return client.userAgent.family;
    }
}
