package com.gorigeek.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gorigeek.springboot.entity.UpdateCodigoInivtarAmigo;
import com.gorigeek.springboot.repository.InvitarAmigoRepository;

@Transactional
@Service
public class InvitarAmigoService {

        @Autowired
        private InvitarAmigoRepository repo;
        
        public List<UpdateCodigoInivtarAmigo> buscarCodigo(String codigo){
            return repo.buscarCodigo(codigo);}
        
        public List<UpdateCodigoInivtarAmigo> buscarCodigoUsuario(String idUser){
            return repo.buscarCodigoUsuario(idUser);}
        
        public int updateCodigo(String codigo, String idUser) {
            return repo.updateCodigo(codigo, idUser);
        }
        
        public int insertFolio(Integer idReferdio, Integer idInvitado, String fecha ) {
            return repo.insertReferido(idReferdio, idInvitado, fecha);
        }
        
}
