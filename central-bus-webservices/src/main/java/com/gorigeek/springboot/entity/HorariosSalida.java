package com.gorigeek.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "t_horarios_salida")
public class HorariosSalida {
    @Id
    @Column(name ="idt_horarios_salida")
    private Long idHorariosSalida;
    
    @Column(name ="hora_salida")
    private String horaSalida;
    
    
    public HorariosSalida() {
        
    }
    
    public HorariosSalida(Long idHorariosSalida,
            String horaSalida) {
        this.idHorariosSalida=idHorariosSalida;
        this.horaSalida=horaSalida;
    }

    public Long getIdHorariosSalida() {
        return idHorariosSalida;
    }

    public void setIdHorariosSalida(Long idHorariosSalida) {
        this.idHorariosSalida = idHorariosSalida;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }
    
    
    
}
