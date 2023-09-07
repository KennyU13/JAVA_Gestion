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
public class ModelCongeDao extends ErrorMessage{
    
    Connection con;
    Database db = new Database();
    PreparedStatement ps;
    ResultSet rs;
    
    public ArrayList lister(){
        
    ArrayList<ModelConge> lis = new ArrayList<>();
    String sql = "SELECT * FROM Conge ORDER BY numConge";
        try{
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next())
             {
                 ModelConge mp = new ModelConge();
                 mp.setNumConge(rs.getString("numConge"));
                 mp.setNumEmp(rs.getString("numEmp"));
                 mp.setMotif(rs.getString("motif"));
                 mp.setNbrJour((rs.getInt("nbrjr")));
                 mp.setDateDemande(String.valueOf(rs.getDate("dateDemande")));
                 mp.setDateRetour(String.valueOf(rs.getDate("dateRetour")));
                 lis.add(mp);
             }        
            
        }catch(SQLException e)
        {
            System.out.println(e);
        }
        return lis;
    }
   
    public boolean enregistrer(ModelConge mp)
    { 
        String sql  = "INSERT INTO Conge (numConge,numEmp,motif,nbrjr,dateDemande,dateRetour ) VALUES" + "(?,?,?,?,?,?) ";
        String sql2 = "UPDATE Conge SET dateRetour = dateDemande + integer  '"+ (mp.getNbrJour()-1) +"' WHERE numConge = ?";
        
        
        int year = decoupeYear(mp.getDateDemande());
        int x = nombreJour(mp.getNumEmp(),year)+mp.getNbrJour();
        
        try
        {
           
            if(!dateExist(mp)) 
            {    
                if(nombreJour(mp.getNumEmp(),year)<=30 && x<=30)
                {
                    con = db.getConnection();
                    ps  = con.prepareStatement(sql);
                    ps.setString(1,mp.getNumConge());
                    ps.setString(2,mp.getNumEmp());
                    ps.setString(3,mp.getMotif());
                    ps.setInt(4,mp.getNbrJour());
                    ps.setDate(5, Date.valueOf(mp.getDateDemande()));
                    ps.setDate(6, Date.valueOf(mp.getDateDemande()));           
                    ps.executeUpdate();
                    ps = con.prepareStatement(sql2);
                    ps.setString(1,mp.getNumConge());
                    ps.executeUpdate();
                    
                    this.type = "SUCCESS";
                    setMessage("save");
                    return true;
                }
                else{
                    this.type = "ERROR";
                    setMessage("conge");
                }   
            }
            else{
                this.type = "ERROR";
                setMessage("date");
            }

        }
        catch(SQLException e){
            
            this.type = "ERROR";
            setMessage(e.getMessage());
            System.out.println("save "+e.getMessage());
        }
        return false;
    }
    public void modifier(ModelConge mp) {
        
        String sql = "UPDATE Conge SET  numEmp=?, motif=?,nbrjr=?,dateDemande=?, dateRetour = dateDemande + integer'"+ (mp.getNbrJour()-1) +"' WHERE numConge=? ";
        int year = decoupeYear(mp.getDateDemande());
        int x = nombreJourEdit(mp.getNumEmp(),mp.getNumConge(),year)+mp.getNbrJour();
       
        try{
            if(!dateExistEdit(mp)) 
            {   
                if(nombreJourEdit(mp.getNumEmp(),mp.getNumConge(),year)<=30 && x<=30)
                {
                    con = db.getConnection();
                    ps  = con.prepareStatement(sql);

                    ps.setString(1, mp.getNumEmp());
                    ps.setString(2, mp.getMotif());
                    ps.setInt(3, mp.getNbrJour());
                    ps.setDate(4, Date.valueOf(mp.getDateDemande()));

                    ps.setString(5, mp.getNumConge());

                    for (int i = 0; i < 2; i++) {
                        ps.executeUpdate();
                    }
                    this.type = "SUCCESS";
                    setMessage("edit");
                }
                else{
                    this.type = "ERROR";
                    setMessage("conge");
                }  
            }else
            {
                this.type = "ERROR";
                setMessage("date");
            }
           
        }
        catch(SQLException e){
            setMessage("conge");
            this.type = "ERROR";
        }
    }
   
    public void deleteSingle(ModelConge mp)
    {
        String sql = "DELETE FROM Conge WHERE numConge= ?";
       
        String pointage = null;
        try{
            ModelEmployeDao mEmployeD = new ModelEmployeDao();
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, mp.getNumConge());
            ps.executeUpdate();
            
        }
        catch(SQLException e){
             System.out.println(e);
        }
    }    
    public int nombreJour(String numEmp,int year)
    {  
        String sql = "SELECT SUM(nbrjr) AS jour FROM Conge WHERE numEmp =? AND EXTRACT(YEAR from dateDemande) = '"+year+"' ";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1,numEmp);
            rs = ps.executeQuery();
        while(rs.next())
        {
           return rs.getInt("jour");
        }    
        } catch (SQLException e) {
           System.out.println("jour "+e.getMessage());
            
        }
        return 0;
    }
     public int nombreJourEdit(String numEmp,String numConge,int year)
    {  
        String sql = "SELECT SUM(nbrjr) AS jour FROM Conge WHERE numEmp =? AND NOT numConge=? AND EXTRACT(YEAR from dateDemande) = '"+year+"' ";//selection au meme années
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1,numEmp);
            ps.setString(2,numConge);
            rs = ps.executeQuery();
            while(rs.next()) return rs.getInt("jour");
        } catch (SQLException e) {
           System.out.println("jour "+e.getMessage());
            
        }
        return 0;
    }
    public boolean inConge(ModelPointage mp)
    {
         int i=0;
        String sql = "SELECT * FROM Conge WHERE (datedemande <= '"+mp.getDate()+"' AND dateRetour >= '"+mp.getDate()+"') AND numEmp = '" + mp.getNumero().toUpperCase()+"'";
        try{
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) i++;
            if(i>0) return true;
        }
        catch(SQLException e)
        {
            System.out.println("conge "+e.getMessage());
        }
        return false;
    }
    private boolean  dateExist(ModelConge mp)
    {
        int i=0;
        String dateRetour = getDateRetour(mp.getDateDemande(), mp.getNbrJour());     
        String sql= "SELECT * FROM Conge WHERE (( '"+mp.getDateDemande()+"' BETWEEN dateDemande AND dateRetour) OR ( '"+dateRetour+"' BETWEEN dateDemande AND dateRetour)) AND numEmp='" + mp.getNumEmp().toUpperCase()+"'" ;
        
        try{
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) i++;
            
            System.out.println(i);
            if(i>0) return true;
        }
        catch(SQLException e)
        {
            System.out.println("date "+e.getMessage());
        }
        return false;
    }
    private boolean  dateExistEdit(ModelConge mp)
    {
        int i=0;
        String dateRetour = getDateRetour(mp.getDateDemande(), mp.getNbrJour());
        
        String sql = "SELECT * FROM Conge WHERE (( '"+mp.getDateDemande()+"' BETWEEN dateDemande AND dateRetour) OR ( '"+dateRetour+"' BETWEEN dateDemande AND dateRetour))AND numEmp = '" + mp.getNumEmp().toUpperCase()+"' AND NOT numConge = '" + mp.getNumConge()+"'";
        
        try{
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next())i++;
            
            if(i>0) return true;
            
        }
        catch(SQLException e)
        {
            System.out.println("date "+e.getMessage());
        }
        return false;
    }
    private int decoupeYear(String date)
    {
        String res=date,concatener="";
        int year = 0;
        for (int i = 0; i < 4; i++) {
             concatener = concatener.concat(String.valueOf(res.charAt(i)));
        }
        year = Integer.valueOf(concatener);
        return year;
    }
    private String getDateRetour(String date, int jour)
    {
        String dateRetour="";
        String sql = "SELECT DATE '" + date + "' + INTEGER '" + (jour - 1) + "' as dateRetour";
        try {
            con = db.getConnection();
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {                
                dateRetour = rs.getString("dateRetour");
            }
            
        } catch (Exception e) {
            System.err.println("DateRetour  ::  "+e.getMessage());
        }
        
        return dateRetour;
    }
    
}