CREATE TABLE IF NOT EXISTS empresa (
                                       id INT PRIMARY KEY AUTO_INCREMENT,
                                       nombre VARCHAR(255) NOT NULL UNIQUE,
                                       sector VARCHAR(255) NOT NULL,
                                       web VARCHAR(255),
                                       fechaRegistro DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS oferta (
                                      id INT PRIMARY KEY AUTO_INCREMENT,
                                      id_empresa INT NOT NULL,
                                      titulo VARCHAR(255) NOT NULL,
                                      descripcion TEXT,
                                      estado VARCHAR(50) NOT NULL CHECK (estado IN ('pendiente', 'entrevista', 'rechazada', 'aceptada')),
                                      fechaPostulacion DATE NOT NULL,
                                      enlace TEXT,
                                      salario DECIMAL(10,2) NOT NULL DEFAULT 0,
                                      notas TEXT,
                                      FOREIGN KEY (id_empresa) REFERENCES empresa(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS entrevista (
                                          id INT PRIMARY KEY AUTO_INCREMENT,
                                          id_oferta INT NOT NULL,
                                          fecha DATE NOT NULL,
                                          tipo VARCHAR(50) NOT NULL CHECK (tipo IN ('telefonica', 'tecnica', 'rrhh', 'final')),
                                          notas TEXT,
                                          resultado VARCHAR(50) NOT NULL CHECK (resultado IN ('pendiente', 'positivo', 'negativo')),
                                          FOREIGN KEY (id_oferta) REFERENCES oferta(id) ON DELETE CASCADE
);