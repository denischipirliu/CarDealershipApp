package com.example.car_dealership;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public class EditProfilePage {
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField address;
    @FXML
    private Button saveButton;
    public EditProfilePage() {
    }
    @FXML
    public void showProfile() {
        ClientDao clientDao = new ClientDao();
        Client client = new Client(null, null, null, null, null);
        clientDao.showProfile(client);
        firstName.setText(client.getFirstName());
        lastName.setText(client.getLastName());
        email.setText(client.getEmail());
        phoneNumber.setText(client.getPhoneNumber());
        address.setText(client.getAddress());
    }
    @FXML
    public void updateProfile(ActionEvent event) {
        try {
        Client client = new Client(firstName.getText(), lastName.getText(), email.getText(), phoneNumber.getText(), address.getText());
        ClientDao clientDao = new ClientDao();
        clientDao.updateClient(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void initialize() {
        showProfile();
        saveButton.setOnAction(this::updateProfile);
    }
}
