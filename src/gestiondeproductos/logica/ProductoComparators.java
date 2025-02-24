/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestiondeproductos.logica;

import java.util.Comparator;

public class ProductoComparators {
    
    // Comparator para ordenar por stock
    public static final Comparator<Producto> COMPARAR_POR_STOCK = 
        (p1, p2) -> Integer.compare(p1.getStock(), p2.getStock());
    
}
