DROP DATABASE IF EXISTS facturacionTienda;
CREATE DATABASE IF NOT EXISTS facturacionTienda;
USE facturacionTienda;

CREATE TABLE IF NOT EXISTS Factura(
	id INT AUTO_INCREMENT KEY,
    fecha DATE,
    nombreCliente VARCHAR(50) NOT NULL,
    formaPago ENUM ('Efectivo','Tarjeta','Transferencia'),
    pagado boolean,
    direccion varchar (100)
);

CREATE TABLE IF NOT EXISTS DetalleFactura(
	idFactura INT,
    numeroLinea INT DEFAULT 1,
    descripcion VARCHAR (20) NOT NULL,
    cantidad DECIMAL DEFAULT 1,
    precio DECIMAL NOT NULL,
    PRIMARY KEY (idFactura, numeroLinea),
    CONSTRAINT fk_idFactura FOREIGN KEY (idFactura) references Factura (id)
		ON UPDATE CASCADE
        ON DELETE CASCADE
);

INSERT INTO Factura (id, fecha, nombreCliente, formaPago, pagado, direccion) VALUES (1, '2025-01-13', "Andres Felipe", "Efectivo", 1, "Calle Valencia");
INSERT INTO Factura (id, fecha, nombreCliente, formaPago, pagado, direccion) VALUES (2, '2025-01-06', "Bejamin Padure", "Tarjeta", 0, "Calle San Clemente");

-- Detalles para la factura 1º.
INSERT INTO DetalleFactura (idFactura, numeroLinea, descripcion, cantidad, precio) VALUES (1, 1, "descripcion1", 10.0, 9.0);
INSERT INTO DetalleFactura (idFactura, numeroLinea, descripcion, cantidad, precio) VALUES (1, 2, "descripcion2", 5.0, 12.0);
INSERT INTO DetalleFactura (idFactura, numeroLinea, descripcion, cantidad, precio) VALUES (1, 3, "descripcion3", 7.0, 15.0);

-- Detalles para la factura 2º.
INSERT INTO DetalleFactura (idFactura, numeroLinea, descripcion, cantidad, precio) VALUES (2, 1, "descripcion1", 6.0, 2.0);
INSERT INTO DetalleFactura (idFactura, numeroLinea, descripcion, cantidad, precio) VALUES (2, 2, "descripcion2", 10.0, 4.0);
INSERT INTO DetalleFactura (idFactura, numeroLinea, descripcion, cantidad, precio) VALUES (2, 3, "descripcion3", 15.0, 6.0);