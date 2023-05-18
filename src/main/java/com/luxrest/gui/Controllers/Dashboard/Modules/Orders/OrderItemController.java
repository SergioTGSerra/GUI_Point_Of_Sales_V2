package com.luxrest.gui.Controllers.Dashboard.Modules.Orders;

import com.luxrest.gui.Controllers.Dashboard.DashboardController;
import com.luxrest.gui.Controllers.Dashboard.Modules.Payments.PaymentCashBodyController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class OrderItemController {
    public ImageView productImage;
    private Integer productId;
    private Double price;
    private Integer quantity;
    @FXML
    public Label productName;
    @FXML
    public Label priceLabel;
    @FXML
    public Label quantityLabel;

    public void setData(Integer id, String name, Double price, ImageView imageView){
        this.quantity = 1;
        this.productId = id;
        this.productName.setText(name);
        this.price = price;
        this.priceLabel.setText(price+" €");
        this.quantityLabel.setText(String.valueOf(this.quantity));
        if(imageView.getImage() != null)
            this.productImage.setImage(imageView.getImage());
    }

    public void removeProduct(ActionEvent event) {
        // get the button that was clicked
        Button button = (Button) event.getSource();
        // get the parent VBox of element
        VBox itemVbox = (VBox) button.getParent().getParent();

        DashboardController.getInstance().order.getChildren().remove(itemVbox);
        updatePriceGlobal();
    }

    public void incrementQnt() {
        this.quantity++;
        this.quantityLabel.setText(String.valueOf(this.quantity));
        this.priceLabel.setText(this.price * this.quantity +" €");
        updatePriceGlobal();
    }

    public void decreaseQnt(ActionEvent event) {
        if(this.quantity == 1){
            removeProduct(event);
            return;
        }
        this.quantity--;
        this.quantityLabel.setText(String.valueOf(this.quantity));
        this.priceLabel.setText(this.price * this.quantity +" €");
        updatePriceGlobal();
    }

    public Integer getProductId() {
        return this.productId;
    }

    public Double getPrice() {
        return this.price;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void updatePriceGlobal() {
        DashboardController.getInstance().getAndUpdatePrice();
        if(PaymentCashBodyController.getInstance() != null)
            PaymentCashBodyController.getInstance().updateAmountToPay();
    }
}