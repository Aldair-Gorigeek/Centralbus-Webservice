package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gorigeek.springboot.entity.Rutas;


public interface RutaRepository extends JpaRepository<Rutas, Long>{
	
	@Query(value = "SELECT * FROM t_rutas WHERE t_id_tiporuta_idc_tiporuta = ?1", nativeQuery = true)
	List<Rutas> getAllRutasAutobuses(long idTipoRuta);
	
	//@Query(value = "SELECT * FROM t_rutas_autobus WHERE idt_rutas idRuta", nativeQuery = true)
    //Rutas getRutasAutobuses(Long idRuta);
/*
    @Query(value = "SELECT idt_rutas, nombreRuta, numeroDias, id_tipoRuta, (SELECT COUNT(id_rutas) FROM t_rutas_autobus WHERE idt_rutas = id_rutas)totalAutobuses FROM t_rutas WHERE idt_rutas = ?1", nativeQuery = true)
    Rutas getRutasAutobuses(String idRutas);
	
	@Query(value = "SELECT idt_rutas, nombreRuta, numeroDias, t_id_tiporuta_idc_tiporuta, (SELECT COUNT(t_idruta_idt_rutas) FROM t_rutasautobus WHERE idt_rutas = t_idruta_idt_rutas)totalAutobuses FROM t_rutas WHERE t_id_tiporuta_idc_tiporuta = ?1", nativeQuery = true)
    List<Rutas> getAllRutasAutobuses(int idTipoRuta);
	
	@Query(value = "SELECT idt_rutas, nombreRuta, numeroDias, t_id_tiporuta_idc_tiporuta, (SELECT COUNT(t_idruta_idt_rutas) FROM t_rutasautobus WHERE idt_rutas = t_idruta_idt_rutas)totalAutobuses FROM t_rutas WHERE idt_rutas = ?1", nativeQuery = true)
	Rutas getRutasAutobuses(Long idRuta);

	@Query(value = "SELECT idt_rutas, nombreRuta, numeroDias, t_id_tiporuta_idc_tiporuta, (SELECT COUNT(t_idruta_idt_rutas) FROM t_rutasautobus WHERE idt_rutas = t_idruta_idt_rutas)totalAutobuses FROM t_rutas WHERE idt_rutas = ?1", nativeQuery = true)
	Rutas getRutasAutobuses(String idRutas);
	
	@Query(value = "SELECT idt_rutas, nombreRuta, numeroDias, id_tipoRuta, idt_autobus, marca, modelo, placas, numeroAutobus, sanitario, AsientosNinios, asientosInapam, asientosAdultos, t_afiliado_idt_afiliado, c_statusdisponibilidad_idc_statusdisponibilidad "
			+ "FROM t_rutas "
			+ "INNER JOIN t_rutas_autobus ON t_rutas.idt_rutas = t_rutas_autobus.id_rutas\r"
			+ "INNER JOIN t_autobus ON t_rutas_autobus.id_autobus = t_autobus.idt_autobus\r"
			+ "WHERE t_rutas.idt_rutas = ?1", nativeQuery = true)
	List<Object> getRuta(Long idRuta);*/
	
}
