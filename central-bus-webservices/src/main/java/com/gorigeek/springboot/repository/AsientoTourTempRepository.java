package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gorigeek.springboot.entity.AsientosTourTemp;

public interface AsientoTourTempRepository extends JpaRepository<AsientosTourTemp, Long> {
    
    @Query(value ="SELECT * FROM temp_asientos_tour \r\n"
            + "WHERE t_autobus = ?1\r\n"
            + "AND t_tour_idt_tour = ?3\r\n"
            + "AND DATE(fecha_viaje)= DATE(?2)\r\n"
            //+ "AND TIME(fecha_viaje) = \"08:46:06\"\r\n"
            + "AND numAsiento\r\n"
            + "NOT IN (SELECT a.numero_asiento\r\n"
            + "        FROM detalle_venta_tour AS a\r\n"
            + "        JOIN t_venta_tour AS b ON a.t_venta_tour = b.idt_venta_tour\r\n"
            + "        WHERE a.t_autobus = 25\r\n"
            + "    AND DATE(a.fecha_viaje) = DATE(?2)\r\n"
           // + "    AND TIME(a.fecha_viaje) = \"08:46:06\"\r\n"
            + "    AND b.t_tour = ?3)",nativeQuery = true)

    List<AsientosTourTemp> getAsientosOcupadosTemp(Long idAutobus, String fecha, int tour);
    
    // Método personalizado para eliminar por el ID de usuario final
    void deleteByUsuarioFinal(Long usuarioFinalTour);

    void deleteByNumeroAsientoAndFechaViajeAndAutobus(int numAsiento, String fechaHora, double autobus);

}
