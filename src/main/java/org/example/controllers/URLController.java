package org.example.controllers;

import eu.bitwalker.useragentutils.UserAgent;
import io.javalin.Javalin;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bson.types.ObjectId;
import org.example.clases.AccessRecord;
import org.example.clases.URL;
import org.example.clases.Usuario;
import org.example.services.AccessRecordServices;
import org.example.services.URLServices;
import org.example.services.UserServices;
import org.example.util.SafeBrowsingUtil;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;

public class URLController extends BaseController{
    public URLController(Javalin app) {
        super(app);
        registerTemplates();
    }

    @Override
    public void aplicarRutas() {
        app.post("/url/shorten", ctx -> {
            String originalUrl = ctx.formParam("URL");
            if (!SafeBrowsingUtil.isUrlSafe(originalUrl)) {
                ctx.status(400).result("La URL proporcionada es maliciosa");
                return;
            }
            String shortUrl = generateShortUrl(originalUrl);
            Usuario user = ctx.sessionAttribute("username");
            URL url = new URL(new ObjectId(), originalUrl, shortUrl, user.getUsername(), true);
            URLServices.getInstance().crear(url);
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
        app.get("/url/api-list/{username}", ctx -> {
             Usuario usuario = UserServices.getInstance().findByUsername(ctx.pathParam("username"));
            System.out.println(usuario.getUsername());
            String username = usuario.getUsername();
            List<URL> urls = URLServices.getInstance().findByUsername(username);
//            List<URL> urls = URLServices.getInstance().find().stream().toList();

            Map<String, Object> model = new HashMap<>();
            model.put("urls", urls);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String json = mapper.writeValueAsString(model);
            // Genera una clave segura
            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

            // Crea el JWT
            String jwt = Jwts.builder()
                    .setSubject(json)
                    .signWith(key)
                    .compact();

            System.out.println("JWT: " + jwt);
            ctx.json(json);
        });
        app.get("/url/api-acess/{username}", ctx -> {
            Usuario usuario = UserServices.getInstance().findByUsername(ctx.pathParam("username"));
            List<URL> urls = URLServices.getInstance().findByUsername(usuario.getUsername());
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
            model.put("accessRecords", accessRecords);
            if(accessRecords.isEmpty()){
                ctx.status(404);
                System.out.println("No hay registros de acceso");
            }else { // Si hay registros de acceso
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                String json = mapper.writeValueAsString(model);
                // Genera una clave segura
                SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

                // Crea el JWT
                String jwt = Jwts.builder()
                        .setSubject(json)
                        .signWith(key)
                        .compact();

                System.out.println("JWT: " + jwt);
                ctx.result(jwt);
                System.out.println("Registros de acceso");
            }

        });


// (b) Creación de registro de URL para un usuario retornando la estructura básica
        app.post("/url/crear/{url_original}", ctx -> {
            String originalUrl = ctx.pathParam("url_original");
            String shortUrl = generateShortUrl(originalUrl);
//            Usuario user = UserServices.getInstance().findByUsername(ctx.pathParam("usuario"));
            URL url = new URL(new ObjectId(), originalUrl, shortUrl,"admin", true);
            URLServices.getInstance().crear(url);
            //String siteImageBase64 = getSiteImageBase64(originalUrl);
            Map<String, Object> model = new HashMap<>();
            model.put("url", url);
//
            ctx.json(model);
        });

    }
    //        ==================================================================================================
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
