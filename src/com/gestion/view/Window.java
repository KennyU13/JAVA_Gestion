/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.gestion.view;

import com.gestion.component.PdfGenerator;
import com.gestion.model.*;
import com.gestion.component.*;
import java.awt.CardLayout;
import java.awt.Color;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import com.gestion.component.TextFieldVerify;
import com.gestion.swing.Notification;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTable;

/**
 * @author routs
 */
public class Window extends javax.swing.JFrame {

    ModelEmploye mEmploye = new ModelEmploye();
    ModelEmployeDao mEmployeD = new ModelEmployeDao();
    ModelPointage mPointage = new ModelPointage();
    ModelPointageDao mPointageD = new ModelPointageDao();
    ModelConge mConge = new ModelConge();
    ModelCongeDao mCongeD = new ModelCongeDao();
    String dateTime;
    CardLayout cardLayout;
    CardLayout cardLog;
    CardLayout cardPointageCE;
    CardLayout cardList;
    CardLayout cardCongeCE;
    Notification panel;
    JFrame frame = this;
    private int ligne;
    private static String numEmp;
    private String date;

    public Window() {

        initComponents();

        cardLayout = (CardLayout) (cardPanel.getLayout());
        cardLog = (CardLayout) (cardCreationEmploye.getLayout());
        cardPointageCE = (CardLayout) (cardCreationPointage.getLayout());
        cardList = (CardLayout) (cardListPointage.getLayout());
        cardCongeCE = (CardLayout) (cardCongeCEE.getLayout());
        DefaultTableModel model = (DefaultTableModel) tableEmploye.getModel();
        DefaultTableModel modelPointage = (DefaultTableModel) tablePointage.getModel();
        DefaultTableModel modelAbsence = (DefaultTableModel) tableNonPointe.getModel();
        DefaultTableModel modelConge = (DefaultTableModel) tableConge.getModel();

        /*--------------------------------------------------------------------------------------------------------------------
        -                                              ACTION EMPLOYE
        ---------------------------------------------------------------------------------------------------------------------*/
        TableActionEventEmploye eventEmploye = new TableActionEventEmploye() {
            @Override
            public void onEdit(int row) {
                try {
                    if( tableEmploye.isEditing()){
                        tableEmploye.getCellEditor().stopCellEditing();
                    }
                    actualiserEmploye();
                    messageCreateEmploye.setText(null);
                    messageEditEmploye.setText(null);
                    cardLog.show(cardCreationEmploye, "editEmploye");
                    numeroEmployeE.setText((String) model.getValueAt(row, 0));
                    nomEmployeE.setText((String) model.getValueAt(row, 1));
                    prenomEmployeE.setText((String) model.getValueAt(row, 2));
                    posteEmployeE.setText((String) model.getValueAt(row, 3));
                    salaireEmployeE.setText((String) model.getValueAt(row, 4).toString().replace("Ar", "").trim());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Edit Employe ::: " + e.getMessage());
                }

            }

            @Override
            public void onDelete(int row) {
                try {
                    if( tableEmploye.isEditing()){
                        tableEmploye.getCellEditor().stopCellEditing();
                    }
                    actualiserConge();
                    cardLog.show(cardCreationEmploye, "createEmploye");
                    mEmploye.setNumero((String) model.getValueAt(row, 0));
                    int confirm = JOptionPane.showConfirmDialog(null, "êtes-vous sûr de voulouir supprimer cette ligne ");
                    if (confirm == 0) {
                        mEmployeD.delete(mEmploye);
                        panel = new Notification(frame, mEmployeD.getType(), Notification.Location.BOTTOM_LEFT, mEmployeD.message);
                        panel.showNotification();
                        afficheEmploye();
                        rechercheEmploye.setText(null);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Delete Employe ::: " + e.getMessage());
                }
            }

            @Override
            public void onCheckConge(int row) {
                try {
                    if( tableEmploye.isEditing()){
                        tableEmploye.getCellEditor().stopCellEditing();
                    }
                    actualiserEmploye();
                    cardLog.show(cardCreationEmploye, "createEmploye");
                    int jour = 30 - mCongeD.nombreJour((String) model.getValueAt(row, 0), LocalDate.now().getYear());
                    String message = "Congé restant : ";
                    panel = new Notification(frame, "INFO", Notification.Location.TOP_CENTER, (jour < 2) ? message + jour + " jour" : message + jour + " jours");
                    panel.showNotification();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Check Conge Employe ::: " + e.getMessage());
                }
            }

            @Override
            public void onFichePaie(int row) {
                if( tableEmploye.isEditing()){
                    tableEmploye.getCellEditor().stopCellEditing();
                }
                cardLog.show(cardCreationEmploye, "fichePaie");
                String numemp = (String) model.getValueAt(row, 0);
                int month = LocalDate.now().getMonth().getValue();
                int year = Integer.parseInt(String.valueOf(LocalDate.now().getYear()));
                
        //TALOHA        int montant =mEmployeD.montant(mPointageD.nombreAbs(year, month, numemp), numemp);   
        
                int montant = Integer.parseInt((String)model.getValueAt(row,4).toString().replace(" Ar","").trim());//NEW 
                int abs = mPointageD.nombreAbs(year, month, numemp);//vaovao
                
                labelMonth.setSelectedIndex(month - 1);
                labelNom.setText((String) model.getValueAt(row, 1));
                labelPrenom.setText((String) model.getValueAt(row, 2));
                labelPoste.setText((String) model.getValueAt(row, 3));
                labelAbs.setText(String.valueOf(mPointageD.nombreAbs(year, month, numemp)));
                
                //----------------------TALOHA--------------------------------
//                labelMontant.setText(String.valueOf(montant) + " Ar ");
//                labelMontantConverti.setText("( " +Conversion.convertir(montant)+ " Ariary )");

                labelMontant.setText(String.valueOf(montant - (abs*10000)) + " Ar ");//vaovao
                labelMontantConverti.setText("( " +Conversion.convertir(montant - (abs*10000))+ " Ariary )");//vaovao
                
                numeroCongeC.setText((String) model.getValueAt(row, 0));
                setLigne(row);
            }

        };
        tableEmploye.getColumnModel().getColumn(5).setCellRenderer(new TableActionCellRenderEmploye());
        tableEmploye.getColumnModel().getColumn(5).setCellEditor(new TableActionCellEditorEmploye(eventEmploye));
        
        /*--------------------------------------------------------------------------------------------------------------------
        -                                              ACTION POINTAGE
        ---------------------------------------------------------------------------------------------------------------------*/
        TableActionEvent eventPointage = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                try {
                    if( tablePointage.isEditing()){
                        tablePointage.getCellEditor().stopCellEditing();
                    }
                    cardPointageCE.show(cardCreationPointage, "editPointage");
                    dateTime = (String) modelPointage.getValueAt(row, 0);
                    numeroPointageE.setText((String) modelPointage.getValueAt(row, 1));
                    checkBoxPointageE.setSelected(test((String) modelPointage.getValueAt(row, 2)));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Edit Pointage ::: " + e.getMessage());
                }

            }

            @Override
            public void onDelete(int row) {
                try {
                    if( tablePointage.isEditing()){
                        tablePointage.getCellEditor().stopCellEditing();
                    }
                    cardPointageCE.show(cardCreationPointage, "createPointage");
                    mPointage.setDate((String) modelPointage.getValueAt(row, 0));
                    mPointage.setPointage((String) modelPointage.getValueAt(row, 2));
                    mPointage.setNumero((String) modelPointage.getValueAt(row, 1));
                    int confirm = JOptionPane.showConfirmDialog(prenomEmployeC, "êtes-vous sûr de voulouir supprimer cette ligne ");
                    if (confirm == 0) {
                        try {
                            mPointageD.deleteSingle(mPointage);
                            panel = new Notification(frame, mPointageD.getType(), Notification.Location.BOTTOM_LEFT, mPointageD.message);
                            panel.showNotification();
                            
                            affichePointage();
                        } catch (Exception e) {
                            System.err.println("Error -xxx- ::: "+e.getMessage() +"  "+e.getStackTrace());
                        }
                    }
                    
                
                } catch (Error e) {
                    System.out.println("Delete Pointage ::: " + e.getStackTrace());
                }
            }
        };
        tablePointage.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRender());
        tablePointage.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(eventPointage));
        /*--------------------------------------------------------------------------------------------------------------------
        -                                              ACTION ABSENCE
        ---------------------------------------------------------------------------------------------------------------------*/
        TableActionEvent eventAbsence = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                try {
                    if( tableNonPointe.isEditing()){
                        tableNonPointe.getCellEditor().stopCellEditing();
                    }
                    cardPointageCE.show(cardCreationPointage, "editPointage");
                    dateTime = (String) modelAbsence.getValueAt(row, 0);
                    numeroPointageE.setText((String) modelAbsence.getValueAt(row, 1));
                    checkBoxPointageE.setSelected(test((String) modelAbsence.getValueAt(row, 2)));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Edit Absence ::: " + e.getMessage());
                }

            }

            @Override
            public void onDelete(int row) {
                try {
                    if( tableNonPointe.isEditing()){
                        tableNonPointe.getCellEditor().stopCellEditing();
                    }
                    cardPointageCE.show(cardCreationPointage, "createPointage");
                    mPointage.setDate((String) modelAbsence.getValueAt(row, 0));
                    mPointage.setPointage((String) modelAbsence.getValueAt(row, 2));
                    mPointage.setNumero((String) modelAbsence.getValueAt(row, 1));

                    int confirm = JOptionPane.showConfirmDialog(prenomEmployeC, "êtes-vous sûr de voulouir supprimer cette ligne ");
                    if (confirm == 0) {
                        try {
                            mPointageD.deleteSingle(mPointage);
                            panel = new Notification(frame, mPointageD.getType(), Notification.Location.BOTTOM_LEFT, mPointageD.message);
                            panel.showNotification();
                            afficheAbs(date);

                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Delete Absence ::: " + e.getMessage());
                }
            }
        };
        tableNonPointe.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRender());
        tableNonPointe.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(eventAbsence));
        /*--------------------------------------------------------------------------------------------------------------------
        -                                              ACTION CONGE
        ---------------------------------------------------------------------------------------------------------------------*/
        TableActionEvent eventConge = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                try {
                    if( tableConge.isEditing()){
                        tableConge.getCellEditor().stopCellEditing();
                    }
                    cardCongeCE.show(cardCongeCEE, "editConge");
                    numeroCongeE.setText((String) modelConge.getValueAt(row, 0));
                    String item = (String) modelConge.getValueAt(row, 1);

                    for (int i = 0; i < numeroEmployeCongeE.getItemCount(); i++) {
                        if (item.equals((String) numeroEmployeCongeE.getItemAt(i))) {
                            numeroEmployeCongeE.setSelectedIndex(i);
                        }
                    }
                    motifCongeE.setText((String) modelConge.getValueAt(row, 2));
                    numeroEmployeCongeE.setSelectedItem(numeroEmployeCongeE.getItemCount());
                    jourCongeE.setText((String) modelConge.getValueAt(row, 3).toString().trim());
                    demandeCongeE.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(modelConge.getValueAt(row, 4).toString()));
                    afficheConge();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Edit Conge ::: " + e.getMessage());
                }
            }

            @Override
            public void onDelete(int row) {
                try {
                    if( tableConge.isEditing()){
                        tableConge.getCellEditor().stopCellEditing();
                    }
                    actualiserConge();
                    cardCongeCE.show(cardCongeCEE, "createConge");
                    mConge.setNumConge((String) modelConge.getValueAt(row, 0));
                    int confirm = JOptionPane.showConfirmDialog(null, "êtes-vous sûr de voulouir supprimer cette ligne ");
                    if (confirm == 0) {
                        mCongeD.deleteSingle(mConge);
                        panel = new Notification(frame, mCongeD.getType(), Notification.Location.BOTTOM_LEFT, mCongeD.message);
                        panel.showNotification();
                        afficheConge();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Delete Conge :: " + e.getMessage());
                }
            }
        };
        tableConge.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        tableConge.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor(eventConge));

        afficheEmploye();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        menu = new javax.swing.JPanel();
        btn_employe = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btn_pointage = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btn_conge = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cardPanel = new javax.swing.JPanel();
        cardEmploye = new javax.swing.JPanel();
        cardCreationEmploye = new com.k33ptoo.components.KGradientPanel();
        createEmploye = new com.k33ptoo.components.KGradientPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        btnCreateEmploye = new com.k33ptoo.components.KButton();
        jLabel46 = new javax.swing.JLabel();
        salaireEmployeC = new javax.swing.JTextField();
        posteEmployeC = new javax.swing.JTextField();
        prenomEmployeC = new javax.swing.JTextField();
        nomEmployeC = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        numeroEmployeC = new javax.swing.JTextField();
        messageCreateEmploye = new javax.swing.JLabel();
        editEmploye = new com.k33ptoo.components.KGradientPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        nomEmployeE = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        prenomEmployeE = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        posteEmployeE = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        returnToCreation = new com.k33ptoo.components.KButton();
        modificationEmploye = new com.k33ptoo.components.KButton();
        jLabel18 = new javax.swing.JLabel();
        salaireEmployeE = new javax.swing.JTextField();
        numeroEmployeE = new javax.swing.JLabel();
        messageEditEmploye = new javax.swing.JLabel();
        fichePaie = new com.k33ptoo.components.KGradientPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        returnToCreation1 = new com.k33ptoo.components.KButton();
        generatePDF = new com.k33ptoo.components.KButton();
        jLabel28 = new javax.swing.JLabel();
        messageEditEmploye1 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        labelMontant = new javax.swing.JLabel();
        labelAbs = new javax.swing.JLabel();
        labelPoste = new javax.swing.JLabel();
        labelPrenom = new javax.swing.JLabel();
        labelNom = new javax.swing.JLabel();
        labelMonth = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        labelMontantConverti = new javax.swing.JTextPane();
        kGradientPanel1 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel2 = new com.k33ptoo.components.KGradientPanel();
        jLabel4 = new javax.swing.JLabel();
        rechercheEmploye = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableEmploye = new com.gestion.component.Table();
        cardPointage = new javax.swing.JPanel();
        cardCreationPointage = new com.k33ptoo.components.KGradientPanel();
        createPointage = new com.k33ptoo.components.KGradientPanel();
        btnCreatePointage = new com.k33ptoo.components.KButton();
        jLabel35 = new javax.swing.JLabel();
        messageCreatePointage = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablePointageC = new com.gestion.component.Table();
        editPointage = new com.k33ptoo.components.KGradientPanel();
        jLabel37 = new javax.swing.JLabel();
        numeroPointageE = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        returnToPointageCreation = new com.k33ptoo.components.KButton();
        modificationPointage = new com.k33ptoo.components.KButton();
        checkBoxPointageE = new javax.swing.JCheckBox();
        messageEditPointage = new javax.swing.JLabel();
        cardListPointage = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel9 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel10 = new com.k33ptoo.components.KGradientPanel();
        jLabel31 = new javax.swing.JLabel();
        btnListAbsent = new com.k33ptoo.components.KButton();
        dateAbs = new com.toedter.calendar.JDateChooser();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablePointage = new com.gestion.component.Table();
        kGradientPanel7 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel8 = new com.k33ptoo.components.KGradientPanel();
        jLabel6 = new javax.swing.JLabel();
        btnListPointage = new com.k33ptoo.components.KButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableNonPointe = new com.gestion.component.Table();
        cardConge = new javax.swing.JPanel();
        cardCongeCEE = new com.k33ptoo.components.KGradientPanel();
        createConge = new com.k33ptoo.components.KGradientPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        btnCreateConge = new com.k33ptoo.components.KButton();
        jLabel52 = new javax.swing.JLabel();
        jourCongeC = new javax.swing.JTextField();
        motifCongeC = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        numeroCongeC = new javax.swing.JTextField();
        messageCreateConge = new javax.swing.JLabel();
        numeroEmployeCongeC = new javax.swing.JComboBox<>();
        demandeCongeC = new com.toedter.calendar.JDateChooser();
        editConge = new com.k33ptoo.components.KGradientPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        motifCongeE = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jourCongeE = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        returnToCongeCreation = new com.k33ptoo.components.KButton();
        modificationConge = new com.k33ptoo.components.KButton();
        numeroCongeE = new javax.swing.JLabel();
        messageEditCongeE = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        demandeCongeE = new com.toedter.calendar.JDateChooser();
        numeroEmployeCongeE = new javax.swing.JComboBox<>();
        kGradientPanel3 = new com.k33ptoo.components.KGradientPanel();
        kGradientPanel4 = new com.k33ptoo.components.KGradientPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableConge = new com.gestion.component.Table();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GESTION DE POINTAGE");
        setMinimumSize(new java.awt.Dimension(1920, 1080));

        jSplitPane1.setDividerSize(0);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        menu.setBackground(java.awt.Color.white);
        menu.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)));
        menu.setMinimumSize(new java.awt.Dimension(1900, 55));
        menu.setPreferredSize(new java.awt.Dimension(1900, 55));

        btn_employe.setBackground(new java.awt.Color(237, 247, 255));
        btn_employe.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(0, 157, 226)));
        btn_employe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_employeMousePressed(evt);
            }
        });
        btn_employe.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel1.setForeground(java.awt.Color.black);
        jLabel1.setText("Employe");
        btn_employe.add(jLabel1, new java.awt.GridBagConstraints());

        btn_pointage.setBackground(java.awt.Color.white);
        btn_pointage.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)));
        btn_pointage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_pointageMousePressed(evt);
            }
        });
        btn_pointage.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel2.setForeground(java.awt.Color.black);
        jLabel2.setText("Pointage");
        btn_pointage.add(jLabel2, new java.awt.GridBagConstraints());

        btn_conge.setBackground(java.awt.Color.white);
        btn_conge.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)));
        btn_conge.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_congeMousePressed(evt);
            }
        });
        btn_conge.setLayout(new java.awt.GridBagLayout());

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel3.setForeground(java.awt.Color.black);
        jLabel3.setText("Conge");
        btn_conge.add(jLabel3, new java.awt.GridBagConstraints());

        javax.swing.GroupLayout menuLayout = new javax.swing.GroupLayout(menu);
        menu.setLayout(menuLayout);
        menuLayout.setHorizontalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLayout.createSequentialGroup()
                .addComponent(btn_employe, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btn_pointage, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btn_conge, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1317, Short.MAX_VALUE))
        );
        menuLayout.setVerticalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLayout.createSequentialGroup()
                .addGroup(menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_employe, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_pointage, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_conge, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jSplitPane1.setTopComponent(menu);

        cardPanel.setLayout(new java.awt.CardLayout());

        cardEmploye.setBackground(new java.awt.Color(244, 244, 244));

        cardCreationEmploye.setBackground(new java.awt.Color(0, 153, 153));
        cardCreationEmploye.setInheritsPopupMenu(true);
        cardCreationEmploye.setkBorderRadius(20);
        cardCreationEmploye.setkEndColor(java.awt.Color.white);
        cardCreationEmploye.setkStartColor(java.awt.Color.white);
        cardCreationEmploye.setLayout(new java.awt.CardLayout());

        createEmploye.setBackground(new java.awt.Color(244, 244, 244));
        createEmploye.setkEndColor(new java.awt.Color(255, 255, 255));
        createEmploye.setkGradientFocus(200);
        createEmploye.setkStartColor(new java.awt.Color(255, 255, 255));
        createEmploye.setMaximumSize(new java.awt.Dimension(408, 581));
        createEmploye.setMinimumSize(new java.awt.Dimension(408, 581));

        jLabel42.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel42.setForeground(java.awt.Color.black);
        jLabel42.setText("Nom :");

        jLabel43.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel43.setForeground(java.awt.Color.black);
        jLabel43.setText("Prenom :");

        jLabel44.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel44.setForeground(java.awt.Color.black);
        jLabel44.setText("Poste :");

        jLabel45.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel45.setForeground(java.awt.Color.black);
        jLabel45.setText("Salaire :");

        btnCreateEmploye.setText("ENREGISTRER");
        btnCreateEmploye.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnCreateEmploye.setkAllowGradient(false);
        btnCreateEmploye.setkBackGroundColor(new java.awt.Color(0, 157, 226));
        btnCreateEmploye.setkEndColor(new java.awt.Color(0, 157, 226));
        btnCreateEmploye.setkHoverColor(new java.awt.Color(7, 110, 155));
        btnCreateEmploye.setkHoverEndColor(new java.awt.Color(0, 157, 226));
        btnCreateEmploye.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnCreateEmploye.setkHoverStartColor(new java.awt.Color(0, 157, 226));
        btnCreateEmploye.setkPressedColor(new java.awt.Color(0, 157, 226));
        btnCreateEmploye.setkSelectedColor(new java.awt.Color(0, 157, 226));
        btnCreateEmploye.setkStartColor(new java.awt.Color(0, 157, 226));
        btnCreateEmploye.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateEmployeActionPerformed(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel46.setForeground(java.awt.Color.black);
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("CREATION");

        salaireEmployeC.setBackground(new Color(0,0,0,0));
        salaireEmployeC.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        salaireEmployeC.setActionCommand("<Not Set>");
        salaireEmployeC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));
        salaireEmployeC.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        posteEmployeC.setBackground(new Color(0,0,0,0));
        posteEmployeC.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        posteEmployeC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        prenomEmployeC.setBackground(new Color(0,0,0,0));
        prenomEmployeC.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        prenomEmployeC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        nomEmployeC.setBackground(new Color(0,0,0,0));
        nomEmployeC.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        nomEmployeC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        jLabel47.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel47.setForeground(java.awt.Color.black);
        jLabel47.setText("Numero :");

        numeroEmployeC.setBackground(new Color(0,0,0,0));
        numeroEmployeC.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        numeroEmployeC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        messageCreateEmploye.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout createEmployeLayout = new javax.swing.GroupLayout(createEmploye);
        createEmploye.setLayout(createEmployeLayout);
        createEmployeLayout.setHorizontalGroup(
            createEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createEmployeLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(createEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCreateEmploye, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                    .addGroup(createEmployeLayout.createSequentialGroup()
                        .addGroup(createEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(createEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
                            .addGroup(createEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(35, 35, 35)
                        .addGroup(createEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(prenomEmployeC)
                            .addComponent(salaireEmployeC)
                            .addComponent(posteEmployeC)
                            .addComponent(nomEmployeC)
                            .addComponent(numeroEmployeC, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(messageCreateEmploye, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(46, 46, 46))
        );
        createEmployeLayout.setVerticalGroup(
            createEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createEmployeLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addGroup(createEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(numeroEmployeC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(createEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(nomEmployeC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(createEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(prenomEmployeC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(createEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(posteEmployeC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(createEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(salaireEmployeC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                .addComponent(btnCreateEmploye, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(messageCreateEmploye)
                .addGap(61, 61, 61))
        );

        cardCreationEmploye.add(createEmploye, "createEmploye");

        editEmploye.setBackground(new java.awt.Color(244, 244, 244));
        editEmploye.setkEndColor(new java.awt.Color(255, 255, 255));
        editEmploye.setkStartColor(new java.awt.Color(255, 255, 255));
        editEmploye.setMaximumSize(new java.awt.Dimension(408, 581));
        editEmploye.setMinimumSize(new java.awt.Dimension(408, 581));

        jLabel11.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("MODIFICATION");

        jLabel12.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel12.setText("Numero :");

        nomEmployeE.setBackground(new Color(0,0,0,0));
        nomEmployeE.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        nomEmployeE.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        jLabel13.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel13.setText("Nom");

        prenomEmployeE.setBackground(new Color(0,0,0,0));
        prenomEmployeE.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        prenomEmployeE.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        jLabel14.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel14.setText("Prenom");

        posteEmployeE.setBackground(new Color(0,0,0,0));
        posteEmployeE.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        posteEmployeE.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        jLabel15.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel15.setText("Poste");

        returnToCreation.setText("RETOUR");
        returnToCreation.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        returnToCreation.setkAllowGradient(false);
        returnToCreation.setkBackGroundColor(new java.awt.Color(0, 157, 226));
        returnToCreation.setkEndColor(new java.awt.Color(0, 157, 226));
        returnToCreation.setkHoverColor(new java.awt.Color(7, 110, 155));
        returnToCreation.setkHoverEndColor(new java.awt.Color(0, 157, 226));
        returnToCreation.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        returnToCreation.setkHoverStartColor(new java.awt.Color(0, 157, 226));
        returnToCreation.setkPressedColor(new java.awt.Color(0, 157, 226));
        returnToCreation.setkSelectedColor(new java.awt.Color(0, 157, 226));
        returnToCreation.setkStartColor(new java.awt.Color(0, 157, 226));
        returnToCreation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToCreationActionPerformed(evt);
            }
        });

        modificationEmploye.setText("MODIFIER");
        modificationEmploye.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        modificationEmploye.setkAllowGradient(false);
        modificationEmploye.setkBackGroundColor(new java.awt.Color(0, 157, 226));
        modificationEmploye.setkEndColor(new java.awt.Color(0, 157, 226));
        modificationEmploye.setkHoverColor(new java.awt.Color(7, 110, 155));
        modificationEmploye.setkHoverEndColor(new java.awt.Color(0, 157, 226));
        modificationEmploye.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        modificationEmploye.setkHoverStartColor(new java.awt.Color(0, 157, 226));
        modificationEmploye.setkPressedColor(new java.awt.Color(0, 157, 226));
        modificationEmploye.setkSelectedColor(new java.awt.Color(0, 157, 226));
        modificationEmploye.setkStartColor(new java.awt.Color(0, 157, 226));
        modificationEmploye.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificationEmployeActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel18.setText("Salaire :");

        salaireEmployeE.setBackground(new Color(0,0,0,0));
        salaireEmployeE.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        salaireEmployeE.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        numeroEmployeE.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        numeroEmployeE.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        messageEditEmploye.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout editEmployeLayout = new javax.swing.GroupLayout(editEmploye);
        editEmploye.setLayout(editEmployeLayout);
        editEmployeLayout.setHorizontalGroup(
            editEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editEmployeLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(editEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editEmployeLayout.createSequentialGroup()
                        .addGroup(editEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(editEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(posteEmployeE)
                            .addComponent(prenomEmployeE)
                            .addComponent(nomEmployeE)
                            .addComponent(salaireEmployeE)
                            .addComponent(numeroEmployeE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editEmployeLayout.createSequentialGroup()
                        .addComponent(returnToCreation, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modificationEmploye, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(messageEditEmploye, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        editEmployeLayout.setVerticalGroup(
            editEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editEmployeLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addGroup(editEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(numeroEmployeE, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(editEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(nomEmployeE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(editEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(prenomEmployeE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(editEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(posteEmployeE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(editEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(salaireEmployeE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                .addGroup(editEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnToCreation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modificationEmploye, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(messageEditEmploye)
                .addGap(55, 55, 55))
        );

        cardCreationEmploye.add(editEmploye, "editEmploye");

        fichePaie.setBackground(new java.awt.Color(244, 244, 244));
        fichePaie.setkEndColor(new java.awt.Color(255, 255, 255));
        fichePaie.setkStartColor(new java.awt.Color(255, 255, 255));
        fichePaie.setMaximumSize(new java.awt.Dimension(408, 581));
        fichePaie.setMinimumSize(new java.awt.Dimension(408, 581));
        fichePaie.setPreferredSize(new java.awt.Dimension(408, 581));

        jLabel23.setBackground(new java.awt.Color(244, 244, 244));
        jLabel23.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel23.setForeground(java.awt.Color.black);
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("FICHE PAIE");

        jLabel24.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel24.setForeground(java.awt.Color.black);
        jLabel24.setText("Date                              :");

        jLabel25.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel25.setForeground(java.awt.Color.black);
        jLabel25.setText("Nom                              :");

        jLabel26.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel26.setForeground(java.awt.Color.black);
        jLabel26.setText("Prénom                        :");

        jLabel27.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel27.setForeground(java.awt.Color.black);
        jLabel27.setText("Poste                            :");

        returnToCreation1.setText("RETOUR");
        returnToCreation1.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        returnToCreation1.setkAllowGradient(false);
        returnToCreation1.setkBackGroundColor(new java.awt.Color(0, 157, 226));
        returnToCreation1.setkEndColor(new java.awt.Color(0, 157, 226));
        returnToCreation1.setkHoverColor(new java.awt.Color(7, 110, 155));
        returnToCreation1.setkHoverEndColor(new java.awt.Color(0, 157, 226));
        returnToCreation1.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        returnToCreation1.setkHoverStartColor(new java.awt.Color(0, 157, 226));
        returnToCreation1.setkPressedColor(new java.awt.Color(0, 157, 226));
        returnToCreation1.setkSelectedColor(new java.awt.Color(0, 157, 226));
        returnToCreation1.setkStartColor(new java.awt.Color(0, 157, 226));
        returnToCreation1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToCreation1ActionPerformed(evt);
            }
        });

        generatePDF.setText("PDF");
        generatePDF.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        generatePDF.setkAllowGradient(false);
        generatePDF.setkBackGroundColor(new java.awt.Color(0, 157, 226));
        generatePDF.setkEndColor(new java.awt.Color(0, 157, 226));
        generatePDF.setkHoverColor(new java.awt.Color(7, 110, 155));
        generatePDF.setkHoverEndColor(new java.awt.Color(0, 157, 226));
        generatePDF.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        generatePDF.setkHoverStartColor(new java.awt.Color(0, 157, 226));
        generatePDF.setkPressedColor(new java.awt.Color(0, 157, 226));
        generatePDF.setkSelectedColor(new java.awt.Color(0, 157, 226));
        generatePDF.setkStartColor(new java.awt.Color(0, 157, 226));
        generatePDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generatePDFActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel28.setForeground(java.awt.Color.black);
        jLabel28.setText("Nombre d'absence  :");

        messageEditEmploye1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel29.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel29.setForeground(java.awt.Color.black);
        jLabel29.setText("Montant                      :");

        labelMontant.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelMontant.setForeground(java.awt.Color.black);
        labelMontant.setText("Montant                      :");

        labelAbs.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelAbs.setForeground(java.awt.Color.black);
        labelAbs.setText("Montant                      :");

        labelPoste.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelPoste.setForeground(java.awt.Color.black);
        labelPoste.setText("Montant                      :");

        labelPrenom.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelPrenom.setForeground(java.awt.Color.black);
        labelPrenom.setText("Montant                      :");

        labelNom.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        labelNom.setForeground(java.awt.Color.black);
        labelNom.setText("Montant                      :");

        labelMonth.setBackground(new Color(0,0,0,0));
        labelMonth.setForeground(java.awt.Color.black);
        labelMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet ", "Août", "Septembre", "Octobre", "Novembre", "Decembre" }));
        labelMonth.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 157, 226)));
        labelMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labelMonthActionPerformed(evt);
            }
        });

        labelMontantConverti.setEditable(false);
        labelMontantConverti.setBackground(java.awt.Color.white);
        labelMontantConverti.setBorder(null);
        labelMontantConverti.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        labelMontantConverti.setSelectedTextColor(new Color(0,0,0,0));
        labelMontantConverti.setSelectionColor(new Color(0,0,0,0));
        jScrollPane2.setViewportView(labelMontantConverti);

        javax.swing.GroupLayout fichePaieLayout = new javax.swing.GroupLayout(fichePaie);
        fichePaie.setLayout(fichePaieLayout);
        fichePaieLayout.setHorizontalGroup(
            fichePaieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fichePaieLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(fichePaieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(fichePaieLayout.createSequentialGroup()
                        .addGroup(fichePaieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(fichePaieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelMontant, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelAbs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelPoste, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelPrenom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelNom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelMonth, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(fichePaieLayout.createSequentialGroup()
                        .addComponent(returnToCreation1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generatePDF, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))
                    .addComponent(messageEditEmploye1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(49, 49, 49))
        );
        fichePaieLayout.setVerticalGroup(
            fichePaieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fichePaieLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(fichePaieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(labelMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(fichePaieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(labelNom))
                .addGap(32, 32, 32)
                .addGroup(fichePaieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(labelPrenom))
                .addGap(32, 32, 32)
                .addGroup(fichePaieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(labelPoste))
                .addGap(32, 32, 32)
                .addGroup(fichePaieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(labelAbs))
                .addGap(32, 32, 32)
                .addGroup(fichePaieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(labelMontant))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(fichePaieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnToCreation1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generatePDF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(messageEditEmploye1)
                .addGap(55, 55, 55))
        );

        cardCreationEmploye.add(fichePaie, "fichePaie");

        kGradientPanel1.setBackground(new java.awt.Color(244, 244, 244));
        kGradientPanel1.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel1.setkStartColor(new java.awt.Color(255, 255, 255));

        kGradientPanel2.setBackground(java.awt.Color.white);
        kGradientPanel2.setkEndColor(new java.awt.Color(0, 157, 226));
        kGradientPanel2.setkFillBackground(false);
        kGradientPanel2.setkGradientFocus(2000);
        kGradientPanel2.setkStartColor(new java.awt.Color(0, 157, 226));

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel4.setForeground(java.awt.Color.black);
        jLabel4.setText("LISTE DES EMPLOYES");

        rechercheEmploye.setBackground(new java.awt.Color(249, 249, 249));
        rechercheEmploye.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        rechercheEmploye.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        rechercheEmploye.setToolTipText("Nom ou Prenom à chercher");
        rechercheEmploye.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));
        rechercheEmploye.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rechercheEmployeKeyReleased(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gestion/icon/recherche.png"))); // NOI18N

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(454, 454, 454)
                .addComponent(rechercheEmploye, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(rechercheEmploye, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 157, 226)));

        tableEmploye.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Numero", "Nom", "Prenom", "Poste", "Salaire", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableEmploye);
        if (tableEmploye.getColumnModel().getColumnCount() > 0) {
            tableEmploye.getColumnModel().getColumn(5).setMinWidth(207);
            tableEmploye.getColumnModel().getColumn(5).setPreferredWidth(207);
            tableEmploye.getColumnModel().getColumn(5).setMaxWidth(207);
        }

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(kGradientPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addGap(86, 86, 86))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(kGradientPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout cardEmployeLayout = new javax.swing.GroupLayout(cardEmploye);
        cardEmploye.setLayout(cardEmployeLayout);
        cardEmployeLayout.setHorizontalGroup(
            cardEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardEmployeLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(cardCreationEmploye, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        cardEmployeLayout.setVerticalGroup(
            cardEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardEmployeLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(cardEmployeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardCreationEmploye, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(179, Short.MAX_VALUE))
        );

        cardPanel.add(cardEmploye, "cardEmploye");
        cardEmploye.getAccessibleContext().setAccessibleName("cardEmploye");

        cardPointage.setBackground(new java.awt.Color(244, 244, 244));

        cardCreationPointage.setBackground(new java.awt.Color(0, 153, 153));
        cardCreationPointage.setInheritsPopupMenu(true);
        cardCreationPointage.setkBorderRadius(20);
        cardCreationPointage.setkEndColor(java.awt.Color.white);
        cardCreationPointage.setkStartColor(java.awt.Color.white);
        cardCreationPointage.setMaximumSize(new java.awt.Dimension(408, 2147483647));
        cardCreationPointage.setPreferredSize(new java.awt.Dimension(408, 946));
        cardCreationPointage.setLayout(new java.awt.CardLayout());

        createPointage.setBackground(new java.awt.Color(244, 244, 244));
        createPointage.setkEndColor(new java.awt.Color(255, 255, 255));
        createPointage.setkGradientFocus(200);
        createPointage.setkStartColor(new java.awt.Color(255, 255, 255));
        createPointage.setMinimumSize(new java.awt.Dimension(408, 0));
        createPointage.setPreferredSize(new java.awt.Dimension(408, 946));

        btnCreatePointage.setText("ENREGISTRER");
        btnCreatePointage.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnCreatePointage.setkAllowGradient(false);
        btnCreatePointage.setkBackGroundColor(new java.awt.Color(0, 157, 226));
        btnCreatePointage.setkEndColor(new java.awt.Color(0, 157, 226));
        btnCreatePointage.setkHoverColor(new java.awt.Color(7, 110, 155));
        btnCreatePointage.setkHoverEndColor(new java.awt.Color(0, 157, 226));
        btnCreatePointage.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnCreatePointage.setkHoverStartColor(new java.awt.Color(0, 157, 226));
        btnCreatePointage.setkPressedColor(new java.awt.Color(0, 157, 226));
        btnCreatePointage.setkSelectedColor(new java.awt.Color(0, 157, 226));
        btnCreatePointage.setkStartColor(new java.awt.Color(0, 157, 226));
        btnCreatePointage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreatePointageActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel35.setForeground(java.awt.Color.black);
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("CREATION");

        messageCreatePointage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jScrollPane7.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 157, 226)));

        tablePointageC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Numero", "Pointé"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(tablePointageC);

        javax.swing.GroupLayout createPointageLayout = new javax.swing.GroupLayout(createPointage);
        createPointage.setLayout(createPointageLayout);
        createPointageLayout.setHorizontalGroup(
            createPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createPointageLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(createPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(messageCreatePointage, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCreatePointage, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        createPointageLayout.setVerticalGroup(
            createPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createPointageLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 709, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCreatePointage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(messageCreatePointage)
                .addGap(61, 61, 61))
        );

        cardCreationPointage.add(createPointage, "createPointage");

        editPointage.setBackground(new java.awt.Color(244, 244, 244));
        editPointage.setkEndColor(java.awt.Color.white);
        editPointage.setkStartColor(java.awt.Color.white);
        editPointage.setMaximumSize(new java.awt.Dimension(408, 32767));
        editPointage.setMinimumSize(new java.awt.Dimension(408, 0));

        jLabel37.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel37.setForeground(java.awt.Color.black);
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("MODIFICATION");

        numeroPointageE.setBackground(new Color(0,0,0,0));
        numeroPointageE.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        numeroPointageE.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        jLabel39.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel39.setForeground(java.awt.Color.black);
        jLabel39.setText("N° Employe :");

        jLabel40.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel40.setForeground(java.awt.Color.black);
        jLabel40.setText("Pointé :");

        returnToPointageCreation.setText("RETOUR");
        returnToPointageCreation.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        returnToPointageCreation.setkAllowGradient(false);
        returnToPointageCreation.setkBackGroundColor(new java.awt.Color(0, 157, 226));
        returnToPointageCreation.setkEndColor(new java.awt.Color(0, 157, 226));
        returnToPointageCreation.setkHoverColor(new java.awt.Color(7, 110, 155));
        returnToPointageCreation.setkHoverEndColor(new java.awt.Color(0, 157, 226));
        returnToPointageCreation.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        returnToPointageCreation.setkHoverStartColor(new java.awt.Color(0, 157, 226));
        returnToPointageCreation.setkPressedColor(new java.awt.Color(0, 157, 226));
        returnToPointageCreation.setkSelectedColor(new java.awt.Color(0, 157, 226));
        returnToPointageCreation.setkStartColor(new java.awt.Color(0, 157, 226));
        returnToPointageCreation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToPointageCreationActionPerformed(evt);
            }
        });

        modificationPointage.setText("MODIFIER");
        modificationPointage.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        modificationPointage.setkAllowGradient(false);
        modificationPointage.setkBackGroundColor(new java.awt.Color(0, 157, 226));
        modificationPointage.setkEndColor(new java.awt.Color(0, 157, 226));
        modificationPointage.setkHoverColor(new java.awt.Color(7, 110, 155));
        modificationPointage.setkHoverEndColor(new java.awt.Color(0, 157, 226));
        modificationPointage.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        modificationPointage.setkHoverStartColor(new java.awt.Color(0, 157, 226));
        modificationPointage.setkPressedColor(new java.awt.Color(0, 157, 226));
        modificationPointage.setkSelectedColor(new java.awt.Color(0, 157, 226));
        modificationPointage.setkStartColor(new java.awt.Color(0, 157, 226));
        modificationPointage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificationPointageActionPerformed(evt);
            }
        });

        messageEditPointage.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        messageEditPointage.setForeground(java.awt.Color.black);

        javax.swing.GroupLayout editPointageLayout = new javax.swing.GroupLayout(editPointage);
        editPointage.setLayout(editPointageLayout);
        editPointageLayout.setHorizontalGroup(
            editPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editPointageLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(editPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editPointageLayout.createSequentialGroup()
                        .addGroup(editPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(editPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(numeroPointageE)
                            .addGroup(editPointageLayout.createSequentialGroup()
                                .addComponent(checkBoxPointageE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editPointageLayout.createSequentialGroup()
                        .addComponent(returnToPointageCreation, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modificationPointage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(messageEditPointage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        editPointageLayout.setVerticalGroup(
            editPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editPointageLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(117, 117, 117)
                .addGroup(editPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(numeroPointageE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(editPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(checkBoxPointageE))
                .addGap(161, 161, 161)
                .addGroup(editPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnToPointageCreation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modificationPointage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(messageEditPointage)
                .addContainerGap(451, Short.MAX_VALUE))
        );

        cardCreationPointage.add(editPointage, "editPointage");

        cardListPointage.setBackground(new java.awt.Color(249, 249, 249));
        cardListPointage.setkEndColor(new java.awt.Color(222, 222, 222));
        cardListPointage.setkStartColor(new java.awt.Color(222, 222, 222));
        cardListPointage.setLayout(new java.awt.CardLayout());

        kGradientPanel9.setBackground(new java.awt.Color(244, 244, 244));
        kGradientPanel9.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel9.setkStartColor(new java.awt.Color(255, 255, 255));

        kGradientPanel10.setBackground(new java.awt.Color(255, 255, 255));
        kGradientPanel10.setkEndColor(new java.awt.Color(0, 157, 226));
        kGradientPanel10.setkFillBackground(false);
        kGradientPanel10.setkGradientFocus(1800);
        kGradientPanel10.setkStartColor(new java.awt.Color(0, 157, 226));

        jLabel31.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel31.setForeground(java.awt.Color.black);
        jLabel31.setText("LISTE POINTAGE");

        btnListAbsent.setText("ABSENTS");
        btnListAbsent.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnListAbsent.setkAllowGradient(false);
        btnListAbsent.setkBackGroundColor(new java.awt.Color(0, 157, 226));
        btnListAbsent.setkEndColor(new java.awt.Color(0, 157, 226));
        btnListAbsent.setkHoverColor(new java.awt.Color(7, 110, 155));
        btnListAbsent.setkHoverEndColor(new java.awt.Color(0, 157, 226));
        btnListAbsent.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnListAbsent.setkHoverStartColor(new java.awt.Color(0, 157, 226));
        btnListAbsent.setkPressedColor(new java.awt.Color(0, 157, 226));
        btnListAbsent.setkSelectedColor(new java.awt.Color(0, 157, 226));
        btnListAbsent.setkStartColor(new java.awt.Color(0, 157, 226));
        btnListAbsent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListAbsentActionPerformed(evt);
            }
        });

        dateAbs.setBackground(new java.awt.Color(255, 255, 255));
        dateAbs.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout kGradientPanel10Layout = new javax.swing.GroupLayout(kGradientPanel10);
        kGradientPanel10.setLayout(kGradientPanel10Layout);
        kGradientPanel10Layout.setHorizontalGroup(
            kGradientPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel10Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 646, Short.MAX_VALUE)
                .addComponent(dateAbs, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnListAbsent, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        kGradientPanel10Layout.setVerticalGroup(
            kGradientPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel10Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(kGradientPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnListAbsent, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(jLabel31)
                    .addComponent(dateAbs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jScrollPane3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 157, 226)));

        tablePointage.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date", "Employe N°", "Pointage", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tablePointage);
        if (tablePointage.getColumnModel().getColumnCount() > 0) {
            tablePointage.getColumnModel().getColumn(3).setMinWidth(162);
            tablePointage.getColumnModel().getColumn(3).setMaxWidth(162);
        }

        javax.swing.GroupLayout kGradientPanel9Layout = new javax.swing.GroupLayout(kGradientPanel9);
        kGradientPanel9.setLayout(kGradientPanel9Layout);
        kGradientPanel9Layout.setHorizontalGroup(
            kGradientPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel9Layout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .addGroup(kGradientPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kGradientPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86))
        );
        kGradientPanel9Layout.setVerticalGroup(
            kGradientPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel9Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(kGradientPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        cardListPointage.add(kGradientPanel9, "listePointage");

        kGradientPanel7.setBackground(new java.awt.Color(244, 244, 244));
        kGradientPanel7.setkEndColor(java.awt.Color.white);
        kGradientPanel7.setkStartColor(java.awt.Color.white);

        kGradientPanel8.setBackground(java.awt.Color.white);
        kGradientPanel8.setForeground(java.awt.Color.black);
        kGradientPanel8.setkEndColor(new java.awt.Color(0, 157, 226));
        kGradientPanel8.setkFillBackground(false);
        kGradientPanel8.setkGradientFocus(1800);
        kGradientPanel8.setkStartColor(new java.awt.Color(0, 157, 226));

        jLabel6.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel6.setText("LISTE DES ABSENTS");

        btnListPointage.setText("POINTAGES");
        btnListPointage.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnListPointage.setkAllowGradient(false);
        btnListPointage.setkBackGroundColor(new java.awt.Color(0, 157, 226));
        btnListPointage.setkEndColor(new java.awt.Color(0, 157, 226));
        btnListPointage.setkHoverColor(new java.awt.Color(7, 110, 155));
        btnListPointage.setkHoverEndColor(new java.awt.Color(0, 157, 226));
        btnListPointage.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnListPointage.setkHoverStartColor(new java.awt.Color(0, 157, 226));
        btnListPointage.setkPressedColor(new java.awt.Color(0, 157, 226));
        btnListPointage.setkSelectedColor(new java.awt.Color(0, 157, 226));
        btnListPointage.setkStartColor(new java.awt.Color(0, 157, 226));
        btnListPointage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListPointageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel8Layout = new javax.swing.GroupLayout(kGradientPanel8);
        kGradientPanel8.setLayout(kGradientPanel8Layout);
        kGradientPanel8Layout.setHorizontalGroup(
            kGradientPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel8Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 836, Short.MAX_VALUE)
                .addComponent(btnListPointage, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        kGradientPanel8Layout.setVerticalGroup(
            kGradientPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(kGradientPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnListPointage, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jScrollPane6.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 157, 226)));

        tableNonPointe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date", "Employe N°", "Pointage", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tableNonPointe);
        if (tableNonPointe.getColumnModel().getColumnCount() > 0) {
            tableNonPointe.getColumnModel().getColumn(3).setMinWidth(162);
            tableNonPointe.getColumnModel().getColumn(3).setMaxWidth(162);
        }

        javax.swing.GroupLayout kGradientPanel7Layout = new javax.swing.GroupLayout(kGradientPanel7);
        kGradientPanel7.setLayout(kGradientPanel7Layout);
        kGradientPanel7Layout.setHorizontalGroup(
            kGradientPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel7Layout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .addGroup(kGradientPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kGradientPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86))
        );
        kGradientPanel7Layout.setVerticalGroup(
            kGradientPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel7Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(kGradientPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        cardListPointage.add(kGradientPanel7, "listeNonPointe");

        javax.swing.GroupLayout cardPointageLayout = new javax.swing.GroupLayout(cardPointage);
        cardPointage.setLayout(cardPointageLayout);
        cardPointageLayout.setHorizontalGroup(
            cardPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPointageLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(cardCreationPointage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(cardListPointage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        cardPointageLayout.setVerticalGroup(
            cardPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardPointageLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(cardPointageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cardListPointage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardCreationPointage, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(179, Short.MAX_VALUE))
        );

        cardPanel.add(cardPointage, "cardPointage");
        cardPointage.getAccessibleContext().setAccessibleName("cardPointage");

        cardConge.setBackground(new java.awt.Color(244, 244, 244));

        cardCongeCEE.setBackground(new java.awt.Color(0, 153, 153));
        cardCongeCEE.setInheritsPopupMenu(true);
        cardCongeCEE.setkBorderRadius(20);
        cardCongeCEE.setkEndColor(java.awt.Color.white);
        cardCongeCEE.setkStartColor(java.awt.Color.white);
        cardCongeCEE.setLayout(new java.awt.CardLayout());

        createConge.setBackground(new java.awt.Color(244, 244, 244));
        createConge.setkEndColor(new java.awt.Color(255, 255, 255));
        createConge.setkGradientFocus(200);
        createConge.setkStartColor(new java.awt.Color(255, 255, 255));
        createConge.setMaximumSize(new java.awt.Dimension(408, 581));
        createConge.setMinimumSize(new java.awt.Dimension(408, 581));

        jLabel48.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel48.setForeground(java.awt.Color.black);
        jLabel48.setText("N°Emp      :");

        jLabel49.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel49.setForeground(java.awt.Color.black);
        jLabel49.setText("Motif         :");

        jLabel50.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel50.setForeground(java.awt.Color.black);
        jLabel50.setText("Nbr Jour  :");

        jLabel51.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel51.setForeground(java.awt.Color.black);
        jLabel51.setText("Demande :");

        btnCreateConge.setText("ENREGISTRER");
        btnCreateConge.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        btnCreateConge.setkAllowGradient(false);
        btnCreateConge.setkBackGroundColor(new java.awt.Color(0, 157, 226));
        btnCreateConge.setkEndColor(new java.awt.Color(0, 157, 226));
        btnCreateConge.setkHoverColor(new java.awt.Color(7, 110, 155));
        btnCreateConge.setkHoverEndColor(new java.awt.Color(0, 157, 226));
        btnCreateConge.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnCreateConge.setkHoverStartColor(new java.awt.Color(0, 157, 226));
        btnCreateConge.setkPressedColor(new java.awt.Color(0, 157, 226));
        btnCreateConge.setkSelectedColor(new java.awt.Color(0, 157, 226));
        btnCreateConge.setkStartColor(new java.awt.Color(0, 157, 226));
        btnCreateConge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateCongeActionPerformed(evt);
            }
        });

        jLabel52.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel52.setForeground(java.awt.Color.black);
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText("CREATION");

        jourCongeC.setBackground(new Color(0,0,0,0));
        jourCongeC.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jourCongeC.setActionCommand("<Not Set>");
        jourCongeC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        motifCongeC.setBackground(new Color(0,0,0,0));
        motifCongeC.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        motifCongeC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        jLabel53.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel53.setForeground(java.awt.Color.black);
        jLabel53.setText("N° Conge :");

        numeroCongeC.setBackground(new Color(0,0,0,0));
        numeroCongeC.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        numeroCongeC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        messageCreateConge.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        numeroEmployeCongeC.setBackground(new Color(0,0,0,0));
        numeroEmployeCongeC.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        demandeCongeC.setBackground(new java.awt.Color(255, 255, 255));
        demandeCongeC.setBorder(new javax.swing.border.MatteBorder(null));
        demandeCongeC.setDateFormatString("yyyy-MM-dd");
        demandeCongeC.setMinSelectableDate(new java.util.Date(-62135776721000L));

        javax.swing.GroupLayout createCongeLayout = new javax.swing.GroupLayout(createConge);
        createConge.setLayout(createCongeLayout);
        createCongeLayout.setHorizontalGroup(
            createCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createCongeLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(createCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCreateConge, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                    .addGroup(createCongeLayout.createSequentialGroup()
                        .addGroup(createCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(createCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                            .addGroup(createCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel53, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(35, 35, 35)
                        .addGroup(createCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(motifCongeC)
                            .addComponent(jourCongeC)
                            .addComponent(numeroCongeC, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(numeroEmployeCongeC, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(demandeCongeC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(messageCreateConge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(46, 46, 46))
        );
        createCongeLayout.setVerticalGroup(
            createCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createCongeLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addGroup(createCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(numeroCongeC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(createCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(numeroEmployeCongeC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(createCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(motifCongeC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(createCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(jourCongeC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(createCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel51)
                    .addComponent(demandeCongeC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                .addComponent(btnCreateConge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(messageCreateConge)
                .addGap(61, 61, 61))
        );

        cardCongeCEE.add(createConge, "createConge");

        editConge.setBackground(new java.awt.Color(244, 244, 244));
        editConge.setkEndColor(new java.awt.Color(255, 255, 255));
        editConge.setkStartColor(new java.awt.Color(255, 255, 255));
        editConge.setMaximumSize(new java.awt.Dimension(408, 581));
        editConge.setMinimumSize(new java.awt.Dimension(408, 581));
        editConge.setPreferredSize(new java.awt.Dimension(408, 581));

        jLabel16.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jLabel16.setForeground(java.awt.Color.black);
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("MODIFICATION");

        jLabel17.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel17.setForeground(java.awt.Color.black);
        jLabel17.setText("N° Conge :");

        jLabel19.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel19.setForeground(java.awt.Color.black);
        jLabel19.setText("N° Emp     :");

        motifCongeE.setBackground(new Color(0,0,0,0));
        motifCongeE.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        motifCongeE.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        jLabel20.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel20.setForeground(java.awt.Color.black);
        jLabel20.setText("Motif          :");

        jourCongeE.setBackground(new Color(0,0,0,0));
        jourCongeE.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        jourCongeE.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        jLabel21.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel21.setForeground(java.awt.Color.black);
        jLabel21.setText("Nbr Jour   :");

        returnToCongeCreation.setText("RETOUR");
        returnToCongeCreation.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        returnToCongeCreation.setkAllowGradient(false);
        returnToCongeCreation.setkBackGroundColor(new java.awt.Color(0, 157, 226));
        returnToCongeCreation.setkEndColor(new java.awt.Color(0, 157, 226));
        returnToCongeCreation.setkHoverColor(new java.awt.Color(7, 110, 155));
        returnToCongeCreation.setkHoverEndColor(new java.awt.Color(0, 157, 226));
        returnToCongeCreation.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        returnToCongeCreation.setkHoverStartColor(new java.awt.Color(0, 157, 226));
        returnToCongeCreation.setkPressedColor(new java.awt.Color(0, 157, 226));
        returnToCongeCreation.setkSelectedColor(new java.awt.Color(0, 157, 226));
        returnToCongeCreation.setkStartColor(new java.awt.Color(0, 157, 226));
        returnToCongeCreation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToCongeCreationActionPerformed(evt);
            }
        });

        modificationConge.setText("MODIFIER");
        modificationConge.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        modificationConge.setkAllowGradient(false);
        modificationConge.setkBackGroundColor(new java.awt.Color(0, 157, 226));
        modificationConge.setkEndColor(new java.awt.Color(0, 157, 226));
        modificationConge.setkHoverColor(new java.awt.Color(7, 110, 155));
        modificationConge.setkHoverEndColor(new java.awt.Color(0, 157, 226));
        modificationConge.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        modificationConge.setkHoverStartColor(new java.awt.Color(0, 157, 226));
        modificationConge.setkPressedColor(new java.awt.Color(0, 157, 226));
        modificationConge.setkSelectedColor(new java.awt.Color(0, 157, 226));
        modificationConge.setkStartColor(new java.awt.Color(0, 157, 226));
        modificationConge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificationCongeActionPerformed(evt);
            }
        });

        numeroCongeE.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        numeroCongeE.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        messageEditCongeE.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel22.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel22.setForeground(java.awt.Color.black);
        jLabel22.setText("Demande  :");

        demandeCongeE.setBackground(new java.awt.Color(255, 255, 255));
        demandeCongeE.setDateFormatString("yyyy-MM-dd");

        numeroEmployeCongeE.setBackground(new Color(0,0,0,0));
        numeroEmployeCongeE.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 157, 226)));

        javax.swing.GroupLayout editCongeLayout = new javax.swing.GroupLayout(editConge);
        editConge.setLayout(editCongeLayout);
        editCongeLayout.setHorizontalGroup(
            editCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editCongeLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(editCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editCongeLayout.createSequentialGroup()
                        .addComponent(returnToCongeCreation, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modificationConge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(messageEditCongeE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editCongeLayout.createSequentialGroup()
                        .addGroup(editCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(editCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jourCongeE)
                            .addComponent(motifCongeE)
                            .addComponent(numeroCongeE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(demandeCongeE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(numeroEmployeCongeE, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        editCongeLayout.setVerticalGroup(
            editCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editCongeLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addGroup(editCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(numeroCongeE, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(editCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(numeroEmployeCongeE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(editCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(motifCongeE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(editCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jourCongeE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(editCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(demandeCongeE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addGroup(editCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(returnToCongeCreation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modificationConge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(messageEditCongeE)
                .addGap(55, 55, 55))
        );

        cardCongeCEE.add(editConge, "editConge");

        kGradientPanel3.setBackground(new java.awt.Color(244, 244, 244));
        kGradientPanel3.setkEndColor(new java.awt.Color(255, 255, 255));
        kGradientPanel3.setkStartColor(new java.awt.Color(255, 255, 255));

        kGradientPanel4.setBackground(java.awt.Color.white);
        kGradientPanel4.setkEndColor(new java.awt.Color(0, 157, 226));
        kGradientPanel4.setkFillBackground(false);
        kGradientPanel4.setkGradientFocus(2000);
        kGradientPanel4.setkStartColor(new java.awt.Color(0, 157, 226));

        jLabel7.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel7.setForeground(java.awt.Color.black);
        jLabel7.setText("LISTE DES EMPLOYES EN CONGE");

        javax.swing.GroupLayout kGradientPanel4Layout = new javax.swing.GroupLayout(kGradientPanel4);
        kGradientPanel4.setLayout(kGradientPanel4Layout);
        kGradientPanel4Layout.setHorizontalGroup(
            kGradientPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(757, Short.MAX_VALUE))
        );
        kGradientPanel4Layout.setVerticalGroup(
            kGradientPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 157, 226)));

        tableConge.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "N° Conge", "N° Employe", "Motif", "Nombre de Jour", "Date Demande", "Date Retour", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tableConge);
        if (tableConge.getColumnModel().getColumnCount() > 0) {
            tableConge.getColumnModel().getColumn(6).setMinWidth(162);
            tableConge.getColumnModel().getColumn(6).setMaxWidth(162);
        }

        javax.swing.GroupLayout kGradientPanel3Layout = new javax.swing.GroupLayout(kGradientPanel3);
        kGradientPanel3.setLayout(kGradientPanel3Layout);
        kGradientPanel3Layout.setHorizontalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel3Layout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kGradientPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86))
        );
        kGradientPanel3Layout.setVerticalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(kGradientPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout cardCongeLayout = new javax.swing.GroupLayout(cardConge);
        cardConge.setLayout(cardCongeLayout);
        cardCongeLayout.setHorizontalGroup(
            cardCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardCongeLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(cardCongeCEE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(kGradientPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        cardCongeLayout.setVerticalGroup(
            cardCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardCongeLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(cardCongeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardCongeCEE, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kGradientPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(179, Short.MAX_VALUE))
        );

        cardPanel.add(cardConge, "cardConge");

        jSplitPane1.setRightComponent(cardPanel);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleName("body");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /*--------------------------------------------------------------------------------------------------------------------
    -
    -                                              BOUTON MENU
    -                                      
    ---------------------------------------------------------------------------------------------------------------------*/
    private void setLigne(int row){this.ligne = row;}
    //----------------------------------------Employe----------------------------------------
    private void btn_employeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_employeMousePressed
        actualiserEmploye();
        setColor(btn_employe);
        resetColor(btn_pointage);
        resetColor(btn_conge);
        cardLog.show(cardCreationEmploye, "createEmploye");
        cardLayout.show(cardPanel, "cardEmploye");

        afficheEmploye();
    }//GEN-LAST:event_btn_employeMousePressed
    //----------------------------------------Pointage----------------------------------------
    private void btn_pointageMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_pointageMousePressed
 
        setColor(btn_pointage);
        resetColor(btn_employe);
        resetColor(btn_conge);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString());
            dateAbs.setDate(date);
        } catch (Exception e) {
            System.out.println(e);
        }
        cardPointageCE.show(cardCreationPointage, "createPointage");
        cardLayout.show(cardPanel, "cardPointage");

        affichePointage();

    }//GEN-LAST:event_btn_pointageMousePressed
    //----------------------------------------Conge--------------------------------------------
    private void btn_congeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_congeMousePressed
        actualiserConge();
        ArrayList<ModelEmploye> listemploye = mEmployeD.lister();
        setColor(btn_conge);
        resetColor(btn_pointage);
        resetColor(btn_employe);
        cardCongeCE.show(cardCongeCEE, "createConge");
        cardLayout.show(cardPanel, "cardConge");

        numeroEmployeCongeC.removeAllItems();
        numeroEmployeCongeE.removeAllItems();
        for (int i = 0; i < listemploye.size(); i++) {
            numeroEmployeCongeC.addItem(listemploye.get(i).getNumero());
            numeroEmployeCongeE.addItem(listemploye.get(i).getNumero());
        }

        afficheConge();

    }//GEN-LAST:event_btn_congeMousePressed
    /*--------------------------------------------------------------------------------------------------------------------
    -
    -                                              POINTAGE
    -                                      
    ---------------------------------------------------------------------------------------------------------------------*/
    private void returnToPointageCreationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnToPointageCreationActionPerformed
        cardPointageCE.show(cardCreationPointage, "createPointage");
        affichePointage(); // pour actualiser la liste des numéros des employées
    }//GEN-LAST:event_returnToPointageCreationActionPerformed
    private void modificationPointageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificationPointageActionPerformed
        try {
            mPointage.setDate(dateTime);
            mPointage.setNumero((String) numeroPointageE.getText());
            mPointage.setPointage(pointe(checkBoxPointageE.isSelected()));
            mPointageD.modifier(mPointage);
            panel = new Notification(this, mPointageD.getType(), Notification.Location.BOTTOM_LEFT, mPointageD.message);
        } catch (Exception e) {
            System.err.println("Modification Pointage ::: "+ e.getMessage());
        }
        panel.showNotification();
        affichePointage();
        afficheAbs(date);
    }//GEN-LAST:event_modificationPointageActionPerformed
    private void btnCreatePointageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreatePointageActionPerformed
        try {

            DefaultTableModel tableCreation = (DefaultTableModel) tablePointageC.getModel();
            mPointage.setDate(String.valueOf(LocalDateTime.now()));
            //Enregistrement de chaque employe affiche dans le tableau
            for (int i = 0; i < tablePointageC.getRowCount(); i++) {
                mPointage.setNumero((String) tableCreation.getValueAt(i, 0));
                mPointage.setPointage(pointe((Boolean) tableCreation.getValueAt(i, 1)));

                //si l'employe est en conge au même date alors pas de poinatge pour lui
                if (!mCongeD.inConge(mPointage)) {
                    mPointageD.enregistrer(mPointage);
                }
            }
            panel = new Notification(this, mPointageD.getType(), Notification.Location.BOTTOM_LEFT, mPointageD.message);
        } catch (Exception e) {
            System.err.println("Creation Pointage ::: "+ e.getMessage());
        }
        panel.showNotification();
        affichePointage();
    }//GEN-LAST:event_btnCreatePointageActionPerformed
    private void btnListPointageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListPointageActionPerformed
        try {
            cardList.show(cardListPointage, "listePointage");
            affichePointage();
        } catch (Exception e) {
            System.err.println("Listage Pointage ::: "+ e.getMessage());
        }
    }//GEN-LAST:event_btnListPointageActionPerformed
    private void btnListAbsentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListAbsentActionPerformed
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            this.date = df.format(dateAbs.getDate());

            afficheAbs(date);
            
            cardList.show(cardListPointage, "listeNonPointe");
        } catch (Exception e) {
            System.err.println("Listage Absence ::: " + e.getMessage());
            panel = new Notification(this, "ERROR", Notification.Location.TOP_CENTER, "Aucune date selectionner");
            panel.showNotification();
        }
    }//GEN-LAST:event_btnListAbsentActionPerformed
    /*--------------------------------------------------------------------------------------------------------------------
    -
    -                                              EMPLOYE
    -                                      
    ---------------------------------------------------------------------------------------------------------------------*/
    private void btnCreateEmployeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateEmployeActionPerformed
        try {
            if (TextFieldVerify.creationField_isNotEmpty(numeroEmployeC, nomEmployeC, prenomEmployeC, posteEmployeC, salaireEmployeC)) {
                mEmploye.setNumero(numeroEmployeC.getText());
                mEmploye.setNom(nomEmployeC.getText());
                mEmploye.setPrenom(prenomEmployeC.getText());
                mEmploye.setPoste(posteEmployeC.getText());
                mEmploye.setSalaire(Integer.parseInt(salaireEmployeC.getText()));

                if (mEmployeD.enregistrer(mEmploye)) {
                    actualiserEmploye();
                }
                panel = new Notification(this, mEmployeD.getType(), Notification.Location.BOTTOM_LEFT, mEmployeD.message);
                mEmployeD.setLabelMessage(messageCreateEmploye);

            } else {
                panel = new Notification(this, "ERROR", Notification.Location.BOTTOM_LEFT, "Veuiller completer tous les champs vide !");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("For input string")) {
                System.err.println(e.getMessage());
                panel = new Notification(this, "ERROR", Notification.Location.BOTTOM_LEFT, "Erreur de saisie au champ salaire");
            }
        }
        panel.showNotification();
        afficheEmploye();

    }//GEN-LAST:event_btnCreateEmployeActionPerformed
    private void modificationEmployeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificationEmployeActionPerformed
        try {
            if (TextFieldVerify.modificationField_isNotEmpty(nomEmployeE, prenomEmployeE, posteEmployeE, salaireEmployeE)) {
                mEmploye.setNumero(numeroEmployeE.getText());
                mEmploye.setNom(nomEmployeE.getText());
                mEmploye.setPrenom(prenomEmployeE.getText());
                mEmploye.setPoste(posteEmployeE.getText());
                mEmploye.setSalaire(Integer.parseInt(salaireEmployeE.getText()));
                mEmployeD.modifier(mEmploye);
                panel = new Notification(this, mEmployeD.getType(), Notification.Location.BOTTOM_LEFT, mEmployeD.message);

            } else {
                panel = new Notification(this, "ERROR", Notification.Location.BOTTOM_LEFT, "Veuiller completer tous les champs vide !");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("For input string")) {
                panel = new Notification(this, "ERROR", Notification.Location.BOTTOM_LEFT, "Erreur de saisie au champ salaire");
            }
        }
        panel.showNotification();
        afficheEmploye();
    }//GEN-LAST:event_modificationEmployeActionPerformed

    private void returnToCreationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnToCreationActionPerformed
        cardLog.show(cardCreationEmploye, "createEmploye");
    }//GEN-LAST:event_returnToCreationActionPerformed

    private void rechercheEmployeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rechercheEmployeKeyReleased
        ArrayList<ModelEmploye> list = mEmployeD.recherche(rechercheEmploye.getText().trim());
        DefaultTableModel tblModel = (DefaultTableModel) tableEmploye.getModel();
        tblModel.setRowCount(0);
        Object rowData[] = new Object[5];
        for (int i = 0; i < list.size(); i++) {
            rowData[0] = list.get(i).getNumero();
            rowData[1] = list.get(i).getNom();
            rowData[2] = list.get(i).getPrenom();
            rowData[3] = list.get(i).getPoste();
            rowData[4] = list.get(i).getSalaire() + " Ar";
            tblModel.addRow(rowData);
        }
        System.err.println(tblModel.getRowCount());
    }//GEN-LAST:event_rechercheEmployeKeyReleased
    
    /*--------------------------------------------------------------------------------------------------------------------
    -
    -                                              CONGE
    -                                      
    ---------------------------------------------------------------------------------------------------------------------*/
    private void btnCreateCongeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateCongeActionPerformed
        try {
            if (TextFieldVerify.congeField_isNotEmpty(numeroCongeC, jourCongeC, motifCongeC)) {
                if (Integer.parseInt(jourCongeC.getText()) == 0) {
                    panel = new Notification(this, "ERROR", Notification.Location.BOTTOM_LEFT, "Le nombre de jour doit être supérieur à 0 ");
                } else {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String demande = df.format(demandeCongeC.getDate());
                    mConge.setNumConge(numeroCongeC.getText());
                    mConge.setNumEmp(numeroEmployeCongeC.getItemAt(numeroEmployeCongeC.getSelectedIndex()));
                    mConge.setMotif(motifCongeC.getText());
                    mConge.setNbrJour(Integer.valueOf(jourCongeC.getText().trim()));
                    mConge.setDateDemande(demande);

                    if (mCongeD.enregistrer(mConge)) {
                        actualiserConge();
                    }
                    panel = new Notification(this, mCongeD.getType(), Notification.Location.BOTTOM_LEFT, mCongeD.message);
                }
            } else {
                panel = new Notification(this, "ERROR", Notification.Location.BOTTOM_LEFT, "Veuiller completer tous les champs vide !");
            }

        } catch (Exception e) {

            if (e.getMessage().contains("For input string")) {
                panel = new Notification(this, "ERROR", Notification.Location.BOTTOM_LEFT, "Erreur de saisie du nombre de jour");
            } else {
                panel = new Notification(this, "ERROR", Notification.Location.BOTTOM_LEFT, "Aucune date selectionner");
            }

        }
        panel.showNotification();
        afficheConge();

    }//GEN-LAST:event_btnCreateCongeActionPerformed

    private void returnToCongeCreationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnToCongeCreationActionPerformed
        actualiserConge();
        cardCongeCE.show(cardCongeCEE, "createConge");
    }//GEN-LAST:event_returnToCongeCreationActionPerformed

    private void modificationCongeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificationCongeActionPerformed
        try {
            if (TextFieldVerify.congeField_isNotEmpty(motifCongeE, motifCongeE, jourCongeE)) {
                if (Integer.parseInt(jourCongeE.getText()) == 0) {
                    panel = new Notification(this, "ERROR", Notification.Location.BOTTOM_LEFT, "Le nombre de jour doit être supérieur à 0 ");
                } else {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String demande = df.format(demandeCongeE.getDate());
                    mConge.setNumConge(numeroCongeE.getText());
                    mConge.setNumEmp(numeroEmployeCongeC.getItemAt(numeroEmployeCongeC.getSelectedIndex()));
                    mConge.setMotif(motifCongeE.getText());
                    mConge.setNbrJour(Integer.parseInt(jourCongeE.getText().trim()));
                    mConge.setDateDemande(demande);
                    mCongeD.modifier(mConge);
                    panel = new Notification(this, mCongeD.getType(), Notification.Location.BOTTOM_LEFT, mCongeD.message);
                }
            } else {
                panel = new Notification(this, "ERROR", Notification.Location.BOTTOM_LEFT, "Veuiller completer tous les champs vide !");
            }
        } catch (Exception e) {
            System.err.println("fghjklkjhg   " + e.getMessage());
            if (e.getMessage().contains("For input string")) {
                panel = new Notification(this, "ERROR", Notification.Location.BOTTOM_LEFT, "Erreur de saisie du nombre de jour");
            } else {
                panel = new Notification(this, "ERROR", Notification.Location.BOTTOM_LEFT, "Aucune date selectionner");
            }
        }
        panel.showNotification();
        afficheConge();
    }//GEN-LAST:event_modificationCongeActionPerformed

    private void returnToCreation1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnToCreation1ActionPerformed
        cardLog.show(cardCreationEmploye, "createEmploye");
    }//GEN-LAST:event_returnToCreation1ActionPerformed
    /*--------------------------------------------------------------------------------------------------------------------
    -
    -                                              OTHER METHODS
    -                                      
    ---------------------------------------------------------------------------------------------------------------------*/
    private void generatePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generatePDFActionPerformed
        
        String month = labelMonth.getItemAt(labelMonth.getSelectedIndex()).trim();
        PdfGenerator pdf = new PdfGenerator();
        pdf.setDate( lengthMonth(month)+ " - "+ month + " - " + LocalDate.now().getYear());
        pdf.setNom(labelNom.getText());
        pdf.setPrenom(labelPrenom.getText());
        pdf.setPoste(labelPoste.getText());
        pdf.setAbsence(Integer.parseInt(labelAbs.getText()));
        pdf.setMontant(labelMontant.getText()  + labelMontantConverti.getText() );
        
        
        
        if (pdf.generate()) {
            panel = new Notification(this, "SUCCESS", Notification.Location.BOTTOM_LEFT, "Fiche de paie générer");
        }

        panel.showNotification();
    }//GEN-LAST:event_generatePDFActionPerformed
    
  
    private void labelMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labelMonthActionPerformed
        DefaultTableModel model = (DefaultTableModel) tableEmploye.getModel();
        int year = Integer.parseInt(String.valueOf(LocalDate.now().getYear()));
        int abs = mPointageD.nombreAbs(year, labelMonth.getSelectedIndex() + 1, numeroCongeC.getText());
       // int montant=0;
        String mont;
        
        int montant = Integer.parseInt((String)model.getValueAt(ligne,4).toString().replace(" Ar", "").trim());//NEW 
        labelMontant.setText((montant-(abs*10000))+" Ar ");//new 
        
        labelAbs.setText(String.valueOf(abs));
        
        //------------------TALOHA------------------
//        labelMontant.setText(String.valueOf(mEmployeD.montant(abs, numeroCongeC.getText())) + " Ar ");
//        mont = labelMontant.getText();
//        if(!mont.isEmpty()) {
//           montant = Integer.parseInt(mont.replace(" Ar ","").trim());
//        }


        labelMontantConverti.setText("( " +Conversion.convertir(montant-(abs*10000))+ " Ariary )");
    }//GEN-LAST:event_labelMonthActionPerformed

    private boolean test(String value) {
        return "oui".equals(value);
    }
    private int lengthMonth(String mois)
    {   int taille = 0;
        switch (mois) {
            case "Janvier":
                taille = 31;
                break;
            case "Février":
                taille = 28;
                break;
            case "Mars":
                taille = 31;
                break;
            case "Avril":
                taille = 30;
                break;
            case "Mai":
                taille = 31;
                break;
            case "Juin":
                taille = 30;
                break;
            case "Juillet":
                taille = 31;
                break;
            case "Août":
                taille = 31;
                break;
            case "Septembre":
                taille = 30;
                break;
            case "Octobre":
                taille = 31;
                break;
            case "Novembre":
                taille = 30;
                break;
            case "Decembre":
                taille = 31;
                break;
            }
        return taille;
    }
    private String pointe(Boolean bool) {
        if (bool == null || bool == false) {
            return "non";
        }
        return "oui";
    }

    private void setColor(JPanel button) {
        button.setBackground(new Color(237, 247, 255));
        button.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(0, 157, 226)));
    }

    private void resetColor(JPanel button) {
        button.setBackground(Color.white);
        button.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(204, 204, 204)));
    }

    private void afficheEmploye() {

        ArrayList<ModelEmploye> list = mEmployeD.lister();
        DefaultTableModel tblModel = (DefaultTableModel) tableEmploye.getModel();

        for (int j = 0; j < tblModel.getRowCount(); j++) {
            tblModel.removeRow(j);
            j = j - 1;
        }

        Object rowData[] = new Object[5];
        for (int i = 0; i < list.size(); i++) {
            rowData[0] = list.get(i).getNumero();
            rowData[1] = list.get(i).getNom();
            rowData[2] = list.get(i).getPrenom();
            rowData[3] = list.get(i).getPoste();
            rowData[4] = list.get(i).getSalaire() + " Ar";
            tblModel.addRow(rowData);
        }
    }
    private void affichePointage() {
      try{
            ArrayList<ModelEmploye> listemploye = mEmployeD.lister();
        DefaultTableModel tableCreation = (DefaultTableModel) tablePointageC.getModel();
        
        // ----------------------affichage sur la creation  pointage-----------------------------------
       
        for (int j = 0; j < tableCreation.getRowCount(); j++) {
            tableCreation.removeRow(j);
            j = j - 1;
        }
        Object rowData[] = new Object[1];
        for (int i = 0; i < listemploye.size(); i++) {

            rowData[0] = listemploye.get(i).getNumero();
            tableCreation.addRow(rowData);

        }
        // ----------------------affichage du liste pointage-----------------------------------
        ArrayList<ModelPointage> list = mPointageD.lister();
        DefaultTableModel tblModel = (DefaultTableModel) tablePointage.getModel();

       
        for (int j = 0; j < tblModel.getRowCount(); j++) {
            tblModel.removeRow(j);
            j = j - 1;
        }

        Object rowDataP[] = new Object[3];
        for (int i = 0; i < list.size(); i++) {

            rowDataP[0] = list.get(i).getDate();
            rowDataP[1] = list.get(i).getNumero();
            rowDataP[2] = list.get(i).getPointage();

            tblModel.addRow(rowDataP);

        }

      }catch(Exception e)
      {
          System.err.println("Erreur Affichage ::: "+ e.getMessage());
      }
    }
    private void afficheAbs(String date) {
        ArrayList<ModelPointage> list = mPointageD.listeNonPointe(date);
        DefaultTableModel tblModel = (DefaultTableModel) tableNonPointe.getModel();

        for (int j = 0; j < tblModel.getRowCount(); j++) {
            tblModel.removeRow(j);
            j = j - 1;
        }

        Object rowData[] = new Object[3];
        for (int i = 0; i < list.size(); i++) {
            rowData[0] = list.get(i).getDate();
            rowData[1] = list.get(i).getNumero();
            rowData[2] = list.get(i).getPointage();
            tblModel.addRow(rowData);
        }
    }
    private void afficheConge() {

        ArrayList<ModelConge> list = mCongeD.lister();
        DefaultTableModel tblModel = (DefaultTableModel) tableConge.getModel();

        for (int j = 0; j < tblModel.getRowCount(); j++) {
            tblModel.removeRow(j);
            j = j - 1;
        }

        Object rowData[] = new Object[6];
        for (int i = 0; i < list.size(); i++) {
            rowData[0] = list.get(i).getNumConge();
            rowData[1] = list.get(i).getNumEmp();
            rowData[2] = list.get(i).getMotif();
            rowData[3] = list.get(i).getNbrJour();
            rowData[4] = list.get(i).getDateDemande();
            rowData[5] = list.get(i).getDateRetour();
            tblModel.addRow(rowData);
        }
    }

    private void actualiserEmploye() {
        numeroEmployeC.setText(null);
        nomEmployeC.setText(null);
        prenomEmployeC.setText(null);
        posteEmployeC.setText(null);
        salaireEmployeC.setText(null);
        rechercheEmploye.setText(null);
    }  
    private void actualiserConge() {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString());
            demandeCongeC.setMinSelectableDate(date);
            demandeCongeC.setDate(date);
        } catch (Exception e) {
            System.out.println(e);
        }
        //MM-dd-yyyy
        numeroCongeC.setText(null);
        motifCongeC.setText(null);
        jourCongeC.setText(null);
    }
    private Integer jour(String date) {
        int jour = Integer.parseInt(String.valueOf(date.charAt(8)).concat(String.valueOf(date.charAt(9))));
        if (jour < 0) {
            return -jour;
        }

        return jour;
    }


    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Window().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.k33ptoo.components.KButton btnCreateConge;
    private com.k33ptoo.components.KButton btnCreateEmploye;
    private com.k33ptoo.components.KButton btnCreatePointage;
    private com.k33ptoo.components.KButton btnListAbsent;
    private com.k33ptoo.components.KButton btnListPointage;
    private javax.swing.JPanel btn_conge;
    private javax.swing.JPanel btn_employe;
    private javax.swing.JPanel btn_pointage;
    private javax.swing.JPanel cardConge;
    private com.k33ptoo.components.KGradientPanel cardCongeCEE;
    private com.k33ptoo.components.KGradientPanel cardCreationEmploye;
    private com.k33ptoo.components.KGradientPanel cardCreationPointage;
    private javax.swing.JPanel cardEmploye;
    private com.k33ptoo.components.KGradientPanel cardListPointage;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JPanel cardPointage;
    private javax.swing.JCheckBox checkBoxPointageE;
    private com.k33ptoo.components.KGradientPanel createConge;
    private com.k33ptoo.components.KGradientPanel createEmploye;
    private com.k33ptoo.components.KGradientPanel createPointage;
    private com.toedter.calendar.JDateChooser dateAbs;
    private com.toedter.calendar.JDateChooser demandeCongeC;
    private com.toedter.calendar.JDateChooser demandeCongeE;
    private com.k33ptoo.components.KGradientPanel editConge;
    private com.k33ptoo.components.KGradientPanel editEmploye;
    private com.k33ptoo.components.KGradientPanel editPointage;
    private com.k33ptoo.components.KGradientPanel fichePaie;
    private com.k33ptoo.components.KButton generatePDF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jourCongeC;
    private javax.swing.JTextField jourCongeE;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel10;
    private com.k33ptoo.components.KGradientPanel kGradientPanel2;
    private com.k33ptoo.components.KGradientPanel kGradientPanel3;
    private com.k33ptoo.components.KGradientPanel kGradientPanel4;
    private com.k33ptoo.components.KGradientPanel kGradientPanel7;
    private com.k33ptoo.components.KGradientPanel kGradientPanel8;
    private com.k33ptoo.components.KGradientPanel kGradientPanel9;
    private javax.swing.JLabel labelAbs;
    private javax.swing.JLabel labelMontant;
    private javax.swing.JTextPane labelMontantConverti;
    private javax.swing.JComboBox<String> labelMonth;
    private javax.swing.JLabel labelNom;
    private javax.swing.JLabel labelPoste;
    private javax.swing.JLabel labelPrenom;
    private javax.swing.JPanel menu;
    private javax.swing.JLabel messageCreateConge;
    private javax.swing.JLabel messageCreateEmploye;
    private javax.swing.JLabel messageCreatePointage;
    private javax.swing.JLabel messageEditCongeE;
    private javax.swing.JLabel messageEditEmploye;
    private javax.swing.JLabel messageEditEmploye1;
    private javax.swing.JLabel messageEditPointage;
    private com.k33ptoo.components.KButton modificationConge;
    private com.k33ptoo.components.KButton modificationEmploye;
    private com.k33ptoo.components.KButton modificationPointage;
    private javax.swing.JTextField motifCongeC;
    private javax.swing.JTextField motifCongeE;
    private javax.swing.JTextField nomEmployeC;
    private javax.swing.JTextField nomEmployeE;
    private javax.swing.JTextField numeroCongeC;
    private javax.swing.JLabel numeroCongeE;
    private javax.swing.JTextField numeroEmployeC;
    private javax.swing.JComboBox<String> numeroEmployeCongeC;
    private javax.swing.JComboBox<String> numeroEmployeCongeE;
    private javax.swing.JLabel numeroEmployeE;
    private javax.swing.JTextField numeroPointageE;
    private javax.swing.JTextField posteEmployeC;
    private javax.swing.JTextField posteEmployeE;
    private javax.swing.JTextField prenomEmployeC;
    private javax.swing.JTextField prenomEmployeE;
    private javax.swing.JTextField rechercheEmploye;
    private com.k33ptoo.components.KButton returnToCongeCreation;
    private com.k33ptoo.components.KButton returnToCreation;
    private com.k33ptoo.components.KButton returnToCreation1;
    private com.k33ptoo.components.KButton returnToPointageCreation;
    private javax.swing.JTextField salaireEmployeC;
    private javax.swing.JTextField salaireEmployeE;
    private com.gestion.component.Table tableConge;
    private com.gestion.component.Table tableEmploye;
    private com.gestion.component.Table tableNonPointe;
    private com.gestion.component.Table tablePointage;
    private com.gestion.component.Table tablePointageC;
    // End of variables declaration//GEN-END:variables

   
}
