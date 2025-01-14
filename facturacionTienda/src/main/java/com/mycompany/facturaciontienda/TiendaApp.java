package com.mycompany.facturaciontienda;

import Clases.DetalleFactura;
import Clases.Factura;
import Clases.Factura.FormaDePago;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TiendaApp {

    private Connection con;
    private final Scanner sc = new Scanner(System.in);

    public void conectar() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/facturacionTienda", "root", "Andresiito30");

            System.out.println("CONECTADO A LA BASE DE DATOS " + "jdbc:mysql://localhost/test");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void desconectar() {
        try {
            con.close();
            System.out.println("jdbc:mysql://localhost/facturacionTienda " + "cerrada");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void menu() {
        int opcion;
        do {
            System.out.println("\n--- Menú Facturas ---");
            System.out.println("0. Salir del programa.");
            System.out.println("1. Insertar factura.");
            System.out.println("2. Pagar factura.");
            System.out.println("3. Eliminar factura.");
            System.out.println("4. Mostrar factura y sus detalles por id.");
            System.out.println("5. Listar facturas.");
            System.out.println("6. Listar detalles de facturas.");

            opcion = introduceEntero();

            switch (opcion) {
                case 1 -> {
                    insertarFactura();
                }

                case 2 -> {
                    pagarFactura();
                }

                case 3 -> {
                    eliminarFactura();
                }

                case 4 -> {
                    mostrarFacturaPorId();
                }

                case 5 -> {
                    listarFacturas();
                }

                case 6 -> {
                    listarDetallesFacturas();
                }

                case 0 -> {
                    System.out.println("Saliendo del programa...");
                }

                default -> {
                    System.out.println("Opción no válida. Intentalo de nuevo");
                }
            }
        } while (opcion != 0);
    }

    public void insertarFactura() {
        // Opción 1. Insertar factura. 
        try {
            System.out.print("Introduce el nombre del cliente: ");
            String nombreCliente = sc.nextLine();

            if (nombreCliente == null || nombreCliente.isEmpty()) {
                System.out.println("El nombre del cliente no cumple el formato. Operación cancelada.");
                return;
            }

            System.out.print("Introduce la direccion: ");
            String direccion = sc.nextLine();

            if (direccion == null || direccion.isEmpty()) {
                System.out.println("La direccion no cumple el formato. Operación cancelada.");
                return;
            }

            LocalDate fecha = LocalDate.now();
            FormaDePago formaPago = null;
            boolean pagado = false;

            Factura nuevaFactura = new Factura(0, fecha, nombreCliente, formaPago, pagado, direccion);
            int idNuevaFactura = consultaInsertarFactura(nuevaFactura);
            // Insertamos la factura, ahora pediremos los datos de la factura para así insertar los detalles de la factura.

            if (idNuevaFactura < 0) {
                System.out.println("No se pudo insertar la factura. Operación cancelada");
                return;
            } else {
                System.out.println("La factura se ha insertado correctamente con el Id: " + idNuevaFactura);
            }

            int numeroLinea = 1; // Por defecto lo ponemos en 1.
            boolean continuar;
            do {
                System.out.print("Introduce la descripción de la factura: ");
                String descripcion = sc.nextLine();

                if (descripcion == null || descripcion.isEmpty()) {
                    System.out.println("La descripción no cumple el formato. Operación cancelada");
                    return;
                }

                System.out.print("Introduce la cantidad: ");
                String cantidadString = sc.nextLine();
                Double cantidad = null;

                if (!cantidadString.isEmpty()) {
                    try {
                        cantidad = Double.parseDouble(cantidadString.replace(",", "."));
                        if (cantidad < 0.0) {
                            System.out.println("La cantidad es inválida. Operación cancelada.");
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("La cantidad no cumple el formato. Operación cancelada.");
                        return;
                    }
                }

                System.out.print("Introduce el precio: ");
                String precioString = sc.nextLine();
                Double precio = null;

                if (!precioString.isEmpty()) {
                    try {
                        precio = Double.parseDouble(precioString.replace(",", "."));
                        if (precio < 0.0) {
                            System.out.println("El precio es inválido. Operación cancelada.");
                        }

                    } catch (NumberFormatException ex) {
                        System.out.println("El precio no cumple el formato. Operación cancelada.");
                        return;
                    }
                }

                int resultadoInsertarDetalleFactura = consultaInsertarDetalleFactura(idNuevaFactura, numeroLinea, descripcion, cantidad, precio);
                if (resultadoInsertarDetalleFactura > 0) {
                    System.out.println("Se insertaron correctamente los detalles de la factura.");
                    numeroLinea++; // Aumentamos el número de linea.
                } else {
                    System.out.println("Error al insertar los detalles de la factura.");
                    return;
                }

                System.out.println("¿Quieres continuar introduciendo detalles de la factura? (True|False)");
                continuar = sc.nextBoolean();
                sc.nextLine();
            } while (continuar == true);

            if (continuar == false) {
                System.out.println("Saliendo del proceso...");
                return;
            }
        } catch (Exception ex) {
            System.out.println("Ha ocurrido un error al insertar la factura: " + ex.getMessage());
        }
    }

    public int consultaInsertarFactura(Factura factura) throws SQLException {
        // Opción 1. Insertar factura. 
        String sql = "INSERT INTO Factura (fecha, nombreCliente, formaPago, pagado, direccion) VALUES (?, ?, ?, ? , ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setDate(1, Date.valueOf(factura.getFecha()));
            stmt.setString(2, factura.getNombreCliente());
            stmt.setString(3, null); // Forma de pago nula
            stmt.setBoolean(4, false); // No pagado
            stmt.setString(5, factura.getDireccion());

            int filasAfectadas = stmt.executeUpdate();
            // Para devolver el Id de la factuy insertada
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                    // Ponemos 1 porque es el valor de la posicion del ID.
                }
            }
            return -1;
        } catch (SQLException ex) {
            System.out.println("Ha ocurrido un error al insertar la factura: " + ex.getMessage());
            return -2;
        }
    }

    public int consultaInsertarDetalleFactura(int idFactura, int numeroLinea, String descripcion, double cantidad, double precio) throws SQLException {
        // Opción 1. Insertar factura. 
        String consulta = "INSERT INTO DetalleFactura (idFactura, numeroLinea, descripcion, cantidad,precio) VALUES (?, ?, ?, ?, ?) ";
        try {
            PreparedStatement stmt = con.prepareStatement(consulta);

            stmt.setInt(1, idFactura);
            stmt.setInt(2, numeroLinea);
            stmt.setString(3, descripcion);
            stmt.setDouble(4, cantidad);
            stmt.setDouble(5, precio);

            return stmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error SQL: " + ex.getMessage());
            return -1;
        }
    }

    public void pagarFactura() {
        // Opción 2. Pagar factura.
        try {
            System.out.print("Introduce el Id de la factura para modificar el metodo de pago: ");
            int idFacturaBuscar = introduceEntero();

            if (idFacturaBuscar < 0) {
                System.out.println("Id inválido. Operación cancelada.");
                return;
            }

            System.out.println("Selecciona un método de pago: (Introduce un número)");
            System.out.println("Efectivo(1)");
            System.out.println("Tarjeta(2)");
            System.out.println("Transferencia(3)");
            int formaPagoInt = introduceEntero();
            String formaPagoString = null;
            Factura.FormaDePago formaPago = null;

            if (formaPagoInt < 0) {
                System.out.println("El método de pago es inválido. Operación cancelada.");
                return;
            }

            if (formaPagoInt == 1) {
                formaPagoString = "Efectivo";
                formaPago = Factura.FormaDePago.valueOf(formaPagoString.toUpperCase());
            } else if (formaPagoInt == 2) {
                formaPagoString = "Tarjeta";
                formaPago = Factura.FormaDePago.valueOf(formaPagoString.toUpperCase());
            } else if (formaPagoInt == 3) {
                formaPagoString = "Transferencia";
                formaPago = Factura.FormaDePago.valueOf(formaPagoString.toUpperCase());
            } else {
                System.out.println("Método de pago no válido. Operación cancelada.");
                return;
            }

            int resultado = consultaModificarFormaPago(idFacturaBuscar, formaPago);

            if (resultado == 0) {
                System.out.println("La forma de pago ha sido modificada.");
            }
        } catch (Exception ex) {
            System.out.println("Ha ocurrido un error: " + ex.getMessage());
        }
    }

    public int consultaModificarFormaPago(int idBuscar, Enum formaPago) {
        // Opción 2. Pagar factura.
        try {
            String consulta = "SELECT f.formaPago, f.id, f.pagado "
                    + "FROM Factura f "
                    + "JOIN DetalleFactura df "
                    + "ON f.id = df.idFactura "
                    + "WHERE f.id = " + idBuscar;

            System.out.println("Consulta SQL para buscar la factura: " + consulta);

            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(consulta);

            if (rs.next()) {
                int idFactura = rs.getInt("id");
                boolean pagado = rs.getBoolean("pagado");

                String modificar = "UPDATE Factura SET formaPago = '" + formaPago.toString() + "'"
                        + (pagado == false ? ", pagado = '1'" : "")
                        + " WHERE id = " + idBuscar;

                System.out.println("Consulta para modificar la forma de pago: " + modificar);

                int filasAfectadas = s.executeUpdate(modificar);

                if (filasAfectadas < 0) {
                    System.out.println("No se ha podido modificar la forma de pago.");
                    return -1;
                }
            } else {
                System.out.println("No se pudo encontrar una factura con el Id: " + idBuscar);
                return -1;
            }

        } catch (SQLException ex) {
            System.out.println("Ha ocurrido un error SQL: " + ex.getMessage());
            return -2;
        }
        return 0;
    }

    public void listarFacturas() {
        try {
            List<Factura> facturas = obtenerFacturas();
            if (facturas == null || facturas.isEmpty()) {
                System.out.println("No hay facturas que mostrar.");
            } else {
                for (Factura f : facturas) {
                    System.out.println(f);
                }
            }
        } catch (Exception ex) {
            System.out.println("Ha habido un error mostrando las facturas: " + ex.getMessage());
        }
    }

    public List<Factura> obtenerFacturas() {
        List<Factura> facturas = new ArrayList();
        String sql = "SELECT * FROM Factura";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");

                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                String nombreCliente = rs.getString("nombreCliente");

                String formaPagoString = rs.getString("formaPago");
                Factura.FormaDePago formaPago = null;

                // Comprobar que no sea null ya que se puede insertar un null.
                if (formaPagoString != null) {
                    formaPago = Factura.FormaDePago.valueOf(formaPagoString.toUpperCase());
                }

                boolean pagado = rs.getBoolean("pagado");
                String direccion = rs.getString("direccion");

                Factura factura = new Factura(id, fecha, nombreCliente, formaPago, pagado, direccion);
                facturas.add(factura);
            }
        } catch (SQLException ex) {
            System.out.println("Ha ocurrido un error al listar las facturas:" + ex.getMessage());
        }
        return facturas;
    }

    public void listarDetallesFacturas() {
        try {
            List<DetalleFactura> detalles = obtenerDetallesFacturas();
            if (detalles == null || detalles.isEmpty()) {
                System.out.println("No hay detalles de facturas que mostrar.");
            } else {
                for (DetalleFactura df : detalles) {
                    System.out.println(df);
                }
            }
        } catch (Exception ex) {
            System.out.println("Ha ocurrido un error al listar los detalles de las facturas: " + ex.getMessage());
        }
    }

    public List<DetalleFactura> obtenerDetallesFacturas() {
        List<DetalleFactura> detalles = new ArrayList();
        String sql = "SELECT * FROM DetalleFactura";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                DetalleFactura detalleFactura = new DetalleFactura(
                        rs.getInt("idFactura"),
                        rs.getInt("numeroLinea"),
                        rs.getString("descripcion"),
                        rs.getDouble("cantidad"),
                        rs.getDouble("precio"));

                detalles.add(detalleFactura);
            }
        } catch (SQLException ex) {
            System.out.println("Ha ocurrido un error al listar los detalles de las facturas: " + ex.getMessage());
        }
        return detalles;
    }

    public void eliminarFactura() {
        // Opción 3. Eliminar factura.
        try {
            System.out.print("Introduce el ID para eliminar la factura: ");
            int idBuscar = introduceEntero();

            if (idBuscar < 0) {
                System.out.println("Id de la factura inválido. Operación cancelada");
                return;
            }

            int resultado = consultaEliminar(idBuscar);

            if (resultado != 0) {
                System.out.println("No se ha podido eliminar la factura.");
            }
        } catch (Exception ex) {
            System.out.println("Ha ocurrido un error al eliminar la factura: " + ex.getMessage());
        }
    }

    public int consultaEliminar(int idBuscar) {
        // Opción 3. Eliminar factura.
        Statement s = null;
        try {
            String consulta = "DELETE FROM Factura "
                    + "WHERE id = " + idBuscar;

            System.out.println("Consulta SQL: " + consulta);

            s = con.createStatement();
            int filasAfectadas = s.executeUpdate(consulta);

            if (filasAfectadas == 0) {
                System.out.println("No se ha podido encontrar una factura con el id: " + idBuscar);
                return -1;
            } else {
                System.out.println("Factura eliminada correctamente.");
            }

        } catch (SQLException ex) {
            System.out.println("Error al eliminar la factura: " + ex.getMessage());
            return -2;
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar el Statement: " + ex.getMessage());
            }
        }
        return 0;
    }

    public void mostrarFacturaPorId() {
        try {
            System.out.print("Introduce el Id de la factura que quieres ver: ");
            int idBuscar = introduceEntero();

            if (idBuscar < 0) {
                System.out.println("Id de la factura inválido. Operación cancelada.");
                return;
            }

            consultaMostrarFacturaPorId(idBuscar);
            System.out.println("\n --- Fin mostrar factura --- ");

            System.out.println();

            consultaMostrarDetalleFacturaPorId(idBuscar);
            System.out.println("\n --- Fin mostrar detalles de factura --- ");

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void consultaMostrarFacturaPorId(int idBuscar) {
        try {
            String sql = "SELECT * "
                    + "FROM Factura "
                    + "WHERE id = " + idBuscar;

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                String nombreCliente = rs.getString("nombreCliente");

                // Pasar la forma de pago a Enum.
                String formaPagoString = rs.getString("formaPago");
                Factura.FormaDePago formaPago = null;
                if (formaPagoString != null) {
                    formaPago = Factura.FormaDePago.valueOf(formaPagoString.toUpperCase());
                }

                boolean pagado = rs.getBoolean("pagado");
                String direccion = rs.getString("direccion");

                System.out.println("\n --- Información de la factura con ID " + idBuscar + " --- ");
                System.out.println("    Id: " + id);
                System.out.println("    Fecha: " + fecha);
                System.out.println("    Nombre del cliente: " + nombreCliente);
                System.out.println("    Forma de pago: " + formaPago);
                System.out.println("    Pagado: " + pagado);
                System.out.print("    Dirección: " + direccion);

                return;

            } else {
                System.out.println("No se ha encontrado una factura con el Id : " + idBuscar);
                return;
            }
        } catch (SQLException ex) {
            System.out.println("Error SQL: " + ex.getMessage());
            return;
        }
    }

    public void consultaMostrarDetalleFacturaPorId(int idBuscar) {
        try {
            String sql = "SELECT * "
                    + "FROM DetalleFactura "
                    + "WHERE idFactura = " + idBuscar;

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet st = stmt.executeQuery();

            double precioTotal = 0; // Para el precio total de la factura.
            boolean encontrado = false;

            while (st.next()) {
                int idFactura = st.getInt("idFactura");
                int numeroLinea = st.getInt("numeroLinea");
                String descripcion = st.getString("descripcion");
                double cantidad = st.getDouble("cantidad");
                double precio = st.getDouble("precio");

                double totalLinea = cantidad * precio; // Para el precio que hay en cada linea.
                precioTotal += totalLinea; // Sumamos el precio que hay en cada linea.

                System.out.println("\n --- Detalles de la factura con ID " + idBuscar + " --- ");
                System.out.println("    Id de la factura: " + idFactura);
                System.out.println("    Número de lineas: " + numeroLinea);
                System.out.println("    Descripción: " + descripcion);
                System.out.println("    Cantidad: " + cantidad);
                System.out.println("    Precio: " + precio);
                System.out.println("    Precio total de la linea: " + totalLinea);
                encontrado = true;
            }
            if (!encontrado) {
                System.out.println("No se ha encontrado una factura con id " + idBuscar + " para mostrar sus detalles.");
                return;
            } else {
                System.out.println("############################################");
                System.out.println("    Precio total de la factura: " + precioTotal);
                System.out.println("############################################");
            }
        } catch (SQLException ex) {
            System.out.println("Error SQL: " + ex.getMessage());
            return;
        }
    }

    public int introduceEntero() {
        int numero;
        try {
            numero = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Debes introducir un número natural válido.");
            return -1;
        }
        return numero;
    }
}
