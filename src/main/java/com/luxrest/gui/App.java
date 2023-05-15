package com.luxrest.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:"+App.class.getResource("database.db"));
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT endPoint, last_username FROM settings");
            String endpoint = rs.getString("endPoint");
            String lastLoginUsername = rs.getString("last_username");
            conn.close();
            stmt.close();
            rs.close();

            //System.out.println(endpoint);

            FXMLLoader fxmlLoader;
            if(endpoint == null || endpoint.isBlank() || endpoint.isEmpty()){
                fxmlLoader = new FXMLLoader(App.class.getResource("Domain/Domain.fxml"));
            }else{
                Auth.getInstance().setEndPoint(endpoint);
                fxmlLoader = new FXMLLoader(App.class.getResource("Login/Login.fxml"));
                if (lastLoginUsername != null)
                    Auth.getInstance().setLastLoginUsername(lastLoginUsername);
            }

            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("App");
            stage.setScene(scene);
            stage.show();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Auth.getInstance().refreshAccessToken();
            }
        }, 0, 40000);

        launch();
    }
}