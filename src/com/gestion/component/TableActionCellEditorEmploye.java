/*
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestion.component;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import com.gestion.view.PanelActionEmploye;

/**
 *
 * @author routs
 */
public class TableActionCellEditorEmploye extends DefaultCellEditor{

    private TableActionEventEmploye event;
    public TableActionCellEditorEmploye(TableActionEventEmploye event) {
        super(new JCheckBox());
        this.event = event;
    }
    @Override
    public Component getTableCellEditorComponent(JTable jtable ,Object o ,boolean bln ,int row ,int column) {
        PanelActionEmploye action = new PanelActionEmploye();
        action.initEvent(event,row);
       // action.setBackground(Color.WHITE); //jtable.getSelectionBackground()
        return action;
        
    } 

}
