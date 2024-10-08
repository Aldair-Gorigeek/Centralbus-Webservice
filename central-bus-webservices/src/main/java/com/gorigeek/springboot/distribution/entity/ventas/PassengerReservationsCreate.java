package com.gorigeek.springboot.distribution.entity.ventas;

import java.util.List;

public class PassengerReservationsCreate {
    private String type;
    private List<Seat> seats;
    private List<Cards> cards;

    // Getters and setters
    public List<Cards> getCards() {
        return cards;
    }

    public void setCards(List<Cards> cards) {
        this.cards = cards;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
