package com.luxrest.gui.Controllers.Dashboard;

import com.luxrest.gui.Auth;
import com.luxrest.gui.HttpConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GridProductItemsController {
    @FXML
    public GridPane products;

    public void updateProductToGrid(int idCategory){
        products.getChildren().clear();
        JSONArray array = HttpConnection.Get("http://" + Auth.getInstance().getEndPoint() +  "/api/v1/products/category/"+idCategory, Auth.getInstance().getAccessToken());
        assert array != null;
        int colunas = 4;
        int linhaAtual = 0;
        int colunaAtual = 0;
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            Button btnProduct = new Button( object.get("price").toString()+"€\n"+object.get("name").toString());
            btnProduct.getStyleClass().add("productButton");
            btnProduct.setId(object.get("id").toString());
            GridPane.setConstraints(btnProduct, colunaAtual, linhaAtual);
            products.getChildren().add(btnProduct);
            btnProduct.setOnAction(event -> DashboardController.getInstance().addProductToOrder(
                    Long.parseLong(object.get("id").toString()),
                    object.get("name").toString(),
                    Double.parseDouble(object.get("price").toString())
            ));
            colunaAtual++;
            if (colunaAtual == colunas) {
                colunaAtual = 0;
                linhaAtual++;
            }
        }
    }

    /*** Singleton ***/
    private static GridProductItemsController instance;

    public GridProductItemsController(){
        instance = this;
    }

    public static GridProductItemsController getInstance(){
        return instance;
    }
}