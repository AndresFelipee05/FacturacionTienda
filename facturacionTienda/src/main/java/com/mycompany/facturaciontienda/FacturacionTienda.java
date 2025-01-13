package com.mycompany.facturaciontienda;

public class FacturacionTienda {

    public static void main(String[] args) {

        TiendaApp app = new TiendaApp();
        
        
        app.conectar();
        
        app.insertarFactura();
        
        app.desconectar();
    }
}
