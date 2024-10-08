package com.gorigeek.springboot.conections;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import java.io.IOException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class TarjetasSecondaryConnection {
    
    static String url=null;
    static String usuario=null;
    static String contrasena=null;
    private static final HikariDataSource dataSource;

    public static void tarjetas() {

        System.err.println("entro a tarjetas");
        try (java.io.InputStream fileInput = TarjetasSecondaryConnection.class.getClassLoader().getResourceAsStream("dbtarjetas.properties")) {
            // Crear un objeto Properties
            Properties properties = new Properties();

            // Cargar las propiedades desde el archivo
            properties.load(fileInput);

            // Obtener las configuraciones de conexión desde el archivo .properties
            url = properties.getProperty("url");
            usuario = properties.getProperty("username");
            contrasena = properties.getProperty("password");

            
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        System.err.println("entro a static");
        HikariConfig config = new HikariConfig();
        tarjetas();
        // Configura tus propiedades HikariCP aquí
        config.setJdbcUrl(url);
        config.setUsername(usuario);
        config.setPassword(contrasena);

        // Configuración adicional según tus necesidades

        dataSource = new HikariDataSource(config);
    }


    public static Connection obtenerConexion() throws SQLException {
        System.err.println("entro a la conexion");
        return dataSource.getConnection();
    }

    public static void cerrarConexion(Connection conexion) {
        
        if (conexion != null) {
            try {
                System.err.println("entro a cerrar la conexion");
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cerrarDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            System.err.println("entro a cerrar el datasource");
            dataSource.close();
        }
    }
    
}
