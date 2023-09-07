/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestion.model;

import java.sql.*;
/**
 *
 * @author routs
 */
public class Database {
    Connection con = null;
    
    public Connection getConnection(){
        
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/crud";
            String user = "rotsy";
            String passwd = "GG591337GG";
            con = DriverManager.getConnection(url, user, passwd);
            
          } catch (ClassNotFoundException | SQLException e) {
            //e.printStackTrace();
            System.out.println("SQL Exception " + e.getMessage());
          }  
        return con;
    }
    
}
