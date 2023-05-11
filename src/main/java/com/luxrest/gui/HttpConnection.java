package com.luxrest.gui;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HttpConnection {
    public static JSONObject Get(String urlPram, String token) {
        try {
            var url = new URL(urlPram);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HttpResponseCode: " + conn.getResponseCode());
            } else {
                var responseString = new StringBuilder();
                var scanner = new Scanner(conn.getInputStream());

                while (scanner.hasNext()) {
                    responseString.append(scanner.nextLine());
                }
                scanner.close();

                var parse = new JSONParser();

                return (JSONObject) parse.parse(String.valueOf(responseString));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject Post(String url, JSONObject postData) {
        try {
            var conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (var outputStream = conn.getOutputStream()) {
                outputStream.write(postData.toString().getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HttpResponseCode: " + conn.getResponseCode());
            } else {
                var responseString = new StringBuilder();
                var scanner = new Scanner(conn.getInputStream());

                while (scanner.hasNext()) {
                    responseString.append(scanner.nextLine());
                }
                scanner.close();

                var parse = new JSONParser();
                return (JSONObject) parse.parse(responseString.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject Post(String url, JSONObject postData, String token) {
        try {
            var conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setDoOutput(true);

            try (var outputStream = conn.getOutputStream()) {
                outputStream.write(postData.toString().getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HttpResponseCode: " + conn.getResponseCode());
            } else {
                var responseString = new StringBuilder();
                var scanner = new Scanner(conn.getInputStream());

                while (scanner.hasNext()) {
                    responseString.append(scanner.nextLine());
                }
                scanner.close();

                var parse = new JSONParser();
                return (JSONObject) parse.parse(responseString.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}