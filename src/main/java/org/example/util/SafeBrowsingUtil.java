package org.example.util;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONObject;

public class SafeBrowsingUtil {
    private static final String API_KEY = "AIzaSyDEtMYm2ChdmtjM_YzGGHajtPReGKqnAk4";
    private static final String API_URL = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key=" + API_KEY;

    public static boolean isUrlSafe(String url) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("client", new JSONObject()
                    .put("clientId", "yourcompanyname")
                    .put("clientVersion", "1.5.2"));
            requestBody.put("threatInfo", new JSONObject()
                    .put("threatTypes", new String[]{"MALWARE", "SOCIAL_ENGINEERING"})
                    .put("platformTypes", new String[]{"ANY_PLATFORM"})
                    .put("threatEntryTypes", new String[]{"URL"})
                    .put("threatEntries", new JSONObject[]{new JSONObject().put("url", url)}));

            HttpResponse<String> response = Unirest.post(API_URL)
                    .header("Content-Type", "application/json")
                    .body(requestBody.toString())
                    .asString();

            JSONObject responseBody = new JSONObject(response.getBody());
            return !responseBody.has("matches");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}