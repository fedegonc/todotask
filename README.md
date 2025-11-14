# Todo Task

Aplicación Spring Boot minimalista para gestionar tareas con interfaz Thymeleaf y API REST.

## Requisitos

- Java 17
- Maven 3.8+
- (Opcional) Docker y Docker Compose

## Ejecución local

```powershell
mvn spring-boot:run
```

Aplicación web y API disponibles en `http://localhost:8080/`.

## Endpoints

- Web: `GET /` (formulario + lista)
- API REST:
  - `GET /api/todo`
  - `POST /api/todo`

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
