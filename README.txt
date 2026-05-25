=== BIBLIOTECA PERSONAL - PROYECTO DAM ===

Autor: Guillermo Rivera Alcón
Tutor: Silvia Prieto Herraez
Fecha: Mayo 2026

=== REQUISITOS DEL SISTEMA ===
- Java JDK 17 o superior
- MySQL Server 8.0 o superior

=== INSTALACIÓN ===
1. Instalar MySQL Server 8.0
2. Ejecutar el script sql/script_biblioteca.sql en MySQL Workbench
3. Configurar la contraseña en ConexionBD.java si es necesario

=== EJECUCIÓN ===
Opción 1: Doble clic en ejecutar.bat
Opción 2: java -jar dist/biblioteca.jar

=== CREDENCIALES POR DEFECTO ===
Usuario MySQL: root
Contraseña: (la que configuraste)

=== FUNCIONALIDADES ===
- CRUD completo de libros
- Gestión de préstamos y devoluciones
- Historial de préstamos
- Alertas de vencimiento (3 días)
- Libros favoritos (⭐)
- Valoración de libros (1-5 estrellas)
- Búsqueda por título, autor o género
- Estadísticas en tiempo real