package com.example.car_dealership;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;


public class AdminPageController {
    @FXML
    private Button addCar;
    @FXML
    private Button viewCars;
    public AdminPageController() {
    }
    @FXML
    private void initialize() {
        addCar.setOnAction(this::handleAddCarClick);
        viewCars.setOnAction(this::handleViewCarsClick);
    }
    @FXML
    private void handleAddCarClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-car.fxml"));
            Parent root = fxmlLoader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleViewCarsClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view-cars.fxml"));
            Parent root = fxmlLoader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
