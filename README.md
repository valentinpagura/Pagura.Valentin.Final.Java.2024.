# Pagura.Valentin.Final.Java.2024.
##Gestora de productos
----------------------
Aplicación Java para gestionar un inventario de productos (alimenticios, electrónicos y ropa). Permite agregar, eliminar, actualizar y filtrar productos, aumentar precios en un 10% mediante Consumers, y exportar listas (filtradas o no) a archivos JSON, CSV y TXT. Ideal para el control y organización de inventarios de manera eficiente.

##Paquetes.
------------
la APP se divide en dos paquetes, uno que maneja la logica (.logica) y otro donde se desarrolla el codigo del ejecutable (interfaz grafica de usuario/gui).
Dentro del paquete de logica se llevara a cabo la creacion de metodos importantes para el sistema como el CRUD (create, read, update, delete) , excepciones y exportacion para manejar la persistencia de datos (JSON, CSV Y TXT) permitiendo al usuario exportar una lista filtrada (o toda la coleccion completa) al tipo de archivo que dicho usuario prefiera. Ademas, es donde se declara la clase abstracta "PRODUCTO" para empezar el modelado de sus clases hijas (Alimento,Ropa y Electronico).
------------
GUI:
![image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/Interfaz%20de%20usuario.jpg?raw=true)

-------------------------------------------------------------------------------------------------------------------------------
Archivo generado .TXT:
![image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/TXT.jpg?raw=true)

-------------------------------------------------------------------------------------------------------------
Archivo generado Json:
![image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/Json.jpg?raw=true)

------------------------------------------------------------------------------------------------------------
Archivo generado CSV:
![image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/CSV.jpg?raw=true)

----------------------
>[!IMPORTANT]
Todos los espacios deben ser rellenados al intentar agregar un producto a la gestora!

![image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/Datos%20requeridos.jpg?raw=true)

-------------

##Filtrado y boton "Mostrar Todo"
--------------------------------
En la aplicacion podremos filtrar por las tres categorias de tipo producto (alimento,ropa y electronico) al tocar el boton de filtrar se despegara un comboBox que le permitira al usuario filtrar y ver los productos con una categoria especifica.
-----------------------------------------------------------------

![image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/Filtrado.jpg?raw=true)

>[!NOTE]
Al tocar el boton "Mostrar Todo" la lista volvera a desplegar todos los productos sin importar el filtrado anterior.

##Metodos SQL-DATE Y chequeo de vencimientos para productos alimenticios.
-------------------------------------------------------------------------
Al agregar un producto alimenticio en la app, aparecerán dos botones en el display: uno para ingresar la fecha de vencimiento, ya sea manualmente o usando SQL-Date para mayor comodidad, y otro para verificar si el producto está vencido, al hacer clic sobre la información y luego presionar el botón.
-----------------------------------------------------------------------------------------------------------------
#Producto no vencido:
![image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/NO%20VENCIDO.jpg?raw=true)

--------------------------------------------------------------------------------------------------------------------

#Producto vencido:
![image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/Vencimiento.jpg?raw=true)

>[!IMPORTANT]
>Al agregar un producto alimenticio a la lista, deberá marcarse la casilla que indique si es perecedero o no.

UML:
[image alt](https://github.com/valentinpagura/Pagura.Valentin.Final.Java.2024./blob/main/Diagrama%20uml.png?raw=true)
