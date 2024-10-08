package com.gorigeek.springboot.distribution.entity.ventas;

public class Seat {
    private String seatCode;
    private int segmentIndex;

    // Getters and setters

    public int getSegmentIndex() {
        return segmentIndex;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public void setSeatCode(String seatCode) {
        this.seatCode = seatCode;
    }

    public void setSegmentIndex(int segmentIndex) {
        this.segmentIndex = segmentIndex;
    }

}
