package com.alura.jdbc.controlador;

import com.alura.jdbc.pruebas.CreaConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {
    public void modificar(String nombre, String descripcion, Integer id) {
        //TODO
    }

    public int eliminar(Integer id) throws SQLException {
        Connection connection = new CreaConexion().recuperarConexion();
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM producto WHERE id = " + id);
        return statement.getUpdateCount();

    }

    public List<Map<String, String>> listar() throws SQLException {
        Connection connection = new CreaConexion().recuperarConexion();
        Statement statement = connection.createStatement();
        statement.execute("SELECT id,nombre,descripcion,cantidad FROM producto");

        ResultSet resultSet = statement.getResultSet();
        List<Map<String, String>> resultado = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, String> fila = new HashMap<>();
            /*map.put(clave,valor)*/
            fila.put("id", String.valueOf(resultSet.getInt("id")));
            fila.put("nombre", resultSet.getString("nombre"));
            fila.put("descripcion", resultSet.getString("descripcion"));
            fila.put("cantidad", String.valueOf(resultSet.getInt("cantidad")));

            resultado.add(fila);
        }
        connection.close();
        return resultado;
    }

    public void guardar(Map<String, String> producto) throws SQLException {
        Connection connection = new CreaConexion().recuperarConexion();
        Statement statement = connection.createStatement();
        /*comillas simples para consultas SQL*/
        statement.execute("INSERT INTO producto(nombre,descripcion,cantidad ) " +
                "VALUES ('" + producto.get("nombre") + "','" + producto.get("descripcion") + "'," + producto.get("cantidad") + ")", Statement.RETURN_GENERATED_KEYS);

        ResultSet resultSet = statement.getGeneratedKeys();

        while (resultSet.next()) {
            System.out.println(String.format("Fue insertado el producto de ID %d", resultSet.getInt(1)));

        }
    }
}
