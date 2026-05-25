package biblioteca.servicio;

import biblioteca.dao.LibroDAO;
import biblioteca.dao.PrestamoDAO;
import biblioteca.dao.IPrestamoDAO;
import biblioteca.dao.ILibroDAO;
import biblioteca.modelo.Prestamo;
import biblioteca.modelo.Libro;
import java.util.ArrayList;
import java.util.Date;

public class PrestamoService {
    
    private IPrestamoDAO prestamoDAO;
    private ILibroDAO libroDAO;
    
    public PrestamoService() {
        this.prestamoDAO = new PrestamoDAO();
        this.libroDAO = new LibroDAO();
    }
    
    
    public boolean validarPrestamo(Prestamo prestamo) {
        if (prestamo.getDestinatario() == null || prestamo.getDestinatario().trim().isEmpty()) {
            System.err.println("Error: El destinatario es obligatorio");
            return false;
        }
        
        if (prestamo.getFechaLimite() == null) {
            System.err.println("Error: La fecha límite es obligatoria");
            return false;
        }
        
        Date hoy = new Date();
        if (prestamo.getFechaLimite().before(hoy)) {
            System.err.println("Error: La fecha límite debe ser posterior a hoy");
            return false;
        }
        
        // Verificar que el libro existe y está disponible
        Libro libro = libroDAO.buscarPorId(prestamo.getIdLibro());
        if (libro == null) {
            System.err.println("Error: El libro no existe");
            return false;
        }
        
        if (!libro.isDisponible()) {
            System.err.println("Error: El libro no está disponible para préstamo");
            return false;
        }
        
        return true;
    }
    
    public boolean registrarPrestamo(Prestamo prestamo) {
        if (!validarPrestamo(prestamo)) {
            return false;
        }
        
        // Registrar el préstamo
        boolean resultado = prestamoDAO.insertar(prestamo);
        
        if (resultado) {
            // Actualizar estado del libro
            libroDAO.actualizarEstado(prestamo.getIdLibro(), "prestado");
        }
        
        return resultado;
    }
    
    public boolean registrarDevolucion(int idLibro) {
        Prestamo prestamoActivo = prestamoDAO.buscarPrestamoActivo(idLibro);
        
        if (prestamoActivo == null) {
            System.err.println("Error: El libro no tiene un préstamo activo");
            return false;
        }
        
        // Cerrar el préstamo
        boolean resultado = prestamoDAO.cerrarPrestamo(
            prestamoActivo.getIdPrestamo(), 
            new Date()
        );
        
        if (resultado) {
            // Actualizar estado del libro
            libroDAO.actualizarEstado(idLibro, "disponible");
        }
        
        return resultado;
    }
    
    public Prestamo obtenerPrestamoActivo(int idLibro) {
        return prestamoDAO.buscarPrestamoActivo(idLibro);
    }
    
    public ArrayList<Prestamo> obtenerHistorialPorLibro(int idLibro) {
        return prestamoDAO.listarHistorialPorLibro(idLibro);
    }
    
    public ArrayList<Prestamo> obtenerPrestamosActivos() {
        return prestamoDAO.listarPrestamosActivos();
    }
    
    public ArrayList<Prestamo> obtenerAlertasVencimiento() {
        return prestamoDAO.listarPrestamosProximosVencer(3);
    }
}