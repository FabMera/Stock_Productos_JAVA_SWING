package com.alura.jdbc.pruebas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreaConexion {

    public Connection recuperarConexion() throws SQLException {

        return DriverManager.getConnection("jdbc:mysql://localhost/control_de_stock?useTimeZone=true&ServerTimeZone=UTC","root","12345");
    }
}
