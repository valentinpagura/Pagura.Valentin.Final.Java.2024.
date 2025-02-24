package gestiondeproductos.logica;

import java.sql.Date;

public class Alimento extends Producto {
    private Date fechaVencimiento; // Fecha de vencimiento del alimento
    private boolean esPerecedero;  // Indica si el alimento es perecedero

    // Constructor de Alimento
    public Alimento(int id, String nombre, double precio, String categoria, int stock, Date fechaVencimiento, boolean esPerecedero) {
        super(id, nombre, precio, categoria, stock); // Llama al constructor de la clase madre (Producto)
        this.fechaVencimiento = fechaVencimiento;
        this.esPerecedero = esPerecedero;
    }

    // Getters y setters específicos de Alimento
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public boolean isEsPerecedero() {
        return esPerecedero;
    }

    public void setEsPerecedero(boolean esPerecedero) {
        this.esPerecedero = esPerecedero;
    }

    // Método para verificar si el alimento está vencido
    public boolean estaVencido() {
        Date fechaActual = new Date(System.currentTimeMillis()); // Fecha actual
        return fechaVencimiento.before(fechaActual); // Verifica si la fecha de vencimiento es anterior a la actual
    }

    // Sobrescribir el método abstracto mostrarDetalles
    @Override
    public void mostrarDetalles() {
        System.out.println("Detalles del Alimento:");
        System.out.println("ID: " + getId());
        System.out.println("Nombre: " + getNombre());
        System.out.println("Precio: " + getPrecio());
        System.out.println("Categoría: " + getCategoria());
        System.out.println("Stock: " + getStock());
        System.out.println("Fecha de Vencimiento: " + fechaVencimiento);
        System.out.println("Es Perecedero: " + (esPerecedero ? "Sí" : "No"));
        System.out.println("¿Está vencido?: " + (estaVencido() ? "Sí" : "No"));
    }
}