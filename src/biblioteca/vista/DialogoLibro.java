package biblioteca.vista;

import biblioteca.modelo.Libro;
import javax.swing.*;
import java.awt.*;

public class DialogoLibro extends JDialog {
    
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtIsbn;
    private JTextField txtEditorial;
    private JSpinner spAño;
    private JComboBox<String> cbGenero;
    private boolean confirmado = false;
    private Libro libroResultado;
    
    public DialogoLibro(JFrame parent, boolean modoEdicion) {
        super(parent, modoEdicion ? "Modificar Libro" : "Añadir Libro", true);
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    public DialogoLibro(JFrame parent, boolean modoEdicion, Libro libro) {
        this(parent, modoEdicion);
        cargarDatosLibro(libro);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setSize(500, 450);
        
        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campos
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Título (*):"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtTitulo = new JTextField(20);
        panelFormulario.add(txtTitulo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panelFormulario.add(new JLabel("Autor (*):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtAutor = new JTextField(20);
        panelFormulario.add(txtAutor, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panelFormulario.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtIsbn = new JTextField(13);
        panelFormulario.add(txtIsbn, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panelFormulario.add(new JLabel("Editorial:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtEditorial = new JTextField(20);
        panelFormulario.add(txtEditorial, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        panelFormulario.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
        spAño = new JSpinner(new SpinnerNumberModel(2024, 1450, 2026, 1));
        panelFormulario.add(spAño, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        panelFormulario.add(new JLabel("Género:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.fill = GridBagConstraints.HORIZONTAL;
        String[] generos = {"Novela", "Poesía", "Ensayo", "Teatro", "Ciencia ficción", 
                            "Fantasía", "Biografía", "Historia", "Infantil", "Otro"};
        cbGenero = new JComboBox<>(generos);
        panelFormulario.add(cbGenero, gbc);
        
        add(panelFormulario, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> cancelar());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void guardar() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        
        if (titulo.isEmpty() || autor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título y el autor son obligatorios", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        libroResultado = new Libro();
        libroResultado.setTitulo(titulo);
        libroResultado.setAutor(autor);
        libroResultado.setIsbn(txtIsbn.getText().trim());
        libroResultado.setEditorial(txtEditorial.getText().trim());
        libroResultado.setAño((Integer) spAño.getValue());
        libroResultado.setGenero((String) cbGenero.getSelectedItem());
        
        confirmado = true;
        dispose();
    }
    
    private void cancelar() {
        confirmado = false;
        dispose();
    }
    
    private void cargarDatosLibro(Libro libro) {
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtIsbn.setText(libro.getIsbn());
        txtEditorial.setText(libro.getEditorial());
        spAño.setValue(libro.getAño());
        if (libro.getGenero() != null) {
            cbGenero.setSelectedItem(libro.getGenero());
        }
    }
    
    public boolean isConfirmado() {
        return confirmado;
    }
    
    public Libro getDatosLibro() {
        return libroResultado;
    }
}