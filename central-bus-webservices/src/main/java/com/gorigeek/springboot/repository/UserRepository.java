package com.gorigeek.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gorigeek.springboot.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	User findFirstByEmail(String email);
	
	List<User> findByEmailAndPass(String email, String Pass);
	
	User findByIdtUsuariosadminAndCodigoVerificacion(Long id,Integer codigoVerificacion );
	
	User findByIdtUsuariosadmin(Long id);

	List<User> findByNombreContainingIgnoreCase(String nombre);

}
