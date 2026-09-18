-- Script de Base de datos para Cavosh Cafe
CREATE DATABASE IF NOT EXISTS cavosh_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE cavosh_db;

-- ============================================
-- 1. USUARIOS Y AUTENTICACION
-- ============================================

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    url_avatar VARCHAR(255),
    puntos_fidelidad INT NOT NULL DEFAULT 0,
    verificado BOOLEAN NOT NULL DEFAULT FALSE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE tokens_verificacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    token VARCHAR(10) NOT NULL,
    expira_en DATETIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_token_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE usuario_direcciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    etiqueta VARCHAR(50) NOT NULL,          -- 'Casa', 'Trabajo', etc.
    direccion VARCHAR(255) NOT NULL,
    latitud DECIMAL(10,7),
    longitud DECIMAL(10,7),
    es_predeterminada BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_direccion_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================
-- 2. SUCURSALES
-- ============================================

CREATE TABLE sucursales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    latitud DECIMAL(10,7),
    longitud DECIMAL(10,7),
    hora_apertura TIME,
    hora_cierre TIME,
    activo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

-- ============================================
-- 3. CATALOGO
-- ============================================

CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    precio_base DECIMAL(10,2) NOT NULL,
    url_imagen VARCHAR(255),
    categoria ENUM('BEBIDAS_CALIENTES','BEBIDAS_FRIAS','BASE_ESPRESSO','SIN_CAFE','POSTRES') NOT NULL,
    tipo_producto ENUM('BEBIDA','COMIDA') NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Grupos de personalización (Tamaño, Tipo de leche, Cafeína, etc.)
CREATE TABLE producto_opciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    nombre_opcion VARCHAR(50) NOT NULL,      -- 'Tamaño', 'Tipo de leche', 'Cafeína'
    tipo_seleccion ENUM('UNICA','MULTIPLE') NOT NULL DEFAULT 'UNICA',
    es_obligatoria BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_opcion_producto FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Valores de cada grupo (Small/Medium/Large, Leche entera/Leche de avena, etc.)
CREATE TABLE producto_opcion_valores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_opcion_id BIGINT NOT NULL,
    nombre_valor VARCHAR(50) NOT NULL,
    modificador_precio DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    es_predeterminado BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_valor_opcion FOREIGN KEY (producto_opcion_id) REFERENCES producto_opciones(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE favoritos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_favorito_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_favorito_producto FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE,
    CONSTRAINT uq_favorito UNIQUE (usuario_id, producto_id)
) ENGINE=InnoDB;

-- ============================================
-- 4. CARRITO
-- ============================================

CREATE TABLE carrito (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_carrito_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE carrito_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrito_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_carritoitem_carrito FOREIGN KEY (carrito_id) REFERENCES carrito(id) ON DELETE CASCADE,
    CONSTRAINT fk_carritoitem_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
) ENGINE=InnoDB;

CREATE TABLE carrito_item_opciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrito_item_id BIGINT NOT NULL,
    producto_opcion_valor_id BIGINT NOT NULL,
    CONSTRAINT fk_cio_item FOREIGN KEY (carrito_item_id) REFERENCES carrito_items(id) ON DELETE CASCADE,
    CONSTRAINT fk_cio_valor FOREIGN KEY (producto_opcion_valor_id) REFERENCES producto_opcion_valores(id)
) ENGINE=InnoDB;

-- ============================================
-- 5. PROMOCIONES
-- ============================================

CREATE TABLE codigos_promocionales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    tipo_descuento ENUM('PORCENTAJE','FIJO') NOT NULL,
    valor_descuento DECIMAL(10,2) NOT NULL,
    valido_desde DATETIME NOT NULL,
    valido_hasta DATETIME NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

-- ============================================
-- 6. PEDIDOS Y PAGOS
-- ============================================

CREATE TABLE pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_pedido VARCHAR(20) NOT NULL UNIQUE,
    usuario_id BIGINT NOT NULL,
    sucursal_id BIGINT NOT NULL,
    direccion_id BIGINT,
    codigo_promocional_id BIGINT,
    metodo_entrega ENUM('RECOJO','DELIVERY') NOT NULL,
    estado ENUM('CREADO','EN_PREPARACION','LISTO','EN_CAMINO','ENTREGADO','CANCELADO') NOT NULL DEFAULT 'CREADO',
    subtotal DECIMAL(10,2) NOT NULL,
    descuento DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total DECIMAL(10,2) NOT NULL,
    hora_recojo DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_pedido_sucursal FOREIGN KEY (sucursal_id) REFERENCES sucursales(id),
    CONSTRAINT fk_pedido_direccion FOREIGN KEY (direccion_id) REFERENCES usuario_direcciones(id),
    CONSTRAINT fk_pedido_codigo FOREIGN KEY (codigo_promocional_id) REFERENCES codigos_promocionales(id)
) ENGINE=InnoDB;

CREATE TABLE pedido_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT,                       -- puede ser NULL si el producto se elimina luego
    nombre_producto VARCHAR(100) NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_pedidoitem_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT fk_pedidoitem_producto FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE pedido_item_opciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_item_id BIGINT NOT NULL,
    nombre_opcion VARCHAR(50) NOT NULL,
    nombre_valor VARCHAR(50) NOT NULL,
    modificador_precio DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_pio_item FOREIGN KEY (pedido_item_id) REFERENCES pedido_items(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE historial_estado_pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    estado ENUM('CREADO','EN_PREPARACION','LISTO','EN_CAMINO','ENTREGADO','CANCELADO') NOT NULL,
    cambiado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_historial_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE pagos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL UNIQUE,
    metodo_pago ENUM('TARJETA','EFECTIVO','PAYPAL') NOT NULL,
    ultimos_cuatro_tarjeta VARCHAR(4),
    estado ENUM('PENDIENTE','COMPLETADO','FALLIDO','REEMBOLSADO') NOT NULL DEFAULT 'PENDIENTE',
    pagado_en DATETIME,
    CONSTRAINT fk_pago_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================
-- 7. NOTIFICACIONES
-- ============================================

CREATE TABLE notificaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    leido BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notificacion_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ============================================
-- DATOS DE PRUEBA
-- ============================================

INSERT INTO sucursales (nombre, direccion, latitud, longitud, hora_apertura, hora_cierre) VALUES
('Cavosh Café - Miraflores', 'Av. Larco 345, Miraflores, Lima', -12.1219, -77.0297, '07:00:00', '22:00:00'),
('Cavosh Café - San Isidro', 'Av. Javier Prado 890, San Isidro, Lima', -12.0932, -77.0234, '07:00:00', '22:00:00');

INSERT INTO productos (nombre, descripcion, precio_base, url_imagen, categoria, tipo_producto) VALUES
('Caramel Macchiato', 'Espresso con leche vaporizada, vainilla y un toque de caramelo.', 4.70, NULL, 'BASE_ESPRESSO', 'BEBIDA'),
('Coconut Macchiato', 'Espresso con leche de coco y notas dulces tropicales.', 4.90, NULL, 'BASE_ESPRESSO', 'BEBIDA'),
('Caffe Mocha', 'Espresso, chocolate y leche vaporizada.', 4.70, NULL, 'BASE_ESPRESSO', 'BEBIDA'),
('Vanilla Latte', 'Espresso suave con leche vaporizada y jarabe de vainilla.', 3.00, NULL, 'BEBIDAS_CALIENTES', 'BEBIDA'),
('Traditional Cappuccino', 'Espresso con espuma de leche clásica.', 3.50, NULL, 'BEBIDAS_CALIENTES', 'BEBIDA'),
('White Chocolate Mocha', 'Espresso, chocolate blanco y leche vaporizada.', 5.20, NULL, 'BASE_ESPRESSO', 'BEBIDA'),
('Cinnamon Roll', 'Rollo de canela horneado con glaseado dulce.', 3.90, NULL, 'POSTRES', 'COMIDA'),
('Blueberry Muffin', 'Muffin esponjoso con arándanos frescos.', 3.20, NULL, 'POSTRES', 'COMIDA');

-- Personalización de ejemplo para Caramel Macchiato
INSERT INTO producto_opciones (producto_id, nombre_opcion, tipo_seleccion, es_obligatoria) VALUES
(1, 'Tamaño', 'UNICA', TRUE),
(1, 'Tipo de leche', 'UNICA', TRUE),
(1, 'Cafeína', 'UNICA', FALSE);

INSERT INTO producto_opcion_valores (producto_opcion_id, nombre_valor, modificador_precio, es_predeterminado) VALUES
(1, 'Small', 0.00, TRUE),
(1, 'Medium', 0.50, FALSE),
(1, 'Large', 1.00, FALSE),
(2, 'Leche entera', 0.00, TRUE),
(2, 'Leche descremada', 0.00, FALSE),
(2, 'Leche de almendra', 0.60, FALSE),
(2, 'Leche de avena', 0.60, FALSE),
(3, 'Con cafeína', 0.00, TRUE),
(3, 'Descafeinado', 0.00, FALSE);