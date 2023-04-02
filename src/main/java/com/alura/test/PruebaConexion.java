package com.alura.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PruebaConexion {
    public static void main(String[] args) throws SQLException {
        Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/control_de_stock?useTimeZone=true&ServerTimeZone=UTC","root","12345");

        System.out.println("Conexión exitosa");
        conexion.close();
    }
}
