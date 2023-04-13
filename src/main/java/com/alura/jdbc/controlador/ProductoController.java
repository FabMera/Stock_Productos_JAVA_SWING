package com.alura.jdbc.controlador;

import com.alura.jdbc.pruebas.CreaConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*SE RECOMIENDA EL USO DE UN TRY-WITCH -RESOURCES PARA QUE LAS CONEXIONES,STATEMENT Y RESULTSET SE CIERREN AUTOMATICAMENTE */
public class ProductoController {
    public int modificar(String nombre, String descripcion, Integer id, Integer Cantidad) throws SQLException {
        final Connection connection = new CreaConexion().recuperarConexion();
        try (connection) {

            final PreparedStatement statement = connection.prepareStatement("UPDATE producto SET nombre = ?, descripcion = ?, cantidad = ? WHERE id = ?");
            try (statement) {
                statement.setString(1, nombre);
                statement.setString(2, descripcion);
                statement.setInt(3, Cantidad);
                statement.setInt(4, id);
                statement.execute();
                int updateCount = statement.getUpdateCount();
                return updateCount;
            }

        }
    }

    public int eliminar(Integer id) throws SQLException {
        final Connection connection = new CreaConexion().recuperarConexion();

        try (connection) {
            final PreparedStatement statement = connection.prepareStatement("DELETE FROM producto WHERE id = ?");
            try (statement) {
                statement.setInt(1, id);
                statement.execute();
                int updateCount = statement.getUpdateCount();

                return updateCount;
            }
        }
    }

    public List<Map<String, String>> listar() throws SQLException {
        final Connection connection = new CreaConexion().recuperarConexion();
        try (connection) {
            final PreparedStatement statement = connection.prepareStatement("SELECT id,nombre,descripcion,cantidad FROM producto");

            try (statement) {
                statement.execute();
                ResultSet resultSet = statement.getResultSet();
                List<Map<String, String>> resultado = new ArrayList<>();
                while (resultSet.next()) {
                    Map<String, String> fila = new HashMap<>(); /*map.put(clave,valor)*/
                    fila.put("id", String.valueOf(resultSet.getInt("id")));
                    fila.put("nombre", resultSet.getString("nombre"));
                    fila.put("descripcion", resultSet.getString("descripcion"));
                    fila.put("cantidad", String.valueOf(resultSet.getInt("cantidad")));
                    resultado.add(fila);
                }
                return resultado;
            }

        }
    }

    public void guardar(Map<String, String> producto) throws SQLException {
        String nombre = producto.get("nombre");
        String descripcion = producto.get("descripcion");
        Integer cantidad = Integer.valueOf(producto.get("cantidad"));
        Integer maximoCantidad = 50;
        final Connection connection = new CreaConexion().recuperarConexion();
        /*tener el control de BD*/
        try (connection) {
            connection.setAutoCommit(false);
            final PreparedStatement statement = connection.prepareStatement("INSERT INTO producto (nombre,descripcion,cantidad) " +
                    "VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);

            try (statement) {

                do {
                    int cantidadParaGuardar = Math.min(cantidad, maximoCantidad);
                    ejecutarRegistro(nombre, descripcion, cantidadParaGuardar, statement);
                    cantidad -= maximoCantidad;
                } while (cantidad > 0);
                connection.commit();
                System.out.println("commit");
            } catch (Exception e) {
                connection.rollback();
                System.out.println("rollback");

            }

        }

    }

    private static void ejecutarRegistro(String nombre, String descripcion, Integer cantidad, PreparedStatement statement) throws SQLException {
        if (cantidad < 50) {
            throw new RuntimeException("Ocurrio un error");
        }

        statement.setString(1, nombre);
        statement.setString(2, descripcion);
        statement.setInt(3, cantidad);
        statement.execute();
        /*TRY WITCH RESOURCES,se cierran solos los objetos como conexiones-*/

        final ResultSet resultSet = statement.getGeneratedKeys();
        try (resultSet) {

            while (resultSet.next()) {
                System.out.println(String.format("Fue insertado el producto de ID %d", resultSet.getInt(1)));

            }
        }

    }
}
