/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Screens;

import gramatica.Analisis;
import gramatica.Conjunto;
import gramatica.Gramatica;
import gramatica.Modelo;
import gramatica.Produccion;
import gramatica.Render;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * @author Andrés Echeverry
 */
public class Main extends JFrame implements ActionListener {         
    private JPanel panelOEPS;
    private final Gramatica gramatica;
    private Analisis motorDeAnalisis;

    public Main(Gramatica gramatica) {
        super();
        configurarVentana();     
        this.gramatica=gramatica;
        inicializarComponentes();  
    }
    
    
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
        motorDeAnalisis.setNoTerminales();
        motorDeAnalisis.setTerminales();
        
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
        String cgramatica="";
        for(int i=0;i<producciones.size();i++){
            Produccion produccion=producciones.get(i);
            for(int j=0;j<produccion.getProducciones().size();j++){
                String produce=produccion.getProduccion(j);
                cgramatica=cgramatica+produccion.getSymbol()+"->"+produce+" ";
            }
        }
        return cgramatica.split(" ");
    }

    public String[] getDataConjunto(ArrayList<Conjunto> conjuntos){
        String[] gramaticas=new String[conjuntos.size()];
        for(int i=0;i<conjuntos.size();i++){
            Conjunto conjunto=conjuntos.get(i);
            gramaticas[i]=conjunto.getSimboloProduccion()+"="+conjunto.getSimbolos().toString();
        }
        return gramaticas;
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
        
        JLabel tituloTM=new JLabel("Validación de cadena");
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
        
        JTable table = new JTable();
        table.setBounds(0, 0, (int)(width*0.975),table.getHeight());
        table.setFont(new Font("Verdana", Font.PLAIN, 15));
        table.setRowHeight(25);
                
        JScrollPane menuScrollPane=new JScrollPane(table);
        menuScrollPane.setBounds((int)(width*0.01),tituloTM.getHeight()+campoValidacion.getHeight()+(int)(height*0.01),(int)(width*0.92),(int)(height*0.79));
        menuScrollPane.setMaximumSize(new Dimension((int)(width*0.92),(int)(height*0.7)));
        
        botonValidar.addActionListener((ActionEvent e) -> {
            if (!campoValidacion.getText().isEmpty()) {
                String cadena=campoValidacion.getText();
                if (cadena.substring(cadena.length()-1, cadena.length()).equals("$")) {
                    cadena=cadena.replace("$", "");
                }
                Modelo model=new Modelo();
                model.setData(validarCadena(cadena));
                table.setModel(model);
                table.setDefaultRenderer(Object.class, new Render());
            }else {
                JOptionPane.showMessageDialog(null, "Ingresa una cadena","Aviso",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        panel.add(tituloTM);
        panel.add(campoValidacion);
        panel.add(botonValidar);
        panel.add(menuScrollPane);
        
        return panel;
    }

    public String[][] validarCadena(String cadena){
        motorDeAnalisis.validarCadena(cadena);
        int n=motorDeAnalisis.getPila().size();
        String[][] data = new String[n+1][3];
        for (int i = 0; i < n; i++) {
            data[i][0] = motorDeAnalisis.getPila().get(i);
            data[i][1] = motorDeAnalisis.getEntrada().get(i);
            data[i][2] = motorDeAnalisis.getSalida().get(i);
        }
        data[n][2]=motorDeAnalisis.getSalida().get(n);
        return data;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}