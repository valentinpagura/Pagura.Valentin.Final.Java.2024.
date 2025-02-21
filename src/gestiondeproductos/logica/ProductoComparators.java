/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestiondeproductos.logica;

import java.util.Comparator;

public class ProductoComparators {
    // Comparator para ordenar por nombre
    public static final Comparator<Producto> COMPARAR_POR_NOMBRE = 
        (p1, p2) -> p1.getNombre().compareToIgnoreCase(p2.getNombre());
    
    // Comparator para ordenar por stock
    public static final Comparator<Producto> COMPARAR_POR_STOCK = 
        (p1, p2) -> Integer.compare(p1.getStock(), p2.getStock());
    
    // Comparator para ordenar por ID
    public static final Comparator<Producto> COMPARAR_POR_ID = 
        (p1, p2) -> Integer.compare(p1.getId(), p2.getId());
    
    // Comparator para ordenar por precio descendente
    public static final Comparator<Producto> COMPARAR_POR_PRECIO_DESC = 
        (p1, p2) -> Double.compare(p2.getPrecio(), p1.getPrecio());
}
