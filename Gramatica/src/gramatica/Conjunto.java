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
public class Conjunto {
    ArrayList<String> simbolos;
    Produccion produccion;
    ArrayList<Integer> indices;
    
    public Conjunto(Produccion p){
        produccion=p;
        simbolos=new ArrayList();
        indices=new ArrayList();
    }
    
    public String getSimboloProduccion(){
        return produccion.getSymbol();
    }
    
    public void setSimbolo(String simbolo,int indiceDelProduce){
        if(!simbolos.contains(simbolo)) {
            simbolos.add(simbolo);
            indices.add(indiceDelProduce);
        }
    }
    
    public void setSimbolo(String simbolo){
        if(!simbolos.contains(simbolo)) {
            simbolos.add(simbolo);
        }
    }
    
    public ArrayList<String> getSimbolos(){
        return simbolos;
    }
    
    public String getSimbolo(int i){
        return simbolos.get(i);
    }
    
    public String getPruce(String simbolo){
        if (simbolos.contains(simbolo)) {
           return produccion.getProduccion(indices.get(simbolos.indexOf(simbolo)));
        }else{
            return "";
        }
    }
    
    public void union(ArrayList<String> siguientesA){
        siguientesA.forEach((String siguiente)->{
            if (!siguiente.equals("&") && !simbolos.contains(siguiente)) {
                simbolos.add(siguiente);
            }
        });
    }
    
}
