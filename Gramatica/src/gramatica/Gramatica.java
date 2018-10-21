/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gramatica;

import Screens.Start;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author andre
 */
public class Gramatica {
    private String[][] M;
    /**
     */
    public Gramatica(){
        
        /*M=new String[noterminales.size()+1][terminales.size()+1];
        tablaM();
        printTablaM();*/
    }
    /*public static void main(String[] args) {
        Start inicio=new Start();
        inicio.setVisible(true);
        Gramatica gramatica=new Gramatica();
    }*/
    
    
    
    
    
    /*public void printTablaM(){
        for (int i = 0; i < noterminales.size()+1; i++) {
            for (int j = 0; j < terminales.size()+1; j++) {
                System.out.print(M[i][j]+" | ");
            }
            System.out.println("");
        }
    }*/
    
    
    
}
/*
public void setSiguientes(int i,String b,String B,String A){
        if(!B.equals("")){
            reglas(i,B,A);
        }else{
            producciones.forEach((ArrayList<String> p)->{
                if (p.get(0).equals(b) && !p.get(0).equals(A)) {
                    
                }
            });
        }
    }
    
    public void reglas(int i,String B,String A){
        if (isTerminal(B)) {
            SIG.get(i).setTSymbols(B);
        }else{
            PRI.forEach((Conjunto p)->{
                if (p.getSymbol().equals(B)) {
                    if (!p.getTSymbols().contains(epsilon)) {
                        SIG.get(i).setUnion(p.getTSymbols());
                    }else{
                        SIG.forEach((Conjunto si)->{
                            if (si.getSymbol().equals(A)) {
                                SIG.get(i).setUnion(si.getTSymbols());
                            }
                        });
                    }
                }
            });
        }
    }
    
    public void Siguientes(){
        System.out.println("SIGUIENTES");
        noterminales.forEach((nt)->{
            SIG.add(new Conjunto(nt));
        });
        SIG.get(0).setTSymbols(fc);
        
        SIG.forEach((Conjunto s)->{
            producciones.forEach((ArrayList<String> pro)->{
                String ps=pro.get(1);
                for (int i = 0; i < ps.length()-s.getSymbol().length(); i++) {
                    String sig=ps.substring(i, i+s.getSymbol().length());
                    if (sig.equals(s.getSymbol())) {
                        String ss="";
                        if (i+s.getSymbol().length()<ps.length()) {
                            ss=ps.substring(i+s.getSymbol().length(), i+s.getSymbol().length()+1);
                            if (isTerminal(ss) && !ss.equals("'")) {
                                s.setTSymbols(ss);
                            }else{
                                setSiguiente(ss,SIG.indexOf(s),pro.get(0));
                            }
                        }else{
                            SIG.forEach((Conjunto si)->{
                                if (si.getSymbol().equals(pro.get(0))) {
                                    SIG.get(SIG.indexOf(s)).setUnion(si.getTSymbols());
                                }
                            });
                        }
                        
                    }
                        
                }
            });
            System.out.println(s.getSymbol()+"="+s.getTSymbols().toString());
        });
        
    }
    
    public void setSiguiente(String s,int b,String a){
        PRI.forEach((Conjunto p)->{
            if (p.getSymbol().equals(s)) {
                if (!p.getTSymbols().contains(epsilon)) {
                    SIG.get(b).setUnion(p.getTSymbols());
                }else{
                    SIG.forEach((Conjunto si)->{
                        if (si.getSymbol().equals(a)) {
                            SIG.get(b).setUnion(si.getTSymbols());
                        }
                    });
                }
            }
        });
    }*/