package com.luxrest.gui.Controllers;

import com.luxrest.gui.App;
import com.luxrest.gui.Auth;
import com.luxrest.gui.HttpConnection;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class LoginController {
    @FXML
    public PasswordField password;
    @FXML
    public TextField username;
    @FXML
    public Label error;

    public void initialize(){
        if(Auth.getInstance().getLastLoginUsername() != null)
            username.setText(Auth.getInstance().getLastLoginUsername());
    }

    public void doLogin() {
        try {
            JSONObject userData = new JSONObject();

            userData.put("username", username.getText());
            userData.put("password", password.getText());

            JSONObject response = HttpConnection.Post(Auth.getInstance().getEndPoint() +"/api/v1/auth/authenticate", userData);

            String accessToken = extractToken(String.valueOf(response));
            Auth.getInstance().setAccessToken(accessToken);

            //Guarda o ultimo user na base de dados
            Connection conn = DriverManager.getConnection("jdbc:sqlite:"+App.class.getResource("database.db"));
            PreparedStatement stmt = conn.prepareStatement("UPDATE settings SET last_username = ?");
            stmt.setString(1, username.getText());
            stmt.executeUpdate();
            stmt.close();
            conn.close();

            //Redireciona
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Dashboard/Dashboard.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            error.setVisible(true);
            error.setText("Username or passwords are invalid");
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