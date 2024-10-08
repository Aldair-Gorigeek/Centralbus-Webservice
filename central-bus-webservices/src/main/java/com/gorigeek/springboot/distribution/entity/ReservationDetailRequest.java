package com.gorigeek.springboot.distribution.entity;

public class ReservationDetailRequest {
    private ReservationsCreateRequest reservationsCreateRequest;
    private VancancyTripResponse vacancyResponse;
    private ReservationsConfirmRequest confirmRequest;
    private String origen;
    private String destino;
    private String lineaTransporte;
    private String logo;
    private String idReservation;
    private String email;
    private Long idUser;
    private Boolean isRedondo;
    private String nAutorizacion;
    private String idTransaccion;
    private String numTarjeta;
    private Double costo;
    private Boolean isDiscountActivatedGo;
    private Boolean isDiscountActivatedReturn;
    private Integer discountRate;
    //PROMO99
    private Integer adultSeatsDiscountIda;    
    private Integer adultSeatsDiscountVuelta;
    private Integer childSeatsDiscountIda;    
    private Integer childSeatsDiscountVuelta;
    private Integer inapamSeatsDiscountIda;    
    private Integer inapamSeatsDiscountVuelta;
    

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getnAutorizacion() {
        return nAutorizacion;
    }

    public void setnAutorizacion(String nAutorizacion) {
        this.nAutorizacion = nAutorizacion;
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public ReservationsCreateRequest getReservationsCreateRequest() {
        return reservationsCreateRequest;
    }

    public void setReservationsCreateRequest(ReservationsCreateRequest reservationsCreateRequest) {
        this.reservationsCreateRequest = reservationsCreateRequest;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }

    public VancancyTripResponse getVacancyResponse() {
        return vacancyResponse;
    }

    public void setVacancyResponse(VancancyTripResponse vacancyResponse) {
        this.vacancyResponse = vacancyResponse;
    }

    public ReservationsConfirmRequest getConfirmRequest() {
        return confirmRequest;
    }

    public void setConfirmRequest(ReservationsConfirmRequest confirmRequest) {
        this.confirmRequest = confirmRequest;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getLineaTransporte() {
        return lineaTransporte;
    }

    public void setLineaTransporte(String lineaTransporte) {
        this.lineaTransporte = lineaTransporte;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Boolean getIsRedondo() {
        return isRedondo;
    }

    public void setIsRedondo(Boolean isRedondo) {
        this.isRedondo = isRedondo;
    }

    public Boolean getIsDiscountActivatedGo() {
        return isDiscountActivatedGo;
    }

    public void setIsDiscountActivatedGo(Boolean isDiscountActivatedGo) {
        this.isDiscountActivatedGo = isDiscountActivatedGo;
    }

    public Boolean getIsDiscountActivatedReturn() {
        return isDiscountActivatedReturn;
    }

    public void setIsDiscountActivatedReturn(Boolean isDiscountActivatedReturn) {
        this.isDiscountActivatedReturn = isDiscountActivatedReturn;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    public Integer getAdultSeatsDiscountIda() {
        return adultSeatsDiscountIda;
    }

    public void setAdultSeatsDiscountIda(Integer adultSeatsDiscountIda) {
        this.adultSeatsDiscountIda = adultSeatsDiscountIda;
    }

    public Integer getAdultSeatsDiscountVuelta() {
        return adultSeatsDiscountVuelta;
    }

    public void setAdultSeatsDiscountVuelta(Integer adultSeatsDiscountVuelta) {
        this.adultSeatsDiscountVuelta = adultSeatsDiscountVuelta;
    }

    public Integer getChildSeatsDiscountIda() {
        return childSeatsDiscountIda;
    }

    public void setChildSeatsDiscountIda(Integer childSeatsDiscountIda) {
        this.childSeatsDiscountIda = childSeatsDiscountIda;
    }

    public Integer getChildSeatsDiscountVuelta() {
        return childSeatsDiscountVuelta;
    }

    public void setChildSeatsDiscountVuelta(Integer childSeatsDiscountVuelta) {
        this.childSeatsDiscountVuelta = childSeatsDiscountVuelta;
    }

    public Integer getInapamSeatsDiscountIda() {
        return inapamSeatsDiscountIda;
    }

    public void setInapamSeatsDiscountIda(Integer inapamSeatsDiscountIda) {
        this.inapamSeatsDiscountIda = inapamSeatsDiscountIda;
    }

    public Integer getInapamSeatsDiscountVuelta() {
        return inapamSeatsDiscountVuelta;
    }

    public void setInapamSeatsDiscountVuelta(Integer inapamSeatsDiscountVuelta) {
        this.inapamSeatsDiscountVuelta = inapamSeatsDiscountVuelta;
    }          
    
}
