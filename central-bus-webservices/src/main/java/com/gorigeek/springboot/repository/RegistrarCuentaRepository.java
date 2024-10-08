package com.gorigeek.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import com.gorigeek.springboot.entity.UserFinal;

@Repository
public interface RegistrarCuentaRepository extends JpaRepository<UserFinal, Long> {

    UserFinal findFirstByEmail(String email);

    Optional<UserFinal> findById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE UserFinal u SET u.token = ?1 WHERE u.email = ?2")
    int updateTokenWithEmail(String token, String email);

}
