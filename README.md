
# IW 24/25

# Propuesta de proyecto
Tema del proyecto: Casa de Subastas en Línea
El proyecto consiste en diseñar y desarrollar una aplicación web de subastas inspirada en el funcionamiento de una casa de subastas tradicional. El objetivo es que los usuarios puedan registrarse, ver los objetos en puja, realizar ofertas y conocer al ganador cuando finalice cada subasta.

Para organizar los datos, se manejarán las siguientes entidades:

- **Usuario**: Almacena la información de quienes participan.
- **Objeto**: Representa el artículo subastado.
- **Subasta**: Contiene los detalles de cada evento de subasta.
- **Puja**: Registra las ofertas realizadas por cada usuario.
- **Saldo**: Gestiona la cantidad de dinero disponible de cada participante.
- **Historial**: Guarda el resultado de subastas cerradas y a los ganadores.
- **Comentarios y valoraciones**: Los compradores valoran los productos de los vendedores.
- **Chat**: Permite que los compradores y vendedores puedan hablar sobre los detalles de los productos.

De este modo, el proyecto cubre el ciclo completo de las subastas:

- Alta de objetos.
- Registro de usuarios.
- Recepción de ofertas.
- Anuncio de los ganadores.

Sirviendo como una plataforma donde las personas compiten de forma justa por objetos de gran valor.

### Roles:

- **Administrador**:
  - Da de alta usuarios.
  - Crea y gestiona subastas.
  - Cierra subastas y adjudica ganadores.
  - Revisa historial y reportes.

- **Participante**:
  - Se registra.
  - Puja por objetos.
  - Administra su saldo y ofertas.
  - Abandona subastas.
  - Revisa su historial ganado.

- **Visitante**:
  - Explora subastas sin interacción.
  - Para pujar, debe registrarse.

# Vistas realizadas en esta entrega
### Subasta
En esta pantalla se muestra el listado de todas las subastas que están teniendo lugar en este momento, con imágenes del objeto en cuestión y el precio por el que actualmente se está pujando. Se presenta un botón para contactar al vendedor y otro para entrar en la puja, para lo cual hay que estar registrado.

### Objeto
Esta pantalla muestra el objeto pujado en mayor detalle, con imágenes y una breve descripción. Consta de un botón que permite pujar la cantidad que se desee. Para acceder a esta pantalla el usuario ha de registrarse previamente.

### Historial
Se muestra una tabla con subastas que han sido realizadas anteriormente, especificando detalles como el ID de la subasta, el objeto que se subastó junto con una imagen, el precio por el que finalmente fue vendido, el user del ganador y la fecha en la que se cerró.

### Chats
En esta vista el usuario puede iniciar conversaciones, comprobar qué otros usuarios están o no activos en este momento, etc.

### Perfil
Una vez registrado, el usuario puede editar su perfil como desee, guardar sus subastas favoritas y revisar su historial de compras y subastas en las que está participando y aún siguen activas.

# Comandos
### Instalar dependencias
```mvn clean install```
### Instalar dependencias saltando tests
```mvn clean install -DskipTests```
### Ejecutar la aplicación
```mvn spring-boot:run```

