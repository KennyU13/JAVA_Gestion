/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author routs
 */
public class ModelPointageDao extends ErrorMessage{
    Connection con;
    Database db = new Database();
    PreparedStatement ps;
    ResultSet rs;
    private int absence;
    public boolean isNotExist(ModelPointage mp)
    {
        String sql = "SELECT * FROM Pointage WHERE datePointage = '" +mp.getDate()+ "'";
        try{
            con = db.getConnection();
            ps  = con.prepareStatement(sql);
            rs  = ps.executeQuery();
            while(rs.next())
            {
                if(mp.getDate().equals(rs.getString("datePointage")))
                {
                  return false;  
                }
            } 
        }catch(SQLException e)
        {
            System.err.println(e);
        }
        return true;
    }
    public void enregistrer(ModelPointage mp){
        String sql = "INSERT INTO Pointage (datePointage, numEmp, pointage) VALUES" + "(?,?,?)";
        try{
            ModelEmployeDao mEmployeD = new ModelEmployeDao();
            con = db.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, mp.getNumero());
            ps.setString(3, mp.getPointage());
            ps.executeUpdate();
            
//            if("non".equals(mp.getPointage()))
//            {
//                mEmployeD.soustractionSalaire(mp.getNumero());
//            }
            this.type = "SUCCESS";
            setMessage("save");
        }
        catch(SQLException e){
             System.err.println(e);
             setMessage(e.getMessage());
             this.type = "ERROR";
        }
    }
    public ArrayList lister(){
        
        ArrayList<ModelPointage> lis = new ArrayList<>();    
        
        String sql = "SELECT * FROM Pointage ";
        try{
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next())
             {
                 ModelPointage mp = new ModelPointage();
                 mp.setDate(rs.getString("datePointage"));//numEmp
                 mp.setNumero(rs.getString("numEmp"));//nom
                 mp.setPointage(rs.getString("pointage"));//Prenom
                 
                                  
                 lis.add(mp);
             }        
            
        }catch(SQLException e)
        {
            System.out.println(e);
        }
        return lis;
    }
    
    public ArrayList listeNonPointe(String date)
    {
        ArrayList<ModelPointage> lis = new ArrayList<>();    
        
        String sql = "SELECT * FROM Pointage WHERE pointage = 'non' ";
        try{
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            String res,concatener = "";
            while(rs.next())
             {
                 
                 res = rs.getString("datePointage");
                 for (int i = 0; i < 10; i++) {
                    concatener = concatener.concat(String.valueOf(res.charAt(i)));
                 }
                 if(concatener.equals(date))
                 {
                    ModelPointage mp = new ModelPointage();
                    mp.setDate(rs.getString("datePointage"));//numEmp
                    mp.setNumero(rs.getString("numEmp"));//nom
                    mp.setPointage(rs.getString("pointage"));//Prenom
                    lis.add(mp);
                 }
                 concatener = "";
             }        
            
        }catch(SQLException e)
        {
            System.out.println(e);
        }
        return lis;
    }
    public int nombreAbs(int year, int month, String numemp)
    {
        int x=0;
        String sql = "SELECT COUNT(pointage) AS nombre FROM pointage WHERE pointage ='non' AND DATE_TRUNC('month',datePointage)='"+year+"-"+month+"-01' AND numemp='"+numemp+"'";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {                
                x= rs.getInt("nombre");
                this.absence = x;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return x;
        
    }
    public int somme(String numemp) // tous les absences
    {
        int x=0;
        String sql = "SELECT COUNT(pointage) AS nombre FROM pointage WHERE pointage ='non' AND  numemp='"+numemp+"' ";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {                
                x= rs.getInt("nombre");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return x*10000;
        
    }
    public void modifier(ModelPointage mp) {
        
        String sql = "UPDATE Pointage SET  numEmp=?, pointage=? WHERE datePointage=? ";
        String sql2 = "SELECT Pointage FROM Pointage WHERE datePointage = ?";
        String pointage=null;
        try{
            ModelEmployeDao mEmployeD = new ModelEmployeDao();
            con = db.getConnection();
            
            ps = con.prepareStatement(sql2);
            ps.setTimestamp(1, Timestamp.valueOf(mp.getDate()));
            rs = ps.executeQuery();
            while(rs.next())
            {
                pointage = rs.getString("pointage");
            }
            
            ps  = con.prepareStatement(sql);
            ps.setString(1, mp.getNumero());
            ps.setString(2, mp.getPointage());
            ps.setTimestamp(3, Timestamp.valueOf(mp.getDate()));
            
            ps.executeUpdate();
            //----------------------TALOHA-------------------
//            if("oui".equals(mp.getPointage()) && "non".equals(pointage))
//            {
//                mEmployeD.additionSalaire(mp.getNumero());
//            }
//            if("non".equals(mp.getPointage()) && "oui".equals(pointage))
//            {
//                mEmployeD.soustractionSalaire(mp.getNumero());
//            }
            this.type = "SUCCESS";
            setMessage("edit");
        }
        catch(SQLException e){
            setMessage(e.getMessage());
            this.type = "ERROR";
        }
    }
    public void deleteSingle(ModelPointage mp)
    {
        String sql = "DELETE FROM Pointage WHERE datePointage=? ";
        String sql2 = "SELECT Pointage FROM Pointage WHERE datePointage = ?";
        String pointage = null;
        try{
            ModelEmployeDao mEmployeD = new ModelEmployeDao();
            con = db.getConnection();
            ps = con.prepareStatement(sql2);
            ps.setTimestamp(1, Timestamp.valueOf(mp.getDate()));
            rs = ps.executeQuery();
            while(rs.next())
            {
                pointage = rs.getString("Pointage");
            }
            
            ps  = con.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(mp.getDate()));
            ps.executeUpdate();
                        //----------------------TALOHA-------------------
//            if("non".equals(pointage))
//            {
//                mEmployeD.additionSalaire(mp.getNumero());
//            }
            setMessage("delete");
            this.type = "SUCCESS";
        }
        catch(SQLException e){
             System.out.println(e);
        }
    }

}
