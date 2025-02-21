package gestiondeproductos.logica;

public class Ropa extends Producto {
    private String material;
    private String talla;

    public Ropa(int id, String nombre, double precio, String categoria, int stock) {
        super(id, nombre, precio, categoria, stock);
        this.material = material;
        this.talla = talla;
    }

    // Métodos getter y setter para material y talla

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }
    

    @Override
    public void mostrarDetalles() {
        System.out.println("Detalles de la Ropa: " + getNombre() + ", Material: " + material + ", Talla: " + talla);
    }
}
