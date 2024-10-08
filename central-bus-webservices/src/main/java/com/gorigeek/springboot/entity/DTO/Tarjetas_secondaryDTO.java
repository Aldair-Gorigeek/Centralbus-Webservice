package com.gorigeek.springboot.entity.DTO;

public class Tarjetas_secondaryDTO {
    
    private Integer id_principal;
    private String key_tarjeta;
    
    public Tarjetas_secondaryDTO() {}

    public Tarjetas_secondaryDTO(Integer id_principal, String key_tarjeta) {
        super();
        this.id_principal=id_principal;
        this.key_tarjeta=key_tarjeta;
        
    }

    public Integer getId_principal() {
        return id_principal;
    }

    public void setId_principal(Integer id_principal) {
        this.id_principal = id_principal;
    }

    public String getKey_tarjeta() {
        return key_tarjeta;
    }

    public void setKey_tarjeta(String key_tarjeta) {
        this.key_tarjeta = key_tarjeta;
    }
}
