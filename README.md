# Test DDD Portal

Aplicación Spring Boot 3 + Thymeleaf para administrar el portal público de un taller creativo. Incluye panel de administración seguro, editor de servicios destacados y secciones del portal que se reflejan de inmediato en la landing.

## Requisitos

- Java 17
- Maven 3.8+
- (Opcional) Docker y Docker Compose

## Ejecución local

```powershell
mvn spring-boot:run
```

Aplicación web y API disponibles en `http://localhost:8080/`.

## Arquitectura

| Capa | Descripción |
| --- | --- |
| Presentación | Thymeleaf + layout base (`layouts/base`) con fragmentos reutilizables (`fragments/brand`, `fragments/cards`, `fragments/admin`).
| Aplicación | Controladores MVC bajo `com.example.testddd` (`home`, `admin`) y servicios de dominio (`card`, `portal`, `image`).
| Dominio | Entidades JPA (`Card`, `PortalTile`) con lógica de negocio encapsulada y formularios (`CardForm`, `PortalTileForm`) para validaciones.
| Infraestructura | Repositorios Spring Data JPA, integración opcional con Cloudinary para subir imágenes (`CloudinaryImageService`).

Los datos iniciales se cargan con `DataInitializer` si las tablas están vacías. El panel admin usa `AdminDashboardModelPopulator` para componer el modelo compartido.

## Endpoints principales

| Tipo | Ruta | Descripción |
| --- | --- | --- |
| público | `GET /` | Landing pública que muestra las secciones activas (`PortalTile`). |
| admin | `GET /admin` | Panel con KPIs y gráfica de actividad. |
| admin | `GET /admin/servicios` | CRUD de cards de servicios (form + listado). |
| admin | `GET /admin/portal-tiles` | CRUD de secciones del portal con subida opcional de imagen. |
| admin | `POST /admin/cards` | Crear servicio destacado. |
| admin | `PUT /admin/cards/{id}` | Actualizar servicio. |
| admin | `DELETE /admin/cards/{id}` | Eliminar servicio. |
| admin | `POST /admin/portal-tiles` | Crear sección del portal. |
| admin | `PUT /admin/portal-tiles/{id}` | Actualizar sección. |
| admin | `DELETE /admin/portal-tiles/{id}` | Eliminar sección. |

El acceso a `/admin/**` requiere autenticación básica (usuario `admin` / contraseña `123456`, configurable en `SecurityConfig`).

## Vistas y fragmentos

| Template | Uso |
| --- | --- |
| `templates/index.html` | Landing pública (usa `fragments/cards`). |
| `templates/admin/admin.html` | Dashboard con KPIs y gráfica. |
| `templates/admin/services.html` | Gestión de tarjetas de servicios. |
| `templates/admin/portal-tiles.html` | Gestión de secciones del portal. |
| `templates/fragments/cards.html` | Renderiza la grilla de secciones en el home. |
| `templates/fragments/admin.html` | Agrupa KPIs, formularios y listados reutilizables del panel. |
| `templates/layouts/base.html` | Shell común (cabecera, tipografías y textura de fondo).

Los estilos específicos del panel viven en `static/css/admin.css` y la lógica de UI en `static/js/admin.js` (preview de imágenes y sparkline del dashboard).

## Base de datos

Usa H2 en memoria (`jdbc:h2:mem:testddd`). Consola disponible en `http://localhost:8080/h2-console` con usuario `sa` y sin contraseña.

## Docker

Construir imagen:

```powershell
docker build -t testddd .
```

Ejecutar contenedor:

```powershell
docker run -p 8080:8080 testddd
```

La aplicación quedará accesible en `http://localhost:8080/`.

## Spring Boot DevTools

Incluimos `spring-boot-devtools` para recarga rápida en desarrollo. Maven lo descarga automáticamente; al ejecutar:

```powershell
mvn spring-boot:run
```

los cambios en clases y recursos estáticos recargan el contexto al guardarlos. En producción **no** debe empaquetarse (la dependencia está marcada como opcional). Si usas IntelliJ/Eclipse, habilita *Build project automatically* para aprovechar el reinicio en caliente.

## Casos de uso

1. **Gestor de servicios**
   - Admin inicia sesión y accede a `/admin/servicios`.
   - Crea una nueva tarjeta completando título, descripción, enlace relativo y subiendo una imagen (opcional).
   - La tarjeta aparece de inmediato como “Servicio destacado” y puede editarse/eliminarse.

2. **Curador del portal**
   - Admin accede a `/admin/portal-tiles`.
   - Define secciones (p. ej. “Productos & Servicios”, “Bitácora del Taller”) apuntando a rutas internas o URLs absolutas.
   - Ordena las secciones con el campo posición y habilita/deshabilita cada una para controlar qué se ve en la landing.

3. **Visitante del sitio**
   - Entra a `/` y ve las secciones activas renderizadas con `fragments/cards`.
   - Hace clic en una sección para navegar al destino configurado (otra página del sitio o recurso externo).

4. **Carga de medios opcional**
   - Si existen credenciales de Cloudinary, al crear/editar servicios o secciones, la imagen se sube automáticamente y se persiste la URL segura.
   - Si no hay configuración, el guardado sigue funcionando omitiendo la carga.

## Flujo de trabajo

1. **Diseñar** – Definir objetivo, componentes afectados y capa involucrada antes de tocar código.
2. **Entidad** – Crear la clase de dominio (`@Entity`/DTO) y compilar (`mvn compile`).
3. **Test unitario rápido** – Construir un `main()` o `@Test` que pruebe la entidad de forma aislada.
4. **Servicio** – Implementar la lógica de negocio en la capa de servicio y ejecutar `mvn compile` + tests.
5. **Controller** – Exponer la funcionalidad vía REST o MVC (`mvn compile` + pruebas con `curl`/Postman).
6. **Vista** – Maquetar HTML/JS; validar primero con datos mockeados en el navegador.
7. **Integrar** – Conectar todas las capas y realizar la prueba end-to-end final.

Siempre seguimos esta secuencia para mantener el proyecto consistente y eficiente.
