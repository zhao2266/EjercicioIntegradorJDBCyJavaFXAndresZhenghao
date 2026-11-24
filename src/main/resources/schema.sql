CREATE TABLE IF NOT EXISTS empresa (
                                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                                       nombre TEXT NOT NULL UNIQUE,
                                       sector TEXT NOT NULL,
                                       web TEXT,
                                       fechaRegistro TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS oferta (
                                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                                      id_empresa INTEGER NOT NULL,
                                      titulo TEXT NOT NULL,
                                      descripcion TEXT,
                                      estado TEXT NOT NULL CHECK (estado IN ('pendiente', 'entrevista', 'rechazado', 'aceptado')),
    fechaPostulacion TEXT NOT NULL,
    enlace TEXT,
    salario REAL NOT NULL DEFAULT 0,
    notas TEXT,
    FOREIGN KEY (id_empresa) REFERENCES empresa (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS entrevista (
                                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                                          id_oferta INTEGER NOT NULL,
                                          fecha TEXT NOT NULL,
                                          tipo TEXT NOT NULL CHECK (tipo IN ('telefonica', 'tecnica', 'presencial', 'online')),
    notas TEXT,
    resultado TEXT NOT NULL CHECK (resultado IN ('pendiente', 'positivo', 'negativo')),
    FOREIGN KEY (id_oferta) REFERENCES oferta (id) ON DELETE CASCADE
    );