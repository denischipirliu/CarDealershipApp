package com.example.car_dealership.model.dao;


import com.example.car_dealership.model.Car;
import com.example.car_dealership.model.CarStatus;

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
    private static final String GET_CAR_QUERY = "SELECT car_id,make_name, model_name, year, price, color, mileage, vin_number, engine_type, transmission_type, fuel_type, seating_capacity, image_path FROM cars " +
            "INNER JOIN makes ON cars.make_id = makes.make_id " +
            "INNER JOIN models ON cars.model_id = models.model_id " +
            "WHERE car_id = ?";
    private static final String GET_CARS_QUERY = "SELECT car_id,make_name, model_name, year, price, color, mileage, vin_number, engine_type, transmission_type, fuel_type, seating_capacity, image_path FROM cars " +
            "INNER JOIN makes ON cars.make_id = makes.make_id " +
            "INNER JOIN models ON cars.model_id = models.model_id AND status = 'available'";
    private static final String GET_CAR_FEATURES_QUERY = "SELECT feature_name FROM car_features " +
            "INNER JOIN features ON car_features.feature_id = features.feature_id " +
            "WHERE car_id = ?";
    private static final String DELETE_CAR_QUERY = "DELETE FROM cars WHERE car_id = ?";
    private static final String UPDATE_CAR_QUERY = "UPDATE cars SET make_id = ?, model_id = ?, year = ?, price = ?, color = ?, mileage = ?, vin_number = ?, engine_type = ?, transmission_type = ?, fuel_type = ?, seating_capacity = ?, image_path = ? WHERE car_id = ? AND status = 'available'";
    private static final String DELETE_CAR_FEATURE_QUERY = "DELETE FROM car_features WHERE car_id = ?";
    private static final String FILTER_SELECT_QUERY = "SELECT car_id,make_name, model_name, year, price, color, mileage, vin_number, engine_type, transmission_type, fuel_type, seating_capacity, image_path FROM cars " +
            "INNER JOIN makes ON cars.make_id = makes.make_id " +
            "INNER JOIN models ON cars.model_id = models.model_id  AND status = 'available'";
    private static final String UPDATE_CAR_STATUS_QUERY = "UPDATE cars SET status = 'sold' WHERE car_id = ?";
    private static final String ADD_MAKE_QUERY = "INSERT INTO makes (make_name) VALUES (?)";
    private static final String ADD_MODEL_QUERY = "INSERT INTO models (model_name, make_id) VALUES (?, ?)";
    private static final String ADD_FEATURE_QUERY = "INSERT INTO features (feature_name) VALUES (?)";
    private static final String GET_TOTAL_NUMBER_OF_CARS_AVAILABLE = "SELECT COUNT(*) FROM cars WHERE status = 'available'";

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
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                car.setId(generatedKeys.getInt("car_id"));
            }
            addFeaturesToCar(car);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    int carId = car.getId();
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
            preparedStatement.setInt(1, car.getId());
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
                int id = resultSet.getInt("car_id");
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
                Car car = new Car(make, model, year, price, color, mileage, vin, engineType, transmissionType, fuelType, seatingCapacity, features, image, CarStatus.AVAILABLE);
                car.setId(id);
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public Car getCar(int id) {
        Car car = null;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CAR_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
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
                car = new Car(make, model, year, price, color, mileage, vin, engineType, transmissionType, fuelType, seatingCapacity, features, image, CarStatus.AVAILABLE);
                car.setId(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car;
    }

    public void updateCar(Car car) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CAR_QUERY)) {
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
            preparedStatement.setInt(13, car.getId());
            preparedStatement.executeUpdate();
            deleteCarFeaturesFromDatabase(connection, car);
            addFeaturesToCar(car);
        } catch (SQLException e) {

        }
    }

    public void deleteCar(Car car) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatementCars = connection.prepareStatement(DELETE_CAR_QUERY);
             PreparedStatement preparedStatementCarFeatures = connection.prepareStatement(DELETE_CAR_FEATURE_QUERY)) {

            preparedStatementCarFeatures.setInt(1, car.getId());
            preparedStatementCarFeatures.executeUpdate();

            preparedStatementCars.setInt(1, car.getId());
            preparedStatementCars.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Car> getFilteredCars(String make, String model, int year, float price, int mileage) {
        List<Car> cars = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder(FILTER_SELECT_QUERY);
        List<Object> params = new ArrayList<>();

        queryBuilder.append(" WHERE 1 = 1");

        if (make != null && !make.isEmpty()) {
            queryBuilder.append(" AND make_name = ?");
            params.add(make);
        }
        if (model != null && !model.isEmpty()) {
            queryBuilder.append(" AND model_name = ?");
            params.add(model);
        }
        if (year != 0) {
            queryBuilder.append(" AND year >= ?");
            params.add(year);
        }
        if (price != 0) {
            queryBuilder.append(" AND price <= ?");
            params.add(price);
        }
        if (mileage != 0) {
            queryBuilder.append(" AND mileage <= ?");
            params.add(mileage);
        }

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("car_id");
                String make1 = resultSet.getString("make_name");
                String model1 = resultSet.getString("model_name");
                int year1 = resultSet.getInt("year");
                float price1 = resultSet.getFloat("price");
                String color = resultSet.getString("color");
                int mileage1 = resultSet.getInt("mileage");
                String vin = resultSet.getString("vin_number");
                String engineType = resultSet.getString("engine_type");
                String transmissionType = resultSet.getString("transmission_type");
                String fuelType = resultSet.getString("fuel_type");
                int seatingCapacity = resultSet.getInt("seating_capacity");
                List<String> features = getCarFeaturesFromDatabase(resultSet.getInt("car_id"));
                String image = resultSet.getString("image_path");
                Car car = new Car(make1, model1, year1, price1, color, mileage1, vin, engineType, transmissionType, fuelType, seatingCapacity, features, image, CarStatus.AVAILABLE);
                car.setId(id);
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public void updateCarStatusSold(int id) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CAR_STATUS_QUERY)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addMake(String make) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_MAKE_QUERY)) {
            preparedStatement.setString(1, make);
            preparedStatement.executeUpdate();
        }
    }

    public void addModel(String model, String make) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_MODEL_QUERY)) {
            preparedStatement.setString(1, model);
            preparedStatement.setInt(2, getMakeIdFromDatabase(make));
            preparedStatement.executeUpdate();
        }
    }

    public void addFeature(String feature) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_FEATURE_QUERY)) {
            preparedStatement.setString(1, feature);
            preparedStatement.executeUpdate();
        }
    }

    public int getTotalNumberOfCars() {
        int totalNumberOfCars = 0;
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_NUMBER_OF_CARS_AVAILABLE)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalNumberOfCars = resultSet.getInt("count");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return totalNumberOfCars;
    }
}
