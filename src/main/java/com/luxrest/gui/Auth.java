package com.luxrest.gui;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Auth {
    private String endPoint;
    private String accessToken;
    private String lastLoginUsername;

    public String getEndPoint() {
        return this.endPoint;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getLastLoginUsername() {
        return lastLoginUsername;
    }
    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setLastLoginUsername(String lastLoginUsername) {
        this.lastLoginUsername = lastLoginUsername;
    }

    public void refreshAccessToken() {
        try {
            JSONObject response = HttpConnection.Post("http://"+ Auth.getInstance().getEndPoint() +"/api/v1/auth/refresh-token", new JSONObject(), Auth.getInstance().getAccessToken());

            String accessToken = extractToken(String.valueOf(response));
            Auth.getInstance().setAccessToken(accessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String extractToken(String tokenString) {
        JSONParser parser = new JSONParser();
        String accessToken = null;
        try {
            JSONObject tokenJSON = (JSONObject) parser.parse(tokenString);
            accessToken = (String) tokenJSON.get("access_token");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return accessToken;
    }


    /*** Singleton ***/

    private static Auth instance;

    public static Auth getInstance(){
        if(instance == null)
            instance = new Auth();
        return instance;
    }
}