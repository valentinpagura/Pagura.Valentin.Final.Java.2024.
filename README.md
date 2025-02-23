# Pagura.Valentin.Final.Java.2024.
##Gestora de productos

Aplicación Java para gestionar un inventario de productos (alimenticios, electrónicos y ropa). Permite agregar, eliminar, actualizar y filtrar productos, aumentar precios en un 10% mediante Consumers, y exportar listas (filtradas o no) a archivos JSON, CSV y TXT. Ideal para el control y organización de inventarios de manera eficiente.

##Paquetes.
la APP se divide en dos paquetes, uno que maneja la logica (.logica) y otro donde se desarrolla el codigo del ejecutable (interfaz grafica de usuario/gui).
Dentro del paquete de logica se llevara a cabo la creacion de metodos importantes para el sistema como el CRUD (create, read, update, delete) , excepciones y exportacion para manejar la persistencia de datos (JSON, CSV Y TXT) permitiendo al usuario exportar una lista filtrada (o toda la coleccion completa) al tipo de archivo que dicho usuario prefiera. Ademas, es donde se declara la clase abstracta "PRODUCTO" para empezar el modelado de sus clases hijas (Alimento,Ropa y Electronico).

GUI:
![image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/Interfaz%20de%20usuario.jpg?raw=true)

Archivo generado .TXT:
![image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/TXT.jpg?raw=true)

Archivo generado Json:
![image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/Json.jpg?raw=true)

Archivo generado CSV:
![image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/CSV.jpg?raw=true)


>[!IMPORTANT]
Todos los espacios deben ser rellenados al intentar agregar un producto a la gestora!
![image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/Datos%20requeridos.jpg?raw=true)
