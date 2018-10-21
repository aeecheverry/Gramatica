/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author andre
 */
public class Start extends javax.swing.JFrame{
    
    private javax.swing.JPanel panel;
   
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel srcGrammarLabel;
    private javax.swing.JButton addGrammarButton;
    ArrayList<ArrayList<String>> producciones;
    
    public Start(){
        iniComponents();
    }

    private void iniComponents(){
        
        setSize(550,300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        panel = new javax.swing.JPanel(new java.awt.BorderLayout());
        
        titleLabel = new javax.swing.JLabel();
        srcGrammarLabel = new javax.swing.JLabel();
        addGrammarButton = new javax.swing.JButton();
        
        
        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 24));
        titleLabel.setText("Gramática");
        titleLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        
        srcGrammarLabel.setFont(new java.awt.Font("Tahoma", 1, 16));
        srcGrammarLabel.setText("src: ");
        srcGrammarLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        
        
        addGrammarButton.setBackground(new java.awt.Color(0, 153, 255));
        addGrammarButton.setFont(new java.awt.Font("Tahoma", 1, 16));
        addGrammarButton.setText("Seleecionar gramática");
        addGrammarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addGrammarButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        addGrammarButton.addActionListener((ActionEvent e) -> {
            chooser();
        });
        panel.setSize(getWidth(),getHeight());
        panel.setBackground(Color.white);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(titleLabel);
        panel.add(srcGrammarLabel);
        panel.add(addGrammarButton);
        
       
        setContentPane(panel);
        
        
        
    }
    
    public ArrayList<ArrayList<String>> getPruducciones(){
        return producciones;
    }
    private void chooser(){
        //Creamos el objeto JFileChooser
        JFileChooser fc=new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileNameExtensionFilter("*.TXT","txt"));
        int seleccion=fc.showOpenDialog(panel);
        
        //Si el usuario, pincha en aceptar
        if(seleccion==JFileChooser.APPROVE_OPTION){
            producciones=new ArrayList();
            //Seleccionamos el fichero
            File file=fc.getSelectedFile();

            
            try(BufferedReader br= new BufferedReader(new FileReader(file))){
                String st;
                producciones.add(new ArrayList());
                int i=0;
                while((st = br.readLine()) != null){
                    producciones.get(i).add(st.split("->")[0]);
                    producciones.get(i).add(st.split("->")[1]);
                    i++;
                }
            } catch (IOException e) {
            }
            //Ecribe la ruta del fichero seleccionado en el campo de texto
            srcGrammarLabel.setText(file.getAbsolutePath());
        }
    }
    
    
}
