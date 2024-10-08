package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.gorigeek.springboot.entity.VersionApp;

public interface VersionAppRepository extends JpaRepository<VersionApp, String>{
    
    @Query(value ="SELECT v FROM VersionApp v WHERE v.idt_version = ?1")
    public List<VersionApp> obtenerVersionApp(String id);
    

}
