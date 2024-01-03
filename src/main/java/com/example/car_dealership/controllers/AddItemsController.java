package com.example.car_dealership.controllers;

import com.example.car_dealership.model.dao.CarDao;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

public class AddItemsController {
    @FXML
    private TextField makeField;
    @FXML
    private TextField modelField;
    @FXML
    private ChoiceBox<String> modelChoiceBox;
    @FXML
    private TextField featureField;
    @FXML
    private Button addMakeButton;
    @FXML
    private Button addModelButton;
    @FXML
    private Button addFeatureButton;

    public AddItemsController() {
    }

    @FXML
    private void initialize() {
        initializeModelChoiceBox();
        addMakeButton.setOnAction(this::handleAddMakeButton);
        addModelButton.setOnAction(this::handleAddModelButton);
        addFeatureButton.setOnAction(this::handleAddFeatureButton);
    }

    private void handleAddMakeButton(ActionEvent event) {
        try {
            if (makeField.getText().isEmpty()) {
                throw new Exception();
            } else {
                CarDao carDAO = new CarDao();
                carDAO.addMake(makeField.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Make added successfully!");
                alert.showAndWait();
                makeField.clear();
            }
        } catch (SQLException throwables) {
            if (throwables.getSQLState().equals("23505")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Make already exists!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Make field cannot be empty!");
            alert.showAndWait();
        }
    }

    private void handleAddModelButton(ActionEvent event) {
        try {
            if (modelField.getText().isEmpty() || modelChoiceBox.getValue().isEmpty()) {
                throw new Exception();
            } else {
                CarDao carDAO = new CarDao();
                carDAO.addModel(modelField.getText(), modelChoiceBox.getValue());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Model added successfully!");
                alert.showAndWait();
                modelField.clear();
            }
        } catch (SQLException throwables) {
            if (throwables.getSQLState().equals("23505")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Model already exists!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Model field cannot be empty!");
            alert.showAndWait();
        }
    }

    private void handleAddFeatureButton(ActionEvent event) {
        try {
            if (featureField.getText().isEmpty()) {
                throw new Exception();
            } else {
                CarDao carDAO = new CarDao();
                carDAO.addFeature(featureField.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Feature added successfully!");
                alert.showAndWait();
                featureField.clear();
            }
        } catch (SQLException throwables) {
            if (throwables.getSQLState().equals("23505")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Feature already exists!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Feature field cannot be empty!");
            alert.showAndWait();
        }
    }

    private void initializeModelChoiceBox() {
        CarDao carDao = new CarDao();
        List<String> makes = carDao.getMakesFromDatabase();
        modelChoiceBox.setItems(FXCollections.observableArrayList(makes));
    }

}
