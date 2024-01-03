package com.example.car_dealership.controllers;

import com.example.car_dealership.model.Client;
import com.example.car_dealership.model.dao.ClientDao;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.util.List;

public class ViewClientsController {
    @FXML
    private Pane rootPane;
    @FXML
    private TableView<Client> clientTableView;
    private List<Client> clients;

    @FXML
    private void initialize() {
        loadClients();
    }

    public ViewClientsController() {
    }

    private void loadClients() {
        ClientDao clientDao = new ClientDao();
        clients = clientDao.getAllClients();
        initializeClientTableView();
    }

    private void initializeClientTableView() {
        clientTableView.getItems().clear();
        clientTableView.getColumns().clear();

        TableColumn<Client, Integer> clientIdColumn = new TableColumn<>("Client ID");
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        TableColumn<Client, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        TableColumn<Client, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        TableColumn<Client, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Client, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        TableColumn<Client, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        clientTableView.getColumns().addAll(clientIdColumn, firstNameColumn, lastNameColumn, emailColumn, phoneNumberColumn, addressColumn);
        clientTableView.setPlaceholder(new javafx.scene.control.Label("No clients found"));
        clientTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        clientTableView.getItems().addAll(clients);
    }

}


