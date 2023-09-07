package com.gestion.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class TableHeader extends JLabel{

    public TableHeader(String text){
        super(text);
        setOpaque(true);
        setBackground(new Color(245,245,245));
        setFont(new Font("Segoe UI Semibold", 1, 14));
        setForeground(Color.BLACK);
        setBorder(new EmptyBorder(10,5,10,5));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(230,230,230));
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }

    
    
}
