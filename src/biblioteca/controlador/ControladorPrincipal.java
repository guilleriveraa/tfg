package biblioteca.controlador;

import biblioteca.vista.DialogoLibro;
import biblioteca.vista.VentanaPrincipal;
import biblioteca.vista.DialogoPrestamo;
import biblioteca.vista.DialogoHistorial;
import biblioteca.servicio.LibroService;
import biblioteca.servicio.PrestamoService;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Prestamo;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ControladorPrincipal {
    
    private VentanaPrincipal vista;
    private LibroService libroService;
    private PrestamoService prestamoService;
    
    public ControladorPrincipal(VentanaPrincipal vista) {
        this.vista = vista;
        this.libroService = new LibroService();
        this.prestamoService = new PrestamoService();
    }
    
    public void iniciar() {
        actualizarListadoLibros();
        actualizarEstadisticas();
        actualizarAlertas();
    }
    
    public void actualizarListadoLibros() {
        ArrayList<Libro> libros = libroService.obtenerTodosLosLibros();
        vista.actualizarTabla(libros);
    }
    
    public void actualizarEstadisticas() {
        ArrayList<Libro> libros = libroService.obtenerTodosLosLibros();
        int total = libros.size();
        int disponibles = 0;
        int prestados = 0;
        
        for (Libro l : libros) {
            if (l.isDisponible()) {
                disponibles++;
            } else {
                prestados++;
            }
        }
        
        ArrayList<Prestamo> alertas = prestamoService.obtenerAlertasVencimiento();
        int numAlertas = alertas.size();
        
        vista.actualizarEstadisticas(total, disponibles, prestados, numAlertas);
    }
    
    public void actualizarAlertas() {
        ArrayList<Prestamo> alertas = prestamoService.obtenerAlertasVencimiento();
        
        if (!alertas.isEmpty()) {
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("⚠️ ¡ATENCIÓN! ⚠️\n\n");
            mensaje.append("Tienes ").append(alertas.size()).append(" préstamo(s) próximo(s) a vencer:\n\n");
            
            for (Prestamo p : alertas) {
                Libro libro = libroService.obtenerLibroPorId(p.getIdLibro());
                mensaje.append("📖 ").append(libro.getTitulo())
                       .append("\n   👤 Destinatario: ").append(p.getDestinatario())
                       .append("\n   📅 Vence: ").append(p.getFechaLimite())
                       .append("\n   ⏰ Días restantes: ").append(p.getDiasRestantes()).append("\n\n");
            }
            
            mensaje.append("Por favor, gestione las devoluciones.");
            
            JOptionPane.showMessageDialog(vista, mensaje.toString(), 
                "Préstamos próximos a vencer", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void buscarLibros() {
        String texto = vista.getTextoBusqueda();
        String criterio = vista.getCriterioBusqueda();
        
        if (texto.isEmpty()) {
            actualizarListadoLibros();
        } else {
            ArrayList<Libro> resultados = libroService.buscarLibros(criterio, texto);
            vista.actualizarTabla(resultados);
        }
    }
    
    public void añadirLibro() {
    try {
        DialogoLibro dialogo = new DialogoLibro(vista, false);
        dialogo.setVisible(true);
        
        if (dialogo.isConfirmado()) {
            Libro nuevoLibro = dialogo.getDatosLibro();
            if (libroService.añadirLibro(nuevoLibro)) {
                vista.mostrarMensaje("Libro añadido correctamente");
                actualizarListadoLibros();
                actualizarEstadisticas();
            } else {
                vista.mostrarError("Error al añadir el libro");
            }
        }
    } catch (Exception e) {
        System.err.println("Error inesperado en añadirLibro: " + e.getMessage());
        vista.mostrarError("Error inesperado, contacte con soporte");
    }
}
   
    public void modificarLibro() {
        int idLibro = vista.getLibroSeleccionado();
        if (idLibro == -1) {
            vista.mostrarError("Seleccione un libro para modificar");
            return;
        }
        
        Libro libro = libroService.obtenerLibroPorId(idLibro);
        DialogoLibro dialogo = new DialogoLibro(vista, true, libro);
        dialogo.setVisible(true);
        
        if (dialogo.isConfirmado()) {
            Libro libroModificado = dialogo.getDatosLibro();
            libroModificado.setIdLibro(idLibro);
            
            if (libroService.modificarLibro(libroModificado)) {
                vista.mostrarMensaje("Libro modificado correctamente");
                actualizarListadoLibros();
                actualizarEstadisticas();
            } else {
                vista.mostrarError("Error al modificar el libro");
            }
        }
    }
    
    public void eliminarLibro() {
    try {
        int idLibro = vista.getLibroSeleccionado();
        if (idLibro == -1) {
            vista.mostrarError("Seleccione un libro para eliminar");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(
            vista, 
            "¿Está seguro de que desea eliminar este libro?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (libroService.eliminarLibro(idLibro)) {
                vista.mostrarMensaje("Libro eliminado correctamente");
                actualizarListadoLibros();
                actualizarEstadisticas();
            } else {
                vista.mostrarError("Error al eliminar el libro");
            }
        }
    } catch (Exception e) {
        System.err.println("Error inesperado en eliminarLibro: " + e.getMessage());
        vista.mostrarError("Error inesperado, contacte con soporte");
    }
}
    public void prestarLibro() {
    try {
        int idLibro = vista.getLibroSeleccionado();
        if (idLibro == -1) {
            vista.mostrarError("Seleccione un libro para prestar");
            return;
        }
        
        Libro libro = libroService.obtenerLibroPorId(idLibro);
        if (!libro.isDisponible()) {
            vista.mostrarError("El libro ya está prestado");
            return;
        }
        
        DialogoPrestamo dialogo = new DialogoPrestamo(vista, libro);
        dialogo.setVisible(true);
        
        if (dialogo.isConfirmado()) {
            Prestamo prestamo = dialogo.getDatosPrestamo();
            prestamo.setIdLibro(idLibro);
            
            if (prestamoService.registrarPrestamo(prestamo)) {
                vista.mostrarMensaje("Préstamo registrado correctamente");
                actualizarListadoLibros();
                actualizarEstadisticas();
            } else {
                vista.mostrarError("Error al registrar el préstamo");
            }
        }
    } catch (Exception e) {
        System.err.println("Error inesperado en prestarLibro: " + e.getMessage());
        vista.mostrarError("Error inesperado, contacte con soporte");
    }
}
    public void devolverLibro() {
        int idLibro = vista.getLibroSeleccionado();
        if (idLibro == -1) {
            vista.mostrarError("Seleccione un libro para devolver");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(
            vista, 
            "¿Registrar devolución de este libro?", 
            "Confirmar devolución", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (prestamoService.registrarDevolucion(idLibro)) {
                vista.mostrarMensaje("Devolución registrada correctamente");
                actualizarListadoLibros();
                actualizarEstadisticas();
            } else {
                vista.mostrarError("Error al registrar la devolución");
            }
        }
    }
    
    public void verHistorial() {
        int idLibro = vista.getLibroSeleccionado();
        if (idLibro == -1) {
            vista.mostrarError("Seleccione un libro para ver su historial");
            return;
        }
        
        Libro libro = libroService.obtenerLibroPorId(idLibro);
        ArrayList<Prestamo> historial = prestamoService.obtenerHistorialPorLibro(idLibro);
        
        DialogoHistorial dialogo = new DialogoHistorial(vista, libro, historial);
        dialogo.setVisible(true);
    }
    
    public void marcarFavorito() {
        int idLibro = vista.getLibroSeleccionado();
        if (idLibro == -1) {
            vista.mostrarError("Seleccione un libro para marcar como favorito");
            return;
        }
        
        Libro libro = libroService.obtenerLibroPorId(idLibro);
        boolean nuevoEstado = !libro.isFavorito();
        
        if (libroService.marcarFavorito(idLibro, nuevoEstado)) {
            vista.mostrarMensaje(nuevoEstado ? "⭐ Libro añadido a favoritos" : "☆ Libro eliminado de favoritos");
            actualizarListadoLibros();
            actualizarEstadisticas();
        } else {
            vista.mostrarError("Error al marcar favorito");
        }
    }
    
    public void valorarLibro() {
        int idLibro = vista.getLibroSeleccionado();
        if (idLibro == -1) {
            vista.mostrarError("Seleccione un libro para valorar");
            return;
        }
        
        String[] opciones = {"★ 1 - Muy malo", "★★ 2 - Malo", "★★★ 3 - Normal", "★★★★ 4 - Bueno", "★★★★★ 5 - Excelente", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(
            vista, 
            "Seleccione la valoración para el libro:", 
            "★ Valorar Libro", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.PLAIN_MESSAGE, 
            null, 
            opciones, 
            opciones[2]
        );
        
        if (seleccion >= 0 && seleccion <= 4) {
            int valoracion = seleccion + 1;
            if (libroService.valorarLibro(idLibro, valoracion)) {
                String mensaje = "Valoración guardada: " + valoracion + " estrella" + (valoracion > 1 ? "s" : "");
                vista.mostrarMensaje("★ " + mensaje);
                actualizarListadoLibros();
                actualizarEstadisticas();
            } else {
                vista.mostrarError("Error al guardar valoración");
            }
        }
    }
    
    public void mostrarSoloFavoritos() {
        ArrayList<Libro> favoritos = libroService.obtenerLibrosFavoritos();
        
        if (favoritos.isEmpty()) {
            vista.mostrarMensaje("No hay libros marcados como favoritos");
        } else {
            vista.actualizarTabla(favoritos);
            vista.mostrarMensaje("Mostrando " + favoritos.size() + " libro(s) favorito(s)");
        }
    }
}