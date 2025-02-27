package gestiondeproductos.logica.gui;

import gestiondeproductos.logica.*;
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
import java.sql.Date;
import java.util.List;

public class GestionDeProductosApp extends Application {
    private final GestorDeProductos gestor = new GestorDeProductos(); // Gestor de productos: maneja la lógica de negocio.
    private TableView<Producto> tabla; // Tabla para mostrar los productos en la interfaz gráfica.

    public static void main(String[] args) {
        launch(args); // Inicia la aplicación JavaFX.
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestión de Productos"); // Establece el título de la ventana principal.

        configurarTabla(); // Configura la tabla con las columnas necesarias.

        VBox vbox = new VBox(10); // Contenedor vertical para los botones de acciones.
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(
            crearBoton("Agregar Producto", this::agregarProducto), // Botón para agregar un nuevo producto.
            crearBoton("Eliminar Producto", this::eliminarProducto), // Botón para eliminar un producto seleccionado.
            crearBoton("Actualizar Producto", this::actualizarProducto), // Botón para actualizar un producto existente.
            crearBoton("Filtrar por Categoría", this::filtrarPorCategoria), // Botón para filtrar productos por categoría.
            crearBoton("Mostrar Todos", () -> { // Botón para mostrar todos los productos sin filtros.
                tabla.getItems().clear();
                tabla.getItems().addAll(gestor.leer());
            }),
            crearBoton("Exportar a JSON", this::exportarJSON), // Botón para exportar los productos a un archivo JSON.
            crearBoton("Exportar a CSV", this::exportarCSV), // Botón para exportar los productos a un archivo CSV.
            crearBoton("Exportar a TXT", this::exportarTXT), // Botón para exportar los productos a un archivo TXT.
            crearBoton("Incrementar Precio 10%", this::incrementarPrecio10PorCiento), // Botón para incrementar el precio de un producto en un 10%.
            crearBoton("Verificar Vencimiento", this::verificarVencimiento), // Botón para verificar si un alimento está vencido.
            crearBoton("Ordenar por Stock", this::ordenarPorStock) // Botón para ordenar los productos por stock.
        );

        HBox root = new HBox(10); // Contenedor principal que organiza la tabla y los botones.
        root.setPadding(new Insets(10));
        root.getChildren().addAll(tabla, vbox); // Agrega la tabla y el panel de botones al contenedor principal.

        Scene scene = new Scene(root, 900, 400); // Crea la escena con el contenedor principal.
        primaryStage.setScene(scene); // Asigna la escena a la ventana principal.
        primaryStage.show(); // Muestra la ventana principal.
    }

    // Método para configurar la tabla con las columnas necesarias.
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

        // Columna adicional para el tipo de producto electrónico.
        TableColumn<Producto, String> tipoElectronicoCol = new TableColumn<>("Tipo Electrónico");
        tipoElectronicoCol.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Electronico) {
                return new SimpleStringProperty(((Electronico) cellData.getValue()).getTipo().toString());
            } else {
                return new SimpleStringProperty("N/A");
            }
        });

        tabla.getColumns().addAll(idCol, nombreCol, precioCol, stockCol, categoriaCol, tipoElectronicoCol); // Agrega todas las columnas a la tabla.
    }

    // Método para crear un botón con un texto y una acción asociada.
    private Button crearBoton(String texto, Runnable accion) {
        Button boton = new Button(texto); // Crea un botón con el texto proporcionado.
        boton.setOnAction(e -> accion.run()); // Asigna la acción al botón.
        return boton;
    }

    // Método para agregar un nuevo producto.
    private void agregarProducto() {
    Dialog<Producto> dialog = new Dialog<>(); // Diálogo para ingresar los datos del nuevo producto.
    dialog.setTitle("Agregar Producto");
    dialog.setHeaderText("Ingrese los datos del producto");

    // Campos del formulario.
    TextField idField = new TextField();
    TextField nombreField = new TextField();
    TextField precioField = new TextField();
    TextField stockField = new TextField();
    TextField categoriaField = new TextField();
    DatePicker fechaVencimientoPicker = new DatePicker(); // DatePicker para la fecha de vencimiento.
    CheckBox esPerecederoCheckBox = new CheckBox("Es Perecedero"); // Checkbox para productos perecederos.

    ComboBox<String> tipoProductoComboBox = new ComboBox<>(); // ComboBox para seleccionar el tipo de producto.
    tipoProductoComboBox.getItems().addAll("Ropa", "Alimento", "Electronico"); // Opciones disponibles.
    tipoProductoComboBox.setValue("Ropa"); // Valor por defecto.

    ComboBox<TipoElectronico> tipoElectronicoComboBox = new ComboBox<>(); // ComboBox para seleccionar el tipo de electrónico.
    tipoElectronicoComboBox.getItems().addAll(TipoElectronico.values()); // Opciones disponibles.
    tipoElectronicoComboBox.setValue(TipoElectronico.COMPUTADORA); // Valor por defecto.

    GridPane grid = new GridPane(); // Layout para organizar los campos del formulario.
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10));

    // Agrega los campos al layout.
    grid.addRow(0, new Label("ID:"), idField);
    grid.addRow(1, new Label("Nombre:"), nombreField);
    grid.addRow(2, new Label("Precio:"), precioField);
    grid.addRow(3, new Label("Stock:"), stockField);
    grid.addRow(4, new Label("Categoría:"), categoriaField);
    grid.addRow(5, new Label("Tipo de producto:"), tipoProductoComboBox);

    // Listener para mostrar/ocultar campos según el tipo de producto seleccionado.
    tipoProductoComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        if (grid.getChildren().contains(fechaVencimientoPicker)) {
            grid.getChildren().remove(fechaVencimientoPicker);
            grid.getChildren().remove(esPerecederoCheckBox);
            grid.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("Fecha de Vencimiento:"));
            grid.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("Es Perecedero:"));
        }
        if (grid.getChildren().contains(tipoElectronicoComboBox)) {
            grid.getChildren().remove(tipoElectronicoComboBox);
            grid.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().equals("Tipo Electrónico:"));
        }

        if ("Alimento".equals(newVal)) {
            grid.addRow(6, new Label("Fecha de Vencimiento:"), fechaVencimientoPicker);
            grid.addRow(7, new Label("Es Perecedero:"), esPerecederoCheckBox);
        } else if ("Electronico".equals(newVal)) {
            grid.addRow(6, new Label("Tipo Electrónico:"), tipoElectronicoComboBox);
        }
    });

    dialog.getDialogPane().setContent(grid); // Asigna el layout al diálogo.
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); // Botones de OK y Cancelar.

    // Ajustar el tamaño del diálogo para que sea más largo (alto).
    dialog.getDialogPane().setMinHeight(400); // Aumenta la altura del diálogo.

    dialog.setResultConverter(buttonType -> {
        if (buttonType == ButtonType.OK) {
            try {
                // Valida que todos los campos obligatorios estén completos.
                if (idField.getText().isEmpty() || nombreField.getText().isEmpty() ||
                    precioField.getText().isEmpty() || stockField.getText().isEmpty() ||
                    categoriaField.getText().isEmpty()) {
                    mostrarAlerta("Error", "Todos los campos son obligatorios.");
                    return null;
                }

                // Obtiene los valores del formulario.
                int id = Integer.parseInt(idField.getText());
                String nombre = nombreField.getText();
                double precio = Double.parseDouble(precioField.getText());
                int stock = Integer.parseInt(stockField.getText());
                String categoria = categoriaField.getText();

                // Crea el producto según el tipo seleccionado.
                return switch (tipoProductoComboBox.getValue()) {
                    case "Ropa" -> new Ropa(id, nombre, precio, categoria, stock);
                    case "Alimento" -> {
                        if (fechaVencimientoPicker.getValue() == null) {
                            mostrarAlerta("Error", "Por favor, seleccione una fecha de vencimiento válida.");
                            yield null;
                        }
                        Date fechaVencimiento = Date.valueOf(fechaVencimientoPicker.getValue());
                        yield new Alimento(id, nombre, precio, categoria, stock, fechaVencimiento, esPerecederoCheckBox.isSelected());
                    }
                    case "Electronico" -> {
                        Electronico electronico = new Electronico(id, nombre, precio, categoria, stock);
                        electronico.setTipo(tipoElectronicoComboBox.getValue());
                        yield electronico;
                    }
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

    Optional<Producto> result = dialog.showAndWait(); // Muestra el diálogo y espera la respuesta.
    result.ifPresent(producto -> { // Optional es una clase de Java que se utiliza para manejar valores que pueden ser null de manera más segura y expresiva.
        gestor.crear(producto); // Agrega el producto al gestor.
        tabla.getItems().add(producto); // Agrega el producto a la tabla.
    });
}

    // Método para eliminar un producto seleccionado.
    private void eliminarProducto() {
        Producto seleccionado = tabla.getSelectionModel().getSelectedItem(); // Obtiene el producto seleccionado.
        if (seleccionado != null) {
            gestor.eliminar(seleccionado.getId()); // Elimina el producto del gestor.
            tabla.getItems().remove(seleccionado); // Elimina el producto de la tabla.
        } else {
            mostrarAlerta("Error", "Seleccione un producto para eliminar.");
        }
    }

    // Método para actualizar un producto existente.
    private void actualizarProducto() {
        Producto seleccionado = tabla.getSelectionModel().getSelectedItem(); // Obtiene el producto seleccionado.
        if (seleccionado != null) {
            Dialog<Producto> dialog = new Dialog<>(); // Diálogo para actualizar el producto.
            dialog.setTitle("Actualizar Producto");
            dialog.setHeaderText("Ingrese los nuevos datos del producto");

            // Campos del formulario con los valores actuales del producto.
            TextField nombreField = new TextField(seleccionado.getNombre());
            TextField precioField = new TextField(String.valueOf(seleccionado.getPrecio()));
            TextField stockField = new TextField(String.valueOf(seleccionado.getStock()));
            TextField categoriaField = new TextField(seleccionado.getCategoria());
            DatePicker fechaVencimientoPicker = new DatePicker();
            CheckBox esPerecederoCheckBox = new CheckBox("Es Perecedero");

            ComboBox<String> tipoProductoComboBox = new ComboBox<>();
            tipoProductoComboBox.getItems().addAll("Ropa", "Alimento", "Electronico");
            tipoProductoComboBox.setValue(seleccionado instanceof Alimento ? "Alimento" :
                                        seleccionado instanceof Electronico ? "Electronico" : "Ropa");

            ComboBox<TipoElectronico> tipoElectronicoComboBox = new ComboBox<>();
            tipoElectronicoComboBox.getItems().addAll(TipoElectronico.values());
            if (seleccionado instanceof Electronico) {
                tipoElectronicoComboBox.setValue(((Electronico) seleccionado).getTipo());
            } else {
                tipoElectronicoComboBox.setValue(TipoElectronico.COMPUTADORA);
            }

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(10));

            grid.addRow(0, new Label("Nombre:"), nombreField);
            grid.addRow(1, new Label("Precio:"), precioField);
            grid.addRow(2, new Label("Stock:"), stockField);
            grid.addRow(3, new Label("Categoría:"), categoriaField);
            grid.addRow(4, new Label("Tipo de producto:"), tipoProductoComboBox);

            if (seleccionado instanceof Alimento) {
                Alimento alimentoSeleccionado = (Alimento) seleccionado;
                fechaVencimientoPicker.setValue(alimentoSeleccionado.getFechaVencimiento().toLocalDate());
                esPerecederoCheckBox.setSelected(alimentoSeleccionado.isEsPerecedero());
                grid.addRow(5, new Label("Fecha de Vencimiento:"), fechaVencimientoPicker);
                grid.addRow(6, new Label("Es Perecedero:"), esPerecederoCheckBox);
            } else if (seleccionado instanceof Electronico) {
                grid.addRow(5, new Label("Tipo Electrónico:"), tipoElectronicoComboBox);
            }

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    try {
                        double precio = Double.parseDouble(precioField.getText());
                        int stock = Integer.parseInt(stockField.getText());

                        if (seleccionado instanceof Ropa) {
                            return new Ropa(seleccionado.getId(), nombreField.getText(),
                                          precio, categoriaField.getText(), stock);
                        } else if (seleccionado instanceof Alimento) {
                            if (fechaVencimientoPicker.getValue() == null) {
                                mostrarAlerta("Error", "Por favor, seleccione una fecha de vencimiento válida.");
                                return null;
                            }
                            Date fechaVencimiento = Date.valueOf(fechaVencimientoPicker.getValue());
                            return new Alimento(seleccionado.getId(), nombreField.getText(),
                                             precio, categoriaField.getText(), stock,
                                             fechaVencimiento, esPerecederoCheckBox.isSelected());
                        } else if (seleccionado instanceof Electronico) {
                            Electronico electronico = new Electronico(seleccionado.getId(), nombreField.getText(),
                                                                   precio, categoriaField.getText(), stock);
                            electronico.setTipo(tipoElectronicoComboBox.getValue());
                            return electronico;
                        }
                        return null;
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Error", "Por favor, ingrese valores válidos para Precio y Stock.");
                        return null;
                    }
                }
                return null;
            });

            Optional<Producto> result = dialog.showAndWait();
            result.ifPresent(producto -> {
                gestor.actualizar(producto); // Actualiza el producto en el gestor.
                tabla.getItems().remove(seleccionado); // Elimina el producto antiguo de la tabla.
                tabla.getItems().add(producto); // Agrega el producto actualizado.
                tabla.refresh(); // Refresca la tabla para mostrar los cambios.
            });
        } else {
            mostrarAlerta("Error", "Seleccione un producto para actualizar.");
        }
    }

    // Método para exportar los productos a un archivo JSON.
    private void exportarJSON() {
        gestor.exportarJSON(tabla.getItems(), "productos.json"); // Exporta los productos a JSON.
        mostrarAlerta("Éxito", "Productos exportados a JSON correctamente.");
    }

    // Método para exportar los productos a un archivo CSV.
    private void exportarCSV() {
        gestor.exportarCSV(tabla.getItems(), "productos.csv"); // Exporta los productos a CSV.
        mostrarAlerta("Éxito", "Productos exportados a CSV correctamente.");
    }

    // Método para exportar los productos a un archivo TXT.
    private void exportarTXT() {
        gestor.exportarTXT(tabla.getItems(), "productos.txt"); // Exporta los productos a TXT.
        mostrarAlerta("Éxito", "Productos exportados a TXT correctamente.");
    }

    // Método para mostrar una alerta con un mensaje.
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Crea una alerta de tipo información.
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait(); // Muestra la alerta y espera a que el usuario la cierre.
    }

    // Método para incrementar el precio de un producto en un 10%.
    private void incrementarPrecio10PorCiento() {
        Producto seleccionado = tabla.getSelectionModel().getSelectedItem(); // Obtiene el producto seleccionado.
        if (seleccionado != null) {
            gestor.incrementarPrecio10PorCiento.accept(seleccionado); // Aplica el incremento de precio.
            tabla.refresh(); // Refresca la tabla para mostrar el nuevo precio.
            mostrarAlerta("Éxito", "Precio incrementado en un 10% para: " + seleccionado.getNombre());
        } else {
            mostrarAlerta("Error", "Seleccione un producto para incrementar su precio.");
        }
    }

    // Método para verificar si un alimento está vencido.
    private void verificarVencimiento() {
        Producto seleccionado = tabla.getSelectionModel().getSelectedItem(); // Obtiene el producto seleccionado.
        if (seleccionado instanceof Alimento) {
            Alimento alimento = (Alimento) seleccionado;
            if (alimento.estaVencido()) {
                mostrarAlerta("Vencimiento", "El alimento " + alimento.getNombre() + " está vencido.");
            } else {
                mostrarAlerta("Vencimiento", "El alimento " + alimento.getNombre() + " no está vencido.");
            }
        } else {
            mostrarAlerta("Error", "Seleccione un alimento para verificar su vencimiento.");
        }
    }

    // Método para filtrar productos por categoría.
    private void filtrarPorCategoria() {
        Dialog<String> dialog = new Dialog<>(); // Diálogo para seleccionar la categoría.
        dialog.setTitle("Filtrar Productos");
        dialog.setHeaderText("Seleccione el tipo de producto");

        ComboBox<String> tipoComboBox = new ComboBox<>(); // ComboBox para seleccionar la categoría.
        tipoComboBox.getItems().addAll("Todos", "Ropa", "Alimento", "Electronico"); // Opciones disponibles.
        tipoComboBox.setValue("Todos"); // Valor por defecto.

        GridPane grid = new GridPane(); // Layout para organizar los elementos del diálogo.
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.addRow(0, new Label("Tipo:"), tipoComboBox);

        dialog.getDialogPane().setContent(grid); // Asigna el layout al diálogo.
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); // Botones de OK y Cancelar.

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return tipoComboBox.getValue(); // Retorna el valor seleccionado.
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait(); // Muestra el diálogo y espera la respuesta.
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
                    tabla.getItems().addAll(productosFiltrados); // Muestra los productos filtrados.
                }
            }
        });
    }

    // Método para ordenar los productos por stock.
    private void ordenarPorStock() {
        gestor.leer().sort((p1, p2) -> Integer.compare(p1.getStock(), p2.getStock())); // Ordena los productos por stock.
        tabla.getItems().clear(); // Limpia la tabla.
        tabla.getItems().addAll(gestor.leer()); // Vuelve a cargar los productos ordenados.
        mostrarAlerta("Éxito", "Productos ordenados por stock.");
    }
}