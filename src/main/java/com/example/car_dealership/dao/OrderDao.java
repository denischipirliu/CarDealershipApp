package com.example.car_dealership.dao;

import com.example.car_dealership.Client;
import com.example.car_dealership.Order;
import com.example.car_dealership.OrderStatus;
import com.example.car_dealership.PaymentMethod;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/car_dealership";
    private static final String DATABASE_USERNAME = "postgres";
    private static final String DATABASE_PASSWORD = "admin";
    public static final String INSERT_ORDER = "INSERT INTO orders (client_id, car_id, status) VALUES (?, ?, 'PENDING')";
    public static final String INSERT_ORDER_DETAILS = "UPDATE order_details SET pickup_date = ?, payment_method = ? WHERE order_id = ?";
    public static final String UPDATE_ORDER_STATUS = "UPDATE orders SET status = ? WHERE order_id = ?";
    public static final String GET_ALL_ORDERS = "SELECT * FROM orders INNER JOIN order_details ON orders.order_id = order_details.order_id ORDER BY orders.order_id DESC";
    public static final String GET_CLIENT_ORDERS = "SELECT * FROM orders INNER JOIN order_details ON orders.order_id = order_details.order_id WHERE client_id = ? ORDER BY orders.order_id DESC";
    public static final String GET_CAR_ORDERS = "SELECT * FROM orders INNER JOIN order_details ON orders.order_id = order_details.order_id WHERE car_id = ? ORDER BY orders.order_id DESC";
    private Connection getConnection() {
        try {
            return DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Or handle the SQLException appropriately based on your application's logic
    }

    public void addOrder(Order order) {
        Connection connection = getConnection();
        try {
            // Insert into orders table
            PreparedStatement orderStmt = connection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, order.getClientId());
            orderStmt.setInt(2, order.getCarId());
            orderStmt.executeUpdate();
            try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    PreparedStatement orderDetailsStmt = connection.prepareStatement(INSERT_ORDER_DETAILS);
                    orderDetailsStmt.setDate(1, Date.valueOf(order.getPickupDate()));
                    orderDetailsStmt.setString(2, order.getPaymentMethod().toString());
                    orderDetailsStmt.setInt(3, orderId); // Using the generated order ID
                    orderDetailsStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
            }
        }
    }

    public void updateOrderStatus(int orderId, OrderStatus status) {
        Connection connection = getConnection();
        try {
            PreparedStatement orderStmt = connection.prepareStatement(UPDATE_ORDER_STATUS);
            orderStmt.setString(1, status.toString());
            orderStmt.setInt(2, orderId);
            orderStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
            }
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement orderStmt = connection.prepareStatement(GET_ALL_ORDERS);
            ResultSet rs = orderStmt.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int clientId = rs.getInt("client_id");
                int carId = rs.getInt("car_id");
                float price = rs.getFloat("price");
                OrderStatus status = OrderStatus.valueOf(rs.getString("status").toUpperCase());
                String orderDate = rs.getString("order_date");
                String pickupDate = rs.getString("pickup_date");
                PaymentMethod paymentMethod = PaymentMethod.valueOf(rs.getString("payment_method").toUpperCase());
                Order order = new Order(carId, clientId, price, status, orderDate, pickupDate, paymentMethod);
                order.setId(orderId);
                orders.add(order);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
            }
        }
        return orders;
    }
    public List<Order> getOrdersByClientId(Client client) {
        List<Order> orders = new ArrayList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement orderStmt = connection.prepareStatement( GET_CLIENT_ORDERS);
            orderStmt.setInt(1, client.getClientId());
            ResultSet rs = orderStmt.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int clientId = rs.getInt("client_id");
                int carId = rs.getInt("car_id");
                float price = rs.getFloat("price");
                OrderStatus status = OrderStatus.valueOf(rs.getString("status").toUpperCase());
                String orderDate = rs.getString("order_date");
                String pickupDate = rs.getString("pickup_date");
                PaymentMethod paymentMethod = PaymentMethod.valueOf(rs.getString("payment_method").toUpperCase());
                Order order = new Order(carId, clientId, price, status, orderDate, pickupDate, paymentMethod);
                order.setId(orderId);
                orders.add(order);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
            }
        }


        return orders;
    }

    public List<Order> getOrdersByCarId(int carId) {
        List<Order> orders = new ArrayList<>();
        Connection connection = getConnection();
        try {
            PreparedStatement orderStmt = connection.prepareStatement( GET_CAR_ORDERS);
            orderStmt.setInt(1, carId);
            ResultSet rs = orderStmt.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int clientId = rs.getInt("client_id");
                float price = rs.getFloat("price");
                OrderStatus status = OrderStatus.valueOf(rs.getString("status").toUpperCase());
                String orderDate = rs.getString("order_date");
                String pickupDate = rs.getString("pickup_date");
                PaymentMethod paymentMethod = PaymentMethod.valueOf(rs.getString("payment_method").toUpperCase());
                Order order = new Order(carId, clientId, price, status, orderDate, pickupDate, paymentMethod);
                order.setId(orderId);
                orders.add(order);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
            }
        }
        return orders;
    }


}
