/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gestiondeproductos.logica;

import java.util.List;

public interface Crud<T> {  //T es un tipo generico de objeto  
    void crear(T t);
    void actualizar(T t);
    void eliminar(int id);
    T leer(int id);
    List<T> leer();  // Método para obtener una lista de elementos
}


