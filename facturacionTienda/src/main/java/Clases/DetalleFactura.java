package Clases;

public class DetalleFactura {
    
    private int idFactura;
    private int numeroLinea;
    private String descripcion;
    private double cantidad;
    private double precio;

    public DetalleFactura(int idFactura, int numeroLinea, String descripcion, double cantidad, double precio) {
        this.idFactura = idFactura;
        this.numeroLinea = numeroLinea;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(int numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "DetalleFactura: " + "Id de la factura: " + idFactura + ", Numero de linea: " + numeroLinea + ", Descripcion: " + descripcion + ", Cantidad: " + cantidad + ", Precio: " + precio;
    }
}
