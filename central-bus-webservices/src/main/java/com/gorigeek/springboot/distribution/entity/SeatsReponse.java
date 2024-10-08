package com.gorigeek.springboot.distribution.entity;

import java.util.List;

public class SeatsReponse {
    private Integer numberOfWc;
    private Integer numberOfSeats;
    private List<Seats> seats;

    public Integer getNumberOfWc() {
        return numberOfWc;
    }

    public void setNumberOfWc(Integer numberOfWc) {
        this.numberOfWc = numberOfWc;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public List<Seats> getSeats() {
        return seats;
    }

    public void setSeats(List<Seats> seats) {
        this.seats = seats;
    }
}
