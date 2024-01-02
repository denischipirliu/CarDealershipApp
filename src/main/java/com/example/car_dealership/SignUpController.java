package com.example.car_dealership;

import com.example.car_dealership.Application;
import com.example.car_dealership.User;
import com.example.car_dealership.dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class SignUpController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button signUpButton;
    @FXML
    private CheckBox adminCheckBox;
    @FXML
    private CheckBox clientCheckBox;
    @FXML
    private Button goBackButton;
    public SignUpController() {
    }
    public void initialize() {
        signUpButton.setOnAction(event -> {
            try {
                signup(event);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception according to your application's requirements
            }
        });
        adminCheckBox.setOnAction(this::handleCheck);
        clientCheckBox.setOnAction(this::handleCheck);
        goBackButton.setOnAction(this::goBack);
    }
    @FXML
    public void signup(ActionEvent event) throws SQLException {
        if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sign up failed!");
            alert.setHeaderText(null);
            alert.setContentText("Please enter username and password!");
            alert.showAndWait();
            return;
        }
        String role = getRole();
        User user = new User(usernameField.getText(), passwordField.getText(), role);
        UserDao userDao = new UserDao();
        userDao.insertUser(user);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign up successful!");
        alert.setHeaderText(null);
        alert.setContentText("Welcome, " + user.getUsername() + "!");
        alert.showAndWait();
    }
    public String getRole() {
        if (adminCheckBox.isSelected()) {
            return "admin";
        } else if (clientCheckBox.isSelected()) {
            return "client";
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sign-up failed!");
            alert.setHeaderText(null);
            alert.setContentText("Please select a role!");
            alert.showAndWait();
            return null;
        }
    }
    @FXML
    public void handleCheck(ActionEvent event) {
        switch (((CheckBox) event.getSource()).getId()) {
            case "adminCheckBox":
                if (adminCheckBox.isSelected()) {
                    clientCheckBox.setSelected(false);
                }
                break;
            case "clientCheckBox":
                if (clientCheckBox.isSelected()) {
                    adminCheckBox.setSelected(false);
                }
                break;
        }
    }
    @FXML
    public void goBack(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-page.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error opening sign up window!");
        }
    }
}
