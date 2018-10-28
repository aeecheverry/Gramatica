/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gramatica;

import java.util.ArrayList;

/**
 *
 * @author andre
 */
public class Produccion {
    private final String symbol;
    ArrayList<String> producciones;
    
    public Produccion(String symbol){
        this.symbol=symbol;
        producciones=new ArrayList();
    }
    
    public String getSymbol(){
        return symbol;
    }
    
    public void setProduccion(String prod){
        if(!producciones.contains(prod)){
            producciones.add(prod);
        }
    }
    
    public String getProduccion(int i){
        return producciones.get(i);
    }
    
    public void editProduccion(int i,String prod){
        producciones.set(i,prod);
    }
    
    public int getIndex(String p){
        return producciones.indexOf(p);
    }
    
    public ArrayList<String> getProducciones(){
        return producciones;
    }
    
}
