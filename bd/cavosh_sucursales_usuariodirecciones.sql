-- Alineación de sucursales y usuario_direcciones con el código.
-- Con spring.jpa.hibernate.ddl-auto=update Hibernate crea estas columnas al arrancar;
-- este script es para entornos donde ddl-auto sea 'validate' o 'none'. Ejecutar UNA vez
-- (MySQL no soporta ADD COLUMN IF NOT EXISTS).

ALTER TABLE sucursales
    ADD COLUMN telefono VARCHAR(20) NULL AFTER direccion;

ALTER TABLE usuario_direcciones
    ADD COLUMN calle      VARCHAR(120) NULL AFTER direccion,
    ADD COLUMN ciudad     VARCHAR(60)  NULL AFTER calle,
    ADD COLUMN distrito   VARCHAR(60)  NULL AFTER ciudad,
    ADD COLUMN referencia VARCHAR(255) NULL AFTER distrito;
