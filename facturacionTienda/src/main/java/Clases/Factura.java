package Clases;

import java.sql.Date;

public class Factura {

    private int id;
    private Date fecha;
    private String nombreCliente;

    public enum FormaDePago {
        EFECTIVO, TARJETA, TRANSFERENCIA;
    }

    private FormaDePago formaPago;
    private boolean pagado;
    private String direccion;

    public Factura(int id, Date fecha, String nombreCliente, FormaDePago formaPago, boolean pagado, String direccion) {
        this.id = id;
        this.fecha = fecha;
        this.nombreCliente = nombreCliente;
        this.formaPago = formaPago;
        this.pagado = pagado;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public FormaDePago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaDePago formaPago) {
        this.formaPago = formaPago;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Factura: " + "Id: " + id + ", Fecha:" + fecha + ", Nombre del cliente: "
                + nombreCliente + ", Forma de pago: " + formaPago + ", Pagado: " + pagado + ", Direccion: " + direccion;
    }
}
