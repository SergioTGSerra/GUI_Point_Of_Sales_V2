package com.luxrest.gui.Controllers.Dashboard;

import com.luxrest.gui.App;
import com.luxrest.gui.Auth;
import com.luxrest.gui.Controllers.Dashboard.Modules.Orders.OrderItemController;
import com.luxrest.gui.Controllers.Dashboard.Modules.Payments.PaymentCashBodyController;
import com.luxrest.gui.Controllers.Dashboard.Modules.Payments.PaymentHeaderController;
import com.luxrest.gui.HttpConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardController {
    @FXML
    public VBox categories;
    @FXML
    public VBox order;
    @FXML
    public VBox MidColunm;
    @FXML
    public Button buttonPay;
    @FXML
    public GridPane dashboardContainer;
    private int contador;

    public void initialize() {
        this.createCategoryBtns();
    }

    private void createCategoryBtns() {
        JSONArray array = HttpConnection.Get("http://" + Auth.getInstance().getEndPoint() +  "/api/v1/categories", Auth.getInstance().getAccessToken());
        assert array != null;
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            Button btnCategory = new Button(object.get("name").toString());
            categories.getChildren().add(btnCategory);
            btnCategory.setId(object.get("id").toString());
            //Atualiza produtos da Grid desta categoria
            btnCategory.setOnAction(event -> {
                this.contador = 0;
                order.setDisable(false);
                MidColunm.getChildren().clear();
                btnCategory.getStyleClass().add("selectedButton");
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Dashboard/Modules/GridProductItems.fxml"));
                try {
                    GridPane root = fxmlLoader.load();
                    GridProductItemsController.getInstance().addProductToGrid(Integer.parseInt(btnCategory.getId()));
                    MidColunm.getChildren().add(root);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void addProductToOrder(Long id, String name, Double price){
        Node existingItem = order.lookup("#" + id);
        if (existingItem != null) {
            OrderItemController oic = (OrderItemController) existingItem.getUserData();
            oic.incrementQnt();
            calculateTotalPrice(order, buttonPay);
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Dashboard/Modules/Orders/OrderItem.fxml"));
            try {
                VBox item = fxmlLoader.load();
                OrderItemController oic = fxmlLoader.getController();
                oic.setData(id, name, price);
                item.setId(String.valueOf(id));
                item.setUserData(oic);
                order.getChildren().add(item);
                calculateTotalPrice(order, buttonPay);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Double calculateTotalPrice(VBox order, Button buttonPay) {
        double cont = 0;
        for (Node node : order.getChildren()) {
            OrderItemController oic = (OrderItemController) node.getUserData();
            cont += oic.getQuantity() * oic.getPrice();
        }
        if(cont != 0) {
            buttonPay.setText(String.valueOf(cont));
//             paymentHeaderController.setDashboardController(this);
//             paymentHeaderController.initialize(cont);
            return cont;
        }
        else buttonPay.setText("Pay");
        return 0.0;
    }

    public Double getPrice(){
        Double price = calculateTotalPrice(order, buttonPay);
        setPriceInPayment(price); // aqui chamamos o novo método criado
        return price;
    }

    public void setPriceInPayment(Double price) {
        PaymentCashBodyController.getInstance().setPrice(price);
    }


    public void deleteOrderAction() {
        order.getChildren().clear();
        calculateTotalPrice(order, buttonPay);
    }


    public void openPayment() {
        Double price = this.calculateTotalPrice(order, buttonPay);
        order.setDisable(true);
        if (price == 0) return;
        MidColunm.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Dashboard/Modules/Payments/PaymentHeader.fxml"));
        try {
            VBox root = fxmlLoader.load();
            PaymentHeaderController controller = fxmlLoader.getController();
            controller.initialize(price);
            MidColunm.getChildren().add(root);
            //resetColorButtonSelected();
            openPaymentSucessful();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openPaymentSucessful() {
        this.contador++;
        if(this.contador == 1) buttonPay.setText("Pay");
        if (this.contador == 2) {
            this.contador = 0;
            MidColunm.getChildren().clear();
            FXMLLoader fxmlLoaderSuccessful = new FXMLLoader(App.class.getResource("Dashboard/Modules/Payments/paymentSuccessful.fxml"));
            try {
                VBox rootSuccessful = fxmlLoaderSuccessful.load();
                MidColunm.getChildren().add(rootSuccessful);
                order.setDisable(true);
                setTimer();
                //order para a db
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Timer setTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    MidColunm.getChildren().clear();
                    order.getChildren().clear();
                    calculateTotalPrice(order, buttonPay);
                    order.setDisable(false);
                    //retornar aos produtos mais usados
                });
            }
        }, 2600);
        return timer;
    }

    /*** Singleton ***/
    private static DashboardController instance;

    public DashboardController(){
        instance = this;
    }

    public static DashboardController getInstance(){
        return instance;
    }
}
