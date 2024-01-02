package com.example.car_dealership;

import com.example.car_dealership.dao.CarDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewCarsController {
    @FXML
    private GridPane carContainer;
    @FXML
    private ChoiceBox<String> makeChoiceBox;
    @FXML
    private ChoiceBox<String> modelChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;
    @FXML
    private ChoiceBox<String> priceChoiceBox;
    @FXML
    private ChoiceBox<String> mileageChoiceBox;
    @FXML
    private Button resetButton;
    @FXML
    private Button filterButton;
    @FXML
    private Button addCarButton;
    private List<Car> cars;
    private Client client;
    int numberOfColumns = 3;
    int numberOfRows;

    public ViewCarsController() {

    }

    @FXML
    private void initialize() {
        initializeCarsContainer();
        initializeChoiceBoxes();
        resetButton.setOnAction(this::handleResetButton);
        filterButton.setOnAction(this::handleFilterButton);
    }

    private void clearCarContainer() {
        carContainer.getChildren().clear();
    }

    @FXML
    private void displayCarInformation(Car car, int index) {
        int rowIndex = index / 3;
        int columnIndex = index % 3;
        String vboxID = "carBox_" + index;

        VBox carBox = new VBox();
        carBox.setId(vboxID);
        carBox.setAlignment(Pos.CENTER);
        carBox.setSpacing(5);

        Label makeAndModelLabel = new Label(car.getMake() + " " + car.getModel());
        makeAndModelLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        Label detailsLabel = new Label("Year: " + car.getYear() + "\nPrice: " + car.getPrice() + " €\nMileage: " + car.getMileage() + " km");
        detailsLabel.setFont(Font.font("System", 14));

        Image carImage = new Image(car.getImage());
        ImageView carImageView = new ImageView(carImage);
        carImageView.setFitHeight(200);
        carImageView.setFitWidth(300);
        carImageView.setPreserveRatio(true);

        carBox.getChildren().addAll(carImageView, makeAndModelLabel, detailsLabel);

        if (client == null) {
            addCarButton.setVisible(true);
            addCarButton.setOnAction(this::handleAddCarButton);
            Button editButton = new Button("Edit");
            editButton.setOnAction(actionEvent -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-car.fxml"));
                    Parent root = fxmlLoader.load();
                    EditCarController editCarController = fxmlLoader.getController();
                    editCarController.setCar(car);
                    Stage editStage = new Stage();
                    editStage.initModality(Modality.APPLICATION_MODAL);
                    editStage.setTitle("Edit Car");
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                    editStage.setScene(scene);
                    editStage.showAndWait();
                    initializeCarsContainer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(actionEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Car");
                alert.setHeaderText("Are you sure you want to delete this car?");
                alert.setContentText("This action cannot be undone.");
                alert.showAndWait();
                if (alert.getResult().getText().equals("OK")) {
                    CarDao carDao = new CarDao();
                    carDao.deleteCar(car);
                    carContainer.getChildren().clear();
                    initializeCarsContainer();
                }
            });

            HBox buttons = new HBox();
            buttons.setSpacing(10);
            buttons.getChildren().addAll(editButton, deleteButton);
            carBox.getChildren().add(buttons);
        } else {
            carBox.setOnMouseClicked(mouseEvent -> handleCarBox(mouseEvent, car));
        }

        carContainer.add(carBox, columnIndex, rowIndex);
    }

    private void initializeCarsContainer() {
        clearCarContainer();
        carContainer.getColumnConstraints().clear();
        carContainer.getRowConstraints().clear();
        CarDao carDao = new CarDao();
        try {
            cars = carDao.getCarsFromDatabase();
            numberOfColumns = 3;
            numberOfRows = cars.size() / numberOfColumns;
            for (int i = 0; i < numberOfColumns; i++) {
                ColumnConstraints column = new ColumnConstraints();
                column.setMinWidth(450);
                carContainer.getColumnConstraints().add(column);
            }
            for (int i = 0; i < numberOfRows; i++) {
                RowConstraints row = new RowConstraints();
                row.setMinHeight(250);
                carContainer.getRowConstraints().add(row);
            }
            for (int i = 0; i < cars.size(); i++) {
                displayCarInformation(cars.get(i), i);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setClient(Client client) {
        this.client = client;
        initializeCarsContainer();
        initializeChoiceBoxes();
        addCarButton.setVisible(false);
    }

    public void handleResetButton(ActionEvent event) {
        initializeCarsContainer();
        makeChoiceBox.setValue(null);
        modelChoiceBox.setValue(null);
        yearChoiceBox.setValue(null);
        priceChoiceBox.setValue(null);
        mileageChoiceBox.setValue(null);
    }

    public void handleFilterButton(ActionEvent event) {
        if (makeChoiceBox.getValue() == null && modelChoiceBox.getValue() == null && yearChoiceBox.getValue() == null && priceChoiceBox.getValue() == null && mileageChoiceBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Filter Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select at least one filter!");
            alert.showAndWait();
            return;
        }
        String make = makeChoiceBox.getValue();
        String model = modelChoiceBox.getValue();
        int year = 0;
        if (yearChoiceBox.getValue() != "Any" && yearChoiceBox.getValue() != null) {
            year = Integer.parseInt(yearChoiceBox.getValue());
        }
        float price = 0;
        if (priceChoiceBox.getValue() != "Any" && priceChoiceBox.getValue() != null) {
            price = Float.parseFloat(priceChoiceBox.getValue().substring(1).replace(",", ""));
        }
        int mileage = 0;
        if (mileageChoiceBox.getValue() != "Any" && mileageChoiceBox.getValue() != null) {
            mileage = Integer.parseInt(mileageChoiceBox.getValue().substring(0, mileageChoiceBox.getValue().length() - 3).replace(",", ""));
        }
        CarDao carDao = new CarDao();
        cars = carDao.getFilteredCars(make, model, year, price, mileage);
        clearCarContainer();
        carContainer.getColumnConstraints().clear();
        carContainer.getRowConstraints().clear();
        for (int i = 0; i < numberOfColumns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setMinWidth(450);
            carContainer.getColumnConstraints().add(column);
        }
        for (int i = 0; i < numberOfRows; i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(250);
            carContainer.getRowConstraints().add(row);
        }
        for (int i = 0; i < cars.size(); i++) {
            displayCarInformation(cars.get(i), i);
        }
    }

    private void initializeChoiceBoxes() {
        CarDao carDao = new CarDao();
        List<String> makes = carDao.getMakesFromDatabase();
        makeChoiceBox.setItems(FXCollections.observableArrayList(makes));
        makeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            List<String> models = carDao.getModelsFromDatabase(newValue);
            modelChoiceBox.setItems(FXCollections.observableArrayList(models));
        });
        int currentYear = Year.now().getValue();
        List<String> years = new ArrayList<>();
        for (int i = currentYear; i >= 1900; i--) {
            years.add(String.valueOf(i));
        }
        yearChoiceBox.setItems(FXCollections.observableArrayList(years));
        ObservableList<String> prices = FXCollections.observableArrayList(
                "Any", "€1,000", "€5,000", "€10,000", "€15,000", "€20,000", "€30,000",
                "€40,000", "€50,000", "€60,000", "€70,000", "€80,000", "€90,000", "€100,000"
        );
        priceChoiceBox.setItems(prices);
        ObservableList<String> mileages = FXCollections.observableArrayList(
                "Any", "10,000 km", "20,000 km", "30,000 km", "40,000 km", "50,000 km", "60,000 km",
                "70,000 km", "80,000 km", "90,000 km", "100,000 km", "110,000 km", "120,000 km",
                "130,000 km", "140,000 km", "150,000 km", "160,000 km", "170,000 km", "180,000 km",
                "190,000 km", "200,000 km"
        );
        mileageChoiceBox.setItems(mileages);
    }

    public void handleCarBox(MouseEvent event, Car selectedCar) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("car-page.fxml"));
            Parent root = fxmlLoader.load();
            CarPageController carPageController = fxmlLoader.getController();
            carPageController.setCar(selectedCar);
            carPageController.setClient(client);
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
            borderPane.setCenter(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleAddCarButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-car.fxml"));
            Parent root = fxmlLoader.load();
            Stage addCarStage = new Stage();
            addCarStage.initModality(Modality.APPLICATION_MODAL);
            addCarStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("icon.png"))));
            addCarStage.setTitle("Add Car");
            Scene scene = new Scene(root);
            addCarStage.setScene(scene);
            addCarStage.showAndWait();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}