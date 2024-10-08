package com.gorigeek.springboot.repository;

import java.util.List;

import com.gorigeek.springboot.entity.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gorigeek.springboot.entity.Afiliado;
import com.gorigeek.springboot.entity.Autobus;


public interface AutobusRepository extends JpaRepository<Autobus, Long>{
    
	List<Autobus> findAllByDisponibilidad(Disponibilidad statusdisponibilidad);

	@Query(value = "SELECT idt_autobus, c_tipotransporte_idc_tipotransporte, marca, modelo, placas, numeroAutobus, sanitario, AsientosNinios, asientosInapam, asientosAdultos, t_afiliado_idt_afiliado, c_statusdisponibilidad_idc_statusdisponibilidad\r\n"
			+ "FROM t_autobus\r\n"
			+ "INNER JOIN t_rutasautobus ON t_autobus.idt_autobus = t_rutasautobus.t_idbus_idt_autobus\r\n"
			+ "WHERE t_rutasautobus.t_idruta_idt_rutas = ?1", nativeQuery = true)
	List<Autobus> getAutobusesByIdRuta(Long idRutas);


	@Query(value = "SELECT idt_autobus, c_tipotransporte_idc_tipotransporte, marca, modelo, placas, numeroAutobus, sanitario, AsientosNinios, asientosInapam, asientosAdultos, t_afiliado_idt_afiliado, c_statusdisponibilidad_idc_statusdisponibilidad\r\n"
	        + "FROM t_autobus\r\n"
	        + "WHERE c_statusdisponibilidad_idc_statusdisponibilidad = ?1 AND idt_autobus\r\n"
	        +"NOT IN(SELECT idt_autobus\r\n"
	        + "FROM t_autobus\r\n"
            + "INNER JOIN t_rutasautobus ON t_autobus.idt_autobus = t_rutasautobus.t_idbus_idt_autobus\r\n"
            + "WHERE t_rutasautobus.t_idruta_idt_rutas = ?2)", nativeQuery = true)
    List<Autobus> findAllByDisponibilidadNotIn(Long statusdisponibilidad, Long ruta);

    List<Autobus> findByAfiliadoAndDisponibilidad(Afiliado idAfiliado, Disponibilidad statusdisponibilidad);
    
    @Query(value = "SELECT idt_autobus, c_tipotransporte_idc_tipotransporte, marca, modelo, placas, numeroAutobus, sanitario, AsientosNinios, asientosInapam, asientosAdultos, t_afiliado_idt_afiliado, c_statusdisponibilidad_idc_statusdisponibilidad\r\n"
            + "FROM t_autobus\r\n"
            + "WHERE t_autobus.marca LIKE %?1%\r\n"
            + "OR t_autobus.modelo LIKE %?1%\r\n"
            + "OR t_autobus.placas LIKE %?1%", nativeQuery = true)
    List<Autobus> findAllBySearch(String palabra);

    @Query(value = "SELECT idt_autobus, c_tipotransporte_idc_tipotransporte, marca, modelo, placas, numeroAutobus, sanitario, AsientosNinios, asientosInapam, asientosAdultos, t_afiliado_idt_afiliado, c_statusdisponibilidad_idc_statusdisponibilidad\r\n"
            + "FROM t_autobus\r\n"
            + "WHERE t_autobus.t_afiliado_idt_afiliado =?1\r\n"
            + "AND c_statusdisponibilidad_idc_statusdisponibilidad = 1\r\n"
            + "AND (t_autobus.marca LIKE %?2%\r\n"
            + "OR t_autobus.modelo LIKE %?2%\r\n"
            + "OR t_autobus.placas LIKE %?2%)", nativeQuery = true)
    List<Autobus> findAllBySearch(Long idAfiliado, String palabra);
}
