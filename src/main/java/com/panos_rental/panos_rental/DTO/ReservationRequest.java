package com.panos_rental.panos_rental.DTO;

import com.panos_rental.panos_rental.entity.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservationRequest {
    private Integer id;
    private int carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String firstName;
    private String lastName;
    private ReservationStatus status;
    private String phone;
    private String idCard;
    private BigDecimal totalPrice;
    private boolean superInsurance;
    private CarDTO car;

    // Getters και Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getSuperInsurance() {
        return superInsurance;
    }

    public void setSuperInsurance(Boolean superInsurance) {
        this.superInsurance = superInsurance;
    }


    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }
}
