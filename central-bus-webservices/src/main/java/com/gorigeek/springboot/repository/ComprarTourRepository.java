package com.gorigeek.springboot.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gorigeek.springboot.entity.TourMovil;

public interface ComprarTourRepository extends JpaRepository<TourMovil, Long> {
    
    @Query(value = "SELECT t_tour.*  FROM t_tour\r\n"
            + "JOIN t_costos_tour ON t_tour.idt_tour = t_costos_tour.t_tour\r\n"
            + "WHERE DATE(fecha_hora_salida) = ?1\r\n"
            + "AND (TIME(fecha_hora_salida) BETWEEN ?2 AND ?3)\r\n"
            + "AND ((t_costos_tour.adulto BETWEEN ?4 AND ?5)OR(t_costos_tour.nino BETWEEN ?4 AND ?5) OR (t_costos_tour.inapam BETWEEN ?4 AND ?5))", nativeQuery = true)
    List<TourMovil> findAllByFiltro(String fecha, String minHora, String maxHora, Double minPrecio, Double maxPrecio);
    
    
    //peticiones alternativas
     @Query(value = "SELECT t_tour.*  FROM t_tour \r\n"
            + "WHERE DATE(fecha_hora_salida) = ?1\r\n"
            + "OR c_estados_origen=?2 OR c_estados_destino = ?3", nativeQuery = true)
     List<TourMovil> findAllByBusqueda(String fecha, int origen, int destino);

    List<TourMovil> findAllByOrderByFechaHoraSalidaAsc();


    Page<TourMovil> findAllByOrderByFechaHoraSalidaAsc(Pageable pageable);


    Page<TourMovil> findAllByFechaHoraSalidaAfterOrderByFechaHoraSalidaAsc(String fechaActual,
            Pageable pageable);


    Page<TourMovil> findAllByFechaHoraSalidaAfterAndAfiliado_cStatuscuentaActivoOrderByFechaHoraSalidaAsc(String string,
            int status, Pageable pageable);

    /*
    Page<TourMovil> findAllByFechaHoraSalidaAfterAndAfiliado_cStatuscuentaActivoAndStatusActivoOrderByFechaHoraSalidaAsc(
            String string, int i, String j, Pageable pageable);*/
    
    //modificacion para eliminar los registros que se encuentren lleno en los asientos
   /* @Query(value = "SELECT * FROM t_tour AS t\r\n" +
            "JOIN tour_has_transporte tt ON tt.t_tour = t.idt_tour\r\n" 
            + "JOIN t_autobus a ON a.idt_autobus = tt.t_autobus\r\n" 
            + "LEFT JOIN t_afiliado af ON af.idt_afiliado = t.t_afiliado\r\n" 
            + "LEFT JOIN t_venta_tour v ON v.t_tour = t.idt_tour\r\n" 
            + "LEFT JOIN detalle_venta_tour dv ON dv.t_venta_tour = v.idt_venta_tour\r\n" 
            + "WHERE t.fecha_hora_salida > ?1\r\n" 
            + "AND af.c_statuscuenta_activo = ?2\r\n" 
            + "AND t.estatus_activo = ?3\r\n" 
            + "GROUP BY t.idt_tour, a.asientos_adultos, a.asientos_ninios, a.asientos_inapam\r\n" 
            + "HAVING COALESCE(COUNT(dv.id_detalle_venta_tour), 0) < (a.asientos_adultos + a.asientos_ninios + a.asientos_inapam)\r\n", nativeQuery = true)*/
    @Query(value = "SELECT * FROM t_tour tm " +
            "JOIN tour_has_transporte tt ON tt.t_tour = tm.idt_tour " +
            "JOIN t_autobus a ON a.idt_autobus = tt.t_autobus " +
            "LEFT JOIN t_afiliado af ON af.idt_afiliado = tm.t_afiliado " +
            "LEFT JOIN t_venta_tour v ON v.t_tour = tm.idt_tour " +
            "LEFT JOIN detalle_venta_tour dv ON dv.t_venta_tour = v.idt_venta_tour " +
            "WHERE tm.fecha_hora_salida > ?1 " +
            "AND af.c_statuscuenta_activo = ?2 " +
            "AND tm.estatus_activo = ?3 " +
            "GROUP BY tm.idt_tour, a.asientos_adultos, a.asientos_ninios, a.asientos_inapam " +
            "HAVING COALESCE(COUNT(dv.id_detalle_venta_tour), 0) < (a.asientos_adultos + a.asientos_ninios + a.asientos_inapam) " + 
            "ORDER BY tm.fecha_hora_salida ASC",
            countQuery = "SELECT COUNT(*) FROM t_tour tm " +
                         "JOIN tour_has_transporte tt ON tt.t_tour = tm.idt_tour " +
                         "JOIN t_autobus a ON a.idt_autobus = tt.t_autobus " +
                         "LEFT JOIN t_afiliado af ON af.idt_afiliado = tm.t_afiliado " +
                         "LEFT JOIN t_venta_tour v ON v.t_tour = tm.idt_tour " +
                         "LEFT JOIN detalle_venta_tour dv ON dv.t_venta_tour = v.idt_venta_tour " +
                         "WHERE tm.fecha_hora_salida > ?1 " +
                         "AND af.c_statuscuenta_activo = ?2 " +
                         "AND tm.estatus_activo = ?3 " +
                         "GROUP BY tm.idt_tour, a.asientos_adultos, a.asientos_ninios, a.asientos_inapam " + 
                         "ORDER BY tm.fecha_hora_salida ASC",
            nativeQuery = true)
    Page<TourMovil> findAllByFechaHoraSalida(
            String fechaActual, int statusAfiliado, String statusTour, Pageable pageable);


    
   /* @Query(value = "SELECT *FROM t_tour \r\n"
            + "WHERE fecha_hora_salida = ?1 \r\n"
            + "AND (horaSalida BETWEEN ?2 AND ?3) \r\n"
            + "AND ((precioadulto BETWEEN ?4 AND ?5)OR(precionino BETWEEN ?4 AND ?5) OR (precioinapam BETWEEN ?4 AND ?5))", nativeQuery = true)
    List<TourMovil> findAllByFiltro(String fecha, String minHora, String maxHora, String minPrecio, String maxPrecio);*/

}
