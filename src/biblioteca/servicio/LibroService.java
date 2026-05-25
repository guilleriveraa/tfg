package biblioteca.servicio;

import biblioteca.dao.ILibroDAO;
import biblioteca.dao.LibroDAO;
import biblioteca.modelo.Libro;
import java.util.ArrayList;

public class LibroService {
    
    private ILibroDAO libroDAO;
    
    public LibroService() {
        this.libroDAO = new LibroDAO();
    }
    
    public boolean validarLibro(Libro libro) {
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            System.err.println("Error: El titulo es obligatorio");
            return false;
        }
        if (libro.getAutor() == null || libro.getAutor().trim().isEmpty()) {
            System.err.println("Error: El autor es obligatorio");
            return false;
        }
        return true;
    }
    
    public boolean añadirLibro(Libro libro) {
        if (!validarLibro(libro)) {
            return false;
        }
        libro.setEstado("disponible");
        return libroDAO.insertar(libro);
    }
    
    public boolean modificarLibro(Libro libro) {
        if (!validarLibro(libro)) {
            return false;
        }
        return libroDAO.actualizar(libro);
    }
    
    public boolean eliminarLibro(int idLibro) {
        return libroDAO.eliminar(idLibro);
    }
    
    public ArrayList<Libro> obtenerTodosLosLibros() {
        return libroDAO.listarTodos();
    }
    
    public Libro obtenerLibroPorId(int idLibro) {
        return libroDAO.buscarPorId(idLibro);
    }
    
    public ArrayList<Libro> buscarLibros(String campo, String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return libroDAO.listarTodos();
        }
        return libroDAO.buscarPorTexto(campo, texto);
    }
    
    public boolean actualizarEstado(int idLibro, String estado) {
        return libroDAO.actualizarEstado(idLibro, estado);
    }
    
    // NUEVOS MÉTODOS
    public boolean marcarFavorito(int idLibro, boolean favorito) {
        Libro libro = obtenerLibroPorId(idLibro);
        if (libro == null) return false;
        libro.setFavorito(favorito);
        return libroDAO.actualizar(libro);
    }
    
    public boolean valorarLibro(int idLibro, int valoracion) {
        Libro libro = obtenerLibroPorId(idLibro);
        if (libro == null) return false;
        libro.setValoracion(valoracion);
        return libroDAO.actualizar(libro);
    }
    
    public ArrayList<Libro> obtenerLibrosFavoritos() {
        ArrayList<Libro> todos = obtenerTodosLosLibros();
        ArrayList<Libro> favoritos = new ArrayList<>();
        for (Libro l : todos) {
            if (l.isFavorito()) favoritos.add(l);
        }
        return favoritos;
    }
}