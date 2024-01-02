package com.example.car_dealership;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;


public class AdminPageController {
    boolean menuVisible = false;
    @FXML
    private VBox sideMenu;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView menuImg;
    @FXML
    private ImageView carImg;
    @FXML
    private Label carLabel;
    @FXML
    private ImageView clientImg;
    @FXML
    private Label clientLabel;
    @FXML
    private ImageView orderImg;
    @FXML
    private Label orderLabel;
    @FXML
    private ImageView logoutImg;

    public AdminPageController() {
    }

    @FXML
    private void initialize() {
        sideMenu.setTranslateX(-100);
        menuImg.setOnMouseClicked(this::toggleMenuVisibility);
        carImg.setOnMouseClicked(this::loadCarsPage);
        carLabel.setOnMouseClicked(this::loadCarsPage);
        clientImg.setOnMouseClicked(this::loadClientsPage);
        clientLabel.setOnMouseClicked(this::loadClientsPage);
        orderImg.setOnMouseClicked(this::loadOrdersPage);
        orderLabel.setOnMouseClicked(this::loadOrdersPage);
        logoutImg.setOnMouseClicked(this::handleLogoutClick);

    }

    private void loadClientsPage(MouseEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("view-clients.fxml"));
            Parent root = fxmlLoader.load();
            borderPane.setCenter(root);
    }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOrdersPage(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("orders-page.fxml"));
            Parent root = fxmlLoader.load();
            OrdersController ordersController = fxmlLoader.getController();
            borderPane.setCenter(root);
            ordersController.loadAdminOrders();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void loadCarsPage(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("view-cars.fxml"));
            Parent root = fxmlLoader.load();
            borderPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogoutClick(MouseEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Are you sure you want to logout?");
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-page.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) borderPane.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleMenuVisibility(MouseEvent event) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), sideMenu);
        if (menuVisible) {
            transition.setToX(-sideMenu.getWidth());
        } else {
            transition.setToX(0);
        }
        transition.play();
        menuVisible = !menuVisible;
    }

}
