package gestiondeproductos.logica;

public abstract class Producto implements Comparable<Producto> {
    private int id;  //parte de this.
    private String nombre;
    private double precio;        //variables globales
    private String categoria;
    private int stock;

    // Constructor de Producto  //variables locales  //los parametros son de la derecha
    public Producto(int id, String nombre, double precio, String categoria, int stock) {
        this.id = id;  
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.stock = stock;
    }

    // Getters y setters  
    public int getId() {  // con el get veo/atraigo datos 
        return id;
    }

    public void setId(int id) {  ////set sirve para modificar valores tambien
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    // Método setPrecio para modificar el precio
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Método abstracto para mostrar detalles
    public abstract void mostrarDetalles();

    @Override
    public int compareTo(Producto otroProducto) {
        return Double.compare(this.precio, otroProducto.precio); // Comparar precios
    }
}