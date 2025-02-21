package gestiondeproductos.logica.gui;

import gestiondeproductos.logica.*;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Optional;
import java.util.stream.Collectors;

public class GestionDeProductosApp extends Application {
    private final GestorDeProductos gestor = new GestorDeProductos(); // Gestor de productos.
    private TableView<Producto> tabla; // Tabla para mostrar productos.

    public static void main(String[] args) {
        launch(args); // Inicia la aplicación.
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestión de Productos"); // Título de la ventana.

        configurarTabla(); // Configura la tabla.

        VBox vbox = new VBox(10); // Contenedor vertical para los botones.
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(
            crearBoton("Agregar Producto", this::agregarProducto), // Botón para agregar.
            crearBoton("Eliminar Producto", this::eliminarProducto), // Botón para eliminar.
            crearBoton("Actualizar Producto", this::actualizarProducto), // Botón para actualizar.
            crearBoton("Filtrar por Categoría", this::filtrarPorCategoria), // Botón para filtrar.
            crearBoton("Mostrar Todos", () -> { // Botón para mostrar todos.
                tabla.getItems().clear();
                tabla.getItems().addAll(gestor.leer());
            }),
            crearBoton("Exportar a JSON", this::exportarJSON), // Botón para exportar a JSON.
            crearBoton("Exportar a CSV", this::exportarCSV), // Botón para exportar a CSV.
            crearBoton("Exportar a TXT", this::exportarTXT) // Botón para exportar a TXT.
        );

        HBox root = new HBox(10); // Contenedor principal.
        root.setPadding(new Insets(10));
        root.getChildren().addAll(tabla, vbox); // Agrega tabla y botones.

        Scene scene = new Scene(root, 800, 400); // Crea la escena.
        primaryStage.setScene(scene); // Asigna la escena a la ventana.
        primaryStage.show(); // Muestra la ventana.
    }

    private void filtrarPorCategoria() {
        Dialog<String> dialog = new Dialog<>(); // Diálogo para seleccionar categoría.
        dialog.setTitle("Filtrar Productos");
        dialog.setHeaderText("Seleccione el tipo de producto");

        ComboBox<String> tipoComboBox = new ComboBox<>(); // ComboBox para categorías.
        tipoComboBox.getItems().addAll("Todos", "Ropa", "Alimento", "Electronico"); // Opciones.
        tipoComboBox.setValue("Todos"); // Valor por defecto.

        GridPane grid = new GridPane(); // Layout para el diálogo.
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.addRow(0, new Label("Tipo:"), tipoComboBox); // Agrega ComboBox al layout.

        dialog.getDialogPane().setContent(grid); // Asigna el layout al diálogo.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); // Botones.

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return tipoComboBox.getValue(); // Retorna el valor seleccionado.
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait(); // Muestra el diálogo.
        result.ifPresent(tipo -> {
            if ("Todos".equals(tipo)) {
                tabla.getItems().clear();
                tabla.getItems().addAll(gestor.leer()); // Muestra todos los productos.
            } else {
                tabla.getItems().clear();
                List<Producto> productosFiltrados = gestor.leer().stream()
                    .filter(p -> {
                        switch (tipo) {
                            case "Ropa":
                                return p instanceof Ropa; // Filtra por Ropa.
                            case "Alimento":
                                return p instanceof Alimento; // Filtra por Alimento.
                            case "Electronico":
                                return p instanceof Electronico; // Filtra por Electronico.
                            default:
                                return false;
                        }
                    })
                    .collect(Collectors.toList());

                if (productosFiltrados.isEmpty()) {
                    mostrarAlerta("Información", "No se encontraron productos del tipo: " + tipo);
                } else {
                    tabla.getItems().addAll(productosFiltrados); // Muestra productos filtrados.
                }
            }
        });
    }

    private void configurarTabla() {
        tabla = new TableView<>(); // Inicializa la tabla.

        // Columnas de la tabla.
        TableColumn<Producto, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        TableColumn<Producto, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

        TableColumn<Producto, Double> precioCol = new TableColumn<>("Precio");
        precioCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecio()).asObject());

        TableColumn<Producto, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());

        TableColumn<Producto, String> categoriaCol = new TableColumn<>("Categoría");
        categoriaCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoria()));

        tabla.getColumns().addAll(idCol, nombreCol, precioCol, stockCol, categoriaCol); // Agrega columnas.
    }

    private Button crearBoton(String texto, Runnable accion) {
        Button boton = new Button(texto); // Crea un botón.
        boton.setOnAction(e -> accion.run()); // Asigna la acción.
        return boton;
    }

    private void agregarProducto() {
        Dialog<Producto> dialog = new Dialog<>(); // Diálogo para agregar producto.
        dialog.setTitle("Agregar Producto");
        dialog.setHeaderText("Ingrese los datos del producto");

        // Campos del formulario.
        TextField idField = new TextField();
        TextField nombreField = new TextField();
        TextField precioField = new TextField();
        TextField stockField = new TextField();
        TextField categoriaField = new TextField();

        ComboBox<String> tipoProductoComboBox = new ComboBox<>(); // ComboBox para tipo de producto.
        tipoProductoComboBox.getItems().addAll("Ropa", "Alimento", "Electronico"); // Opciones.
        tipoProductoComboBox.setValue("Ropa"); // Valor por defecto.

        GridPane grid = new GridPane(); // Layout para el diálogo.
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        // Agrega campos al layout.
        grid.addRow(0, new Label("ID:"), idField);
        grid.addRow(1, new Label("Nombre:"), nombreField);
        grid.addRow(2, new Label("Precio:"), precioField);
        grid.addRow(3, new Label("Stock:"), stockField);
        grid.addRow(4, new Label("Categoría:"), categoriaField);
        grid.addRow(5, new Label("Tipo de producto:"), tipoProductoComboBox);

        dialog.getDialogPane().setContent(grid); // Asigna el layout al diálogo.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); // Botones.

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    // Valida campos obligatorios.
                    if (idField.getText().isEmpty() || nombreField.getText().isEmpty() ||
                        precioField.getText().isEmpty() || stockField.getText().isEmpty() ||
                        categoriaField.getText().isEmpty()) {
                        mostrarAlerta("Error", "Todos los campos son obligatorios.");
                        return null;
                    }
                    
                    // Obtiene valores del formulario.
                    int id = Integer.parseInt(idField.getText());
                    String nombre = nombreField.getText();
                    double precio = Double.parseDouble(precioField.getText());
                    int stock = Integer.parseInt(stockField.getText());
                    String categoria = categoriaField.getText();

                    // Crea el producto según el tipo seleccionado.
                    return switch (tipoProductoComboBox.getValue()) {
                        case "Ropa" -> new Ropa(id, nombre, precio, categoria, stock);
                        case "Alimento" -> new Alimento(id, nombre, precio, categoria, stock);
                        case "Electronico" -> new Electronico(id, nombre, precio, categoria, stock);
                        default -> {
                            mostrarAlerta("Error", "Tipo de producto no válido.");
                            yield null;
                        }
                    };
                } catch (NumberFormatException e) {
                    mostrarAlerta("Error", "Por favor, ingrese valores válidos para ID, Precio y Stock.");
                    return null;
                }
            }
            return null;
        });

        Optional<Producto> result = dialog.showAndWait(); // Muestra el diálogo.
        result.ifPresent(producto -> {
            gestor.crear(producto); // Agrega el producto al gestor.
            tabla.getItems().add(producto); // Agrega el producto a la tabla.
        });
    }

    private void eliminarProducto() {
        Producto seleccionado = tabla.getSelectionModel().getSelectedItem(); // Obtiene el producto seleccionado.
        if (seleccionado != null) {
            gestor.eliminar(seleccionado.getId()); // Elimina el producto del gestor.
            tabla.getItems().remove(seleccionado); // Elimina el producto de la tabla.
        } else {
            mostrarAlerta("Error", "Seleccione un producto para eliminar.");
        }
    }

    private void actualizarProducto() {
        Producto seleccionado = tabla.getSelectionModel().getSelectedItem(); // Obtiene el producto seleccionado.
        if (seleccionado != null) {
            Dialog<Producto> dialog = new Dialog<>(); // Diálogo para actualizar producto.
            dialog.setTitle("Actualizar Producto");
            dialog.setHeaderText("Ingrese los nuevos datos del producto");

            // Campos del formulario con valores actuales.
            TextField nombreField = new TextField(seleccionado.getNombre());
            TextField precioField = new TextField(String.valueOf(seleccionado.getPrecio()));
            TextField stockField = new TextField(String.valueOf(seleccionado.getStock()));
            TextField categoriaField = new TextField(seleccionado.getCategoria());

            GridPane grid = new GridPane(); // Layout para el diálogo.
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(10));
            
            // Agrega campos al layout.
            grid.addRow(0, new Label("Nombre:"), nombreField);
            grid.addRow(1, new Label("Precio:"), precioField);
            grid.addRow(2, new Label("Stock:"), stockField);
            grid.addRow(3, new Label("Categoría:"), categoriaField);

            dialog.getDialogPane().setContent(grid); // Asigna el layout al diálogo.
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); // Botones.

            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    try {
                        // Obtiene nuevos valores del formulario.
                        double precio = Double.parseDouble(precioField.getText());
                        int stock = Integer.parseInt(stockField.getText());
                        
                        // Crea el producto actualizado según el tipo.
                        if (seleccionado instanceof Ropa) {
                            return new Ropa(seleccionado.getId(), nombreField.getText(), 
                                          precio, categoriaField.getText(), stock);
                        } else if (seleccionado instanceof Alimento) {
                            return new Alimento(seleccionado.getId(), nombreField.getText(), 
                                             precio, categoriaField.getText(), stock);
                        } else if (seleccionado instanceof Electronico) {
                            return new Electronico(seleccionado.getId(), nombreField.getText(), 
                                                precio, categoriaField.getText(), stock);
                        }
                        return null;
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Error", "Por favor, ingrese valores válidos para Precio y Stock.");
                        return null;
                    }
                }
                return null;
            });

            Optional<Producto> result = dialog.showAndWait(); // Muestra el diálogo.
            result.ifPresent(producto -> {
                gestor.actualizar(producto); // Actualiza el producto en el gestor.
                tabla.getItems().remove(seleccionado); // Elimina el producto antiguo de la tabla.
                tabla.getItems().add(producto); // Agrega el producto actualizado.
                tabla.refresh(); // Refresca la tabla.
            });
        } else {
            mostrarAlerta("Error", "Seleccione un producto para actualizar.");
        }
    }

    private void exportarJSON() {
        gestor.exportarJSON(tabla.getItems(), "productos.json"); // Exporta a JSON.
        mostrarAlerta("Éxito", "Productos exportados a JSON correctamente.");
    }

    private void exportarCSV() {
        gestor.exportarCSV(tabla.getItems(), "productos.csv"); // Exporta a CSV.
        mostrarAlerta("Éxito", "Productos exportados a CSV correctamente.");
    }

    private void exportarTXT() {
        gestor.exportarTXT(tabla.getItems(), "productos.txt"); // Exporta a TXT.
        mostrarAlerta("Éxito", "Productos exportados a TXT correctamente.");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Crea una alerta.
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait(); // Muestra la alerta.
    }
}