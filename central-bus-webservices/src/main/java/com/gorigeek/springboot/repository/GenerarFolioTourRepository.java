package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.gorigeek.springboot.entity.DetalleVentaTourModel;

public interface GenerarFolioTourRepository  extends JpaRepository<DetalleVentaTourModel, String>{

    @Modifying
    @Query(value = "SELECT * FROM detalle_venta_tour v "
            + "INNER JOIN t_venta_tour o ON o.idt_venta_tour = v.t_venta_tour "
            + "WHERE o.t_afiliado = ?1 "
            + "GROUP BY v.id_detalle_venta_tour "
            + "ORDER by v.id_detalle_venta_tour DESC LIMIT 1", nativeQuery = true)
    public List<DetalleVentaTourModel> obtenerUltimoFolioTour(Long idAfiliado);

}
