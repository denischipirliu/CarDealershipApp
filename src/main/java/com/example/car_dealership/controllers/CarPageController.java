package com.example.car_dealership.controllers;

import com.example.car_dealership.Application;
import com.example.car_dealership.model.Car;
import com.example.car_dealership.model.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CarPageController {
    @FXML
    private ImageView carImage;
    @FXML
    private Button buyButton;
    @FXML
    private Button backButton;
    @FXML
    private Label carName;
    @FXML
    private Label carDetails;
    @FXML
    private Label carPrice;
    @FXML
    private Label carMake;
    @FXML
    private Label carModel;
    @FXML
    private Label carYear;
    @FXML
    private Label carMileage;
    @FXML
    private Label carColor;
    @FXML
    private Label carVIN;
    @FXML
    private Label carEngine;
    @FXML
    private Label carTransmission;
    @FXML
    private Label carSeats;
    @FXML
    private Label carFuel;
    @FXML
    private VBox carFeatures;
    private Client client;
    private Car car;

    public CarPageController() {
    }

    @FXML
    private void initialize() {
        buyButton.setOnAction(this::handleBuyButton);
        backButton.setOnAction(this::handleBackButton);
    }

    public void setCar(Car car) {
        this.car = car;
        Image image = new Image(car.getImage());
        carImage.setImage(image);
        carName.setText(car.getMake() + " " + car.getModel());
        carDetails.setText(car.getYear() + "·" + car.getMileage() + " km·" + car.getEngineType() + "·" + car.getFuelType());
        carPrice.setText(car.getPrice() + " €");
        carMake.setText("Make: " + car.getMake());
        carModel.setText("Model: " + car.getModel());
        carYear.setText("Year: " + String.valueOf(car.getYear()));
        carMileage.setText("Mileage: " + String.valueOf(car.getMileage()));
        carColor.setText("Color: " + car.getColor());
        carEngine.setText("Engine: " + car.getEngineType());
        carFuel.setText("Fuel: " + car.getFuelType());
        carTransmission.setText("Transmission: " + car.getTransmissionType());
        carSeats.setText("Seats: " + String.valueOf(car.getSeatingCapacity()));
        carVIN.setText("VIN: " + car.getVin());
        for (String feature : car.getFeatures()) {
            Label featureLabel = new Label(feature);
            carFeatures.getChildren().add(featureLabel);
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }
    private void handleBuyButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/fxml/buy-car.fxml"));
            Parent root = fxmlLoader.load();
            BuyCarController buyCarController = fxmlLoader.getController();
            buyCarController.setCarAndClient(car, client);
            Stage stage = new Stage();
            stage.setTitle("Buy Car");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error loading cars");
            alert.setContentText("Please try again later.");
            alert.showAndWait();
        }
    }
    private void handleBackButton(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/fxml/view-cars.fxml"));
            Parent root = fxmlLoader.load();
            ViewCarsController viewCarsController = fxmlLoader.getController();
            viewCarsController.setClient(client);
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            borderPane.setCenter(root);

        }catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error loading cars");
            alert.setContentText("Please try again later.");
            alert.showAndWait();
        }
    }
}
