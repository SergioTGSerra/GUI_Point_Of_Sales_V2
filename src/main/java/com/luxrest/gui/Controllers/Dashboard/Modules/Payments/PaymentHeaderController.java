package com.luxrest.gui.Controllers.Dashboard.Modules.Payments;

import com.luxrest.gui.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PaymentHeaderController {
    public VBox body;
    public Button cashButton;
    private Double price;

    public void initialize(Double price) {
        this.price = price;
        showContentCash();
    }

    public void showContentCash() {
        body.getChildren().clear();
        cashButton.getStyleClass().add("selectedButton");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Dashboard/Modules/Payments/PaymentCashBody.fxml"));
        try {
            VBox paymentCashBody = fxmlLoader.load();
            PaymentCashBodyController paymentCashBodyController = fxmlLoader.getController();
            paymentCashBodyController.setPrice(this.price);
            body.getChildren().add(paymentCashBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}