package biblioteca.vista;

import biblioteca.controlador.ControladorPrincipal;
import biblioteca.modelo.Libro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class VentanaPrincipal extends JFrame {
    
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JComboBox<String> cbCriterio;
    private JLabel lblTotalLibros;
    private JLabel lblDisponibles;
    private JLabel lblPrestados;
    private JLabel lblAlertas;
    
    private ControladorPrincipal controlador;
    
    // Colores del tema claro
    private final Color COLOR_FONDO = new Color(240, 240, 245);
    private final Color COLOR_PANEL = new Color(255, 255, 255);
    private final Color COLOR_BORDE = new Color(200, 200, 210);
    private final Color COLOR_TEXTO = new Color(50, 50, 60);
    private final Color COLOR_TEXTO_CLARO = new Color(100, 100, 110);
    
    public VentanaPrincipal() {
        configurarTema();
        controlador = new ControladorPrincipal(this);
        initComponents();
        controlador.iniciar();
    }
    
    private void configurarTema() {
        UIManager.put("Panel.background", COLOR_FONDO);
        UIManager.put("Label.foreground", COLOR_TEXTO);
        UIManager.put("Button.background", new Color(60, 60, 65));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", COLOR_PANEL);
        UIManager.put("TextField.foreground", COLOR_TEXTO);
        UIManager.put("TextField.caretForeground", COLOR_TEXTO);
        UIManager.put("Table.background", COLOR_PANEL);
        UIManager.put("Table.foreground", COLOR_TEXTO);
        UIManager.put("TableHeader.background", new Color(230, 230, 235));
        UIManager.put("TableHeader.foreground", COLOR_TEXTO);
        UIManager.put("ComboBox.background", COLOR_PANEL);
        UIManager.put("ComboBox.foreground", COLOR_TEXTO);
        UIManager.put("ScrollPane.background", COLOR_FONDO);
        UIManager.put("OptionPane.background", COLOR_PANEL);
        UIManager.put("OptionPane.messageForeground", COLOR_TEXTO);
    }
    
    private void initComponents() {
        setTitle("📚 Biblioteca Personal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(COLOR_FONDO);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);
        
        JPanel headerPanel = crearHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel searchPanel = crearPanelBusqueda();
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        
        JScrollPane scrollTabla = crearTabla();
        mainPanel.add(scrollTabla, BorderLayout.CENTER);
        
        JPanel statsPanel = crearPanelEstadisticas();
        mainPanel.add(statsPanel, BorderLayout.SOUTH);
        
        JPanel botonesPanel = crearPanelBotones();
        mainPanel.add(botonesPanel, BorderLayout.EAST);
    }
    
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(230, 230, 235));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDE),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel titulo = new JLabel("📚 Biblioteca Personal");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(40, 40, 50));
        
        JLabel subtitulo = new JLabel("Gestión de libros y préstamos");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitulo.setForeground(COLOR_TEXTO_CLARO);
        
        JPanel tituloPanel = new JPanel(new BorderLayout());
        tituloPanel.setOpaque(false);
        tituloPanel.add(titulo, BorderLayout.NORTH);
        tituloPanel.add(subtitulo, BorderLayout.SOUTH);
        
        header.add(tituloPanel, BorderLayout.WEST);
        
        return header;
    }
    
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel lblBuscar = new JLabel("🔍 Buscar por:");
        lblBuscar.setForeground(COLOR_TEXTO);
        panel.add(lblBuscar);
        
        cbCriterio = new JComboBox<>(new String[]{"Título", "Autor", "Género"});
        panel.add(cbCriterio);
        
        txtBuscar = new JTextField(20);
        panel.add(txtBuscar);
        
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(e -> controlador.buscarLibros());
        panel.add(btnBuscar);
        
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(new Color(46, 204, 113));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.addActionListener(e -> controlador.actualizarListadoLibros());
        panel.add(btnRefrescar);
        
        return panel;
    }
    
    private JScrollPane crearTabla() {
        String[] columnas = {"⭐", "ID", "📖 Título", "✍️ Autor", "★ Valoración", "📌 Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setRowHeight(30);
        tablaLibros.setSelectionBackground(new Color(52, 152, 219, 80));
        tablaLibros.setSelectionForeground(COLOR_TEXTO);
        tablaLibros.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        tablaLibros.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaLibros.getColumnModel().getColumn(1).setPreferredWidth(40);
        tablaLibros.getColumnModel().getColumn(2).setPreferredWidth(220);
        tablaLibros.getColumnModel().getColumn(3).setPreferredWidth(180);
        tablaLibros.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaLibros.getColumnModel().getColumn(5).setPreferredWidth(90);
        
        tablaLibros.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if ("disponible".equals(value)) {
                        c.setBackground(new Color(46, 204, 113));
                        c.setForeground(Color.WHITE);
                    } else if ("prestado".equals(value)) {
                        c.setBackground(new Color(231, 76, 60));
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(COLOR_PANEL);
                        c.setForeground(COLOR_TEXTO);
                    }
                }
                setHorizontalAlignment(CENTER);
                return c;
            }
        });
        
        JScrollPane scroll = new JScrollPane(tablaLibros);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
        return scroll;
    }
    
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 10));
        panel.setBackground(new Color(230, 230, 235));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDE),
            new EmptyBorder(12, 15, 12, 15)
        ));
        
        lblTotalLibros = crearTarjetaStats("📚 Total Libros", "0");
        lblDisponibles = crearTarjetaStats("✅ Disponibles", "0");
        lblPrestados = crearTarjetaStats("📤 Prestados", "0");
        lblAlertas = crearTarjetaStats("⚠️ Alertas", "0");
        
        panel.add(lblTotalLibros);
        panel.add(lblDisponibles);
        panel.add(lblPrestados);
        panel.add(lblAlertas);
        
        return panel;
    }
    
    private JLabel crearTarjetaStats(String titulo, String valor) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + titulo + "<br><b style='font-size: 22px; color:#333333;'>" + valor + "</b></div></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(COLOR_TEXTO);
        return label;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setPreferredSize(new Dimension(200, 0));
        
        JLabel lblAcciones = new JLabel("🛠️ ACCIONES");
        lblAcciones.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblAcciones.setForeground(new Color(52, 152, 219));
        lblAcciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblAcciones);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JButton btnAñadir = crearBoton("➕ Añadir Libro", new Color(46, 204, 113));
        JButton btnModificar = crearBoton("✏️ Modificar", new Color(52, 152, 219));
        JButton btnEliminar = crearBoton("🗑️ Eliminar", new Color(231, 76, 60));
        JButton btnPrestar = crearBoton("📤 Prestar", new Color(241, 196, 15));
        JButton btnDevolver = crearBoton("📥 Devolver", new Color(155, 89, 182));
        JButton btnHistorial = crearBoton("📜 Historial", new Color(52, 73, 94));
        JButton btnFavorito = crearBoton("⭐ Marcar Favorito", new Color(243, 156, 18));
        JButton btnValorar = crearBoton("★ Valorar Libro", new Color(142, 68, 173));
        JButton btnVerFavoritos = crearBoton("⭐ Ver Favoritos", new Color(243, 156, 18));
        JButton btnVerTodos = crearBoton("📋 Ver Todos", new Color(52, 73, 94));
        
        btnAñadir.addActionListener(e -> controlador.añadirLibro());
        btnModificar.addActionListener(e -> controlador.modificarLibro());
        btnEliminar.addActionListener(e -> controlador.eliminarLibro());
        btnPrestar.addActionListener(e -> controlador.prestarLibro());
        btnDevolver.addActionListener(e -> controlador.devolverLibro());
        btnHistorial.addActionListener(e -> controlador.verHistorial());
        btnFavorito.addActionListener(e -> controlador.marcarFavorito());
        btnValorar.addActionListener(e -> controlador.valorarLibro());
        btnVerFavoritos.addActionListener(e -> controlador.mostrarSoloFavoritos());
        btnVerTodos.addActionListener(e -> controlador.actualizarListadoLibros());
        
        panel.add(btnAñadir);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnModificar);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnEliminar);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(new JSeparator());
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(btnPrestar);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnDevolver);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnHistorial);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(new JSeparator());
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(btnFavorito);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnValorar);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnVerFavoritos);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnVerTodos);
        
        return panel;
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(180, 40));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }
    
    public void actualizarTabla(ArrayList<Libro> libros) {
        modeloTabla.setRowCount(0);
        for (Libro libro : libros) {
            Object[] fila = {
                libro.getFavoritoIcono(),
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getEstrellasTexto(),
                libro.getEstado()
            };
            modeloTabla.addRow(fila);
        }
    }
    
    public void actualizarEstadisticas(int total, int disponibles, int prestados, int alertas) {
        lblTotalLibros.setText("<html><div style='text-align: center;'>📚 Total Libros<br><b style='font-size: 22px; color:#333333;'>" + total + "</b></div></html>");
        lblDisponibles.setText("<html><div style='text-align: center;'>✅ Disponibles<br><b style='font-size: 22px; color:#333333;'>" + disponibles + "</b></div></html>");
        lblPrestados.setText("<html><div style='text-align: center;'>📤 Prestados<br><b style='font-size: 22px; color:#333333;'>" + prestados + "</b></div></html>");
        lblAlertas.setText("<html><div style='text-align: center;'>⚠️ Alertas<br><b style='font-size: 22px; color:#e74c3c;'>" + alertas + "</b></div></html>");
    }
    
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void mostrarError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public int getLibroSeleccionado() {
        int fila = tablaLibros.getSelectedRow();
        if (fila >= 0) {
            return (int) modeloTabla.getValueAt(fila, 1);
        }
        return -1;
    }
    
    public String getTextoBusqueda() {
        return txtBuscar.getText();
    }
    
    public String getCriterioBusqueda() {
        String criterio = (String) cbCriterio.getSelectedItem();
        if ("Título".equals(criterio)) return "titulo";
        if ("Autor".equals(criterio)) return "autor";
        if ("Género".equals(criterio)) return "genero";
        return "titulo";
    }
}