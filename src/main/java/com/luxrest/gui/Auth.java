package com.luxrest.gui;

public class Auth {
    private static Auth instance;
    private String endPoint;
    private String accessToken;
    private String lastLoginUsername;

    private Auth(){}

    public static Auth getInstance(){
        if(instance == null)
            instance = new Auth();
        return instance;
    }

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
}