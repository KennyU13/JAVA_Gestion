/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestion.model;

import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author routs
 */
public class ErrorMessage {
    
    public String message = null;
    public String type;
  
    
    public void setMessage(String message)
    {
        switch (message) {
            case "save":
                this.message = "Données enregistrer";
                break;
            case "edit":
                this.message = "Données modifier";
                break;
            case "delete":
                this.message = "Données supprimer";
                break;
            case "date":
                this.message = "Date déja occuper par le même employée";
                break;
            case "conge":
                this.message = "Jour de conge depasse le 30 jour maximum";
                break;
        }
       
       if(message.contains("employe_pkey") || message.contains("conge_pkey")) this.message = "Numero déjà enregistrer";
       if(message.contains("employe_salaire_check")) this.message = "Le salaire doit être supérieur à 0 Ar";
       
        
    }
    public void setLabelMessage(JLabel label)
    {
//        if(type)
//        {
//            label.setForeground(Color.blue);
//            label.setText(message);
//        }
//        else
//        {
//            label.setForeground(Color.red);
//            label.setText(message);
//        }
    }
    public String getType()
    {
        return type;
    }
    
}
