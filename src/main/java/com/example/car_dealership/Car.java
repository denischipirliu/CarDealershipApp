package com.example.car_dealership;

import java.util.List;

public class Car {
    int id;
    private String make;
    private String model;
    private int year;
    private float price;
    private String color;
    private int mileage;
    private String vin;
    private String engineType;
    private String transmissionType;
    private String fuelType;
    private int seatingCapacity;
    private List<String> features;
    private String image;

    public Car(String make, String model, int year, float price, String color, int mileage, String vin, String engineType, String transmissionType, String fuelType, int seatingCapacity, List<String> features, String image) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.color = color;
        this.mileage = mileage;
        this.vin = vin;
        this.engineType = engineType;
        this.transmissionType = transmissionType;
        this.fuelType = fuelType;
        this.seatingCapacity = seatingCapacity;
        this.features = features;
        this.image = image;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public float getPrice() {
        return price;
    }

    public String getColor() {
        return color;
    }

    public int getMileage() {
        return mileage;
    }

    public String getVin() {
        return vin;
    }

    public String getEngineType() {
        return engineType;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
