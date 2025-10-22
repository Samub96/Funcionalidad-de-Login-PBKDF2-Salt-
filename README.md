# Funcionalidad-de-Login-PBKDF2-Salt
Proyecto final de Ciberseguridad

## Integrantes
- Samuel Barona
- Juan Velosa

## Enunciado
> Funcionalidad de login para una plataforma. Este programa permite gestionar los nombres de usuario y contraseñas de una plataforma cualquiera.

> Debe tener dos tipos de usuarios:

- Administrador (solo uno) y usuarios comunes.
- El usuario administrador puede consultar los nombres de los usuarios existentes, eliminar un usuario o poner en blanco la contraseña de un usuario.
- Los usuarios comunes pueden consultar su última fecha/hora de login y cambiar su contraseña.
- Las contraseñas se almacenan en una base de datos usando **salt**. Se emplea el algoritmo **PBKDF2** para el hashing de contraseñas.
- El proyecto se desarrolló en equipos de 3 personas. No se permite plagio o colaboración entre equipos.

> Entregables:
- Código fuente documentado en GitHub.
- Informe en Markdown con descripción del desarrollo, dificultades y conclusiones.

---

## Tecnologías y dependencias

- **openJDK-19+**
- **MySQL 5.7+**
- **Maven** para gestión de dependencias
- **MySQL Connector/J 8.0.33** (importado desde [Maven Repository](https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.33))

---

## Configuración de MySQL con Docker

Archivo `docker-compose.yml` incluido:

```yaml
version: '3.8'

services:
  db:
    platform: linux/x86_64
    image: mysql:5.7
    command: ["--max_connections=1000"]
    restart: always
    environment:
      MYSQL_DATABASE: 'DB'
      MYSQL_USER: 'dbTest'
      MYSQL_PASSWORD: 'password'
      MYSQL_ROOT_PASSWORD: 'password'
    ports:
      - '3306:3306'
    volumes:
      - mysql_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  mysql_data:
```
## Estrucutra del proyecto

```
├── 📁 src
│   ├── ☕ DatabaseManager.java
│   ├── ☕ Main.java
│   ├── ☕ PasswordUtils.java
│   ├── ☕ User.java
│   ├── ☕ UserService.java
│   └── ⚙️ docker-compose.yml
├── ⚙️ .gitignore
├── 📄 Funcionalidad-de-Login-PBKDF2-Salt-.iml
└── 📝 README.md
```

## Funcionalidades
### Base de datos 
```sql
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    salt VARBINARY(32) NOT NULL,
    hash VARBINARY(64) NOT NULL,
    iterations INT NOT NULL,
    is_admin BOOLEAN NOT NULL DEFAULT 0,
    last_login DATETIME NULL
)

```
## Seguridad de la contraseña 
- Se implementó salt aleatorio de 16 bytes por usuario
- Se implemetó  hash usando el algoritmo PBKDF2 con HMAC-SHA256 con 100.000 iteraciones
- El hash generado es comparado usando ```Arrays.equals()``` para validar que la contraseña digitada coincida con la guardada en la db
  ### Como funciona PBKDF2+HMAC-SHA256
  ------
  PBKDF2 (Password-Based Key Derivation Function 2) es una función para transformar una contraseña débil en una clave segura usando:

- Una contraseña (texto plano).

- Un salt (valor aleatorio único).

- Un número de iteraciones (cuántas veces aplicar la función de hash).

- Un hash subyacente, en este caso HMAC-SHA256.

El resultado es un hash largo y seguro que se puede almacenar en la base de datos.


### Qué hace HMAC-SHA256
------
- HMAC (Hash-based Message Authentication Code): añade autenticidad e integridad.

- SHA-256: genera un hash de 256 bits resistente a colisiones.

- Combinados, producen un hash único y seguro incluso si la contraseña es débil.

# Ejecucion 

1. levantar la DB MySQL con docker
   
  ```bash
sudo docker compose up -d
  ```
2. compilar
   En este caso fue compilado y ejecutado con Intellij IDE usando el jdk ```openjdk-19```
## visibilidad del trabajo 
### inicializacion por primera vez

![primera vez]()

### creacion de usuario admin
![usuario admin]()

### visibilidad en la DB
![dbvisilbe]()
Como podemos observar por temas academicos se creó esta tabla separando explicitamente el salt, el hash y las iteraciones con el fin de mostar que si funcionó

### Login admin
![login]()

en este caso no quisimos censurar la conatraseña por motivos academicos y para desmotrar que efectivamente usando la contraseña creada se puede hacer login

## Login fallido

![fail]()

aqui queremos mostrar que si intentamos loggear con otra contraseña, directamente el programa lanza error y se acaba la ejecucion 


## Conclusion 

Durante el desarrollo de este proyecto, adquirimos experiencia práctica en la implementación de sistemas de autenticación seguros usando Java y bases de datos MySQL. Aprendimos a:

- Gestionar usuarios con diferentes roles (administrador y usuarios comunes).

- Aplicar buenas prácticas de seguridad en contraseñas mediante salt y PBKDF2, protegiendo la información sensible.

- Integrar un proyecto Java con MySQL usando MySQL Connector/J y Docker para facilitar la configuración del entorno.

- Organizar la arquitectura del proyecto de manera modular, separando la lógica de negocio, la capa de datos y la interfaz de usuario.

- Manejar correctamente la comunicación con la base de datos y errores, garantizando la confiabilidad del sistema.

En resumen, este proyecto nos permitió combinar conocimientos de seguridad informática, programación y administración de bases de datos, fortaleciendo nuestras habilidades para desarrollar aplicaciones robustas y seguras.
   
