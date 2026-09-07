# 🎟️ EventPass

**EventPass** es un sistema multiplataforma para la gestión de eventos, generación de entradas mediante códigos QR y control de acceso de asistentes.

El proyecto fue desarrollado como **Trabajo Fin de Grado del Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)**.

## 👨‍💻 Autores

- **Jeddy Jiménez Peña**
- **Alejandro** — GitHub: @aleexbenaa

## 📌 Descripción

EventPass permite gestionar el proceso completo de organización y acceso a eventos.

El sistema permite a los administradores crear y gestionar eventos, registrar asistentes y generar entradas digitales. Cada entrada dispone de un código QR único que puede ser validado mediante una aplicación Android destinada al personal de control de acceso.

Cuando se escanea una entrada, la aplicación consulta el backend para comprobar su validez y registrar el acceso, evitando que una misma entrada pueda utilizarse varias veces.

## ✨ Funcionalidades principales

- Creación, edición y gestión de eventos.
- Registro y gestión de usuarios.
- Registro de asistentes.
- Generación de entradas digitales.
- Generación de códigos QR.
- Validación de entradas mediante QR.
- Control de entradas utilizadas y duplicadas.
- Aplicación Android para usuarios.
- Aplicación Android para validadores.
- Gestión de diferentes roles de usuario.
- Integración de pagos mediante Stripe.
- Persistencia de datos en PostgreSQL.
- API REST para comunicación entre aplicaciones.

## 🛠️ Tecnologías utilizadas

### Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Maven
- API REST

### Frontend web
- HTML5
- CSS3
- Thymeleaf

### Aplicación móvil
- Android
- Java
- XML
- Retrofit

### Base de datos
- PostgreSQL
- Supabase

### Servicios e infraestructura
- Railway
- GitHub
- Stripe
- Generación y lectura de códigos QR

## 🏗️ Arquitectura

EventPass utiliza una arquitectura por capas en el backend:

- **Controller** — gestión de peticiones.
- **Service** — lógica de negocio.
- **Repository** — acceso a datos.
- **Model / Entity** — representación de las entidades.

El backend desarrollado con Spring Boot proporciona los servicios necesarios para la aplicación web y la aplicación Android.

La base de datos PostgreSQL está alojada mediante Supabase y el backend puede desplegarse mediante Railway.

## 📱 Funcionamiento general

El flujo principal del sistema es:

1. El administrador crea un evento.
2. Se registran los asistentes.
3. EventPass genera las entradas correspondientes.
4. Cada entrada contiene un código QR.
5. El usuario presenta su entrada en el acceso al evento.
6. El personal validador escanea el código QR mediante la aplicación Android.
7. La aplicación consulta el backend.
8. El sistema comprueba la validez de la entrada.
9. Si la entrada es válida, se registra el acceso.
10. Una entrada ya utilizada no puede volver a validarse.

## 📂 Estructura del repositorio

```text
EventPass-TFG-DAM/
│
├── eventos/          # Backend y aplicación web
├── eventosmobile/    # Aplicación Android
├── diagramas jeddy/  # Diagramas y documentación técnica
├── video EventPass/  # Recursos utilizados para la presentación
├── .gitignore
└── README.md