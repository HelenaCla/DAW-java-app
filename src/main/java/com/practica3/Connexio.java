package com.practica3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase Connexio
 * 
 * Proporciona una conexión a la base de datos MariaDB para la gestión de libros.
 * Se encarga de cargar el controlador JDBC y devolver una instancia de conexión.
 * 
 */
public class Connexio {
    // URL de la base de datos
    private static final String URL = "jdbc:mariadb://192.168.1.10:3306/llibres";

    // Usuario de la base de datos
    private static final String USER = "root";

    // Contraseña de la base de datos
    private static final String PASSWORD = "123456";

    /**
     * Obtiene una conexión a la base de datos.
     *
     * @return una instancia de {@link Connection} conectada a la base de datos.
     * @throws SQLException           si ocurre un error al establecer la conexión.
     * @throws ClassNotFoundException si el controlador MariaDB no se encuentra.
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Carga el controlador JDBC de MariaDB
        Class.forName("org.mariadb.jdbc.Driver");

        // Devuelve una conexión usando los parámetros configurados
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
