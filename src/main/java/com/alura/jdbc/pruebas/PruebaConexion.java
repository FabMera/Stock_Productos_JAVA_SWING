package com.alura.jdbc.pruebas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PruebaConexion {
    public static void main(String[] args) throws SQLException {
        Connection connection = new CreaConexion().recuperarConexion();

        System.out.println("Cerrando la conexion..");
        connection.close();
    }
}
