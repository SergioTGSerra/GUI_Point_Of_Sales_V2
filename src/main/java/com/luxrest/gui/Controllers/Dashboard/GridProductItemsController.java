package com.luxrest.gui.Controllers.Dashboard;

import com.luxrest.gui.Auth;
import com.luxrest.gui.HttpConnection;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

public class GridProductItemsController {
    @FXML
    public GridPane products;

    public void updateProductToGrid(int idCategory){
        products.getChildren().clear();
        JSONArray array = HttpConnection.Get(Auth.getInstance().getEndPoint() +  "/api/v1/products/category/"+idCategory, Auth.getInstance().getAccessToken());

        assert array != null;
        int colunas = 4;
        int linhaAtual = 0;
        int colunaAtual = 0;
        for (Object o : array) {
            JSONObject object = (JSONObject) o;


            ImageView imageView = new ImageView();
            if (object.get("image") != null) {
                InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode((String) object.get("image")));
                Image image = new Image(inputStream);
                imageView.setImage(image);
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);

            }

            Button btnProduct;
            if (imageView.getImage() != null) {
                VBox vBox = new VBox(imageView, new Label("\n" + object.get("price").toString() + "€\n" + object.get("name").toString()));
                vBox.setAlignment(Pos.CENTER);
                btnProduct = new Button();
                btnProduct.setGraphic(vBox);
            } else {
                btnProduct = new Button("\n" + object.get("price").toString() + "€\n" + object.get("name").toString());
            }

            btnProduct.getStyleClass().add("productButton");
            btnProduct.setId(object.get("id").toString());
            GridPane.setConstraints(btnProduct, colunaAtual, linhaAtual);
            products.getChildren().add(btnProduct);
            btnProduct.setOnAction(event -> DashboardController.getInstance().addProductToOrder(
                    Integer.parseInt(object.get("id").toString()),
                    object.get("name").toString(),
                    Double.parseDouble(object.get("price").toString()),
                    imageView
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