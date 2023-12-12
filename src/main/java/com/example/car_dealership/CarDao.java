package com.example.car_dealership;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDao {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/car_dealership";
    private static final String DATABASE_USERNAME = "postgres";
    private static final String DATABASE_PASSWORD = "admin";
    private static final String INSERT_CAR_QUERY = "INSERT INTO cars (make_id, model_id, year, price, color, mileage, vin_number, engine_type, transmission_type, fuel_type, seating_capacity, image_path) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_MAKE_ID_QUERY = "SELECT make_id FROM makes WHERE make_name = ?";
    private static final String GET_MODEL_ID_QUERY = "SELECT model_id FROM models WHERE model_name = ?";
    private static final String GET_MAKES_QUERY = "SELECT make_name FROM makes";
    private static final String GET_MODELS_QUERY = "SELECT model_name FROM models WHERE make_id = ?";
    private static final String GET_FEATURES_QUERY = "SELECT feature_name FROM features";
    private static final String INSERT_CAR_FEATURE_QUERY = "INSERT INTO car_features (car_id, feature_id) VALUES (?, ?)";
    private static final String GET_FEATURE_ID_QUERY = "SELECT feature_id FROM features WHERE feature_name = ?";
    private static final String GET_CAR_ID_QUERY = "SELECT car_id FROM cars WHERE vin_number = ?";
    private static final String GET_CARS_QUERY = "SELECT car_id,make_name, model_name, year, price, color, mileage, vin_number, engine_type, transmission_type, fuel_type, seating_capacity, image_path FROM cars " +
            "INNER JOIN makes ON cars.make_id = makes.make_id " +
            "INNER JOIN models ON cars.model_id = models.model_id";
    private static final String GET_CAR_FEATURES_QUERY = "SELECT feature_name FROM car_features " +
            "INNER JOIN features ON car_features.feature_id = features.feature_id " +
            "WHERE car_id = ?";
    private static final String DELETE_CAR_QUERY = "DELETE FROM cars WHERE car_id = ?";
    private static final String UPDATE_CAR_QUERY = "UPDATE cars SET make_id = ?, model_id = ?, year = ?, price = ?, color = ?, mileage = ?, vin_number = ?, engine_type = ?, transmission_type = ?, fuel_type = ?, seating_capacity = ?, image_path = ? WHERE car_id = ?";
    private static final String DELETE_CAR_FEATURE_QUERY = "DELETE FROM car_features WHERE car_id = ?";
    public List<String> getMakesFromDatabase() {
        List<String> makes = new ArrayList<>();
        String sql = GET_MAKES_QUERY; // Your SQL query to fetch makes

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                makes.add(resultSet.getString("make_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return makes;
    }

    public List<String> getModelsFromDatabase(String make) {
        List<String> models = new ArrayList<>();
        String sql = GET_MODELS_QUERY;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            int makeId = getMakeIdFromDatabase(make);

            preparedStatement.setInt(1, makeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                models.add(resultSet.getString("model_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return models;
    }

    public int getMakeIdFromDatabase(String make) {
        int makeId = 0;
        String sql = GET_MAKE_ID_QUERY;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, make);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                makeId = resultSet.getInt("make_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return makeId;
    }

    private int getModelIdFromDatabase(String model) {
        int modelId = 0;
        String sql = GET_MODEL_ID_QUERY;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, model);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                modelId = resultSet.getInt("model_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelId;
    }

    public void addCar(Car car) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CAR_QUERY)) {
            preparedStatement.setInt(1, getMakeIdFromDatabase(car.getMake()));
            preparedStatement.setInt(2, getModelIdFromDatabase(car.getModel()));
            preparedStatement.setInt(3, car.getYear());
            preparedStatement.setDouble(4, car.getPrice());
            preparedStatement.setString(5, car.getColor());
            preparedStatement.setInt(6, car.getMileage());
            preparedStatement.setString(7, car.getVin());
            preparedStatement.setString(8, car.getEngineType());
            preparedStatement.setString(9, car.getTransmissionType());
            preparedStatement.setString(10, car.getFuelType());
            preparedStatement.setInt(11, car.getSeatingCapacity());
            preparedStatement.setString(12, car.getImage());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        addFeaturesToCar(car);
    }

    public List<String> getFeaturesFromDatabase() {
        List<String> features = new ArrayList<>();
        String sql = GET_FEATURES_QUERY;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                features.add(resultSet.getString("feature_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return features;
    }

    private void addFeaturesToCar(Car car) {
        String sql = INSERT_CAR_FEATURE_QUERY;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (String feature : car.getFeatures()) {
                    int carId = getCarIdFromDatabase(connection, car);
                    int featureId = getFeatureIdFromDatabase(connection, feature);

                    if (carId != 0 && featureId != 0) {
                        preparedStatement.setInt(1, carId);
                        preparedStatement.setInt(2, featureId);
                        preparedStatement.addBatch();
                    }
                }

                preparedStatement.executeBatch();
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteCarFeaturesFromDatabase(Connection connection, Car car) throws SQLException {
        String sql = DELETE_CAR_FEATURE_QUERY;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, getCarIdFromDatabase(connection, car));
            preparedStatement.executeUpdate();
        }
    }

    public List<String> getCarFeaturesFromDatabase(int carId) {
        List<String> carFeatures = new ArrayList<>();
        String sql = GET_CAR_FEATURES_QUERY;
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(GET_CAR_FEATURES_QUERY)) {
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                carFeatures.add(rs.getString("feature_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carFeatures;
    }


    private int getCarIdFromDatabase(Connection connection, Car car) throws SQLException {
        int carId = 0;
        String sql = GET_CAR_ID_QUERY;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, car.getVin());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    carId = resultSet.getInt("car_id");
                }
            }
        }
        return carId;
    }

    private int getFeatureIdFromDatabase(Connection connection, String feature) throws SQLException {
        int featureId = 0;
        String sql = GET_FEATURE_ID_QUERY;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, feature);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    featureId = resultSet.getInt("feature_id");
                }
            }
        }
        return featureId;
    }

    public List<Car> getCarsFromDatabase() throws SQLException {
        List<Car> cars = new ArrayList<>();
        String sql = GET_CARS_QUERY;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String make = resultSet.getString("make_name");
                String model = resultSet.getString("model_name");
                int year = resultSet.getInt("year");
                float price = resultSet.getFloat("price");
                String color = resultSet.getString("color");
                int mileage = resultSet.getInt("mileage");
                String vin = resultSet.getString("vin_number");
                String engineType = resultSet.getString("engine_type");
                String transmissionType = resultSet.getString("transmission_type");
                String fuelType = resultSet.getString("fuel_type");
                int seatingCapacity = resultSet.getInt("seating_capacity");
                List<String> features = getCarFeaturesFromDatabase(resultSet.getInt("car_id"));
                String image = resultSet.getString("image_path");
                Car car = new Car(make, model, year, price, color, mileage, vin, engineType, transmissionType, fuelType, seatingCapacity, features, image);
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
    public Car getCarFromDatabase(String vin) {
        Car car = null;
        String sql = GET_CARS_QUERY + " WHERE vin_number = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, vin);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String make = resultSet.getString("make_name");
                String model = resultSet.getString("model_name");
                int year = resultSet.getInt("year");
                float price = resultSet.getFloat("price");
                String color = resultSet.getString("color");
                int mileage = resultSet.getInt("mileage");
                String vinNumber = resultSet.getString("vin_number");
                String engineType = resultSet.getString("engine_type");
                String transmissionType = resultSet.getString("transmission_type");
                String fuelType = resultSet.getString("fuel_type");
                int seatingCapacity = resultSet.getInt("seating_capacity");
                List<String> features = getCarFeaturesFromDatabase(resultSet.getInt("car_id"));
                String image = resultSet.getString("image_path");
                car = new Car(make, model, year, price, color, mileage, vinNumber, engineType, transmissionType, fuelType, seatingCapacity, features, image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car;
    }
    public void updateCar(Car car) {
        try(Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CAR_QUERY)) {
            int id = getCarIdFromDatabase(connection,car);
            preparedStatement.setInt(1, getMakeIdFromDatabase(car.getMake()));
            preparedStatement.setInt(2, getModelIdFromDatabase(car.getModel()));
            preparedStatement.setInt(3, car.getYear());
            preparedStatement.setDouble(4, car.getPrice());
            preparedStatement.setString(5, car.getColor());
            preparedStatement.setInt(6, car.getMileage());
            preparedStatement.setString(7, car.getVin());
            preparedStatement.setString(8, car.getEngineType());
            preparedStatement.setString(9, car.getTransmissionType());
            preparedStatement.setString(10, car.getFuelType());
            preparedStatement.setInt(11, car.getSeatingCapacity());
            preparedStatement.setString(12, car.getImage());
            preparedStatement.setInt(13, id);
            preparedStatement.executeUpdate();
            deleteCarFeaturesFromDatabase(connection, car);
            addFeaturesToCar(car);
        } catch (SQLException e){

        }
    }

    public void deleteCar(Car car) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatementCars = connection.prepareStatement(DELETE_CAR_QUERY);
             PreparedStatement preparedStatementCarFeatures = connection.prepareStatement(DELETE_CAR_FEATURE_QUERY)) {

            int carId = getCarIdFromDatabase(connection, car);

            preparedStatementCarFeatures.setInt(1, carId);
            preparedStatementCarFeatures.executeUpdate();

            preparedStatementCars.setInt(1, carId);
            preparedStatementCars.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
