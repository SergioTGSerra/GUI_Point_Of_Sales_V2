package com.luxrest.gui.Controllers.Dashboard;

import com.luxrest.gui.Auth;
import com.luxrest.gui.HttpConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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



            ImageView imageView = new ImageView();

            try {
                // Fazer a requisição HTTP para obter a imagem
                URL url = new URL("http://185.113.143.51:8081/api/v1/products/image/5");  // Substitua pela URL correta da imagem
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Adicionar cabeçalho de autorização
                connection.setRequestProperty("Authorization", "Bearer " + Auth.getInstance().getAccessToken());

                // Verificar o código de resposta da requisição
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Ler a imagem a partir do InputStream
                    InputStream inputStream = connection.getInputStream();
                    Image image = new Image(inputStream);

                    // Exibir a imagem no ImageView
                    imageView.setImage(image);
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(50);
                } else {
                    System.out.println("Falha na requisição: " + responseCode);
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Button btnProduct;
            if (imageView != null)
                btnProduct = new Button(object.get("price").toString() + "€\n" + object.get("name").toString(), imageView);
            else
                btnProduct = new Button(object.get("price").toString() + "€\n" + object.get("name").toString());


            btnProduct.getStyleClass().add("productButton");
            btnProduct.setId(object.get("id").toString());
            GridPane.setConstraints(btnProduct, colunaAtual, linhaAtual);
            products.getChildren().add(btnProduct);
            btnProduct.setOnAction(event -> DashboardController.getInstance().addProductToOrder(
                    Integer.parseInt(object.get("id").toString()),
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