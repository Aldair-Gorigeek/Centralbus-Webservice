package com.gorigeek.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gorigeek.springboot.entity.DetalleVentaTourModel;
import com.gorigeek.springboot.entity.DetalleVentasModel;
import com.gorigeek.springboot.entity.TAfiliado;
import com.gorigeek.springboot.entity.TUsuariosAdmin;
import com.gorigeek.springboot.repository.GenerarFolioRepository;
import com.gorigeek.springboot.repository.GenerarFolioTourRepository;

@Transactional
@Service
public class GenerarFolioService {

    @Autowired
    private GenerarFolioRepository repo;
    
    @Autowired
    private GenerarFolioTourRepository rep;

    public List<DetalleVentasModel> getData(String idAfiliado) {
        return repo.obtenerUltimoFolio(idAfiliado);
    }

    public List<DetalleVentaTourModel> obtenerUltimoFolioTour(String idAfiliado) {
        return rep.obtenerUltimoFolioTour(Long.parseLong(idAfiliado));
    }

    public List<TAfiliado> getAfiliado(String idAfiliado) {
        return repo.obtenerAfiliado(idAfiliado);
    }

    public List<TUsuariosAdmin> getUsuario(String idAfiliado) {
        return repo.obtenerUsuario(idAfiliado);
    }

    public int insertFolio(Integer t_usuarios_final, Integer t_afiliado, String folio, String dv_uno, String dv_dos,
            String fecha_viaje, String fecha_generacion, Integer t_autobus, String tipo) {
        return repo.insertFolio(t_usuarios_final, t_afiliado, folio, dv_uno, dv_dos, fecha_viaje,
                t_autobus, tipo);
    }
    public int insertFolioSinUsuario(Integer t_afiliado, String folio, String dv_uno, String dv_dos,
            String fecha_viaje, String fecha_generacion, Integer t_autobus, String tipo) {
        return repo.insertFolioSinUsuario(t_afiliado, folio, dv_uno, dv_dos, fecha_viaje,
                t_autobus, tipo);
    }

}
