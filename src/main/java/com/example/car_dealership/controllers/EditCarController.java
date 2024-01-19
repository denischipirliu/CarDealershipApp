package com.example.car_dealership.controllers;

import com.example.car_dealership.model.Car;
import com.example.car_dealership.model.CarStatus;
import com.example.car_dealership.model.dao.CarDao;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditCarController {
    @FXML
    private Button editCarButton;
    @FXML
    private ChoiceBox<String> makeChoiceBox;
    @FXML
    private ChoiceBox<String> modelChoiceBox;
    @FXML
    private TextField yearField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField colorField;
    @FXML
    private TextField mileageField;
    @FXML
    private TextField vinField;
    @FXML
    private TextField engineTypeField;
    @FXML
    private TextField transmissionTypeField;
    @FXML
    private TextField fuelTypeField;
    @FXML
    private TextField seatingCapacityField;
    @FXML
    private VBox checkboxContainer;
    private List<String> features;
    @FXML
    private Button selectImageButton;
    @FXML
    private ImageView carImageView;
    @FXML
    private Image carImage;
    private Car car;

    public EditCarController() {

    }

    @FXML
    private void initialize() {
        CarDao carDao = new CarDao();
        features = carDao.getFeaturesFromDatabase();
        List<String> makes = carDao.getMakesFromDatabase();
        makeChoiceBox.setItems(FXCollections.observableArrayList(makes));
        makeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            List<String> models = carDao.getModelsFromDatabase(newValue);
            modelChoiceBox.setItems(FXCollections.observableArrayList(models));
        });
        initializeCheckBoxes();
        selectImageButton.setOnAction(this::handleSelectImage);
        editCarButton.setOnAction(this::handleEditCarButton);
    }

    private void initializeCheckBoxes() {
        for (String feature : features) {
            CheckBox checkBox = new CheckBox(feature);
            checkboxContainer.getChildren().add(checkBox);
        }
    }
    @FXML
    private void initializeCarInformation() {
        makeChoiceBox.setValue(car.getMake());
        modelChoiceBox.setValue(car.getModel());
        yearField.setText(String.valueOf(car.getYear()));
        priceField.setText(String.valueOf(car.getPrice()));
        colorField.setText(car.getColor());
        mileageField.setText(String.valueOf(car.getMileage()));
        vinField.setText(car.getVin());
        engineTypeField.setText(car.getEngineType());
        transmissionTypeField.setText(car.getTransmissionType());
        fuelTypeField.setText(car.getFuelType());
        seatingCapacityField.setText(String.valueOf(car.getSeatingCapacity()));
        carImage = new Image(car.getImage());
        carImageView.setImage(carImage);
        List<String> features = car.getFeatures();
        for (int i = 0; i < checkboxContainer.getChildren().size(); i++) {
            CheckBox checkBox = (CheckBox) checkboxContainer.getChildren().get(i);
            if (features.contains(checkBox.getText())) {
                checkBox.setSelected(true);
            }
        }
    }

    @FXML
    private void handleEditCarButton(ActionEvent event) {
        try{String make = makeChoiceBox.getValue();
        String model = modelChoiceBox.getValue();
        int year = Integer.parseInt(yearField.getText());
        float price = Float.parseFloat(priceField.getText());
        String color = colorField.getText();
        int mileage = Integer.parseInt(mileageField.getText());
        String vin = vinField.getText();
        String engineType = engineTypeField.getText();
        String transmissionType = transmissionTypeField.getText();
        String fuelType = fuelTypeField.getText();
        int seatingCapacity = Integer.parseInt(seatingCapacityField.getText());
        String image = carImage.getUrl();
        List<String> features = new ArrayList<>();
        for (int i = 0; i < checkboxContainer.getChildren().size(); i++) {
            CheckBox checkBox = (CheckBox) checkboxContainer.getChildren().get(i);
            if (checkBox.isSelected()) {
                features.add(checkBox.getText());
            }
        }
        Car updatedCar = new Car(make, model, year, price, color, mileage, vin, engineType, transmissionType, fuelType, seatingCapacity, features, image, CarStatus.AVAILABLE);
        updatedCar.setId(car.getId());
        CarDao carDao = new CarDao();
        carDao.updateCar(updatedCar);
        Alert carAdded = new Alert(Alert.AlertType.INFORMATION);
        carAdded.setTitle("Car Updated");
        carAdded.setContentText("The car has been updated in the database.");
        carAdded.showAndWait();

        Stage currentStage = (Stage) editCarButton.getScene().getWindow();
        currentStage.close();}
        catch (SQLException throwables) {
            if (throwables.getSQLState().equals("23505") || throwables.getSQLState().equals("23514")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid input!");
                alert.showAndWait();
            }
        }
    }

    public void handleSelectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Car Image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        carImage = new Image(fileChooser.showOpenDialog(null).toURI().toString());
        carImageView.setImage(carImage);
    }

    public void setCar(Car car) {
        this.car = car;
        initializeCarInformation();
    }
}
