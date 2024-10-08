package com.gorigeek.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.gorigeek.springboot.entity.UserFinal;

public interface EliminarCuentaRepository extends JpaRepository<UserFinal, Long>{
    
    @Modifying
    @Query(value ="UPDATE UserFinal u SET u.estatusActivo = 3 WHERE u.idtUsuariosfinal = ?1")
    public int updateEstatusCuenta(Long idUser);

}
