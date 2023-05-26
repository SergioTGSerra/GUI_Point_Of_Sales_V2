package com.luxrest.gui.Controllers.Dashboard;

import com.luxrest.gui.App;
import com.luxrest.gui.Auth;
import com.luxrest.gui.Controllers.Dashboard.Modules.Orders.OrderItemController;
import com.luxrest.gui.HttpConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
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

    private Boolean paymentOpen = false;

    public void initialize() {
        this.createCategoryBtns();
    }

    private void createCategoryBtns() {
        JSONArray array = HttpConnection.Get(Auth.getInstance().getEndPoint() +  "/api/v1/categories", Auth.getInstance().getAccessToken());
        assert array != null;
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            Button btnCategory = new Button(object.get("name").toString());
            btnCategory.getStyleClass().add("categoryButton");
            categories.getChildren().add(btnCategory);
            btnCategory.setId(object.get("id").toString());
            //Atualiza os produtos da grid quando clicado no butão da categoria
            btnCategory.setOnAction(event -> {
                MidColunm.getChildren().clear();
                paymentOpen = false;
                categories.getChildren().forEach(node -> node.getStyleClass().remove("categoryButtonSelected"));
                btnCategory.getStyleClass().add("categoryButtonSelected");
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Dashboard/Modules/GridProductItems.fxml"));
                try {
                    GridPane root = fxmlLoader.load();
                    GridProductItemsController.getInstance().updateProductToGrid(Integer.parseInt(btnCategory.getId()));
                    MidColunm.getChildren().add(root);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void addProductToOrder(Integer id, String name, Double price, ImageView imageView){
        Node existingItem = order.lookup("#" + id);
        if (existingItem != null) {
            OrderItemController oic = (OrderItemController) existingItem.getUserData();
            oic.incrementQnt();
            getAndUpdatePrice();
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Dashboard/Modules/Orders/OrderItem.fxml"));
            try {
                VBox item = fxmlLoader.load();
                OrderItemController oic = fxmlLoader.getController();
                oic.setData(id, name, price, imageView);
                item.setId(String.valueOf(id));
                item.setUserData(oic);
                order.getChildren().add(item);
                getAndUpdatePrice();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Double getAndUpdatePrice() {
        double cont = 0;
        for (Node node : order.getChildren()) {
            OrderItemController oic = (OrderItemController) node.getUserData();
            cont += oic.getQuantity() * oic.getPrice();
        }
        if(cont != 0) {
            buttonPay.setText(cont + " €");
            return cont;
        }
        else{
            buttonPay.setText("Pay");
            return 0.0;
        }
    }

    public void deleteOrderAction() {
        if(paymentOpen){
            MidColunm.getChildren().clear();
            paymentOpen = false;
        }
        order.getChildren().clear();
        getAndUpdatePrice();
    }

    public void buttonPaymentAction(){
        if(paymentOpen){
            saveProductsDB();
            openPaymentSucessful();
            paymentOpen = false;
        }else{
            paymentOpen = true;
            openPaymentHeader();
        }
    }

    private void saveProductsDB() {
        JSONObject newOrder = new JSONObject();
        newOrder.put("orderNote", "GUI Order");
        newOrder.put("orderStatus", "PROCESSING");
        newOrder.put("paymentMethod", "CASH");

        JSONArray orderLineArray = new JSONArray();

        for (Node node : order.getChildren()) {
            OrderItemController orderItemController = (OrderItemController) node.getUserData();


            JSONObject orderLine = new JSONObject();
            orderLine.put("quantity", orderItemController.getQuantity());
            orderLine.put("product", orderItemController.getProductId());

            orderLineArray.add(orderLine);
        }

        newOrder.put("orderLine", orderLineArray);

        HttpConnection.Post(Auth.getInstance().getEndPoint() +"/api/v1/orders", newOrder, Auth.getInstance().getAccessToken());
    }

    public void openPaymentHeader() {
        MidColunm.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Dashboard/Modules/Payments/PaymentHeader.fxml"));
        try {
            VBox root = fxmlLoader.load();
            MidColunm.getChildren().add(root);
            categories.getChildren().forEach(node -> node.getStyleClass().remove("categoryButtonSelected"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openPaymentSucessful() {
        MidColunm.getChildren().clear();
        order.setDisable(true);
        FXMLLoader fxmlLoaderSuccessful = new FXMLLoader(App.class.getResource("Dashboard/Modules/Payments/paymentSuccessful.fxml"));
        try {
            VBox rootSuccessful = fxmlLoaderSuccessful.load();
            MidColunm.getChildren().add(rootSuccessful);
            animationPayment();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void animationPayment() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    MidColunm.getChildren().clear();
                    order.getChildren().clear();
                    order.setDisable(false);
                    getAndUpdatePrice();
                    paymentOpen = false;
                });
            }
        }, 2600);
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
