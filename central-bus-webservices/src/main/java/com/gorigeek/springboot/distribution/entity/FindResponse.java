package com.gorigeek.springboot.distribution.entity;

import java.util.List;

public class FindResponse {
    private boolean bookedOut;
    private String arrivalTime;
    private String departureTime;
    private Long duration;
    private String fareClassCode;
    private Double price;
    private String offerBundle;
    private String offerId;
    private Integer totalSeatsLeft;
    private String departureStation;
    private String arrivalStation;
    private String departureStationName;
    private String arrivalStationName;
    private String departureStationDirection;
    private String arrivalStationDirection;
    private String idMarketingCarrier;
    private String tradeNameMarketingCarrier;
    private String logo;
    private String passengerTypes;
    private List<String> fareFeatures;
    //Descuentos
    private Boolean isDiscountActivated;
    private Integer discountRate;
    private String typeDiscount;
    private Double alternativePrice;
    private Integer availableSeatsDiscount;

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
    }

    public Boolean getIsDiscountActivated() {
        return isDiscountActivated;
    }

    public void setIsDiscountActivated(Boolean isDiscountActivated) {
        this.isDiscountActivated = isDiscountActivated;
    }

    public String getDepartureStationName() {
        return departureStationName;
    }

    public void setDepartureStationName(String departureStationName) {
        this.departureStationName = departureStationName;
    }

    public String getArrivalStationName() {
        return arrivalStationName;
    }

    public void setArrivalStationName(String arrivalStationName) {
        this.arrivalStationName = arrivalStationName;
    }

    public String getDepartureStationDirection() {
        return departureStationDirection;
    }

    public void setDepartureStationDirection(String departureStationDirection) {
        this.departureStationDirection = departureStationDirection;
    }

    public String getArrivalStationDirection() {
        return arrivalStationDirection;
    }

    public void setArrivalStationDirection(String arrivalStationDirection) {
        this.arrivalStationDirection = arrivalStationDirection;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<String> getFareFeatures() {
        return fareFeatures;
    }

    public void setFareFeatures(List<String> fareFeatures) {
        this.fareFeatures = fareFeatures;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getPassengerTypes() {
        return passengerTypes;
    }

    public void setPassengerTypes(String passengerTypes) {
        this.passengerTypes = passengerTypes;
    }

    public boolean isBookedOut() {
        return bookedOut;
    }

    public void setBookedOut(boolean bookedOut) {
        this.bookedOut = bookedOut;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getFareClassCode() {
        return fareClassCode;
    }

    public void setFareClassCode(String fareClassCode) {
        this.fareClassCode = fareClassCode;
    }

    public String getOfferBundle() {
        return offerBundle;
    }

    public void setOfferBundle(String offerBundle) {
        this.offerBundle = offerBundle;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public Integer getTotalSeatsLeft() {
        return totalSeatsLeft;
    }

    public void setTotalSeatsLeft(Integer totalSeatsLeft) {
        this.totalSeatsLeft = totalSeatsLeft;
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

    public String getIdMarketingCarrier() {
        return idMarketingCarrier;
    }

    public void setIdMarketingCarrier(String idMarketingCarrier) {
        this.idMarketingCarrier = idMarketingCarrier;
    }

    public String getTradeNameMarketingCarrier() {
        return tradeNameMarketingCarrier;
    }

    public void setTradeNameMarketingCarrier(String tradeNameMarketingCarrier) {
        this.tradeNameMarketingCarrier = tradeNameMarketingCarrier;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTypeDiscount() {
        return typeDiscount;
    }

    public void setTypeDiscount(String typeDiscount) {
        this.typeDiscount = typeDiscount;
    }

    public Double getAlternativePrice() {
        return alternativePrice;
    }

    public void setAlternativePrice(Double alternativePrice) {
        this.alternativePrice = alternativePrice;
    }

    public Integer getAvailableSeatsDiscount() {
        return availableSeatsDiscount;
    }

    public void setAvailableSeatsDiscount(Integer availableSeatsDiscount) {
        this.availableSeatsDiscount = availableSeatsDiscount;
    }  
    
}
