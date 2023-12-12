package com.example.car_dealership;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.prefs.Preferences;

public class ViewCarsController {
    @FXML
    private VBox carContainer;
    private List<Car> cars;

    public ViewCarsController() {

    }

    @FXML
    private void initialize() {
        initializeCarsContainer();
    }
    private void clearCarContainer() {
        carContainer.getChildren().clear();
    }

    private void displayCarInformation(Car car) {
        HBox makeAndModel = new HBox();
        makeAndModel.setSpacing(5);
        Label makeLabel = new Label(car.getMake());
        Label modelLabel = new Label(car.getModel());
        makeAndModel.getChildren().addAll(makeLabel, modelLabel);
        HBox details = new HBox();
        Label yearLabel = new Label("Year: " + String.valueOf(car.getYear()));
        Label priceLabel = new Label("Price: " + String.valueOf(car.getPrice()) + " €");
        Label mileageLabel = new Label("Mileage: " + String.valueOf(car.getMileage()) + " km");
        details.setSpacing(10);
        details.getChildren().addAll(yearLabel, priceLabel, mileageLabel);
        Image carImage = new Image(car.getImage());
        ImageView carImageView = new ImageView(carImage);
        carImageView.setFitWidth(200);
        carImageView.setFitHeight(150);

        Button editButton = new Button("Edit");
        editButton.setOnAction(actionEvent -> {
            Preferences preferences = Preferences.userNodeForPackage(EditCarController.class);
            preferences.put("car", car.getVin());
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-car.fxml"));
                Parent root = fxmlLoader.load();
                Stage editStage = new Stage();
                editStage.initModality(Modality.APPLICATION_MODAL);
                editStage.setTitle("Edit Car");
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                editStage.setScene(scene);
                editStage.showAndWait();
                initializeCarsContainer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Car");
            alert.setHeaderText("Are you sure you want to delete this car?");
            alert.setContentText("This action cannot be undone.");
            alert.showAndWait();
            if(alert.getResult().getText().equals("OK")) {
                CarDao carDao = new CarDao();
                carDao.deleteCar(car);
                carContainer.getChildren().clear();
                initializeCarsContainer();
            }
        });

        carContainer.getChildren().addAll(carImageView, makeAndModel, details, editButton, deleteButton);
    }

    private void initializeCarsContainer() {
        try {
            CarDao carDao = new CarDao();
            cars = carDao.getCarsFromDatabase();
            clearCarContainer();
            for (Car car : cars) {
                displayCarInformation(car);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
