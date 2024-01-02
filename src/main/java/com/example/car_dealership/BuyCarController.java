package com.example.car_dealership;

import com.example.car_dealership.dao.OrderDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.time.LocalDate;

public class BuyCarController {
    private Car car;
    private Client client;
    @FXML
    private Button placeOrderButton;
    @FXML
    private ImageView carImage;
    @FXML
    private Label carName;
    @FXML
    private DatePicker pickupDate;
    @FXML
    private ChoiceBox<String> paymentMethod;
    @FXML
    private Label carPrice;

    @FXML
    public void initialize() {
        paymentMethod.getItems().addAll(PaymentMethod.CASH.toString(), PaymentMethod.CREDIT_CARD.toString(), PaymentMethod.CHECK.toString(), PaymentMethod.LEASING.toString());
        placeOrderButton.setOnAction(this::handlePlaceOrderButton);
    }
    public BuyCarController() {
    }

    public void setCarAndClient(Car car, Client client) {
        this.car = car;
        this.client = client;
        Image image = new Image(car.getImage());
        carImage.setImage(image);
        carName.setText(car.getMake() + " " + car.getModel() + ", " + car.getYear());
        carPrice.setText("Price:" + String.valueOf(car.getPrice()) + "€");
    }

    public void handlePlaceOrderButton(ActionEvent event) {
        try {
            if (pickupDate.getValue().toString().isEmpty() || paymentMethod.getValue().isEmpty()) {
                throw new Exception();
            } else {
                LocalDate currentDate = LocalDate.now();
                Order order = new Order(car.getId(), client.getClientId(), car.getPrice(), OrderStatus.PENDING, currentDate.toString(), pickupDate.getValue().toString(), PaymentMethod.valueOf(paymentMethod.getValue()));
                OrderDao orderDAO = new OrderDao();
                orderDAO.addOrder(order);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Order placed successfully!");
                alert.showAndWait();
                Stage stage = (Stage) placeOrderButton.getScene().getWindow();
                stage.close();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the fields!");
            alert.showAndWait();
        }
    }
}
