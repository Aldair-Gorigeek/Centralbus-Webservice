package com.gorigeek.springboot.view.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gorigeek.springboot.view.entity.AsientosOcupados;

@Repository
public interface AsientosOcupadosRepository extends JpaRepository<AsientosOcupados, String>{
    @Query(value = "SELECT * FROM asientos_ocupados WHERE fecha_viaje = :fechaViaje AND hora_viaje = :horaViaje", nativeQuery = true)
    List<AsientosOcupados> findAsientosOcupadosByFechaViajeAndHoraViaje(String fechaViaje, String horaViaje);
    
    @Query(value = "SELECT * FROM asientos_ocupados WHERE fecha_viaje >= :fechaHoy", nativeQuery = true)
    List<AsientosOcupados> findAsientosOcupadosFromTodayOnwards(String fechaHoy);
}
