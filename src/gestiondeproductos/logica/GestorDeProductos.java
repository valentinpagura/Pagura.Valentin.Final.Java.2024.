package gestiondeproductos.logica;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.function.Consumer;

public class GestorDeProductos implements Crud<Producto> {
    private final List<Producto> productos; // Lista de productos.

    public GestorDeProductos() {
        this.productos = new ArrayList<>(); // Inicializa la lista de productos.
    }

    @Override
    public void crear(Producto producto) { // Recibe un tipo PRODUCTO y nombra producto (create)
        if (existeProducto(producto.getId())) {
            throw new RuntimeException("Producto duplicado"); // Evita productos duplicados.
        }
        productos.add(producto); // Agrega el producto a la lista.
    }

    @Override
    public List<Producto> leer() { // (read)
        return productos; // Devuelve la lista completa de productos.
    }

    @Override
    public Producto leer(int id) { // (read)
        return productos.stream()
                       .filter(p -> p.getId() == id) // Busca por ID.
                       .findFirst()
                       .orElse(null); // Retorna null si no encuentra el producto.
    }

    @Override
    public void actualizar(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == producto.getId()) {
                productos.set(i, producto); // Actualiza el producto.
                return;
            }
        }
    }

    @Override
    public void eliminar(int id) {
        productos.removeIf(p -> p.getId() == id); // Elimina el producto por ID.
    }

    private boolean existeProducto(int id) {
        return productos.stream().anyMatch(p -> p.getId() == id); // Verifica si el producto existe.
    }

    public void ordenarPorPrecio() {
        Collections.sort(productos); // Ordena por precio (usa Comparable en Producto).
    }

    public void ordenarPor(Comparator<Producto> comparator) {
        Collections.sort(productos, comparator); // Ordena usando un comparador personalizado.
    }

    public List<Producto> filtrarPorPrecioMayorA(double precioMinimo) {
        return productos.stream()
                       .filter(p -> p.getPrecio() > precioMinimo) // Filtra por precio mínimo.
                       .collect(Collectors.toList());
    }

    public void procesarProductos(List<? super Producto> destino, Predicate<Producto> filtro) {
        productos.stream()
                .filter(filtro) // Aplica el filtro.
                .forEach(destino::add); // Agrega los productos filtrados al destino.
    }

    public List<Producto> filtrarPorNombre(String texto) {
        return productos.stream()
                       .filter(p -> p.getNombre().toLowerCase().contains(texto.toLowerCase())) // Filtra por nombre.
                       .collect(Collectors.toList());
    }

    public List<Producto> filtrarPorRangoPrecio(double min, double max) {
        return productos.stream()
                       .filter(p -> p.getPrecio() >= min && p.getPrecio() <= max) // Filtra por rango de precio.
                       .collect(Collectors.toList());
    }

    public List<Producto> filtrarPorCategoria(String categoria) {
        return productos.stream()
                       .filter(p -> p.getCategoria().equalsIgnoreCase(categoria)) // Filtra por categoría.
                       .collect(Collectors.toList());
    }

    public List<Alimento> filtrarPorFechaVencimiento(Date fechaLimite) {
        return productos.stream()
                       .filter(p -> p instanceof Alimento) // Solo filtra Alimentos.
                       .map(p -> (Alimento) p) // Convierte a Alimento.
                       .filter(a -> a.getFechaVencimiento().before(fechaLimite)) // Filtra por fecha de vencimiento.
                       .collect(Collectors.toList());
    }

    // Filtro adicional para electrónicos basado en tipo.
    public List<Electronico> filtrarPorTipoElectronico(TipoElectronico tipo) {
        return productos.stream()
                       .filter(p -> p instanceof Electronico) // Solo filtra Electrónicos.
                       .map(p -> (Electronico) p) // Convierte a Electronico.
                       .filter(e -> e.getTipo() == tipo)
                       .collect(Collectors.toList());
    }

    // Método para exportar a JSON (acepta una lista de productos).
    public void exportarJSON(List<Producto> productos, String archivo) {
        if (productos.isEmpty()) {
            System.err.println("La lista de productos está vacía. No se exportará nada.");
            return;
        }

        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Date.class, new DateSerializer()) // Serializador personalizado para java.sql.Date
            .create();

        File file = new File(archivo);
        if (file.exists()) {
            file.delete(); // Elimina el archivo existente antes de escribir uno nuevo.
        }

        try (Writer writer = new FileWriter(archivo)) {
            gson.toJson(productos, writer);
            System.out.println("Productos exportados a '" + archivo + "' correctamente.");
        } catch (IOException e) {
            System.err.println("Error al exportar a JSON: " + e.getMessage());
        }
    }

    // Método para exportar a CSV (acepta una lista de productos).
    public void exportarCSV(List<Producto> productos, String archivo) {
        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write("ID,Nombre,Precio,Stock,Categoría,FechaVencimiento,EsPerecedero\n"); // Escribe la cabecera del CSV.
            for (Producto producto : productos) {
                if (producto instanceof Alimento) {
                    Alimento alimento = (Alimento) producto;
                    writer.write(
                        producto.getId() + "," +
                        producto.getNombre() + "," +
                        producto.getPrecio() + "," +
                        producto.getStock() + "," +
                        producto.getCategoria() + "," +
                        alimento.getFechaVencimiento() + "," +
                        alimento.isEsPerecedero() + "\n"
                    );
                } else {
                    writer.write(
                        producto.getId() + "," +
                        producto.getNombre() + "," +
                        producto.getPrecio() + "," +
                        producto.getStock() + "," +
                        producto.getCategoria() + ",,\n"
                    );
                }
            }
            System.out.println("Productos exportados a '" + archivo + "' correctamente.");
        } catch (IOException e) {
            System.err.println("Error al exportar a CSV: " + e.getMessage());
        }
    }

    // Método para exportar a TXT (acepta una lista de productos).
    public void exportarTXT(List<Producto> productos, String archivo) {
        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write("=== Listado de Productos ===\n\n"); // Escribe el encabezado.
            writer.write(String.format("%-5s %-20s %-10s %-10s %-15s %-15s %-15s\n", 
                "ID", "Nombre", "Precio", "Stock", "Categoría", "Fecha Venc.", "Perecedero")); // Formato de columnas.
            writer.write("-------------------------------------------------\n");

            for (Producto producto : productos) {
                if (producto instanceof Alimento) {
                    Alimento alimento = (Alimento) producto;
                    writer.write(String.format("%-5d %-20s %-10.2f %-10d %-15s %-15s %-15s\n", 
                        producto.getId(), 
                        producto.getNombre(), 
                        producto.getPrecio(), 
                        producto.getStock(), 
                        producto.getCategoria(),
                        alimento.getFechaVencimiento(),
                        alimento.isEsPerecedero()));
                } else {
                    writer.write(String.format("%-5d %-20s %-10.2f %-10d %-15s %-15s %-15s\n", 
                        producto.getId(), 
                        producto.getNombre(), 
                        producto.getPrecio(), 
                        producto.getStock(), 
                        producto.getCategoria(),
                        "N/A",
                        "N/A"));
                }
            }

            System.out.println("Listado exportado a '" + archivo + "' correctamente.");
        } catch (IOException e) {
            System.err.println("Error al exportar el listado: " + e.getMessage());
        }
    }

    // Método para importar desde JSON.
    public void importarJSON(String archivo) {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer()) // Deserializador personalizado para java.sql.Date
            .create();

        try (FileReader reader = new FileReader(archivo)) {
            Type tipoListaProductos = new TypeToken<List<Producto>>() {}.getType(); // Define el tipo de lista.
            List<Producto> productosImportados = gson.fromJson(reader, tipoListaProductos); // Convierte JSON a lista.

            if (productosImportados != null) {
                this.productos.clear(); // Limpia la lista actual.
                this.productos.addAll(productosImportados); // Agrega los productos importados.
                System.out.println("Productos importados desde '" + archivo + "' correctamente.");
            } else {
                System.err.println("El archivo JSON está vacío o no contiene datos válidos.");
            }
        } catch (IOException e) {
            System.err.println("Error al importar desde JSON: " + e.getMessage());
        }
    }

    // Consumer para incrementar el precio en un 10%
    public Consumer<Producto> incrementarPrecio10PorCiento = producto -> {
        double nuevoPrecio = producto.getPrecio() * 1.10; // Aumenta el precio en un 10%
        producto.setPrecio(nuevoPrecio);
        System.out.println("Precio actualizado: " + producto.getNombre() + " - Nuevo precio: " + nuevoPrecio);
    };
}