
# BaratoBarato

## Entrega Post Examen
### 👨‍🎓 Alumno: Jose Villacres Zumaeta

### REALIZADO DE MANERA INDIVIDUAL

#### 🚩 Problemas y sus soluciones:
* 💡 ser posible subir imágenes de producto, independientemente de plataforma (con sólo ésto llegáis al 8). Mejor si es posibl subir varias imágenes de un mismo producto (esto lo podéis hacer subiéndolas a una carpeta `subasta/<id>/<nombre>.jpg`).
  - ✅ Solucion:
    - Cambié el guardado de archivos para que use LocalData, una clase que se encarga de construir las rutas sin importar si se ejecuta en Windows o Linux.
    - En application.properties la carpeta base ahora es la del propio proyecto, así no hay diferencias entre entornos.
    - Para permitir varias fotos por producto:
      - El formulario acepta varios ficheros (multiple).
      - Las imágenes se guardan en subasta/<id>/….
      - En la web se muestran con un carrusel; lo he añadido tanto al alta como a la edición de la subasta.

* 💡 tener descripciones de producto algo mejores que un campo de texto sin formato. Mirad https://github.com/commonmark/commonmark-java - la idea es que el creador de la subasta escriba la descripción en markdown, y en la vista de usuario que quiere pujar, se vea renderizado como html.
  - ✅ Solucion: 
    - Añadí la librería CommonMark y un pequeño componente Markdown que traduce el texto escrito por el usuario a HTML seguro.
    - Al crear o editar una subasta el vendedor puede escribir con formato (negritas, listas, títulos, etc.).
    - En la ficha que ve el comprador, esa descripción se renderiza ya formateada.


* 💡 haber perfiles más detallados, al menos de cara a que un usuario sepa cuál es su posición global (cuánto pujado dónde, cuándo se acaba cada puja, en cuáles voy ganando)
  - ✅ Solucion: 
    - En la base de datos genero un resumen por subasta: cuánto he ofrecido yo, cuál es la puja máxima global, si voy ganando y cuándo termina.
    - Ese resumen se muestra en mi página de perfil:
      - Una tabla con todas las subastas donde participo.
      - Un bloque arriba que indica el dinero total “en juego”, cuántas subastas siguen activas y cuántas lidero.
    

* 💡 cualquier cambio que mejore la usabilidad/seguridad (ver por ejemplo preguntas de examen)
  - ✅ Solucion:
    - Implementé un rate-limit muy simple: cada dirección IP sólo puede intentar iniciar sesión o registrarse 10 veces por minuto. 
      Así se evitan ataques de fuerza bruta y spam de cuentas.
    - Añadí un canal en tiempo real (WebSocket) que actualiza al momento el precio de las subastas en la lista, sin tener que recargar la página.

####  🧷 Resumiendo
- Ahora las fotos se guardan y muestran sin problemas en Windows o Linux, y cada producto puede enseñar varias imágenes en un pequeño carrusel.
- Las descripciones de los productos se escriben en Markdown y el comprador las ve ya formateadas, con negritas, listas, etc.
- El perfil del usuario incluye un panel que le dice cuánto dinero tiene comprometido, cuántas subastas sigue y en cuáles va ganando, todo en una tabla clara.
- Se ha añadido un límite de 10 intentos por minuto para login y registro desde la misma IP, y los precios de las subastas se actualizan en tiempo real sin recargar la página.
