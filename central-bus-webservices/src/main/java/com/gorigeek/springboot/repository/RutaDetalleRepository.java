package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gorigeek.springboot.entity.Afiliado;
import com.gorigeek.springboot.entity.Autobus;

import com.gorigeek.springboot.entity.RutasDetalles;

public interface RutaDetalleRepository extends JpaRepository<RutasDetalles, Long>{

	//List<RutasDetalles> findAllByIdRutas(Long idRutas);
	
	
	@Query(value = "SELECT * \r\n"
			+ "FROM t_detalleruta\r\n"
			+ "WHERE t_terminales_t_origen = ?1 AND t_terminales_idt_destino = ?2\r\n", nativeQuery = true)
	List<RutasDetalles> getRutasByIdTerminales(String idTer1, String idTer2);
	
	
	@Query(value="SELECT DISTINCTROW * FROM t_detalle_ruta RIGHT JOIN ruta_has_transporte ON t_detalle_ruta.idt_detalle_ruta=ruta_has_transporte.t_detalle_ruta "
	        //+ "INNER JOIN t_ruta ON t_ruta.idt_ruta=t_detalle_ruta.t_ruta "
	        //+ "INNER JOIN t_horarios_salida ON t_horarios_salida.idt_horarios_salida=ruta_has_transporte.t_horarios_salida "
	        + "WHERE t_detalle_ruta.t_terminales_origen=5 AND t_detalle_ruta.t_terminales_destino=6 ",nativeQuery = true)
	List<RutasDetalles> getAllRutasInfo();


	//obtener catalogo de la lista de ruta
	@Query(value="SELECT * FROM t_detalle_ruta WHERE t_ruta=?1",nativeQuery = true)
    List<RutasDetalles> findListByRuta(int i);
	
	@Query("SELECT DISTINCT t FROM RutasDetalles t LEFT JOIN FETCH t.t_ruta ruta LEFT JOIN FETCH t.idTerminalOrigen origen LEFT JOIN FETCH t.idTerminalDestino destino WHERE ruta.statusRuta = '1'")
	List<RutasDetalles> findAllRutasActivas();
	
	/*
	 * Deprecated for a optimization on RutaDetalle.java 
	 * @Query(value = "SELECT id_autobus \r\n"
			+ "FROM t_rutas_autobus\r\n"
			+ "WHERE id_rutas = ?1\r\n", nativeQuery = true)
	String getIdAutobusByIdRuta(String idRut);*/
	
	/*
	@Modifying
    @Transactional 
    @Query(value ="delete from t_detalleruta where idt_detalleruta = :idDetalle",nativeQuery = true)
    public void eliminarById(@Param("idDetalle") Long idDetalle);
	*/

    //RutasDetalles findByIdAutobusAndIdAfiliado(Long idAutobus, Afiliado afiliado);


    //RutasDetalles findByIdAutobus(Long idAutobus);
}
