package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gorigeek.springboot.entity.UpdateCodigoInivtarAmigo;

public interface InvitarAmigoRepository extends JpaRepository<UpdateCodigoInivtarAmigo, String> {

    @Query(value = "SELECT u FROM UpdateCodigoInivtarAmigo u WHERE u.codigo_amigo =?1")
    public List<UpdateCodigoInivtarAmigo> buscarCodigo(String codigo);
    
    @Query(value = "SELECT u FROM UpdateCodigoInivtarAmigo u WHERE u.idt_usuarios_final =?1")
    public List<UpdateCodigoInivtarAmigo> buscarCodigoUsuario(String idUser);

    @Modifying
    @Query(value = "UPDATE UpdateCodigoInivtarAmigo u SET u.codigo_amigo =?1 WHERE u.idt_usuarios_final = ?2")
    public int updateCodigo(String codigo, String idUser);

    @Modifying
    @Query(value = "INSERT INTO t_referidos (t_usuarios_final_referente, t_usuarios_final_invitado, fecha_hora) VALUES "
            + "(:t_usuarios_final_referente,:t_usuarios_final_invitado,:fecha_hora)", nativeQuery = true)
    public int insertReferido(@Param("t_usuarios_final_referente") Integer t_usuarios_final_referente,
            @Param("t_usuarios_final_invitado") Integer t_usuarios_final_invitado,
            @Param("fecha_hora") String fecha_hora);

}
