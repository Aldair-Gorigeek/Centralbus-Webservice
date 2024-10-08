package com.gorigeek.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gorigeek.springboot.entity.UserFinal;

@Repository
public interface RecuperarCuentaRepository extends JpaRepository<UserFinal, Long>{

    UserFinal findFirstByEmail(String correo);

}
