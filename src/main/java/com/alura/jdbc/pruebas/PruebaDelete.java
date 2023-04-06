package com.alura.jdbc.pruebas;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PruebaDelete {
    public static void main(String[] args) throws SQLException {
        Connection connection = new CreaConexion().recuperarConexion();
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM producto WHERE id = 99");
        System.out.println(statement.getUpdateCount());

    }
}
