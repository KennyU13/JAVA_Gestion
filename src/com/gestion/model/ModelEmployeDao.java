/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestion.model;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author routs
 */
public class ModelEmployeDao extends ErrorMessage{
    
    Connection con;
    Database db = new Database();
    PreparedStatement ps;
    ResultSet rs;
    
    

    public boolean enregistrer(ModelEmploye mp){
        String sql = "INSERT INTO Employe (numEmp, Nom, Prenom, poste,salaire) VALUES" + "(?,?,?,?,?)";
        try
        {
            con = db.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setString(1, mp.getNumero());
            ps.setString(2, mp.getNom());
            ps.setString(3, mp.getPrenom());
            ps.setString(4, mp.getPoste());
            ps.setInt(5,mp.getSalaire() );
            ps.executeUpdate();
            this.type = "SUCCESS";
            setMessage("save");
            return true;
            
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
            setMessage(e.getMessage());
            this.type = "ERROR";
            
        }
        return false;
    } 
    public ArrayList lister(){
        
        ArrayList<ModelEmploye> lis = new ArrayList<>();    
        
        String sql = "SELECT * FROM Employe ORDER BY numemp ";
        try{
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next())
             {
                 ModelEmploye mEmploye = new ModelEmploye();
                 mEmploye.setNumero(rs.getString("numEmp"));//numEmp
                 mEmploye.setNom(rs.getString("Nom"));//nom
                 mEmploye.setPrenom(rs.getString("Prenom"));//Prenom
                 mEmploye.setPoste(rs.getString("poste"));//poste
                 mEmploye.setSalaire(rs.getInt("salaire"));//salaire
                 lis.add(mEmploye);
             }        
            
        }catch(SQLException e)
        {
            System.out.println(e);
        }
        return lis;
    }
    public ArrayList recherche(String name){
        
        ArrayList<ModelEmploye> lis = new ArrayList<>();    
        
        String sql = "SELECT * FROM Employe WHERE nom LIKE ? or prenom LIKE ? ORDER BY numEmp ";
        try{
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1,"%"+ name +"%");
            ps.setString(2,"%"+ name +"%");
            rs = ps.executeQuery();
            
            while(rs.next())
             {
                 ModelEmploye mEmploye = new ModelEmploye();
                 mEmploye.setNumero(rs.getString("numEmp"));//numEmp
                 mEmploye.setNom(rs.getString("Nom"));//nom
                 mEmploye.setPrenom(rs.getString("Prenom"));//Prenom
                 mEmploye.setPoste(rs.getString("poste"));//poste
                 mEmploye.setSalaire(rs.getInt("salaire"));//salaire
                 lis.add(mEmploye);
             }        
            
        }catch(SQLException e)
        {
            System.out.println(e);
        }
        return lis;
    }
    public void modifier(ModelEmploye mp) {
        
        String sql = "UPDATE Employe SET  Nom=?, Prenom=?, poste=?,salaire=? WHERE numEmp=? ";
        try{
            con = db.getConnection();
            ps  = con.prepareStatement(sql);
            
            ps.setString(1, mp.getNom());
            ps.setString(2, mp.getPrenom());
            ps.setString(3, mp.getPoste());
            ps.setInt(4,mp.getSalaire());
            ps.setString(5, mp.getNumero());
            ps.executeUpdate();
            this.type = "SUCCESS";
            setMessage("edit");
           
        }
        catch(SQLException e){
            setMessage(e.getMessage());
            this.type = "ERROR";
        }
    }
    public void delete(ModelEmploye mp){
        ModelPointageDao mpd = new ModelPointageDao();
        
        String sql = "DELETE FROM Employe WHERE numEmp=? ";
        try{
            
            con = db.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setString(1, mp.getNumero());
            ps.executeUpdate();
            this.type = "SUCCESS";
            setMessage("delete");
            
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
    public void additionSalaire(String salaire){
        String sql = "UPDATE Employe SET salaire= salaire + 10000 WHERE numEmp=?  ";
        try{
            
            con = db.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setString(1, salaire);
            ps.executeUpdate();
            
        }
        catch(SQLException e){
             System.out.println(e);
        }
    }
    public void soustractionSalaire(String salaire){
        String sql = "UPDATE Employe SET salaire= salaire - 10000 WHERE numEmp=?  ";
        try{
            
            con = db.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setString(1, salaire);
            ps.executeUpdate();
        }
        catch(SQLException e){
             System.out.println(e);
        }
    }
    public int montant(int absence, String numemp){
        int x=0;
        String sql = "SELECT salaire FROM employe WHERE numemp = '"+numemp+"'";
        ModelPointageDao mPointageD = new ModelPointageDao();
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
           
            rs = ps.executeQuery();
            while (rs.next()) {                
                x= rs.getInt("salaire");
            }
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return x + mPointageD.somme(numemp)-(absence*10000);
    }
    
}
