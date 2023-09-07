/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestion.component;

/**
 *
 * @author routs
 */
public interface TableActionEventEmploye {
    
    public void onEdit(int row);
    
    public void onDelete(int row);
    
    public void onCheckConge(int row);
    
    public void onFichePaie(int row);
}
