package biblioteca.pruebas;

import biblioteca.servicio.LibroService;
import biblioteca.servicio.PrestamoService;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Prestamo;
import java.util.Date;
import java.util.Calendar;

public class PruebasSistema {
    
    private static LibroService libroService = new LibroService();
    private static PrestamoService prestamoService = new PrestamoService();
    private static int pruebasOK = 0;
    private static int pruebasFAIL = 0;
    
    public static void main(String[] args) {
        System.out.println("=== PRUEBAS AUTOMATIZADAS DEL SISTEMA ===\n");
        
        pruebaConexionBD();
        pruebaValidarLibroSinTitulo();
        pruebaValidarLibroSinAutor();
        pruebaValidarPrestamoSinDestinatario();
        pruebaValidarPrestamoFechaPasada();
        pruebaListarLibros();
        
        System.out.println("\n=== RESUMEN DE PRUEBAS ===");
        System.out.println("✅ Pruebas superadas: " + pruebasOK);
        System.out.println("❌ Pruebas fallidas: " + pruebasFAIL);
        
        if (pruebasFAIL == 0) {
            System.out.println("\n🎉 TODAS LAS PRUEBAS PASARON CORRECTAMENTE");
        } else {
            System.out.println("\n⚠️ Hay pruebas fallidas que deben revisarse");
        }
    }
    
    /**
     * Prueba 1: Verificar la conexión a la base de datos MySQL
     */
    private static void pruebaConexionBD() {
        try {
            // Intentar obtener la conexión (si falla lanza excepción)
            biblioteca.util.ConexionBD.getInstance().getConnection();
            System.out.println("✅ [P-01] Conexión a MySQL: OK");
            pruebasOK++;
        } catch (Exception e) {
            System.out.println("❌ [P-01] Conexión a MySQL: FAIL - " + e.getMessage());
            pruebasFAIL++;
        }
    }
    
    /**
     * Prueba 2: Validar que un libro sin título sea rechazado
     */
    private static void pruebaValidarLibroSinTitulo() {
        Libro libro = new Libro();
        libro.setTitulo("");
        libro.setAutor("Autor de prueba");
        
        boolean resultado = libroService.validarLibro(libro);
        
        if (resultado == false) {
            System.out.println("✅ [P-02] Validar libro sin título: OK (devuelve false)");
            pruebasOK++;
        } else {
            System.out.println("❌ [P-02] Validar libro sin título: FAIL (debería devolver false)");
            pruebasFAIL++;
        }
    }
    
    /**
     * Prueba 3: Validar que un libro sin autor sea rechazado
     */
    private static void pruebaValidarLibroSinAutor() {
        Libro libro = new Libro();
        libro.setTitulo("Título de prueba");
        libro.setAutor("");
        
        boolean resultado = libroService.validarLibro(libro);
        
        if (resultado == false) {
            System.out.println("✅ [P-03] Validar libro sin autor: OK (devuelve false)");
            pruebasOK++;
        } else {
            System.out.println("❌ [P-03] Validar libro sin autor: FAIL (debería devolver false)");
            pruebasFAIL++;
        }
    }
    
    /**
     * Prueba 4: Validar que un préstamo sin destinatario sea rechazado
     */
    private static void pruebaValidarPrestamoSinDestinatario() {
        Prestamo prestamo = new Prestamo();
        prestamo.setDestinatario("");
        prestamo.setIdLibro(1);
        prestamo.setFechaLimite(new Date());
        
        boolean resultado = prestamoService.validarPrestamo(prestamo);
        
        if (resultado == false) {
            System.out.println("✅ [P-04] Validar préstamo sin destinatario: OK (devuelve false)");
            pruebasOK++;
        } else {
            System.out.println("❌ [P-04] Validar préstamo sin destinatario: FAIL (debería devolver false)");
            pruebasFAIL++;
        }
    }
    
    /**
     * Prueba 5: Validar que un préstamo con fecha límite pasada sea rechazado
     */
    private static void pruebaValidarPrestamoFechaPasada() {
        Prestamo prestamo = new Prestamo();
        prestamo.setDestinatario("Prueba Fecha");
        prestamo.setIdLibro(1);
        
        // Establecer una fecha pasada (ayer)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        prestamo.setFechaLimite(cal.getTime());
        
        boolean resultado = prestamoService.validarPrestamo(prestamo);
        
        if (resultado == false) {
            System.out.println("✅ [P-05] Validar préstamo fecha pasada: OK (devuelve false)");
            pruebasOK++;
        } else {
            System.out.println("❌ [P-05] Validar préstamo fecha pasada: FAIL (debería devolver false)");
            pruebasFAIL++;
        }
    }
    
    /**
     * Prueba 6: Verificar que se pueden listar los libros desde la BD
     */
    private static void pruebaListarLibros() {
        try {
            var libros = libroService.obtenerTodosLosLibros();
            if (libros != null && libros.size() > 0) {
                System.out.println("✅ [P-06] Listar libros: OK (" + libros.size() + " libros encontrados)");
                pruebasOK++;
            } else {
                System.out.println("❌ [P-06] Listar libros: FAIL (no hay libros en la BD)");
                pruebasFAIL++;
            }
        } catch (Exception e) {
            System.out.println("❌ [P-06] Listar libros: FAIL - " + e.getMessage());
            pruebasFAIL++;
        }
    }
}