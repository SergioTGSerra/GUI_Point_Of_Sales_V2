package com.luxrest.gui.Controllers;

import com.luxrest.gui.App;
import com.luxrest.gui.Auth;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DomainController {
    @FXML
    public TextField domain;

    public void validDomain() {
        String endpoint = domain.getText();

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:"+App.class.getResource("database.db"));
            String sql = "INSERT INTO settings (endPoint) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, endpoint);
            stmt.executeUpdate();
            conn.close();

            Auth.getInstance().setEndPoint(endpoint);

            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Login/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) domain.getScene().getWindow();
            stage.setScene(scene);

        } catch (SQLException e) {
            System.out.println("Erro ao salvar o EndPoint: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erro ao carregar a Login Page: " + e.getMessage());
        }
    }
}
