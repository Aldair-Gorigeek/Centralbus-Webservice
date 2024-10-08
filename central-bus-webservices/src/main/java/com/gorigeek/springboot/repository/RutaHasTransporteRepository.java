package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gorigeek.springboot.entity.RutaHasTransporte;
import com.gorigeek.springboot.entity.RutasDetalles;

public interface RutaHasTransporteRepository extends JpaRepository<RutaHasTransporte, Long> {

    @Query(value = "SELECT * FROM ruta_has_transporte "
            + " INNER JOIN t_detalle_ruta ON t_detalle_ruta.idt_detalle_ruta = ruta_has_transporte.t_detalle_ruta "
            + "WHERE ruta_has_transporte.t_autobus IN (SELECT DISTINCT ruta_has_transporte.t_autobus from t_detalle_ruta AS busdes\r\n"
            + "INNER JOIN ruta_has_transporte ON ruta_has_transporte.t_detalle_ruta=busdes.idt_detalle_ruta\r\n"
            + "INNER JOIN (SELECT DISTINCT  t_autobus from t_detalle_ruta AS busorg INNER JOIN ruta_has_transporte ON ruta_has_transporte.t_detalle_ruta=busorg.idt_detalle_ruta\r\n"
            + "WHERE busorg.t_terminales_origen=:terminalO) coso ON coso.t_autobus=ruta_has_transporte.t_autobus\r\n"
            + "WHERE busdes.t_terminales_destino=:terminalD)", nativeQuery = true)
    List<RutaHasTransporte> getAllRutasTrans(@Param("terminalO") Long terminalO, @Param("terminalD") Long terminalD);

    List<RutaHasTransporte> findByDetalleRutaIdTerminalOrigenIdTerminalesAndDetalleRutaIdTerminalDestinoIdTerminales(
            Long idOrigen, Long idDestino);

    RutaHasTransporte findByDetalleRutaIdTerminalOrigenIdTerminalesAndDetalleRutaIdTerminalDestinoIdTerminalesAndHorariosSalidaHoraSalida(
            Long idOrigen, Long idDestino, String hora);

}
