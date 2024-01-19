package com.example.car_dealership;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/example/car_dealership/fxml/login-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("icons/icon.png"))));
        stage.setTitle("Car Dealership");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}