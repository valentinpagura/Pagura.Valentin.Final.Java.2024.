//package gestiondeproductos.logica;
//
//public class main {
//    public static void main(String[] args) {
//        // Crear una instancia del gestor de productos
//        GestorDeProductos gestor = new GestorDeProductos();
//
//        // Crear algunos productos de ejemplo
//        gestor.crear(new Alimento(1, "Manzana", 0.5, "01/01/2025", true, 100, 123));
//        gestor.crear(new Electronico(2, "Smartphone", 500.0, TipoElectronico.TELEFONO, 50, 456));
//        gestor.crear(new Ropa("Nike", 42, 3, "Camiseta", 25.0, "Ropa", 200, 789));
//
//        // Exportar la lista de productos a un archivo JSON
//        gestor.exportarJSON("productos.json");
//        System.out.println("Productos exportados a 'productos.json' con éxito!!!");
//
//        // Exportar a CSV (nuevo método)
//        gestor.exportarACsv("productos.csv");
//        System.out.println("Productos exportados a 'productos.csv' con éxito!!!");
//    }
//}