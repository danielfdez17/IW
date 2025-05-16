
# IW 24/25

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