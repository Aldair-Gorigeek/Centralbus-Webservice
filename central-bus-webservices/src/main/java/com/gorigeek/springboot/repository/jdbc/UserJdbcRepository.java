package com.gorigeek.springboot.repository.jdbc;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gorigeek.springboot.entity.UserFinal;

import javax.sql.DataSource;
import java.sql.ResultSet;

@Repository
public class UserJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserJdbcRepository(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public UserFinal findFirstByEmail(String email) {
        String sql = "SELECT email, fechaRegistro, pass, codigo_verificacion, estatus_activo FROM t_usuarios_final WHERE email = :email";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("email", email);

        return namedParameterJdbcTemplate.query(
                sql,
                parameters,
                (ResultSet rs, int rowNum) -> {
                    UserFinal user = new UserFinal();
                    user.setEmail(rs.getString("email"));
                    user.setFechaRegistro(rs.getObject("fechaRegistro", String.class));
                    user.setCodigoVerificacion(rs.getObject("codigo_verificacion", String.class));
                    user.setPass(rs.getObject("pass", String.class));
                    user.setEstatusActivo(rs.getInt("estatus_activo"));
                    return user;
                }).stream().findFirst().orElse(null);
    }

    public boolean updateCodigoVerificacion(UserFinal user) {
        String sql = "UPDATE t_usuarios_final SET codigo_verificacion = :codigo_verificacion WHERE email = :email";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("codigo_verificacion", user.getCodigoVerificacion());
        parameters.addValue("email", user.getEmail());

        int rowsAffected = namedParameterJdbcTemplate.update(sql, parameters);
        return rowsAffected > 0;
    }

    public boolean updateStatusActivo(UserFinal user) {
        String sql = "UPDATE t_usuarios_final SET estatus_activo = :estatus_activo WHERE email = :email";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("estatus_activo", user.getEstatusActivo());
        parameters.addValue("email", user.getEmail());

        int rowsAffected = namedParameterJdbcTemplate.update(sql, parameters);
        return rowsAffected > 0;
    }
    
    public boolean updatePassword(UserFinal user) {
        String sql = "UPDATE t_usuarios_final SET pass = :pass WHERE email = :email";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("pass", user.getPass());
        parameters.addValue("email", user.getEmail());

        int rowsAffected = namedParameterJdbcTemplate.update(sql, parameters);
        return rowsAffected > 0;
    }
}
