package com.example.car_dealership;

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
import java.util.prefs.*;

import java.io.IOException;

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
        User user = new UserDao().getUser(usernameField.getText());
        UserDao userDao = new UserDao();
        boolean flag = userDao.validate(user);
        String role = userDao.getUserRole(user);

        if (flag) {
            System.out.println("Login successful!");

            if (role != null) {
                switch (role) {
                    case "admin":
                        Alert alertAdmin = new Alert(Alert.AlertType.INFORMATION);
                        alertAdmin.setTitle("Login successful!");
                        alertAdmin.setHeaderText(null);
                        alertAdmin.setContentText("Admin " + user.getUsername() + " logged in!");
                        alertAdmin.showAndWait();
                        loadPage("admin-page.fxml");
                        break;
                    case "client":
                        Preferences userPreferences = Preferences.userRoot();
                        userPreferences.put("username", user.getUsername());
                        userPreferences.put("password", user.getPassword());
                        userPreferences.put("role", user.getRole());
                        Alert alertClient = new Alert(Alert.AlertType.INFORMATION);
                        alertClient.setTitle("Login successful!");
                        alertClient.setHeaderText(null);
                        alertClient.setContentText("Welcome, " + user.getUsername() + "!");
                        alertClient.showAndWait();
                        loadPage("client-page.fxml");
                        break;
                    default:
                        System.out.println("Unrecognized role: " + role);
                        break;
                }
            } else {
                System.out.println("Role not found for user: " + user.getUsername());
            }
        } else {
            System.out.println("Invalid username or password!");
        }
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(fxmlFile));
            Parent root = fxmlLoader.load();

            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

            Stage newStage = new Stage();
            newStage.getIcons().add(new Image(Application.class.getResourceAsStream("icon.png")));
            newStage.setTitle("Car Dealership");
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error opening new window!");
        }
    }


    public void signUp (ActionEvent event){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("sign-up.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) signUpButton.getScene().getWindow();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error opening sign up window!");
            }
        }
    }