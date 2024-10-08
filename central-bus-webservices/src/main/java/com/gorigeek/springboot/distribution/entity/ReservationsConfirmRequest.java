package com.gorigeek.springboot.distribution.entity;

import java.util.List;

import com.gorigeek.springboot.distribution.entity.ventas.PassengerReservationsConfirm;

public class ReservationsConfirmRequest {
	private String reservationId;
	private Boolean executePayment;
	private String paymentMethod;	
	private Boolean termsAccepted;
	private Boolean sendCustomerEmail;
	private String title;	
	private String firstName;
	private String lastName;	
	private String email;	
	private String phone;	
	private String city;	
	private String zipCode;	
	private String streetAndNumber;
	
	private List<PassengerReservationsConfirm> passengers;
	
	public String getReservationId() {
		return reservationId;		
	}
	
	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public Boolean getExecutePayment() {
		return executePayment;
	}
	
	public void setExecutePayment(Boolean executePayment) {
		this.executePayment = executePayment;
	}
	
	public String getPaymentMethod() {
		return paymentMethod;
	}
	
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	public Boolean getTermsAccepted() {
		return termsAccepted;
	}
	
	public void setTermsAccepted(Boolean termsAccepted) {
		this.termsAccepted = termsAccepted;
	}
	
	public Boolean getSendCustomerEmail() {
		return sendCustomerEmail;
	}
	
	public void setSendCustomerEmail(Boolean sendCustomerEmail) {
		this.sendCustomerEmail = sendCustomerEmail;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone){
		this.phone = phone;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public String getStreetAndNumber() {
		return streetAndNumber;
	}
	
	public void SetStreetAndNumber(String streetAndNumber) {
		this.streetAndNumber = streetAndNumber;
	}
	
	

	public List<PassengerReservationsConfirm> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<PassengerReservationsConfirm> passengers) {
		this.passengers = passengers;
	}
	
}