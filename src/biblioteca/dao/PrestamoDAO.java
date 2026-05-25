package biblioteca.dao;

import biblioteca.util.ConexionBD;
import biblioteca.dao.IPrestamoDAO;
import biblioteca.modelo.Prestamo;
import biblioteca.modelo.Prestamo;
import biblioteca.util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class PrestamoDAO implements IPrestamoDAO {
    
    private Connection conexion;
    
    public PrestamoDAO() {
        this.conexion = ConexionBD.getInstance().getConnection();
    }
    
    @Override
    public boolean insertar(Prestamo prestamo) {
        String sql = "INSERT INTO prestamos (id_libro, destinatario, fecha_salida, fecha_limite) "
                   + "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, prestamo.getIdLibro());
            ps.setString(2, prestamo.getDestinatario());
            ps.setDate(3, new java.sql.Date(prestamo.getFechaSalida().getTime()));
            ps.setDate(4, new java.sql.Date(prestamo.getFechaLimite().getTime()));
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar préstamo: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean cerrarPrestamo(int idPrestamo, Date fechaDevolucion) {
        String sql = "UPDATE prestamos SET fecha_devolucion=? WHERE id_prestamo=?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(fechaDevolucion.getTime()));
            ps.setInt(2, idPrestamo);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al cerrar préstamo: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Prestamo buscarPrestamoActivo(int idLibro) {
        String sql = "SELECT * FROM prestamos WHERE id_libro=? AND fecha_devolucion IS NULL";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idLibro);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearPrestamo(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar préstamo activo: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public ArrayList<Prestamo> listarPrestamosActivos() {
        ArrayList<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE fecha_devolucion IS NULL ORDER BY fecha_limite";
        
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                prestamos.add(mapearPrestamo(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar préstamos activos: " + e.getMessage());
        }
        return prestamos;
    }
    
    @Override
    public ArrayList<Prestamo> listarHistorialPorLibro(int idLibro) {
        ArrayList<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE id_libro=? ORDER BY fecha_salida DESC";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idLibro);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                prestamos.add(mapearPrestamo(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar historial: " + e.getMessage());
        }
        return prestamos;
    }
    
    @Override
    public ArrayList<Prestamo> listarPrestamosProximosVencer(int dias) {
        ArrayList<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE fecha_devolucion IS NULL "
                   + "AND DATEDIFF(fecha_limite, CURDATE()) <= ? "
                   + "ORDER BY fecha_limite";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, dias);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                prestamos.add(mapearPrestamo(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar préstamos próximos a vencer: " + e.getMessage());
        }
        return prestamos;
    }
    
    // Método privado para mapear ResultSet a objeto Prestamo
    private Prestamo mapearPrestamo(ResultSet rs) throws SQLException {
        Prestamo prestamo = new Prestamo();
        prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
        prestamo.setIdLibro(rs.getInt("id_libro"));
        prestamo.setDestinatario(rs.getString("destinatario"));
        prestamo.setFechaSalida(rs.getDate("fecha_salida"));
        prestamo.setFechaLimite(rs.getDate("fecha_limite"));
        prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion"));
        return prestamo;
    }
}