package biblioteca.dao;

import biblioteca.util.ConexionBD;
import biblioteca.modelo.Libro;
import java.sql.*;
import java.util.ArrayList;

public class LibroDAO implements ILibroDAO {
    
    private Connection conexion;
    
    public LibroDAO() {
        this.conexion = ConexionBD.getInstance().getConnection();
    }
    
    @Override
    public boolean insertar(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor, isbn, editorial, año, genero, portada, estado, favorito, valoracion) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getAño());
            ps.setString(6, libro.getGenero());
            ps.setBytes(7, libro.getPortada());
            ps.setString(8, libro.getEstado());
            ps.setBoolean(9, libro.isFavorito());
            ps.setObject(10, libro.getValoracion(), java.sql.Types.INTEGER);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar libro: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE libros SET titulo=?, autor=?, isbn=?, editorial=?, "
                   + "año=?, genero=?, portada=?, estado=?, favorito=?, valoracion=? WHERE id_libro=?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getAño());
            ps.setString(6, libro.getGenero());
            ps.setBytes(7, libro.getPortada());
            ps.setString(8, libro.getEstado());
            ps.setBoolean(9, libro.isFavorito());
            ps.setObject(10, libro.getValoracion(), java.sql.Types.INTEGER);
            ps.setInt(11, libro.getIdLibro());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean eliminar(int idLibro) {
        String sql = "DELETE FROM libros WHERE id_libro=?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idLibro);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Libro buscarPorId(int idLibro) {
        String sql = "SELECT * FROM libros WHERE id_libro=?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idLibro);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearLibro(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar libro por ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public ArrayList<Libro> listarTodos() {
        ArrayList<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros ORDER BY titulo";
        
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                libros.add(mapearLibro(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar libros: " + e.getMessage());
        }
        return libros;
    }
    
    @Override
    public ArrayList<Libro> buscarPorTexto(String campo, String texto) {
        ArrayList<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE " + campo + " LIKE ? ORDER BY titulo";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, "%" + texto + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                libros.add(mapearLibro(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
        }
        return libros;
    }
    
    @Override
    public boolean actualizarEstado(int idLibro, String estado) {
        String sql = "UPDATE libros SET estado=? WHERE id_libro=?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idLibro);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean existeLibro(int idLibro) {
        String sql = "SELECT 1 FROM libros WHERE id_libro=?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idLibro);
            ResultSet rs = ps.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia: " + e.getMessage());
            return false;
        }
    }
    
    private Libro mapearLibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        libro.setIdLibro(rs.getInt("id_libro"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setIsbn(rs.getString("isbn"));
        libro.setEditorial(rs.getString("editorial"));
        libro.setAño(rs.getInt("año"));
        libro.setGenero(rs.getString("genero"));
        libro.setPortada(rs.getBytes("portada"));
        libro.setEstado(rs.getString("estado"));
        libro.setFavorito(rs.getBoolean("favorito"));
        
        int valoracionInt = rs.getInt("valoracion");
        if (rs.wasNull()) {
            libro.setValoracion(null);
        } else {
            libro.setValoracion(valoracionInt);
        }
        
        return libro;
    }
}