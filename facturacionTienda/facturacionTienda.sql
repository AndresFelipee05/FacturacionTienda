DROP DATABASE IF EXISTS facturacionTienda;
CREATE DATABASE IF NOT EXISTS facturacionTienda;
USE facturacionTienda;

CREATE TABLE IF NOT EXISTS Factura(
	id INT PRIMARY KEY,
    fecha DATE,
    nombreCliente VARCHAR(50) NOT NULL,
    formaPago ENUM ('Efectivo','Tarjeta','Transferencia'),
    pagado boolean,
    direccion varchar (40)
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