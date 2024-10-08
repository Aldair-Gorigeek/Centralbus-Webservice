package com.gorigeek.springboot.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_version")
public class VersionApp implements Serializable {
    
    @Id
    private String idt_version;
    private String c_app_version;
    private String version;

    public String getIdt_version() {
        return idt_version;
    }

    public void setIdt_version(String idt_version) {
        this.idt_version = idt_version;
    }

    public String getC_app_version() {
        return c_app_version;
    }

    public void setC_app_version(String c_app_version) {
        this.c_app_version = c_app_version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    private static final long serialVersionUID = 1L;

}
