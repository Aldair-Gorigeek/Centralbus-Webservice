package com.gorigeek.springboot.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gorigeek.springboot.entity.TBoletosDescuentos;

public interface TBoletosDescuentosService extends JpaRepository<TBoletosDescuentos, Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM TBoletosDescuentos b WHERE b.linea = :linea AND b.origen = :origen AND b.destino = :destino AND b.fechaSalida = :fechaSalida")
    List<TBoletosDescuentos> findByLineaOrigenDestinoAndFechaSalidaPessimistic(@Param("linea") String linea,
                                                                              @Param("origen") String origen,
                                                                              @Param("destino") String destino,
                                                                              @Param("fechaSalida") String fechaSalida);   
    
    @Query("SELECT b FROM TBoletosDescuentos b WHERE b.linea = :linea AND b.origen = :origen AND b.destino = :destino AND b.fechaSalida = :fechaSalida")
    List<TBoletosDescuentos> findByLineaOrigenDestinoAndFechaSalida(@Param("linea") String linea,
                                                                              @Param("origen") String origen,
                                                                              @Param("destino") String destino,
                                                                              @Param("fechaSalida") String fechaSalida);   
    
    @Query("SELECT b FROM TBoletosDescuentos b WHERE b.fechaCreacion < :tenMinutesAgo AND b.estatus != 2")
    List<TBoletosDescuentos> findOldRecords(@Param("tenMinutesAgo") Date tenMinutesAgo);
    
    @Modifying
    @Transactional
    @Query("UPDATE TBoletosDescuentos b SET b.estatus = 2 WHERE b.id = :id")
    void updateEstatusToTwo(@Param("id") Long id);

}
