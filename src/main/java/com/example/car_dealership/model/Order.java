package com.example.car_dealership.model;

public class Order {
    private int id;
    private int carId;
    private int clientId;
    private float price;
    private OrderStatus status;
    private String orderDate;
    private String pickupDate;
    private PaymentMethod paymentMethod;


    public Order(int carId, int clientId, float price, OrderStatus status, String orderDate, String pickupDate, PaymentMethod paymentMethod) {
        this.carId = carId;
        this.clientId = clientId;
        this.price = price;
        this.status = status;
        this.orderDate = orderDate;
        this.pickupDate = pickupDate;
        this.paymentMethod = paymentMethod;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public float getPrice() {
        return price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getClientId() {
        return clientId;
    }
}
