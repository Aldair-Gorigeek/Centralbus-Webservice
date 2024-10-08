package com.gorigeek.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gorigeek.springboot.repository.EliminarCuentaRepository;

@Transactional
@Service
public class EliminarCuentaService {
    @Autowired
    private EliminarCuentaRepository repo;
    
    public int updateEstatusCuenta(Long idUser) {
        return repo.updateEstatusCuenta(idUser);
    }

}
