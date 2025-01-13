package com.mycompany.facturaciontienda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class TiendaApp {
    
    private Connection con;
    private final Scanner sc = new Scanner(System.in);
    
    public void conectar() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/facturacionTienda", "root", "root");

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
    
    public int insertarFactura(){
        System.out.print("Introduce el nombre del cliente: ");
        String nombreCliente = sc.nextLine();
        
        System.out.print("Introduce la direccion: ");
        String direccion = sc.nextLine();
        
        
    }
}
