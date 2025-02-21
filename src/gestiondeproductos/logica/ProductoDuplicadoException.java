package gestiondeproductos.logica;


public class ProductoDuplicadoException extends Exception {
    public ProductoDuplicadoException(String mensaje) {
        super("su producto ya se encuentra en la maldita lista!!!!");
    }
}
