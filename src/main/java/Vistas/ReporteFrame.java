package Vistas;

import com.alura.jdbc.controlador.CategoriaController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReporteFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tablaReporte;
    private DefaultTableModel modelo;
    private CategoriaController categoriaController;

    public ReporteFrame(ControlDeStockFrame controlDeStockFrame) {
        super("Reporte de productos del Stock");
        this.categoriaController = new CategoriaController();
        Container container = getContentPane();
        setLayout(null);
        tablaReporte = new JTable();
        tablaReporte.setBounds(0, 0, 600, 400);
        container.add(tablaReporte);
        setSize(600, 400);
        setVisible(true);
        setLocationRelativeTo(controlDeStockFrame);

        modelo = (DefaultTableModel) tablaReporte.getModel();
        modelo.addColumn("");
        modelo.addColumn("");
        modelo.addColumn("");
        modelo.addColumn("");
        cargaReporte();
    }

    private void cargaReporte() {
        var contenido = categoriaController.cargarReporte();

        //TODO: Cargar el contenido en la tabla
        contenido.forEach(fila -> modelo.addRow(new Object[]{}));
    }
}
