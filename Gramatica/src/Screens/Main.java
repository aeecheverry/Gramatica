/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Screens;

import gramatica.Analisis;
import gramatica.Conjunto;
import gramatica.Gramatica;
import gramatica.Produccion;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * @author Andrés Echeverry
 */
public class Main extends JFrame implements ActionListener {

    private JLabel tituloGO;           
    private JLabel tituloGE;           
    private JLabel tituloPrimeros;           
    private JLabel tituloSiguientes;           
    private JPanel panelOEPS;
    private JPanel panelVC;
    private JPanel panelTM;
    private Gramatica gramatica;
    private Analisis motorDeAnalisis;

    public Main(Gramatica gramatica) {
        super();
        configurarVentana();     
        this.gramatica=gramatica;
        inicializarComponentes();  
    }
    
    /*public static void main(String[] args) {
        Main V = new Main();      // creamos una ventana
        V.setVisible(true);             // hacemos visible la ventana creada
    }*/
    
    private void configurarVentana() {
        this.setTitle("Gramática");                   
        this.setSize(1000, 600);                                 
        this.setLocationRelativeTo(null);                       
        this.setLayout(null);                                   
        this.setResizable(false);                               
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void inicializarComponentes() {
        panelOEPS = new JPanel();
        panelOEPS.setBounds(0, 0, (int)(getWidth()*0.7), (int)(getHeight()*0.5));
        panelOEPS.setLayout(new GridLayout(1,4));
        panelOEPS.setBackground(Color.white);
        this.add(panelOEPS);
        
        //Motor de motorDeAnalisis gramatical
        motorDeAnalisis= new Analisis(gramatica.getProducciones());
        //motorDeAnalisis.gramaticaSample();
        motorDeAnalisis.setNoTerminales();
        motorDeAnalisis.setTerminales();
        if (motorDeAnalisis.getProducciones().size()!=motorDeAnalisis.getNoTerminales().size()) {
            
        }
        //Gramatica original
        panelOEPS.add(getPanelGramatica((int)(panelOEPS.getWidth()/4),panelOEPS.getHeight(),"Original",getDataGramatica(motorDeAnalisis.getProducciones())));
        
        //Gramatica mejorada
        motorDeAnalisis.eliminarRecursividad();
        motorDeAnalisis.factorizar();
        motorDeAnalisis.setNoTerminales();
        motorDeAnalisis.setTerminales();
        panelOEPS.add(getPanelGramatica((int)(panelOEPS.getWidth()/4),panelOEPS.getHeight(),"Mejorada",getDataGramatica(motorDeAnalisis.getProducciones())));
        
        //Conjunto primeros
        motorDeAnalisis.primeros();
        panelOEPS.add(getPanelGramatica((int)(panelOEPS.getWidth()/4),panelOEPS.getHeight(),"PRIMEROS",getDataConjunto(motorDeAnalisis.getPrimeros())));
        
        //Conjunto siguientes
        motorDeAnalisis.siguientes();
        panelOEPS.add(getPanelGramatica((int)(panelOEPS.getWidth()/4),panelOEPS.getHeight(),"SIGUIENTES",getDataConjunto(motorDeAnalisis.getSiguientes())));
        
        //Genera tabla M
        motorDeAnalisis.setTablaM();
        this.add(getPanelTablaM(0, panelOEPS.getX()+panelOEPS.getHeight(), (int)(getWidth()*0.7), (int)(getHeight()*0.5)));
        
        this.add(getPanelDeValidacion(panelOEPS.getWidth(), 0, (int)(getWidth()*0.3), getHeight()));
    }
    
    private JPanel getPanelGramatica(int width,int height,String titulo,String[] data){
        JPanel panel=new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(width,height));
        panel.setBackground(Color.white);
        
        JLabel tituloG=new JLabel(titulo);
        tituloG.setBounds(panel.getX()+(int)(width*0.05), panel.getY(), (int)(width*0.9),(int)(height*0.15));
        tituloG.setFont(new Font("Verdana", Font.BOLD, 16));
        
        JList lista=new JList(data);
        lista.setBounds(panel.getX()+(int)(width*0.05),tituloG.getHeight(),(int)(width*0.9),(int)(height*0.82));
        lista.setFont(new Font("Verdana", Font.PLAIN, 15));
        
        JScrollPane menuScrollPane=new JScrollPane(lista);
        menuScrollPane.setBounds(panel.getX()+(int)(width*0.05),tituloG.getHeight(),(int)(width*0.9),(int)(height*0.82));
        
        panel.add(tituloG);
        panel.add(menuScrollPane);
        
        return panel;
    }
    
    public String[] getDataGramatica(ArrayList<Produccion> producciones){
        String gramatica="";
        for(int i=0;i<producciones.size();i++){
            Produccion produccion=producciones.get(i);
            for(int j=0;j<produccion.getProducciones().size();j++){
                String produce=produccion.getProduccion(j);
                gramatica=gramatica+produccion.getSymbol()+"->"+produce+" ";
            }
        }
        return gramatica.split(" ");
    }

    public String[] getDataConjunto(ArrayList<Conjunto> conjuntos){
        String gramaticas="";
        for(int i=0;i<conjuntos.size();i++){
            Conjunto conjunto=conjuntos.get(i);
            gramaticas=gramaticas+conjunto.getSimboloProduccion()+"="+conjunto.getSimbolos().toString()+"¡";
        }
        return gramaticas.split("¡");
    }
    
    public JPanel getPanelTablaM(int x,int y,int width, int height){
        JPanel panel=new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.white);
        panel.setBounds(x, y, width, height);
        
        JLabel tituloTM=new JLabel("Tabla M");
        tituloTM.setFont(new Font("Verdana", Font.BOLD, 16));
        tituloTM.setBounds((int)(width*0.01), 0, width,(int)(height*0.15));
        
        int n=motorDeAnalisis.getTablaM().length-1;
        int m=motorDeAnalisis.getTablaM()[0].length;
        String[][] data = new String[n][];
        for (int i = 0; i < n; i++) {
            data[i] = Arrays.copyOfRange(motorDeAnalisis.getTablaM()[i+1], 0, m);
        }
        
        String[] columnNames=Arrays.copyOfRange(motorDeAnalisis.getTablaM()[0], 0, m);
        
        JTable table = new JTable(data, columnNames);
        table.setBounds(0, 0, (int)(width*0.975),table.getHeight());
        table.setFont(new Font("Verdana", Font.PLAIN, 15));
        table.setRowHeight(25);
                
        JScrollPane menuScrollPane=new JScrollPane(table);
        menuScrollPane.setBounds((int)(width*0.01),tituloTM.getHeight(),(int)(width*0.975),(int)(height*0.7));
        menuScrollPane.setMaximumSize(new Dimension((int)(width*0.975),(int)(height*0.7)));
        
        panel.add(tituloTM);
        panel.add(menuScrollPane);
        
        return panel;
    }
    
    public JPanel getPanelDeValidacion(int x,int y,int width, int height){
        JPanel panel=new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.white);
        panel.setBounds(x, y, width, height);
        
        JLabel tituloTM=new JLabel("Validacion de cadena");
        tituloTM.setFont(new Font("Verdana", Font.BOLD, 16));
        tituloTM.setBounds((int)(width*0.01), 0, width,(int)(height*0.075));
        
        JTextField campoValidacion=new JTextField();
        campoValidacion.setFont(new Font("Verdana", Font.BOLD, 14));
        campoValidacion.setBounds((int)(width*0.01), tituloTM.getHeight(), (int)(width*0.6),(int)(height*0.05));
        
        JButton botonValidar=new JButton("Validar");
        botonValidar.setFont(new Font("Verdana", Font.BOLD, 14));
        botonValidar.setBounds(campoValidacion.getWidth()+(int)(width*0.025), tituloTM.getHeight(), (int)(width*0.3),(int)(height*0.05));
        botonValidar.setForeground(Color.white);
        botonValidar.setBackground(Color.darkGray);
        botonValidar.setFocusPainted(false);
        
        /*botonValidar.addActionListener((ActionEvent e) -> {
            validarCadena(campoValidacion.getText());
        });*/
        
        int n=motorDeAnalisis.getTablaM().length-1;
        String[][] data = new String[n][];
        for (int i = 0; i < n; i++) {
            data[i] = Arrays.copyOfRange(motorDeAnalisis.getTablaM()[i+1], 0, 3);
        }
        
        String[] columnNames={"Pila","Entada","Salida"};
        
        JTable table = new JTable(data, columnNames);
        table.setBounds(0, 0, (int)(width*0.975),table.getHeight());
        table.setFont(new Font("Verdana", Font.PLAIN, 15));
        table.setRowHeight(25);
                
        JScrollPane menuScrollPane=new JScrollPane(table);
        menuScrollPane.setBounds((int)(width*0.01),tituloTM.getHeight()+campoValidacion.getHeight()+(int)(height*0.01),(int)(width*0.92),(int)(height*0.79));
        menuScrollPane.setMaximumSize(new Dimension((int)(width*0.92),(int)(height*0.7)));
        
        panel.add(tituloTM);
        panel.add(campoValidacion);
        panel.add(botonValidar);
        panel.add(menuScrollPane);
        
        return panel;
    }

    public void validarCadena(String cadena){
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}