package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gorigeek.springboot.entity.TarjetaUsuarios;

public interface  TarjetaUsuariosRepository extends JpaRepository<TarjetaUsuarios, Long> {
    List<TarjetaUsuarios> findByUsuarioFinal(Long usuarioFinal);
    void deleteByIdtTarjetasUsuarios(Long idtTarjetasUsuarios);
    TarjetaUsuarios findByIdtTarjetasUsuarios(Long idtTarjetasUsuarios);

}
