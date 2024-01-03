package com.example.car_dealership.controllers;

import com.example.car_dealership.Application;
import com.example.car_dealership.model.dao.CarDao;
import com.example.car_dealership.model.dao.OrderDao;
import com.example.car_dealership.model.Car;
import com.example.car_dealership.model.Client;
import com.example.car_dealership.model.Order;
import com.example.car_dealership.model.OrderStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OrdersController {
    private List<Order> clientOrders;
    private List<Order> adminOrders;
    private Client client;
    @FXML
    private GridPane clientOrdersContainer;
    @FXML
    private TableView<Order> adminOrdersTable;

    @FXML
    private void initialize() {
        adminOrdersTable.setVisible(false);
    }

    public void loadClientOrders() {
        if (client != null) {
            setClientOrders();
            initializeClientOrdersContainer();
        }
    }

    public OrdersController() {
    }

    public void setClient(Client client) {
        this.client = client;
    }

    private void setClientOrders() {
        OrderDao orderDao = new OrderDao();
        clientOrders = orderDao.getOrdersByClientId(client);
    }

    private void displayClientOrderInformation(Order order, int index) {
        int column = index % 3;
        int row = index / 3;
        CarDao carDao = new CarDao();
        Car car = carDao.getCar(order.getCarId());
        VBox orderContainer = new VBox();
        Label orderId = new Label("Order no." + String.valueOf(order.getId()));
        Label carName = new Label(car.getMake() + " " + car.getModel() + ", " + car.getYear());
        Label carPrice = new Label("Price:" + String.valueOf(car.getPrice()) + "€");
        Label orderDate = new Label("Order date: " + order.getOrderDate());
        Label pickupDate = new Label("Pickup date: " + order.getPickupDate());
        Label paymentMethod = new Label("Payment method: " + order.getPaymentMethod().toString());
        Label orderStatus = new Label("Order status: " + order.getStatus().toString());
        orderContainer.getChildren().addAll(orderId, carName, carPrice, orderDate, pickupDate, paymentMethod, orderStatus);
        clientOrdersContainer.add(orderContainer, column, row);
    }

    private void initializeClientOrdersContainer() {
        clearClientOrdersContainer();
        int numberOfColumns = 3;
        int numberOfRows = clientOrders.size() / numberOfColumns;

        for (int i = 0; i < numberOfColumns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setMinWidth(450);
            clientOrdersContainer.getColumnConstraints().add(column);
        }

        for (int i = 0; i < numberOfRows; i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(250);
            clientOrdersContainer.getRowConstraints().add(row);
        }

        for (int i = 0; i < clientOrders.size(); i++) {
            displayClientOrderInformation(clientOrders.get(i), i);
        }
    }

    private void clearClientOrdersContainer() {
        clientOrdersContainer.getChildren().clear();
        clientOrdersContainer.getRowConstraints().clear();
        clientOrdersContainer.getColumnConstraints().clear();
    }

    private void setAdminOrders() {
        OrderDao orderDao = new OrderDao();
        adminOrders = orderDao.getAllOrders();
    }

    private void displayAdminOrders() {
        adminOrdersTable.getItems().clear();
        adminOrdersTable.getColumns().clear();

        TableColumn<Order, Integer> orderIdCol = new TableColumn<>("Order ID");
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Order, Integer> carIdCol = new TableColumn<>("Car ID");
        carIdCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
        carIdCol.setCellFactory(col -> {
            TableCell<Order, Integer> cell = new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.valueOf(item));
                    }
                }
            };

            cell.setOnMouseClicked(event -> {
                if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2 && !cell.isEmpty()) {
                    Integer carId = cell.getItem();
                    Car car = new CarDao().getCar(carId);
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/fxml/edit-car.fxml"));
                        Parent root = fxmlLoader.load();
                        EditCarController editCarController = fxmlLoader.getController();
                        editCarController.setCar(car);
                        Stage stage = new Stage();
                        stage.setTitle("Car Details");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("icons/icon.png"))));
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return cell;
        });

        TableColumn<Order, Integer> clientIdCol = new TableColumn<>("Client ID");
        clientIdCol.setCellValueFactory(new PropertyValueFactory<>("clientId"));

        TableColumn<Order, Date> orderDateCol = new TableColumn<>("Order Date");
        orderDateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        TableColumn<Order, Date> pickupDateCol = new TableColumn<>("Pickup Date");
        pickupDateCol.setCellValueFactory(new PropertyValueFactory<>("pickupDate"));

        TableColumn<Order, String> paymentMethodCol = new TableColumn<>("Payment Method");
        paymentMethodCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        TableColumn<Order, Float> carPriceCol = new TableColumn<>("Car Price");
        carPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Order, String> orderStatusCol = new TableColumn<>("Order Status");
        orderStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        adminOrdersTable.getColumns().addAll(
                orderIdCol, carIdCol, clientIdCol, orderDateCol,
                pickupDateCol, paymentMethodCol, carPriceCol,
                orderStatusCol, createActionColumn() // Add the new action column here
        );
        adminOrdersTable.getItems().addAll(adminOrders);
    }

    private <S, T> TableColumn<S, T> createColumn(String title, String propertyName) {
        TableColumn<S, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private TableColumn<Order, Void> createActionColumn() {
        TableColumn<Order, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final HBox buttonsBox = new HBox();
            private final Button completeButton = new Button("Complete");
            private final Button cancelButton = new Button("Cancel");

            {
                completeButton.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    order.setStatus(OrderStatus.COMPLETED);
                    new OrderDao().updateOrderStatus(order.getId(), OrderStatus.COMPLETED);
                    new CarDao().updateCarStatusSold(order.getCarId());

                    // Get all orders for the same car
                    List<Order> ordersForCar = new OrderDao().getOrdersByCarId(order.getCarId());
                    for (Order carOrder : ordersForCar) {
                        if (carOrder.getId() != order.getId()) {
                            carOrder.setStatus(OrderStatus.CANCELLED);
                            new OrderDao().updateOrderStatus(carOrder.getId(), OrderStatus.CANCELLED);
                        }
                    }

                    loadAdminOrders();
                });

                cancelButton.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    order.setStatus(OrderStatus.CANCELLED);
                    new OrderDao().updateOrderStatus(order.getId(), OrderStatus.CANCELLED);
                    loadAdminOrders();
                });

                buttonsBox.getChildren().addAll(completeButton, cancelButton);
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Order order = getTableView().getItems().get(getIndex());
                if (order.getStatus() == OrderStatus.PENDING) {
                    setGraphic(buttonsBox);
                    completeButton.setDisable(false);
                    cancelButton.setDisable(false);
                } else if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonsBox);
                    completeButton.setDisable(true);
                    cancelButton.setDisable(true);
                }
                setText(null);
            }
        });
        return actionCol;
    }


    public void loadAdminOrders() {
        setAdminOrders();
        displayAdminOrders();
        adminOrdersTable.setVisible(true);


    }
}
