package com.alura.jdbc.dao;

import com.alura.jdbc.modelo.Producto;
import com.alura.jdbc.pruebas.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoDAO {

    final private Connection connection;

    public ProductoDAO(Connection connection) {
        this.connection = connection;
    }

    public void guardar(Producto producto) {
        try {
            PreparedStatement statement;
            statement = connection.prepareStatement(
                    "INSERT INTO PRODUCTO "
                            + "(nombre, descripcion, cantidad)"
                            + " VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            try (statement) {
                statement.setString(1, producto.getNombre());
                statement.setString(2, producto.getDescripcion());
                statement.setInt(3, producto.getCantidad());
                statement.execute();
                final ResultSet resultSet = statement.getGeneratedKeys();

                try (resultSet) {
                    while (resultSet.next()) {
                        producto.setId(resultSet.getInt(1));
                        System.out.println(String.format("Fue insertado el producto: %s", producto));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void ejecutarRegistro(Producto producto, PreparedStatement statement) throws SQLException {

        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getDescripcion());
        statement.setInt(3, producto.getCantidad());
        statement.execute();
        final ResultSet resultSet = statement.getGeneratedKeys();
        try (resultSet) {
            while (resultSet.next()) {
                producto.setId(resultSet.getInt(1));
                System.out.println(String.format("Fue insertado el producto %s", producto));

            }
        }

    }

    public List<Producto> listar() {
        List<Producto> resultado = new ArrayList<>();

        try {
            final PreparedStatement statement = connection.prepareStatement("SELECT id,nombre,descripcion,cantidad FROM producto");

            try (statement) {
                statement.execute();
                final ResultSet resultSet = statement.getResultSet();
                try (resultSet) {
                    while (resultSet.next()) {
                        resultado.add(new Producto(resultSet.getInt("ID"),
                                resultSet.getString("NOMBRE"),
                                resultSet.getString("DESCRIPCION"),
                                resultSet.getInt("CANTIDAD")));
                    }
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }

    public int eliminar(Integer id) {
        try {
            final PreparedStatement statement = connection.prepareStatement("DELETE FROM producto WHERE id = ?");
            try (statement) {
                statement.setInt(1, id);
                statement.execute();
                int updateCount = statement.getUpdateCount();

                return updateCount;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int Modificar(String nombre, String descripcion, Integer id, Integer Cantidad) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "UPDATE producto SET nombre = ?, descripcion = ?, cantidad = ? WHERE id = ?");
            try (statement) {
                statement.setString(1, nombre);
                statement.setString(2, descripcion);
                statement.setInt(3, Cantidad);
                statement.setInt(4, id);
                statement.execute();
                int updateCount = statement.getUpdateCount();
                return updateCount;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}