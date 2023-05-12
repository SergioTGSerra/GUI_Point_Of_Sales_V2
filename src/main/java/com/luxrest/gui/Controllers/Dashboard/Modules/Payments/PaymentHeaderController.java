package com.luxrest.gui.Controllers.Dashboard.Modules.Payments;

import com.luxrest.gui.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PaymentHeaderController {
    public VBox body;
    public Button cashButton;

    public void initialize() {
        showContentCash();
    }

    public void showContentCash() {
        body.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Dashboard/Modules/Payments/PaymentCashBody.fxml"));
        try {
            VBox paymentCashBody = fxmlLoader.load();
            body.getChildren().add(paymentCashBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}