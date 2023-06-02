package com.alura.jdbc.controlador;

import com.alura.jdbc.modelo.Producto;
import com.alura.jdbc.dao.ProductoDAO;
import com.alura.jdbc.pruebas.ConnectionFactory;


import java.sql.*;
import java.util.List;


/*SE RECOMIENDA EL USO DE UN TRY-WITH -RESOURCES PARA QUE LAS CONEXIONES,STATEMENT Y RESULTSET SE CIERREN AUTOMATICAMENTE EXTIENDEN DE LA INTERFAZ AUTOCLOSEABLE */
public class ProductoController {

    private ProductoDAO productoDAO;

    public ProductoController() {
        var factory = new ConnectionFactory();
        this.productoDAO = new ProductoDAO(factory.recuperarConexion());
    }

    public int modificar(String nombre, String descripcion, Integer id, Integer Cantidad) {
        return productoDAO.Modificar(nombre, descripcion, id, Cantidad);
    }

    public int eliminar(Integer id)  {
       return productoDAO.eliminar(id);
    }

    public List<Producto> listar() {
        return productoDAO.listar();
    }

    public void guardar(Producto producto) {
        productoDAO.guardar(producto);

    }
}