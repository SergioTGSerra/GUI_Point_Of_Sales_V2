package com.luxrest.gui.Controllers.Dashboard.Modules.Orders;

import com.luxrest.gui.Controllers.Dashboard.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class OrderItemController {
    private Double price;
    private int quantity;
    @FXML
    public Label productName;
    @FXML
    public Label priceLabel;
    @FXML
    public Label quantityLabel;
    @FXML
    public Button remove;
    @FXML
    public Button increment;
    @FXML
    public Button decrease;

    public void setData(Long id, String name, Double price){
        quantity = 1;
        this.productName.setText(name);
        this.price = price;
        this.priceLabel.setText(price+" €");
        this.quantityLabel.setText(String.valueOf(quantity));
        remove.setOnAction(this::removeProduct);
        decrease.setOnAction(this::decreaseQnt);
        increment.setOnAction(this::incrementQnt);
    }

    public void removeProduct(ActionEvent event) {
        // get the button that was clicked
        Button button = (Button) event.getSource();
        // get the parent VBox of element
        VBox itemVbox = (VBox) button.getParent().getParent();
        // get the VBox Parent of the itemVbox
        VBox orderVBox = (VBox) itemVbox.getParent();
        // get the 3 vbox dashboard right
        VBox dashboardVbox3 = (VBox) orderVBox.getParent().getParent().getParent().getParent();
        Button buttonPay = (Button) dashboardVbox3.lookup("#buttonPay");
        orderVBox.getChildren().remove(itemVbox);
        DashboardController.getInstance().getAndUpdatePrice();
    }

    public void incrementQnt(ActionEvent event) {
        // get the button that was clicked
        Button button = (Button) event.getSource();
        // get the parent VBox of element
        VBox itemVbox = (VBox) button.getParent().getParent();
        // get the VBox Parent of the itemVbox
        VBox orderVBox = (VBox) itemVbox.getParent();
        // get the 3 vbox dashboard right
        VBox dashboardVbox3 = (VBox) orderVBox.getParent().getParent().getParent().getParent();
        Button buttonPay = (Button) dashboardVbox3.lookup("#buttonPay");
        incrementQnt();
        DashboardController.getInstance().getAndUpdatePrice();
    }

    public void incrementQnt() {
        this.quantity++;
        this.quantityLabel.setText(String.valueOf(quantity));
        this.priceLabel.setText(this.price * quantity +" €");
    }

    public void decreaseQnt(ActionEvent event) {
        // get the button that was clicked
        Button button = (Button) event.getSource();
        // get the parent VBox of element
        VBox itemVbox = (VBox) button.getParent().getParent();
        // get the VBox Parent of the itemVbox
        VBox orderVBox = (VBox) itemVbox.getParent();
        // get the 3 vbox dashboard right
        VBox dashboardVbox3 = (VBox) orderVBox.getParent().getParent().getParent().getParent();
        Button buttonPay = (Button) dashboardVbox3.lookup("#buttonPay");
        if(this.quantity == 1){
            removeProduct(event);
            return;
        }
        this.quantity--;
        this.quantityLabel.setText(String.valueOf(quantity));
        this.priceLabel.setText(this.price * this.quantity +" €");
        DashboardController.getInstance().getAndUpdatePrice();
    }

    public Double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}