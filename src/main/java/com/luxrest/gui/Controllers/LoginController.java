package com.luxrest.gui.Controllers;

import com.luxrest.gui.App;
import com.luxrest.gui.Auth;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class LoginController {
    @FXML
    public PasswordField password;
    @FXML
    public TextField user;
    @FXML
    public Label error;

    public void initialize(){
        if(Auth.getInstance().getLastLoginUsername() != null)
            user.setText(Auth.getInstance().getLastLoginUsername());
    }

    public void doLogin() {
        try {
            JSONObject requestJson = new JSONObject();

            requestJson.put("username", user.getText());
            requestJson.put("password", password.getText());

            String requestBody = requestJson.toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://"+ Auth.getInstance().getEndPoint() +"/api/v1/auth/authenticate"))
                    .header("Content-Type", "application/json; utf-8")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpClient httpClient = HttpClient.newBuilder()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if (statusCode == 200){
                String responseBody = response.body();
                String accessToken = extractToken(responseBody);
                System.out.println(accessToken);
                Auth.getInstance().setAccessToken(accessToken);

                FXMLLoader root = new FXMLLoader(App.class.getResource("Dashboard/Dashboard.fxml"));
                try {
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:"+App.class.getResource("database.db"));
                    String sql = "UPDATE settings SET lastLoginUsername = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, user.getText());
                    stmt.executeUpdate();
                    stmt.close();
                    conn.close();

                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Login/login.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = (Stage) user.getScene().getWindow();
                    stage.setScene(scene);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else if (statusCode == 401){
                error.setVisible(true);
                error.setText("Username or passwords are invalid");
            }
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
}