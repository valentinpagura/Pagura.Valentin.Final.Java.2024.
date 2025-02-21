package gestiondeproductos.logica;

import java.time.LocalDate;

public class Alimento extends Producto {
    private String fechaVencimiento;
    private boolean esPerecedero;

    // Constructor de Alimento
    public Alimento(int id, String nombre, double precio, String categoria, int stock) {
        super(id, nombre, precio, categoria, stock);
        this.fechaVencimiento = fechaVencimiento;
        this.esPerecedero = esPerecedero;
    }

    // Métodos getter y setter
    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public boolean isEsPerecedero() {
        return esPerecedero;
    }

    public void setEsPerecedero(boolean esPerecedero) {
        this.esPerecedero = esPerecedero;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Producto: " + getNombre());
        System.out.println("ID: " + getId());
        System.out.println("Precio: $" + getPrecio());
        System.out.println("Categoría: " + getCategoria());
        System.out.println("Stock disponible: " + getStock());
        System.out.println("Fecha de vencimiento: " + fechaVencimiento);
        System.out.println("Es perecedero: " + (esPerecedero ? "Sí" : "No"));
    }

    // No es necesario implementar compareTo en Alimento porque ya se implementa en Producto.
}
