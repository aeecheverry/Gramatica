/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Screens;

import gramatica.Gramatica;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author andre
 */
public class Start extends JFrame{
    
    private javax.swing.JPanel panel;
   
    private javax.swing.JLabel etiquetaTitulo;
    private javax.swing.JLabel etiquetaSrc;
    private javax.swing.JButton botonCargarGramatica;
    private javax.swing.JButton botonIniciar;
    
    private JFileChooser fc;
    Gramatica gramatica;
    private boolean gramaticaCargada;
    
    public Start(){
        configurarVentana();
        gramaticaCargada=false;
        gramatica=new Gramatica();
        iniComponents();
    }
    
    public static void main(String[] args) {
        Start inicio=new Start();
        inicio.setVisible(true);
    }
    
    private void configurarVentana() {
        this.setTitle("Cargar gramática");                   
        this.setSize(550,300);                              
        this.setLocationRelativeTo(null);                       
        this.setLayout(null);                                   
        this.setResizable(false);                               
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("./iconp.png")));
    }

    private void iniComponents(){
        panel = new javax.swing.JPanel(new java.awt.BorderLayout());
        panel.setLayout(null);
        panel.setBackground(Color.white);
        panel.setBounds(0,0,getWidth(),getHeight());
        
        etiquetaTitulo = new javax.swing.JLabel("Gramática",SwingConstants.CENTER);
        etiquetaTitulo.setFont(new java.awt.Font("Tahoma", 1, 24));
        etiquetaTitulo.setBounds(0, 0,getWidth(), (int)(getHeight()*0.2));
        
        etiquetaSrc = new javax.swing.JLabel("SRC:",SwingConstants.CENTER);
        etiquetaSrc.setFont(new java.awt.Font("Tahoma", 1, 16));
        etiquetaSrc.setBounds(0, etiquetaTitulo.getHeight(), getWidth(), (int)(getHeight()*0.3));
        etiquetaSrc.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        botonCargarGramatica = new javax.swing.JButton();
        botonCargarGramatica.setBackground(new java.awt.Color(0, 153, 255));
        botonCargarGramatica.setFont(new java.awt.Font("Tahoma", 1, 16));
        botonCargarGramatica.setText("Cargar gramática");
        botonCargarGramatica.setFocusPainted(false);
        botonCargarGramatica.setForeground(Color.white);
        botonCargarGramatica.setBounds((getWidth()-360)/2, etiquetaTitulo.getHeight()+etiquetaSrc.getHeight(), 200, 40);
        
        botonIniciar = new javax.swing.JButton();
        botonIniciar.setBackground(Color.GREEN);
        botonIniciar.setFont(new java.awt.Font("Tahoma", 1, 16));
        botonIniciar.setText("Iniciar");
        botonIniciar.setFocusPainted(false);
        botonIniciar.setForeground(Color.white);
        botonIniciar.setBounds(botonCargarGramatica.getX()+botonCargarGramatica.getWidth()+20, etiquetaTitulo.getHeight()+etiquetaSrc.getHeight(), 100, 40);
        
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e){}
        
        fc=new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileNameExtensionFilter("*.TXT","txt"));
        
        botonCargarGramatica.addActionListener((ActionEvent e) -> {
            chooser();
        });
        
        botonIniciar.addActionListener((ActionEvent e) -> {
            iniciar();
        });
        
        panel.add(etiquetaTitulo);
        panel.add(etiquetaSrc);
        panel.add(botonCargarGramatica);
        panel.add(botonIniciar);
        
        this.add(panel);
    }

    private void chooser(){
        //Creamos el objeto JFileChooser
        int seleccion=fc.showOpenDialog(this);
        //Si el usuario, pincha en aceptar
        if(seleccion==JFileChooser.APPROVE_OPTION){
            //Seleccionamos el fichero
            gramaticaCargada=true;
            gramatica.getProducciones().clear();
            File file=fc.getSelectedFile();
            try(BufferedReader br= new BufferedReader(new FileReader(file))){
                String st;
                int i=0;
                while((st = br.readLine()) != null){
                    st=st.replaceAll(" ", "");
                    gramatica.cargarProduccion(st);
                    i++;
                }
            } catch (IOException e) {
                gramaticaCargada=false;
            }
            //Ecribe la ruta del fichero seleccionado en el campo de texto
            etiquetaSrc.setText(file.getAbsolutePath());
        }
    }
    
    public void iniciar(){
        if (gramaticaCargada && gramatica!=null) {
            Main main=new Main(gramatica);
            main.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(null, "Para inciar debes carga una gramática primero","Aviso",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
}
