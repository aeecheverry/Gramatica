/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Screens;

import gramatica.Analisis;
import gramatica.Conjunto;
import gramatica.Produccion;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * Clase Ventana
 * Muestra la estructuta que deberia tener una Ventana en Java con la libreria
 * Swing, contiene una etiqueta, un caja de texto y un boton, que tiene la
 * accion de mostrar el texto en la caja por una ventana de mensaje.
 * @author Daniel Alvarez (a3dany)
 */
public class Main extends JFrame implements ActionListener {

    private JLabel tituloGO;           // titulo gramatica original
    private JLabel tituloGE;           // titulo gramatica sin recursividad y factorizada
    private JLabel tituloPrimeros;           //
    private JLabel tituloSiguientes;           //
    private JPanel panelOEPS;
    private JPanel panelVC;
    private JPanel panelTM;
    
    private Analisis analisis;

    public Main() {
        super();
        configurarVentana();        
        inicializarComponentes();   
    }

    private void configurarVentana() {
        this.setTitle("Esta Es Una Ventana");                   
        this.setSize(1000, 600);                                 
        this.setLocationRelativeTo(null);                       
        this.setLayout(null);                                   
        this.setResizable(false);                               
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void inicializarComponentes() {
        panelOEPS = new JPanel();
        panelOEPS.setBounds(0, 0, (int)(getWidth()*0.7), (int)(getHeight()*0.5));
        panelOEPS.setLayout(new GridLayout(1,4));
        panelOEPS.setBackground(Color.white);
        
        panelVC = new JPanel();
        panelVC.setBounds(panelOEPS.getWidth(), 0, (int)(getWidth()*0.3), getHeight());
        panelVC.setBackground(Color.gray);
        
        
        
        panelTM = new JPanel();
        panelTM.setBounds(0, panelOEPS.getHeight(), (int)(getWidth()*0.7), (int)(getHeight()*0.5));
        panelTM.setBackground(Color.DARK_GRAY);
        //panelTM.add(getPanelTM());
        
        this.add(panelOEPS);
        this.add(panelVC);
        this.add(panelTM);  
        
        //Motor de analisis gramatical
        analisis= new Analisis();
        analisis.gramaticaSample();
        analisis.setNoTerminales();
        analisis.setTerminales();
        
        //Gramatica original
        panelOEPS.add(getPanelGramatica(0,(int)(panelOEPS.getWidth()/4),panelOEPS.getHeight(),"Original",getDataGramatica(analisis.getProducciones())));
        
        //Gramatica mejorada
        analisis.eliminarRecursividad();
        analisis.factorizar();
        analisis.setNoTerminales();
        analisis.setTerminales();
        panelOEPS.add(getPanelGramatica(1,(int)(panelOEPS.getWidth()/4),panelOEPS.getHeight(),"Mejorada",getDataGramatica(analisis.getProducciones())));
        
        //Conjunto primeros
        analisis.primeros();
        panelOEPS.add(getPanelGramatica(2,(int)(panelOEPS.getWidth()/4),panelOEPS.getHeight(),"PRIMEROS",getDataConjunto(analisis.getPrimeros())));
        
        //Conjunto siguientes
        analisis.siguientes();
        panelOEPS.add(getPanelGramatica(3,(int)(panelOEPS.getWidth()/4),panelOEPS.getHeight(),"SIGUIENTES",getDataConjunto(analisis.getSiguientes())));
        
        //Genera tabla M
        analisis.setTablaM();
        panelTM.add(getPanelTablaM(panelTM.getWidth(),panelTM.getHeight()));
    }
    
    private JPanel getPanelGramatica(int i,int width,int height,String titulo,String[] data){
        JPanel panel=new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(width,height));
        
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
        String gramatica="";
        for(int i=0;i<conjuntos.size();i++){
            Conjunto conjunto=conjuntos.get(i);
            gramatica=gramatica+conjunto.getSimboloProduccion()+"="+conjunto.getSimbolos().toString()+"¡";
        }
        return gramatica.split("¡");
    }
    
    public JPanel getPanelTablaM(int width, int height){
        JPanel panel=new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(width,height));
        
        JLabel tituloTM=new JLabel("Tabla M");
        tituloTM.setBounds(panel.getX()+(int)(width*0.05), panel.getY(), (int)(width*0.9),(int)(height*0.15));
        tituloTM.setFont(new Font("Verdana", Font.BOLD, 16));
        
        int n=analisis.getTablaM().length-1;
        int m=analisis.getTablaM()[0].length;
        String[][] data = new String[n][];
        for (int i = 0; i < n; i++) {
            data[i] = Arrays.copyOfRange(analisis.getTablaM()[i+1], 0, m);
        }
        
        String[] columnNames=Arrays.copyOfRange(analisis.getTablaM()[0], 0, m);
        
        JTable table = new JTable(data, columnNames);
        table.setBounds(panel.getX()+(int)(width*0.05), tituloTM.getHeight(), (int)(width*0.9),(int)(height*0.7));
        table.setFont(new Font("Verdana", Font.PLAIN, 15));
                
        JScrollPane menuScrollPane=new JScrollPane(table);
        menuScrollPane.setBounds(panel.getX()+(int)(width*0.05),tituloTM.getHeight(),(int)(width*0.9),(int)(height*0.7));
        
        panel.add(tituloTM);
        panel.add(menuScrollPane);
        
        
        
        return panel;
    }
    
    
    public static void main(String[] args) {
        Main V = new Main();      // creamos una ventana
        V.setVisible(true);             // hacemos visible la ventana creada
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}