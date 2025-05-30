
# IW 24/25

## Entrega Post Examen
* ser posible subir imágenes de producto, independientemente de plataforma (con sólo ésto llegáis al 8). Mejor si es posibl subir varias imágenes de un mismo producto (esto lo podéis hacer subiéndolas a una carpeta `subasta/<id>/<nombre>.jpg`).
  - Solucion: 

* tener descripciones de producto algo mejores que un campo de texto sin formato. Mirad https://github.com/commonmark/commonmark-java - la idea es que el creador de la subasta escriba la descripción en markdown, y en la vista de usuario que quiere pujar, se vea renderizado como html.
  - Solucion: 

* haber perfiles más detallados, al menos de cara a que un usuario sepa cuál es su posición global (cuánto pujado dónde, cuándo se acaba cada puja, en cuáles voy ganando)
  - Solucion: 

* cualquier cambio que mejore la usabilidad/seguridad (ver por ejemplo preguntas de examen)
  - Solucion:

## Entrega final Examen
### ¿Qué hace?
Nuestra aplicación es una plataforma de subastas en línea que, tras un sencillo proceso de registro y autenticación, permite a cualquier usuario crear subastas y pujar en las de otros. En la página principal se muestran siempre las subastas «En curso» y las que están «Pendientes» de comenzar, para que los visitantes identifiquen rápidamente las oportunidades activas.
Cada subasta pasa por cuatro estados: Pendiente (configurada, pero aún sin iniciar), En curso (abierta a pujas), Finalizada (con un ganador confirmado) y Cancelada (porque nadie pujó o por intervención del administrador). Cuando la puja ganadora se confirma y el creador señala que el pedido ha sido enviado, se libera automáticamente el pago al vendedor y el comprador puede valorar la subasta y dejar una reseña del producto; estas calificaciones se acumulan como parte de la reputación pública del usuario.
Además de pujar, los participantes pueden conversar en tiempo real con el creador de la subasta para resolver dudas o negociar detalles. Todos los usuarios disponen de un historial completo donde se listan, sin excepciones, las subastas en cualquier estado (pendientes, en curso, canceladas o finalizadas). Desde su perfil, cada usuario puede editar sus datos, recargar saldo y, si lo desea, modificar las subastas que haya creado.
La plataforma cuenta también con un panel de administración donde se pueden habilitar o deshabilitar usuarios, así como editar o cancelar subastas de forma irreversible.

### ¿Como se usa?
Iniciar sesión con las cuentas de prueba

| Rol               | Usuario | Contraseña |
| ----------------- | ------- | ---------- |
| **Administrador** | `a`     | `aa`       |
| **Usuario**       | `b`     | `aa`       |

Funciones según el rol
• Usuario (rol “User”)
– Crear subastas indicando título, descripción, precio inicial, fechas y fotos.
– Pujar en cualquier subasta que esté en curso.
– Chatear en tiempo real con el vendedor desde la página de la subasta.
– Consultar el historial completo de subastas (pendientes, en curso, canceladas y finalizadas).
– Editar su perfil y recargar saldo.
– Modificar sus propias subastas mientras no hayan terminado.
– Valorar y reseñar el producto cuando gane una subasta y el vendedor confirme el envío.

• Administrador (rol “Admin”)
– Todas las acciones de un usuario normal.
– Habilitar o deshabilitar cuentas de usuarios.
– Editar los detalles de cualquier subasta.
– Cancelar subastas de forma irreversible para garantizar el buen funcionamiento de la plataforma.

### Partes de Terceros
| Componente               | Versión / archivos incluidos                                                                                                                                  | Origen (CDN / sitio oficial)                                                             | Licencia   |
| ------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------- | ---------- |
| **Bootstrap** (CSS + JS) | `bootstrap-5.3.3.css` (copia local) <br>CDN alternativo: `https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css`                            | [https://getbootstrap.com](https://getbootstrap.com)                                     | MIT        |
| **Bootstrap Icons**      | Hoja de estilo `bootstrap-icons.min.css` + fuentes/SVG (v 1.11.3) <br>CDN: `https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css` | [https://icons.getbootstrap.com](https://icons.getbootstrap.com)                         | MIT        |
| **STOMP.js**             | `stomp.min.js` (v 2.3.3) <br>CDN: `https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js`                                                               | [https://github.com/jmesnil/stomp-websocket](https://github.com/jmesnil/stomp-websocket) | Apache 2.0 |


## Segunda Entrega

En esta versión, nuestra web ha sido actualizada para permitir la creación de subastas en las que los usuarios pueden pujar según su saldo actual, además de contactar con el vendedor a través de un chat habilitado para ello. También tienen la posibilidad de dejar reseñas en subastas finalizadas, basadas en puntuaciones de 1 a 5 estrellas y un apartado de comentarios.

En la vista de perfil, el usuario podrá visualizar:

- Las subastas que ha creado.
- Las subastas en las que está participando.
- Su saldo disponible en tiempo real.
- Todas las opciones de edición de su perfil.

### Funcionalidades implementadas:

-  Chat entre el creador de una subasta y los usuarios que pujan.
-  Sistema de pujas: solo se permiten pujas superiores al precio actual y si el usuario tiene saldo suficiente.
-  Sección de reseñas: permite dejar puntuaciones de 1 a 5 estrellas y comentarios.
-  Funcionalidad para habilitar y deshabilitar subastas desde una cuenta de administrador.
-  Listado dinámico de subastas creadas y en las que se está participando en la vista de usuario
-  Edición de perfil
-  Edición de subastas una vez creadas

### Aún por implementar:

-  Vincular el historial de subastas con la base de datos
-  Modificar el saldo al editar el perfil

# Estructura de Base de datos
![Estructura de BBDD](/bd.png)

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
### Ejecutar tests
```mvn test -Dtest=InternalRunner```
```mvn test -Dtest=ExternalRunner```
