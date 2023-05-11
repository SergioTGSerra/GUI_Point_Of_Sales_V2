package com.luxrest.gui.Controllers.Dashboard.Modules.Payments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PaymentCashBodyController {
    @FXML
    public Label amountPay;
    @FXML
    public Label receive;
    @FXML
    public Label change;
    public Double price = 0.0;
    private Double receivedPrice = 0.0;
    public void initialize(){
        amountPay.setText(this.price + "€");
        receive.setText(this.receivedPrice + "€");
        change.setText(this.receivedPrice - this.price + "€");
    }


    public void setPrice(Double price) {
        this.price = price;
        amountPay.setText(this.price + "€");
        receive.setText(this.receivedPrice + "€");
    }

    public void addRecivePrice(Double price) {
        this.receivedPrice += price;
        receive.setText( this.receivedPrice + "€");
        if(this.receivedPrice >= this.price) {
            change.setText(this.receivedPrice - this.price + "€");
        }
    }

    public void buttonClicked(ActionEvent event) {
        Button button = (Button) event.getSource();
        String buttonText = button.getText().replace("€", "");
        Double value = Double.parseDouble(buttonText);
        addRecivePrice(value);
    }

    public void buttonClear() {
        this.receivedPrice = 0.0;
        receive.setText(this.receivedPrice + "€");
        change.setText(this.receivedPrice + "€");
    }

    /*** Singleton ***/
    private static PaymentCashBodyController instance;

    public PaymentCashBodyController(){
        instance = this;
    }

    public static PaymentCashBodyController getInstance(){
        return instance;
    }
}