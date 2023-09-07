/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestion.component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author routs
 */
public class PdfGenerator {
    
    private static String date;
    private static String nom;
    private static String prenom;
    private static String poste;
    private static int absence;
    private static  String montant;

    public void setDate(String date) {this.date = date;}
    public void setNom(String nom) {this.nom = nom;}
    public void setPrenom(String prenom) {this.prenom = prenom;}
    public void setPoste(String poste) {this.poste = poste;}
    public void setAbsence(int absence) {this.absence = absence;}
    public void setMontant(String montant) {this.montant = montant;}
    
    public boolean generate()
    {
        try {
                   
            JFileChooser file = new JFileChooser();
            file.setSize(350, 450);
            FileNameExtensionFilter filtre = new FileNameExtensionFilter("*.pdf", "pdf");
            file.addChoosableFileFilter(filtre);
            int result = file.showSaveDialog(null);
            if(result == JFileChooser.APPROVE_OPTION)
            {
                Document doc = new Document();
                PdfWriter.getInstance(doc,new FileOutputStream(file.getSelectedFile()));

                doc.open();
                    doc.addTitle("Fiche de paie");
                    doc.add( new Paragraph("FICHE DE PAIE"));
                    doc.add( new Paragraph(" "));
                    doc.add( new LineSeparator());
                    doc.add( new Paragraph(" "));
                    doc.add( new Paragraph(" "));
                    doc.add( new Paragraph("Date                        :   "+ date));
                    doc.add( new Paragraph("Nom                        :   "+ nom));
                    doc.add( new Paragraph("Prenom                   :   "+ prenom));
                    doc.add( new Paragraph("Poste                       :   "+ poste));
                    doc.add( new Paragraph("Nombre d'absence  :   "+ absence));
                    doc.add( new Paragraph("Montant                   :   "+ montant));
                    doc.add( new Paragraph(" "));
                    doc.add( new Paragraph(" "));
                    doc.add( new Paragraph(" "));
                    doc.add( new Paragraph(" "));
                    doc.add( new Paragraph(" "));
                    doc.add( new Paragraph(" "));
                    doc.add( new LineSeparator());
                doc.close();
                return true;
               
            }
//             
           
               
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
}
