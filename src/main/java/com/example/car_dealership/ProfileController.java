package com.example.car_dealership;

import com.example.car_dealership.dao.ClientDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public class ProfileController {
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
    private Client client;


    public ProfileController() {
    }
    @FXML
    public void setClient(Client client) {
        this.client = client;
        if(client != null) {
            firstName.setText(client.getFirstName());
            lastName.setText(client.getLastName());
            email.setText(client.getEmail());
            phoneNumber.setText(client.getPhoneNumber());
            address.setText(client.getAddress());
        }
        else {
            firstName.setText("");
            lastName.setText("");
            email.setText("");
            phoneNumber.setText("");
            address.setText("");
        }
    }
    @FXML
    public void initialize() {
        saveButton.setOnAction(this::handleSaveClick);
    }
    @FXML
    private void handleSaveClick(ActionEvent event) {
        ClientDao clientDao = new ClientDao();
        client.setFirstName(firstName.getText());
        client.setLastName(lastName.getText());
        client.setEmail(email.getText());
        client.setPhoneNumber(phoneNumber.getText());
        client.setAddress(address.getText());
        try {
            clientDao.updateClient(client);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
