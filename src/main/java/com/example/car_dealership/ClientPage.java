package com.example.car_dealership;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientPage {
    @FXML
    private MenuItem editProfile;
    public ClientPage() {
    }
    @FXML
    private void initialize() {
        editProfile.setOnAction(this::handleEditClick);
    }

    @FXML
    private void handleEditClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-profile.fxml"));
            Parent root = fxmlLoader.load();
            Stage editProfileStage = new Stage();
            editProfileStage.initModality(Modality.APPLICATION_MODAL);
            editProfileStage.getIcons().add(new Image(Application.class.getResourceAsStream("icon.png")));
            editProfileStage.setTitle("Edit Profile");
            editProfileStage.setScene(new Scene(root));
            root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            editProfileStage.showAndWait();;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
