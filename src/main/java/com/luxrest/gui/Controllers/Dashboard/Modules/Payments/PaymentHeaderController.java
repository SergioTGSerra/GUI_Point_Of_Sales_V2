package com.luxrest.gui.Controllers.Dashboard.Modules.Payments;

import com.luxrest.gui.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PaymentHeaderController {
    @FXML
    public VBox body;
    @FXML
    public Button cashButton;
    @FXML
    public Button cardButton;
    @FXML
    public HBox paymentMethods;

    public void initialize() {
        showContentCash();
    }

    public void showContentCash() {
        paymentMethods.getChildren().forEach(node -> node.getStyleClass().remove("selectedOptionPaymentButton"));
        cashButton.getStyleClass().add("selectedOptionPaymentButton");
        body.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Dashboard/Modules/Payments/PaymentCashBody.fxml"));
        try {
            VBox paymentCashBody = fxmlLoader.load();
            body.getChildren().add(paymentCashBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showContentCard() {
        paymentMethods.getChildren().forEach(node -> node.getStyleClass().remove("selectedOptionPaymentButton"));
        cardButton.getStyleClass().add("selectedOptionPaymentButton");
        body.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Dashboard/Modules/Payments/PaymentCardBody.fxml"));
        try {
            ImageView paymentCashBody = fxmlLoader.load();
            body.getChildren().add(paymentCashBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}