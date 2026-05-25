package biblioteca.vista;

import biblioteca.modelo.Libro;
import biblioteca.modelo.Prestamo;
import javax.swing.*;
import java.util.Date;
import java.util.Calendar;

public class DialogoPrestamo extends JDialog {
    
    private JTextField txtDestinatario;
    private JSpinner spFechaLimite;
    private boolean confirmado = false;
    private Prestamo prestamoResultado;
    private Libro libro;
    
    public DialogoPrestamo(JFrame parent, Libro libro) {
        super(parent, "Registrar Préstamo - " + libro.getTitulo(), true);
        this.libro = libro;
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(400, 200);
        
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Información del libro
        JLabel lblInfo = new JLabel("Libro: " + libro.getTitulo() + " - " + libro.getAutor());
        lblInfo.setAlignmentX(CENTER_ALIGNMENT);
        panelFormulario.add(lblInfo);
        panelFormulario.add(Box.createVerticalStrut(15));
        
        // Destinatario
        JPanel pDestinatario = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        pDestinatario.add(new JLabel("Destinatario:"));
        txtDestinatario = new JTextField(20);
        pDestinatario.add(txtDestinatario);
        panelFormulario.add(pDestinatario);
        
        // Fecha límite
        JPanel pFecha = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        pFecha.add(new JLabel("Fecha límite:"));
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 14);
        spFechaLimite = new JSpinner(new SpinnerDateModel(cal.getTime(), new Date(), null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spFechaLimite, "dd/MM/yyyy");
        spFechaLimite.setEditor(editor);
        pFecha.add(spFechaLimite);
        panelFormulario.add(pFecha);
        
        add(panelFormulario, java.awt.BorderLayout.CENTER);
        
        // Botones
        JPanel panelBotones = new JPanel(new java.awt.FlowLayout());
        JButton btnGuardar = new JButton("Registrar Préstamo");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> cancelar());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        add(panelBotones, java.awt.BorderLayout.SOUTH);
    }
    
    private void guardar() {
        String destinatario = txtDestinatario.getText().trim();
        if (destinatario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El destinatario es obligatorio", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Date fechaLimite = (Date) spFechaLimite.getValue();
        
        prestamoResultado = new Prestamo();
        prestamoResultado.setDestinatario(destinatario);
        prestamoResultado.setFechaLimite(fechaLimite);
        prestamoResultado.setFechaSalida(new Date());
        
        confirmado = true;
        dispose();
    }
    
    private void cancelar() {
        confirmado = false;
        dispose();
    }
    
    public boolean isConfirmado() {
        return confirmado;
    }
    
    public Prestamo getDatosPrestamo() {
        return prestamoResultado;
    }
}