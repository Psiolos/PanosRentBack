package com.panos_rental.panos_rental.DTO;

public class AvailableCarDTO {
    private String brand;
    private String model;
    private Long id;
    private int year;
    private String carGroup;
    private String photoPath;
    private double price;

    public AvailableCarDTO() {
    }

    public AvailableCarDTO(String brand, String model, Long id, int year,
                           String carGroup, String photoPath, double price) {
        this.brand = brand;
        this.model = model;
        this.id = id;
        this.year = year;
        this.carGroup = carGroup;
        this.photoPath = photoPath;
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCarGroup() {
        return carGroup;
    }

    public void setCarGroup(String carGroup) {
        this.carGroup = carGroup;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
