package com.example.car_dealership.model.dao;

import com.example.car_dealership.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.car_dealership.model.dao.UserDao.printSQLException;

public class ClientDao {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/car_dealership";
    private static final String DATABASE_USERNAME = "postgres";
    private static final String DATABASE_PASSWORD = "admin";
    private static final String UPDATE_QUERY = "UPDATE clients " +
            "SET first_name = ?, last_name = ?, email = ?, phone_number = ?, address = ? " +
            "WHERE id = ?";
    private static final String SELECT_QUERY = "SELECT * FROM clients WHERE user_id = ?";
    private static final String GET_ALL_CLIENTS_QUERY = "SELECT * FROM clients";
    private static final String GET_TOTAL_NUMBER_OF_CUSTOMERS_QUERY = "SELECT COUNT(*) FROM clients";

    public Client getClient(int userId) throws SQLException {
        Client client = new Client();
        try (Connection connection = (Connection) DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                client.setClientId(resultSet.getInt("id"));
                client.setFirstName(resultSet.getString("first_name"));
                client.setLastName(resultSet.getString("last_name"));
                client.setEmail(resultSet.getString("email"));
                client.setPhoneNumber(resultSet.getString("phone_number"));
                client.setAddress(resultSet.getString("address"));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return client;
    }

    public void updateClient(Client client) throws SQLException {
        try (Connection connection = (Connection) DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getLastName());
            preparedStatement.setString(3, client.getEmail());
            preparedStatement.setString(4, client.getPhoneNumber());
            preparedStatement.setString(5, client.getAddress());
            preparedStatement.setInt(6, client.getClientId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = (Connection) DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_CLIENTS_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Client client = new Client();
                client.setClientId(resultSet.getInt("id"));
                client.setFirstName(resultSet.getString("first_name"));
                client.setLastName(resultSet.getString("last_name"));
                client.setEmail(resultSet.getString("email"));
                client.setPhoneNumber(resultSet.getString("phone_number"));
                client.setAddress(resultSet.getString("address"));
                clients.add(client);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return clients;
    }

    public int getTotalNumberOfCustomers() {
        int totalNumberOfCustomers = 0;
        try (Connection connection = (Connection) DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_NUMBER_OF_CUSTOMERS_QUERY)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalNumberOfCustomers = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return totalNumberOfCustomers;
    }
}
