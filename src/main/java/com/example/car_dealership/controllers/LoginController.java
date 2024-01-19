package com.example.car_dealership.controllers;

import com.example.car_dealership.Application;
import com.example.car_dealership.model.User;
import com.example.car_dealership.model.dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button signUpButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(this::login);
        signUpButton.setOnAction(this::signUp);
    }

    public LoginController() {
    }

    public void login(ActionEvent event) {
        UserDao userDao = new UserDao();
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login failed!");
            alert.setHeaderText(null);
            alert.setContentText("Please enter username and password!");
            alert.showAndWait();
            return;
        }
        User user = userDao.getUser(usernameField.getText());
        if(user == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login failed!");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password!");
            alert.showAndWait();
            return;
        }
        user.setPassword(passwordField.getText());
        boolean flag = userDao.validate(user);
        String role = userDao.getUserRole(user);

        if (flag) {


            if (role != null) {
                switch (role) {
                    case "admin":
                        Alert alertAdmin = new Alert(Alert.AlertType.INFORMATION);
                        alertAdmin.setTitle("Login successful!");
                        alertAdmin.setHeaderText(null);
                        alertAdmin.setContentText("Admin " + user.getUsername() + " logged in!");
                        alertAdmin.showAndWait();
                        loadPage("/com/example/car_dealership/fxml/admin-page.fxml");
                        break;
                    case "client":
                        Alert alertClient = new Alert(Alert.AlertType.INFORMATION);
                        alertClient.setTitle("Login successful!");
                        alertClient.setHeaderText(null);
                        alertClient.setContentText("Welcome, " + user.getUsername() + "!");
                        alertClient.showAndWait();
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/example/car_dealership/fxml/client-page.fxml"));
                            Parent root = fxmlLoader.load();
                            ClientPageController clientPageController = fxmlLoader.getController();
                            clientPageController.setUser(user);

                            Stage currentStage = (Stage) loginButton.getScene().getWindow();
                            currentStage.close();

                            Stage newStage = new Stage();
                            newStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("icons/icon.png"))));
                            newStage.setTitle("Car Dealership");
                            newStage.setScene(new Scene(root));
                            newStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login failed!");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password!");
            alert.showAndWait();
        }
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(fxmlFile));
            Parent root = fxmlLoader.load();

            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

            Stage newStage = new Stage();
            newStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("icons/icon.png"))));
            newStage.setTitle("Car Dealership");
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error opening new window!");
        }
    }


    public void signUp(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/example/car_dealership/fxml/sign-up.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error opening sign up window!");
        }
    }
}