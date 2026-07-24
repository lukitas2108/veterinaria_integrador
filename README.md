# Sistema de Gestión Veterinaria — Grupo [completar]

Trabajo Integrador Final — Programación Orientada a Objetos I

## Cómo correr el proyecto

Requisitos: JDK 17+ y Maven instalados.

```bash
mvn clean javafx:run
```

La primera vez va a descargar las dependencias (JavaFX, Hibernate, H2), puede tardar un par de minutos.

Si abre una ventana con el título "Sistema de Gestión Veterinaria" y el botón "Probar conexión a la base de datos" funciona sin error, el entorno está OK.

La base de datos H2 se crea automáticamente como archivo en `./data/veterinaria.mv.db` la primera vez que se ejecuta (esa carpeta está en `.gitignore`, cada uno tiene su copia local — no se sube al repo).

## Estructura de paquetes

- `modelo/` — entidades del dominio (clases JPA con `@Entity`)
- `repositorio/` — capa de acceso a datos (EntityManager, queries)
- `servicio/` — lógica de negocio y validaciones que coordinan repositorios
- `controlador/` — controladores de JavaFX (vinculan `.fxml` con el modelo, vía servicio/repositorio — nunca hablan directo con EntityManager)
- `excepciones/` — excepciones propias del dominio
- `util/` — utilidades (ej. `JpaUtil` para obtener el EntityManagerFactory)

Vistas `.fxml` en `src/main/resources/com/veterinaria/vista/`.

## Convenciones

- Fechas: siempre `LocalDate`/`LocalDateTime`/`LocalTime`, nunca `java.util.Date`.
- Estados fijos (ej. estado de turno): `enum`.
- Métodos que pueden no devolver resultado: `Optional<T>`, nunca `null`.
- Regla de oro del enunciado: si una regla de negocio se puede poner dentro de una entidad, va ahí (no en el controlador).

## Nota sobre persistence.xml

A medida que cada uno cree sus entidades `@Entity`, hay que agregarlas en
`src/main/resources/META-INF/persistence.xml` con una línea `<class>paquete.NombreClase</class>`.
Si a alguien le tira error de "entity not found" al correr, seguramente falta esa línea.

## División de módulos

| Módulo | Responsable | Paquetes/clases principales |
|---|---|---|
| A — Clientes y Mascotas | | `Cliente`, `Mascota`, `Raza`, `Especie` |
| B — Turnos y Veterinarios | | `Veterinario`, `Especialidad`, `Turno`, `EstadoTurno` |
| C — Servicios y Vacunación | | `Servicio` (+ subclases), `DetalleTurnoServicio`, `TipoVacuna`, `RegistroVacunacion` |
