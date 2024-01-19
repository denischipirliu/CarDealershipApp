package com.example.car_dealership.controllers;

import com.example.car_dealership.Application;
import com.example.car_dealership.model.dao.ClientDao;
import com.example.car_dealership.model.Client;
import com.example.car_dealership.model.User;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class ClientPageController {
    @FXML
    private BorderPane borderPane;
    private User user;
    private Client client;
    @FXML
    private VBox sideMenu;
    @FXML
    private ImageView menuImg;
    private boolean menuVisible = false;
    @FXML
    private ImageView profileImg;
    @FXML
    private Label profileLabel;
    @FXML
    private ImageView carImg;
    @FXML
    private Label carLabel;
    @FXML
    private ImageView orderImg;
    @FXML
    private Label orderLabel;
    @FXML
    private ImageView contactImg;
    @FXML
    private Label contactLabel;
    @FXML
    private ImageView logoutImg;


    public ClientPageController() {
    }

    public void setUser(User user) {
        this.user = user;

    }

    @FXML
    private void initialize() {
        sideMenu.setTranslateX(-100);
        menuImg.setOnMouseClicked(this::toggleMenuVisibility);
        profileImg.setOnMouseClicked(this::loadProfilePage);
        profileLabel.setOnMouseClicked(this::loadProfilePage);
        carImg.setOnMouseClicked(this::loadCarsPage);
        carLabel.setOnMouseClicked(this::loadCarsPage);
        orderImg.setOnMouseClicked(this::loadOrdersPage);
        orderLabel.setOnMouseClicked(this::loadOrdersPage);
        contactImg.setOnMouseClicked(this::loadContactPage);
        contactLabel.setOnMouseClicked(this::loadContactPage);
        logoutImg.setOnMouseClicked(this::handleLogoutClick);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client initializeClient(User user) throws SQLException {
        ClientDao clientDao = new ClientDao();
        client = clientDao.getClient(user.getId());
        return client;
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

    @FXML
    private void loadProfilePage(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/example/car_dealership/fxml/profile-page.fxml"));
            Parent root = fxmlLoader.load();
            ProfileController profileController = fxmlLoader.getController();
            profileController.setClient(initializeClient(user));
            borderPane.setCenter(root);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadCarsPage(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/example/car_dealership/fxml/view-cars.fxml"));
            Parent root = fxmlLoader.load();
            ViewCarsController viewCarsController = fxmlLoader.getController();
            viewCarsController.setClient(initializeClient(user));
            borderPane.setCenter(root);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadOrdersPage(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/example/car_dealership/fxml/orders-page.fxml"));
            Parent root = fxmlLoader.load();
            OrdersController ordersController = fxmlLoader.getController();
            ordersController.setClient(initializeClient(user));
            borderPane.setCenter(root);
            ordersController.loadClientOrders();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadContactPage(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/example/car_dealership/fxml/contact-page.fxml"));
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
            if(alert.getResult().getText().equals("OK")) {
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/example/car_dealership/fxml/login-page.fxml"));
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
}
