# Tracker de Candidaturas Laborales (v2.0 - JDBC + JavaFX)

Aplicación en Java para llevar el seguimiento de las candidaturas de empleo o prácticas: a qué empresas se ha enviado el CV, en qué estado está cada proceso y qué entrevistas se han hecho.

Proyecto del ejercicio integrador del primer trimestre (Acceso a Datos y Desarrollo de Interfaces). Esta versión migra la persistencia a una base de datos con JDBC y sustituye la CLI por una interfaz gráfica JavaFX diseñada con Scene Builder.

## Equipo

- Zhenghao
- Andres

## Tecnologías

- Java 24
- Maven
- JavaFX 24.0.1 y Scene Builder (FXML)
- JDBC con SQLite (sqlite-jdbc 3.46.1.3)
- Figma (prototipo)

## Estructura del proyecto

```
src/main/java/com/tracker
├── App.java            arranque y navegación entre pantallas
├── model               Empresa, Oferta, Entrevista, FiltroOferta
├── dao                 acceso a datos con JDBC
├── service             validación y lógica de negocio
├── controller          controladores JavaFX
└── util                conexión, alertas y utilidades
 
src/main/resources
├── config.properties   configuración de la conexión
├── schema.sql          creación del esquema
└── com/tracker         pantallas FXML (Scene Builder)
```

## Cómo compilar y ejecutar

Requisitos: JDK 24 o superior y Maven.

Compilar:

```
mvn clean package
```

Ejecutar (desde la carpeta del proyecto):

```
mvn javafx:run
```

## Base de datos

- El esquema está en `src/main/resources/schema.sql` (tablas `empresa`, `oferta` y `entrevista`).
- La primera vez que se ejecuta la aplicación se crea el archivo `tracker.db` con ese esquema.
- La conexión se configura en `src/main/resources/config.properties` (`db.url`, `db.user`, `db.password`).

## Funcionalidades

- Listado de candidaturas en tabla, con ordenación por columnas
- Filtros combinados: empresa, estado, salario mínimo, fecha y título
- Cambio de estado de una candidatura desde el listado
- Detalle de oferta con su empresa y un panel expandible de entrevistas
- Alta, edición y borrado de ofertas, empresas y entrevistas, con validación de campos
- Importación de candidaturas desde CSV a la base de datos en una única transacción

## Datos de prueba

Archivo CSV de candidaturas: <ruta/al/archivo.csv>

Para cargarlo, pulsa **Importar CSV** en la pantalla de listado y selecciónalo.

Formato: `id;titulo;empresa;estado;fechaPostulacion;enlace;notas`, con fechas `dd/MM/yyyy`.


## Contribuciones

| Miembro | Tareas realizadas en la v2.0 |
|---|---|
| Zhenghao  |
| Andres |
 