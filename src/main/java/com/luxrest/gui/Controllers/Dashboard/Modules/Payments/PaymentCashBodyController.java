package com.luxrest.gui.Controllers.Dashboard.Modules.Payments;

import com.luxrest.gui.Controllers.Dashboard.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PaymentCashBodyController {
    @FXML
    public Label amountPayLabel;
    @FXML
    public Label receiveLabel;
    @FXML
    public Label changeLabel;
    public Double amountToPay = DashboardController.getInstance().getAndUpdatePrice();
    private Double receivedPrice = 0.0;

    public void initialize(){
        this.amountPayLabel.setText(this.amountToPay + "€");
        this.receiveLabel.setText("Awaits Insertion");
        this.changeLabel.setText("Awaits Insertion");
    }

    public void calculateChange(ActionEvent event) {
        Button button = (Button) event.getSource();
        //Get value of clicked button
        double value = Double.parseDouble(button.getText().replace("€", ""));

        this.receivedPrice += value;
        this.receiveLabel.setText( this.receivedPrice + "€");
        if(this.receivedPrice >= this.amountToPay)
            this.changeLabel.setText(this.receivedPrice - this.amountToPay + "€");
    }

    public void updateAmountToPay() {
        this.amountToPay = DashboardController.getInstance().getAndUpdatePrice();
        this.amountPayLabel.setText(this.amountToPay + "€");
        if(this.receivedPrice > 0)
            this.changeLabel.setText(this.receivedPrice - this.amountToPay + "€");
    }

    public void buttonClear() {
        this.receivedPrice = 0.0;
        this.receiveLabel.setText("0 €");
        this.changeLabel.setText("0 €");
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