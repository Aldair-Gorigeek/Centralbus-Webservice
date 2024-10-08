package com.gorigeek.springboot.distribution.entity;

public class VacancyTripRequest {
    private String marketingCarrier;
    private String departureStation;
    private String arrivalStation;
    private String departureTime;
    private String returnDepartureTime;
    private String arrivalTime;
    private String fareClass;
    private String returnArrivalTime;
    private String passengerType;
    private Double cheapestPrice;
    private String pax;

    public String getFareClass() {
        return fareClass;
    }

    public Double getCheapestPrice() {
        return cheapestPrice;
    }

    public void setCheapestPrice(Double cheapestPrice) {
        this.cheapestPrice = cheapestPrice;
    }

    public void setFareClass(String fareClass) {
        this.fareClass = fareClass;
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

    public String getPax() {
        return pax;
    }

    public void setPax(String pax) {
        this.pax = pax;
    }

    public String getMarketingCarrier() {
        return marketingCarrier;
    }

    public void setMarketingCarrier(String marketingCarrier) {
        this.marketingCarrier = marketingCarrier;
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

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
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

    @Override
    public String toString() {
        return "VacancyTripRequest [marketingCarrier=" + marketingCarrier + ", departureStation=" + departureStation
                + ", arrivalStation=" + arrivalStation + ", departureTime=" + departureTime + ", returnDepartureTime="
                + returnDepartureTime + ", arrivalTime=" + arrivalTime + ", returnArrivalTime=" + returnArrivalTime
                + ", passengerType=" + passengerType + ", pax=" + pax + "]";
    }

}
