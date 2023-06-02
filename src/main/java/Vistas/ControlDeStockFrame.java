package Vistas;

import com.alura.jdbc.controlador.CategoriaController;
import com.alura.jdbc.controlador.ProductoController;
import com.alura.jdbc.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

public class ControlDeStockFrame extends JFrame {
    private JLabel labelNombre, labelDescripcion, labelCantidad, labelaCategoria;
    private JTextField textoNombre, textotDescripcion, textoCantidad;
    private JComboBox comboCategoria;
    private JButton botonGuardar, botonModificar, botonEliminar, botonReporte, botonLimpiar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private final ProductoController productoController;
    private final CategoriaController categoriaController;

    public ControlDeStockFrame() {
        super("Productos");
        this.productoController = new ProductoController();
        this.categoriaController = new CategoriaController();
        Container container = getContentPane();
        setLayout(null);
        configurarCamposDelFormulario(container);
        configurarTablaDeContenido(container);
        configurarAccionesDelFormulario(container);
    }


    private void configurarTablaDeContenido(Container container) {
        tabla = new JTable();
        /*MODELO*/
        modelo = (DefaultTableModel) tabla.getModel();
        modelo.addColumn("Identificador del producto");
        modelo.addColumn("Nombre del producto");
        modelo.addColumn("Descripcion del producto");
        modelo.addColumn("Cantidad");

        /*TABLA*/
        cargarTabla();

        tabla.setBounds(10, 205, 760, 280);
        /*BOTONES*/

        botonEliminar = new JButton("Eliminar");
        botonEliminar.setBounds(10, 500, 80, 20);
        botonModificar = new JButton("Modificar");
        botonModificar.setBounds(100, 500, 80, 20);
        botonReporte = new JButton("Reporte");
        botonReporte.setBounds(190, 500, 80, 20);

        container.add(tabla);
        container.add(botonEliminar);
        container.add(botonModificar);
        container.add(botonReporte);

        setSize(800, 600);
        setVisible(true);
        setLocationRelativeTo(null);

    }

    private void configurarCamposDelFormulario(Container container) {
        labelNombre = new JLabel("Nombre del producto");
        labelDescripcion = new JLabel("Descripcion del producto");
        labelCantidad = new JLabel("Cantidad");
        labelaCategoria = new JLabel("Categoria del Producto");

        labelNombre.setBounds(10, 10, 240, 15);
        labelDescripcion.setBounds(10, 50, 240, 15);
        labelCantidad.setBounds(10, 90, 240, 15);
        labelaCategoria.setBounds(10, 130, 240, 15);

        labelNombre.setForeground(Color.BLACK);
        labelDescripcion.setForeground(Color.BLACK);
        labelaCategoria.setForeground(Color.BLACK);

        textoNombre = new JTextField();
        textotDescripcion = new JTextField();
        textoCantidad = new JTextField();

        textoNombre.setBounds(10, 25, 265, 20);
        textotDescripcion.setBounds(10, 65, 265, 20);
        textoCantidad.setBounds(10, 105, 265, 20);


        comboCategoria = new JComboBox<>();
        comboCategoria.addItem("Seleccione una categoria");
        comboCategoria.setBounds(10, 145, 240, 20);


        //TODO
        var categorias = this.categoriaController.listar();
        //categorias.forEach(categoria -> comboCategoria.addItem(categoria));

        botonGuardar = new JButton("Guardar");
        botonLimpiar = new JButton("Limpiar");
        botonGuardar.setBounds(10, 175, 80, 20);
        botonLimpiar.setBounds(100, 175, 80, 20);

        container.add(labelNombre);
        container.add(labelDescripcion);
        container.add(labelCantidad);
        container.add(labelaCategoria);
        container.add(textoNombre);
        container.add(textotDescripcion);
        container.add(textoCantidad);
        container.add(comboCategoria);
        container.add(botonGuardar);
        container.add(botonLimpiar);

    }

    private void configurarAccionesDelFormulario(Container container) {
        botonGuardar.addActionListener(e -> {
            guardar();
            limpiarTabla();
            cargarTabla();
        });
        botonLimpiar.addActionListener(e -> limpiarFormulario());
        botonEliminar.addActionListener(e -> {
            eliminar();
            limpiarTabla();
            cargarTabla();
        });
        botonModificar.addActionListener(e -> {
            modificar();
            limpiarTabla();
            cargarTabla();
        });
        botonReporte.addActionListener(e -> generarReporte());


    }

    private void generarReporte() {
        new ReporteFrame(this);
    }

    private void limpiarTabla() {
        modelo.getDataVector().clear();
    }

    private boolean tieneFilaSeleccionada() {
        return tabla.getSelectedRowCount() == 0 || tabla.getSelectedColumnCount() == 0;
    }

    private void modificar() {
        if (tieneFilaSeleccionada()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un item");
            return;
        }
        Optional.ofNullable(modelo.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn())).ifPresentOrElse(fila -> {
            Integer id = Integer.valueOf(modelo.getValueAt(tabla.getSelectedRow(), 0).toString());
            String nombre = (String) modelo.getValueAt(tabla.getSelectedRow(), 1);
            String descripcion = (String) modelo.getValueAt(tabla.getSelectedRow(), 2);
            Integer cantidad = Integer.valueOf(modelo.getValueAt(tabla.getSelectedRow(), 3).toString());
            var filasModificadas = this.productoController.modificar(nombre, descripcion, cantidad,id);

            JOptionPane.showMessageDialog(this, String.format("%d item modificado con exito", filasModificadas));
        }, () -> JOptionPane.showMessageDialog(this, "Debe seleccionar un item"));


    }

    private void eliminar() {
        if (tieneFilaSeleccionada()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un item");
            return;
        }
        Optional.ofNullable(modelo.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn())).ifPresentOrElse(fila -> {
            Integer id = Integer.valueOf(modelo.getValueAt(tabla.getSelectedRow(), 0).toString());
            var filasModificadas = this.productoController.eliminar(id);

            modelo.removeRow(tabla.getSelectedRow());
            JOptionPane.showMessageDialog(this, String.format("%d item eliminado con exito", filasModificadas));
        }, () -> JOptionPane.showMessageDialog(this, "Debe seleccionar un item"));
    }

    private void cargarTabla() {
        var producto = this.productoController.listar();
        producto.forEach(product -> modelo.addRow(new Object[]{
                product.getId(),
                product.getNombre(),
                product.getDescripcion(),
                product.getCantidad()}));
    }

    private void guardar() {
        if (textoNombre.getText().isBlank() || textotDescripcion.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Los campos nombre y descripcion son obligatorios");
            return;
        }
        Integer cantidadInt;
        try {
            cantidadInt = Integer.parseInt(textoCantidad.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, String.format("El campo cantidad debe ser un numero dentro del rango %d - %d", 0, Integer.MAX_VALUE));
            return;
        }

        //TODO
        var producto = new Producto(textoNombre.getText(), textotDescripcion.getText(), cantidadInt);
        var categoria = comboCategoria.getSelectedItem();
        this.productoController.guardar(producto);

        JOptionPane.showMessageDialog(this, "Producto guardado con exito");
        this.limpiarFormulario();

    }

    private void limpiarFormulario() {
        this.textoNombre.setText("");
        this.textotDescripcion.setText("");
        this.textoCantidad.setText("");
        this.comboCategoria.setSelectedIndex(0);
    }

}
