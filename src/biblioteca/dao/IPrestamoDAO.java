package biblioteca.dao;   // ← CAMBIADO a minúsculas

import biblioteca.modelo.Prestamo;
import java.util.ArrayList;
import java.util.Date;

public interface IPrestamoDAO {
    
    // Operaciones básicas
    boolean insertar(Prestamo prestamo);
    boolean cerrarPrestamo(int idPrestamo, Date fechaDevolucion);
    
    // Consultas
    Prestamo buscarPrestamoActivo(int idLibro);
    ArrayList<Prestamo> listarPrestamosActivos();
    ArrayList<Prestamo> listarHistorialPorLibro(int idLibro);
    ArrayList<Prestamo> listarPrestamosProximosVencer(int dias);
}