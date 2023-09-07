/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestion.model;

/**
 *
 * @author routs
 */
public class ModelPointage {
    
    private String date;
    private String numero;
    private String pointage;

    public ModelPointage() {
    }

    
    public ModelPointage(String date, String numero,String pointage) {
        this.date = date;
        this.numero = numero;
        this.pointage = pointage;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    public void setPointage(String pointage) {
        this.pointage = pointage;
    }

    public String getPointage() {
        return pointage;
    }
    
    
    
    
    
}
