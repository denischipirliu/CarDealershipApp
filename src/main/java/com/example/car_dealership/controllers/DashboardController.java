package com.example.car_dealership.controllers;

import com.example.car_dealership.model.dao.CarDao;
import com.example.car_dealership.model.dao.ClientDao;
import com.example.car_dealership.model.dao.OrderDao;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML
    private Label totalCustomersLabel;
    @FXML
    private Label totalSalesLabel;
    @FXML
    private Label totalCarsLabel;

    public DashboardController() {
    }
    @FXML
    private void initialize() {
        setDashboard();
    }

    private void setDashboard(){
        CarDao carDao = new CarDao();
        ClientDao clientDao = new ClientDao();
        OrderDao orderDao = new OrderDao();
        totalCarsLabel.setText("Number of cars: " + String.valueOf(carDao.getTotalNumberOfCars()));
        totalCustomersLabel.setText("Number of customers: " + String.valueOf(clientDao.getTotalNumberOfCustomers()));
        totalSalesLabel.setText("Total sales: " + String.valueOf(orderDao.getTotalSales()) + "€");
    }
}
