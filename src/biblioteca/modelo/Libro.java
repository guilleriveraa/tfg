package biblioteca.modelo;

public class Libro {
    
    private int idLibro;
    private String titulo;
    private String autor;
    private String isbn;
    private String editorial;
    private int año;
    private String genero;
    private byte[] portada;
    private String estado;
    private boolean favorito;
    private Integer valoracion;
    
    // Constructores
    public Libro() {
        this.estado = "disponible";
        this.favorito = false;
        this.valoracion = null;
    }
    
    public Libro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
        this.estado = "disponible";
        this.favorito = false;
        this.valoracion = null;
    }
    
    // Getters y Setters
    public int getIdLibro() {
        return idLibro;
    }
    
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getAutor() {
        return autor;
    }
    
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getEditorial() {
        return editorial;
    }
    
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
    
    public int getAño() {
        return año;
    }
    
    public void setAño(int año) {
        this.año = año;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public byte[] getPortada() {
        return portada;
    }
    
    public void setPortada(byte[] portada) {
        this.portada = portada;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public boolean isFavorito() {
        return favorito;
    }
    
    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
    
    public Integer getValoracion() {
        return valoracion;
    }
    
    public void setValoracion(Integer valoracion) {
        if (valoracion != null && (valoracion < 1 || valoracion > 5)) {
            throw new IllegalArgumentException("La valoración debe ser entre 1 y 5");
        }
        this.valoracion = valoracion;
    }
    
    // Métodos útiles
    public boolean isDisponible() {
        return estado.equals("disponible");
    }
    
    public String getEstrellasTexto() {
        if (valoracion == null) return "☆☆☆☆☆";
        StringBuilder estrellas = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            if (i <= valoracion) {
                estrellas.append("★");
            } else {
                estrellas.append("☆");
            }
        }
        return estrellas.toString();
    }
    
    public String getFavoritoIcono() {
        return favorito ? "⭐" : "☆";
    }
    
    @Override
    public String toString() {
        return titulo + " - " + autor;
    }
}