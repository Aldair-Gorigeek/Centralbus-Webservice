package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gorigeek.springboot.entity.ImagenesTour;

public interface ImagenesTourRepository extends JpaRepository<ImagenesTour, Long> {

    List<ImagenesTour> findByTour(Long idt_tour);
    

}
