package com.example.car_dealership;

import java.sql.*;
import java.util.prefs.Preferences;

import static com.example.car_dealership.UserDao.printSQLException;

public class ClientDao {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/car_dealership";
    private static final String DATABASE_USERNAME = "postgres";
    private static final String DATABASE_PASSWORD = "admin";
    private static final String UPDATE_QUERY = "UPDATE clients " +
            "SET first_name = ?, last_name = ?, email = ?, phone_number = ?, address = ? " +
            "WHERE user_id = (SELECT id FROM users WHERE username = ?)";
    private static final String SELECT_QUERY = "SELECT * FROM clients JOIN users ON clients.user_id = users.id WHERE users.username = ?";

    public void showProfile(Client client) {
        try (Connection connection = (Connection) DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
               preparedStatement.setString(1, client.getUsername());
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                client.setFirstName(rs.getString("first_name"));
                client.setLastName(rs.getString("last_name"));
                client.setEmail(rs.getString("email"));
                client.setPhoneNumber(rs.getString("phone_number"));
                client.setAddress(rs.getString("address"));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    public void updateClient(Client client) throws SQLException {
        try (Connection connection = (Connection) DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getLastName());
            preparedStatement.setString(3, client.getEmail());
            preparedStatement.setString(4, client.getPhoneNumber());
            preparedStatement.setString(5, client.getAddress());
            preparedStatement.setString(6, (client.getUsername()));
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

}
