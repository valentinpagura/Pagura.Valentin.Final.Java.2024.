package gestiondeproductos.logica;

public class Electronico extends Producto {
    private int garantia; // en meses
    private TipoElectronico tipo; // Tipo del producto electrónico

    // Constructor
    public Electronico(int id, String nombre, double precio, String categoria, int stock) {
        super(id, nombre, precio, categoria, stock);
        this.garantia = garantia;
        this.tipo = tipo;
    }

    // Getter y Setter
    public int getGarantia() {
        return garantia;
    }

    public void setGarantia(int garantia) {
        this.garantia = garantia;
    }

    public TipoElectronico getTipo() {
        return tipo;
    }

    public void setTipo(TipoElectronico tipo) {
        this.tipo = tipo;
    }

    // Implementación del método abstracto
    @Override
    public void mostrarDetalles() {
        System.out.println("Producto Electrónico:");
        System.out.println("ID: " + getId());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Precio: $" + getPrecio());
        System.out.println("Categoría: " + getCategoria());
        System.out.println("Stock: " + getStock());
        System.out.println("Garantía: " + garantia + " meses");
        System.out.println("Tipo: " + tipo); // Muestra el tipo de producto electrónico
    }
}