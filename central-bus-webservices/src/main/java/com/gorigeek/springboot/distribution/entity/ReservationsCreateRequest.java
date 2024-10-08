package com.gorigeek.springboot.distribution.entity;

import java.util.List;

import com.gorigeek.springboot.distribution.entity.ventas.PassengerReservationsCreate;

public class ReservationsCreateRequest {
    private String marketingCarrier;
    private String departureStation;
    private String arrivalStation;
    private String departureTime;
    private String arrivalTime;
    private String returnDepartureTime;
    private String returnArrivalTime;
    private String retailerPartnerNumber;
    private String locale;
    private String currency;
    private String fareClass;
    private List<PassengerReservationsCreate> passengers;

    // Getters and setters

    public String getMarketingCarrier() {
        return marketingCarrier;
    }

    public String getReturnDepartureTime() {
        return returnDepartureTime;
    }

    public void setReturnDepartureTime(String returnDepartureTime) {
        this.returnDepartureTime = returnDepartureTime;
    }

    public String getReturnArrivalTime() {
        return returnArrivalTime;
    }

    public void setReturnArrivalTime(String returnArrivalTime) {
        this.returnArrivalTime = returnArrivalTime;
    }

    public String getRetailerPartnerNumber() {
        return retailerPartnerNumber;
    }

    public void setRetailerPartnerNumber(String retailerPartnerNumber) {
        this.retailerPartnerNumber = retailerPartnerNumber;
    }

    public void setMarketingCarrier(String marketingCarrier) {
        this.marketingCarrier = marketingCarrier;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFareClass() {
        return fareClass;
    }

    public void setFareClass(String fareClass) {
        this.fareClass = fareClass;
    }

    public List<PassengerReservationsCreate> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerReservationsCreate> passengers) {
        this.passengers = passengers;
    }
}




