package com.gorigeek.springboot.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gorigeek.springboot.conections.TarjetasSecondaryConnection;
import com.gorigeek.springboot.entity.DTO.Tarjetas_secondaryDTO;

@Repository
@Transactional(rollbackFor = Exception.class)
public class TarjetasSecondaryDAO {
    
    @Transactional
    public List<Tarjetas_secondaryDTO> guardar(String id_principal, String key_tarjeta) throws SQLException {
        List<Tarjetas_secondaryDTO> tarjetas = new ArrayList<Tarjetas_secondaryDTO>();

        Connection connection = TarjetasSecondaryConnection.obtenerConexion();

        try (PreparedStatement preparedStatementInsert = connection.prepareStatement(
                "INSERT INTO t_tarjetas_usuarios (id_principal, key_tarjeta) VALUES (?, ?)")) {
            // Configuramos los parámetros para la inserción
            preparedStatementInsert.setString(1, id_principal);
            preparedStatementInsert.setString(2, key_tarjeta);
            
            // Ejecutamos la inserción
            preparedStatementInsert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // No olvides cerrar la conexión cuando hayas terminado
            TarjetasSecondaryConnection.cerrarConexion(connection);
        }

        return tarjetas;
    }
    
    @Transactional
    public List<Tarjetas_secondaryDTO> obtenerTodasLasTarjetas() throws SQLException {
        List<Tarjetas_secondaryDTO> tarjetas = new ArrayList<>();

        Connection connection = TarjetasSecondaryConnection.obtenerConexion();

        try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM t_tarjetas_usuarios")) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id_principal");
                String key_tarjeta = resultSet.getString("key_tarjeta");

                Tarjetas_secondaryDTO tarjeta = new Tarjetas_secondaryDTO();
                tarjeta.setId_principal(id);
                tarjeta.setKey_tarjeta(key_tarjeta);

                tarjetas.add(tarjeta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // No olvides cerrar la conexión cuando hayas terminado
            TarjetasSecondaryConnection.cerrarConexion(connection);
        }

        return tarjetas;
    }


    @Transactional
    public List<Tarjetas_secondaryDTO> tarjetas(String id_principal) throws SQLException {
        List<Tarjetas_secondaryDTO> tarjetas = new ArrayList<Tarjetas_secondaryDTO>();

        Connection connection = TarjetasSecondaryConnection.obtenerConexion();

        try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM t_tarjetas_usuarios where id_principal=?")) {
            preparedStatement.setString(1, id_principal);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
            
                int id = resultSet.getInt("id_principal");
                String key_tarejta = resultSet.getString("key_tarjeta");
                
                Tarjetas_secondaryDTO tarjeta = new Tarjetas_secondaryDTO();
                
                tarjeta.setId_principal(id);
                tarjeta.setKey_tarjeta(key_tarejta);
                
                tarjetas.add(tarjeta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // No olvides cerrar la conexión cuando hayas terminado
            TarjetasSecondaryConnection.cerrarConexion(connection);

        }
        return tarjetas;

    }
    
    @Transactional
    public List<Tarjetas_secondaryDTO> eliminarPorIdPrincipal(String id_principal) throws SQLException {
    List<Tarjetas_secondaryDTO> tarjetas = new ArrayList<>();

    Connection connection = TarjetasSecondaryConnection.obtenerConexion();

    try (PreparedStatement preparedStatementDelete = connection.prepareStatement(
            "DELETE FROM t_tarjetas_usuarios WHERE id_principal = ?")) {
        
        // Configuramos el parámetro para la eliminación
        preparedStatementDelete.setString(1, id_principal);

        // Ejecutamos la eliminación
        preparedStatementDelete.executeUpdate();
        
        
        
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // No olvides cerrar la conexión cuando hayas terminado
        TarjetasSecondaryConnection.cerrarConexion(connection);
    }

    return tarjetas;
}
    
    
    
    
   /* public List<Tarjetas_secondaryDTO> eliminarPorIdPrincipal(String id_principal) throws SQLException {
        List<Tarjetas_secondaryDTO> tarjetas = new ArrayList<>();

        try (Connection connection = TarjetasSecondaryConnection.obtenerConexion();
             PreparedStatement preparedStatementDelete = connection.prepareStatement(
                     "DELETE FROM t_tarjetas_usuarios WHERE id_principal = ?")) {

            preparedStatementDelete.setString(1, id_principal);
            // Ejecutamos la eliminación
            preparedStatementDelete.executeUpdate();

            // Recuperar las tarjetas eliminadas 
            try (PreparedStatement preparedStatementSelect = connection.prepareStatement(
                    "SELECT * FROM t_tarjetas_usuarios WHERE id_principal = ?")) {
                preparedStatementSelect.setString(1, id_principal);
                ResultSet resultSet = preparedStatementSelect.executeQuery();
                while (resultSet.next()) {
                    // Crea instancias de Tarjetas_secondaryDTO y agrégalas a la lista
                    Tarjetas_secondaryDTO tarjeta = new Tarjetas_secondaryDTO();
                    // Configura las propiedades de la tarjeta según tu esquema
                    
                    tarjetas.add(tarjeta);
                }
            }
        } catch (SQLException e) {
            // Puedes lanzar una excepción personalizada o manejarla de manera más específica
            throw new RuntimeException("Error al eliminar tarjetas secundarias", e);
        }

        return tarjetas;
    }*/


    
  

}
