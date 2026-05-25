package biblioteca.vista;

import biblioteca.modelo.Libro;
import biblioteca.modelo.Prestamo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DialogoHistorial extends JDialog {
    
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    
    public DialogoHistorial(JFrame parent, Libro libro, ArrayList<Prestamo> historial) {
        super(parent, "Historial de Préstamos - " + libro.getTitulo(), true);
        initComponents();
        cargarHistorial(historial);
        setSize(700, 400);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Columnas de la tabla
        String[] columnas = {"ID", "Destinatario", "Fecha Salida", "Fecha Límite", "Fecha Devolución", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaHistorial = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaHistorial);
        add(scroll, BorderLayout.CENTER);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelBoton = new JPanel(new FlowLayout());
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);
    }
    
    private void cargarHistorial(ArrayList<Prestamo> historial) {
        modeloTabla.setRowCount(0);
        for (Prestamo p : historial) {
            String estado = p.isActivo() ? "ACTIVO" : "DEVUELTO";
            Object[] fila = {
                p.getIdPrestamo(),
                p.getDestinatario(),
                p.getFechaSalida(),
                p.getFechaLimite(),
                p.getFechaDevolucion() != null ? p.getFechaDevolucion() : "Pendiente",
                estado
            };
            modeloTabla.addRow(fila);
            
            // Resaltar filas activas en rojo
            if (p.isActivo()) {
                // Se podría añadir color, pero requiere renderer personalizado
            }
        }
    }
}