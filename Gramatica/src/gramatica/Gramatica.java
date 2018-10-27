/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gramatica;

import java.util.ArrayList;

/*
 *
 * @author andre
 */
public class Gramatica {
    
    ArrayList<Produccion> producciones;

    
    public Gramatica(){
        producciones=new ArrayList();
    }
    
    
    public void cargarProduccion(String produccion){
        
        //Validacion de la produccion
        if (produccion.matches("^[A-Z]'?->(\\w|\\W)*$")) {
            String[] produce=produccion.split("->");
            if (!containProduccion(produce[0])) {
                producciones.add(new Produccion(produce[0]));
            }
            if (produce.length==1) {
                getProduccion(produce[0]).setProduccion("&");
            }else{
                getProduccion(produce[0]).setProduccion(produce[1]);
            }
        }
        /*String[] produce=produccion.split("->");
        if (produce.length<=2 && produce.length>0) {
            if (!containProduccion(produce[0].toUpperCase())) {
                producciones.add(new Produccion(produce[0].toUpperCase()));
                
            }
            if (produce.length==1) {
                getProduccion(produce[0].toUpperCase()).setProduccion("&");
            }else{
                getProduccion(produce[0].toUpperCase()).setProduccion(produce[1]);
            }
        }*/
    }
    
    
    public boolean containProduccion(String simbolo){
        for(int i=0;i<producciones.size();i++){
            Produccion produccion=producciones.get(i);
            if (produccion.getSymbol().equals(simbolo)) {
                return true;
            }
        }
        return false;
    }
    
    
    public Produccion getProduccion(String simbolo){
        for(int i=0;i<producciones.size();i++){
            Produccion produccion=producciones.get(i);
            if (produccion.getSymbol().equals(simbolo)) {
                return produccion;
            }
        }
        return null;
    }
    
    
    public ArrayList<Produccion> getProducciones() {
        return producciones;
    }
}