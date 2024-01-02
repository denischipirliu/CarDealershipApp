package com.example.car_dealership;

import com.example.car_dealership.Car;
import com.example.car_dealership.dao.CarDao;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddCarController {
    @FXML
    private Button addCarButton;
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
    @FXML
    private Button backButton;
    @FXML
    private Button viewCarsButton;

    public AddCarController() {

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
        addCarButton.setOnAction(this::handleAddCarButton);
    }

    private void initializeCheckBoxes() {
        for (String feature : features) {
            CheckBox checkBox = new CheckBox(feature);
            checkboxContainer.getChildren().add(checkBox);
        }
    }

    @FXML
    private void handleAddCarButton(ActionEvent event) {
        String make = makeChoiceBox.getValue();
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
        Car newCar = new Car(make, model, year, price, color, mileage, vin, engineType, transmissionType, fuelType, seatingCapacity, features, image, CarStatus.AVAILABLE);
        CarDao carDao = new CarDao();
        carDao.addCar(newCar);
        Alert carAdded = new Alert(Alert.AlertType.INFORMATION);
        carAdded.setTitle("Car Added");
        carAdded.setContentText("The car has been added to the database.");
        carAdded.showAndWait();
    }

    public void handleSelectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Car Image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        carImage = new Image(fileChooser.showOpenDialog(null).toURI().toString());
        carImageView.setImage(carImage);
    }
}
