package biblioteca.modelo;

import java.util.Date;

public class Prestamo {
    
    private int idPrestamo;
    private int idLibro;
    private String destinatario;
    private Date fechaSalida;
    private Date fechaLimite;
    private Date fechaDevolucion;
    
    // Constructores
    public Prestamo() {
        this.fechaSalida = new Date(); // fecha actual por defecto
    }
    
    public Prestamo(int idLibro, String destinatario, Date fechaLimite) {
        this.idLibro = idLibro;
        this.destinatario = destinatario;
        this.fechaLimite = fechaLimite;
        this.fechaSalida = new Date();
    }
    
    // Getters y Setters
    public int getIdPrestamo() {
        return idPrestamo;
    }
    
    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }
    
    public int getIdLibro() {
        return idLibro;
    }
    
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }
    
    public String getDestinatario() {
        return destinatario;
    }
    
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    
    public Date getFechaSalida() {
        return fechaSalida;
    }
    
    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
    
    public Date getFechaLimite() {
        return fechaLimite;
    }
    
    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }
    
    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }
    
    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
    
    // Métodos útiles
    public boolean isActivo() {
        return fechaDevolucion == null;
    }
    
    @SuppressWarnings("deprecation")
    public int getDiasRestantes() {
        if (!isActivo()) return 0;
        long diff = fechaLimite.getTime() - new Date().getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
}