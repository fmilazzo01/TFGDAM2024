drop database gym;
create database if not exists gym;
use gym;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    direccion VARCHAR(100),
    telefono VARCHAR(15),
    gimnasio_nombre VARCHAR(100) NOT NULL
);

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    apellidos VARCHAR(50),
    fechanacimiento DATE,
    telefono VARCHAR(15),
    telemergencias VARCHAR(15),
    fechainicio DATE,
    email VARCHAR(100),
    usuario_id INT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    fecha_pago DATE,
    tipo_pago ENUM('Mensualidad', 'Trimestre', 'Semestre', 'Ano'),
    membresia_hasta DATE,
    cantidad DECIMAL(10, 2),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
);

CREATE TABLE precios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    gimnasio_id INT,
    precio_mes DOUBLE,
    precio_trimestre DOUBLE,
    precio_semestre DOUBLE,
    precio_anio DOUBLE,
    FOREIGN KEY (gimnasio_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

