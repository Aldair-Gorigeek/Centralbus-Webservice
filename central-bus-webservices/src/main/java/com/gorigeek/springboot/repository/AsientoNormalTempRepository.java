package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gorigeek.springboot.entity.AsientoNormalTemp;

public interface AsientoNormalTempRepository extends JpaRepository<AsientoNormalTemp, Long> {

    @Query(value = "SELECT * FROM temp_asientos_normal \r\n"
            + "WHERE t_terminales_origen = ?3\r\n"
            + "AND t_terminales_destino = ?4\r\n"
            + "AND t_autobus = ?1\r\n"
            + "AND DATE(fecha_viaje)= ?2\r\n"
            + "AND TIME(fecha_viaje) = ?5\r\n"
            + "AND numAsiento \r\n"
            + "NOT IN (SELECT a.numero_asiento\r\n"
            + "FROM detalle_venta AS a \r\n"
            + "JOIN t_venta_viajes AS b ON a.t_venta_viajes = b.idt_venta_viajes\r\n"
            + "WHERE a.t_autobus = ?1\r\n"
            + "AND DATE(a.fecha_viaje) = ?2\r\n"
            + "AND TIME(a.fecha_viaje) = ?5\r\n"
            + "AND b.t_terminales_origen = ?3\r\n"
            + "AND b.t_terminales_destino = ?4) FOR UPDATE", nativeQuery = true)
    List<AsientoNormalTemp> getAsientosOcupadosTemp(Long idAutobus, String fecha, int origen, int destino, String hora);

    // Método personalizado para eliminar por el ID de usuario final
    void deleteByUsuarioFinal(Long usuarioFinal);

    void deleteByNumeroAsientoAndFechaViajeAndAutobus(int numAsiento, String fechaHora, double autobus);

}
