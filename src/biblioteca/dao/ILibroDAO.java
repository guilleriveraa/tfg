package biblioteca.dao;

import biblioteca.modelo.Libro;
import java.util.ArrayList;

public interface ILibroDAO {
    boolean insertar(Libro libro);
    boolean actualizar(Libro libro);
    boolean eliminar(int idLibro);
    Libro buscarPorId(int idLibro);
    ArrayList<Libro> listarTodos();
    ArrayList<Libro> buscarPorTexto(String campo, String texto);
    boolean actualizarEstado(int idLibro, String estado);
    boolean existeLibro(int idLibro);
}