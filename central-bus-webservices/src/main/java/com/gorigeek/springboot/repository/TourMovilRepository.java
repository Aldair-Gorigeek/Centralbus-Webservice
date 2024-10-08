package com.gorigeek.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gorigeek.springboot.entity.TourMovil;

@Repository
public interface TourMovilRepository extends JpaRepository<TourMovil, Long> {

}
