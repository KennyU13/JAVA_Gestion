/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestion.component;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author routs
 */
public class TextFieldVerify {
    
    
    public static boolean creationField_isNotEmpty(JTextField field1 ,JTextField field2 ,JTextField field3 ,JTextField field4 ,JTextField field5){
        if(field1.getText().isEmpty() || field2.getText().isEmpty() || field3.getText().isEmpty() || field4.getText().isEmpty() || field5.getText().isEmpty())
        {
            
            return false;
        }
        return true;
    }
 
    public static boolean modificationField_isNotEmpty(JTextField field1 ,JTextField field2 ,JTextField field3 ,JTextField field4)
    {
        if( field1.getText().isEmpty()|| field2.getText().isEmpty() || field3.getText().isEmpty() || field4.getText().isEmpty())
        {
           
            return false;
        }
        return true;
    }
    public static boolean congeField_isNotEmpty(JTextField field1 ,JTextField field2 ,JTextField field3)
    {
        if( field1.getText().isEmpty()|| field2.getText().isEmpty() || field3.getText().isEmpty())
        {
           
            return false;
        }
        return true;
    }
    
}
