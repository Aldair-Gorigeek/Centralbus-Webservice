package com.gorigeek.springboot.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.gorigeek.springboot.entity.Ciudad;
import com.gorigeek.springboot.entity.Terminales;

public interface TerminalRepository extends JpaRepository<Terminales, Long> {
    List<Terminales> findByCiudad(Ciudad ciudad);

    @Query(value = "SELECT *  FROM t_terminales \r\n"
            + "WHERE t_afiliado = ?1", nativeQuery = true)
    List<Terminales> findByIdAfiliado(int idAfiliado);

    @Query("SELECT t FROM Terminales t WHERE t.afiliado.idt_afiliado IN :afiliadosIds AND t.statusTerminal = '1'")
    List<Terminales> findAllByAfiliados(@Param("afiliadosIds") Set<Long> afiliadosIds);

    @Query("SELECT t FROM Terminales t WHERE t.statusTerminal = '1'")
    List<Terminales> findAllActive();

    @Query("SELECT DISTINCT t FROM Terminales t " +
            "JOIN RutasDetalles rd ON t.idTerminales = rd.idTerminalOrigen " +
            "JOIN Afiliado a ON t.afiliado.idt_afiliado = a.idt_afiliado " +
            "WHERE rd.t_ruta.statusRuta = '1' " +
            "AND t.statusTerminal = '1' " +
            "AND a.cStatuscuentaActivo = 1")
    /*
     * @Query("SELECT DISTINCT t FROM Terminales t " +
     * "JOIN RutasDetalles rd ON t.idTerminales = rd.idTerminalOrigen " +
     * "JOIN Afiliado a ON t.afiliado.idt_afiliado = a.idt_afiliado " +
     * "WHERE rd.t_ruta.statusRuta = '1' " +
     * "AND t.statusTerminal = '1' " +
     * "AND a.c_statuscuenta_activo = 1")
     */
    List<Terminales> findAllTerminalesOrigen();

    @Query("SELECT DISTINCT t FROM Terminales t " +
            "JOIN RutasDetalles rd ON t.idTerminales = rd.idTerminalDestino " +
            "JOIN Afiliado a ON t.afiliado.idt_afiliado = a.idt_afiliado " +
            "WHERE rd.t_ruta.statusRuta = '1' " +
            "AND t.statusTerminal = '1' " +
            "AND a.cStatuscuentaActivo = 1")
    /*
     * @Query("SELECT DISTINCT t FROM Terminales t " +
     * "JOIN RutasDetalles rd ON t.idTerminales = rd.idTerminalDestino " +
     * "JOIN Afiliado a ON t.afiliado.idt_afiliado = a.idt_afiliado " +
     * "WHERE rd.t_ruta.statusRuta = '1' " +
     * "AND t.statusTerminal = '1' " +
     * "AND a.c_statuscuenta_activo = 1")
     */
    List<Terminales> findAllTerminalesDestino();

    @Query(value = "SELECT DISTINCT tt.*\r\n"
            + "FROM t_terminales tt\r\n"
            + "JOIN t_detalle_ruta tdr ON tt.idt_terminales = tdr.t_terminales_destino\r\n"
            + "JOIN t_ruta tr ON tdr.t_ruta = tr.idt_ruta\r\n"
            + "JOIN t_afiliado ta ON tr.t_afiliado = ta.idt_afiliado\r\n"
            + "WHERE tdr.t_ruta IN (\r\n"
            + "    SELECT t_ruta\r\n"
            + "    FROM t_detalle_ruta\r\n"
            + "    WHERE t_terminales_origen IN :origenIds \r\n"
            + ")\r\n"
            + "AND tdr.secuencia >= (\r\n"
            + "    SELECT secuencia\r\n"
            + "    FROM t_detalle_ruta\r\n"
            + "    WHERE t_terminales_origen IN :origenIds \r\n"
            + "    AND t_ruta = tdr.t_ruta\r\n"
            + ")\r\n"
            + "AND tt.c_estatus_terminal = 1\r\n"
            + "AND tr.c_status_ruta = 1\r\n"
            + "AND ta.c_statuscuenta_activo = 1\r\n"
            + "ORDER BY tt.idt_terminales;", nativeQuery = true)
    List<Terminales> findAllTerminalesDestinoByOrigenIds(@Param("origenIds") Set<Long> origenIds);

    @Query(value = "SELECT DISTINCT tt.*\r\n"
            + "FROM t_terminales tt\r\n"
            + "JOIN t_detalle_ruta tdr ON tt.idt_terminales = tdr.t_terminales_origen\r\n"
            + "JOIN t_ruta tr ON tdr.t_ruta = tr.idt_ruta\r\n"
            + "JOIN t_afiliado ta ON tr.t_afiliado = ta.idt_afiliado\r\n"
            + "WHERE tdr.t_ruta IN (\r\n"
            + "    SELECT t_ruta\r\n"
            + "    FROM t_detalle_ruta\r\n"
            + "    WHERE t_terminales_destino IN :destinoIds \r\n"
            + ")\r\n"
            + "AND tdr.secuencia >= (\r\n"
            + "    SELECT secuencia\r\n"
            + "    FROM t_detalle_ruta\r\n"
            + "    WHERE t_terminales_destino IN :destinoIds \r\n"
            + "    AND t_ruta = tdr.t_ruta\r\n"
            + ")\r\n"
            + "AND tt.c_estatus_terminal = 1\r\n"
            + "AND tr.c_status_ruta = 1\r\n"
            + "AND ta.c_statuscuenta_activo = 1\r\n"
            + "ORDER BY tt.idt_terminales;", nativeQuery = true)
    List<Terminales> findAllTerminalesOrigenByDestinoIds(@Param("destinoIds") Set<Long> destinoIds);

}
