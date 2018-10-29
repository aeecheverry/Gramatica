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

public class Analisis {
    
    ArrayList<Produccion> producciones;
    
    ArrayList<String> terminales;
    ArrayList<String> noTerminales;
    ArrayList<String> unusedNoTerminales;
    ArrayList<String> simbolosGramaticales;
    
    ArrayList<Conjunto> primeros;
    ArrayList<Conjunto> siguientes;
    
    String[][] tablaM;
    
    ArrayList<String> pila;
    ArrayList<String> entrada;
    ArrayList<String> salida;

    final String epsilon="&";
    final String fs="$";
    
    public Analisis(ArrayList<Produccion> producciones){
        this.producciones=producciones;
    }
    
    
    //Factoriza la gramatica
    public void factorizar(){
        for(int pi=0;pi<producciones.size();pi++){
            Produccion produccion=producciones.get(pi);
            ArrayList<Integer> indices=new ArrayList();
            
            for(int producei=0;producei<produccion.getProducciones().size()-1;producei++){
                String produce=produccion.getProduccion(producei);
                indices.clear();
                int minIndex=Integer.MAX_VALUE;
                
                for(int producej=producei+1;producej<produccion.getProducciones().size();producej++){
                    String produceI=produccion.getProduccion(producej);
                    int min=Math.min(produce.length(), produceI.length());
                    boolean factorizable=false;
                    int i=1;
                    while(i<=min && produce.substring(0, i).equals(produceI.substring(0, i))){
                        minIndex=i;
                        factorizable=true;
                        i++;
                    }
                    if (factorizable) {
                        if (!indices.contains(produccion.getIndex(produce))) {
                            indices.add(produccion.getIndex(produce));
                        }
                        indices.add(produccion.getIndex(produceI));
                        
                    }
                }
                if (minIndex>0 && minIndex<Integer.MAX_VALUE) {
                    setFactorizacion(produccion,indices,minIndex);
                }
            }
        }
    }
    
    //Busca los simbolos que coinsidan de izquierda a derecha y factoriza
    public void setFactorizacion(Produccion produccion,ArrayList<Integer> indices,int minIndex){
        String newProduccion=produccion.getSymbol();
        newProduccion=!unusedNoTerminales.contains(newProduccion)? unusedNoTerminales.get((int)(Math.random()*(unusedNoTerminales.size()-1))): newProduccion;
        int inp=producciones.indexOf(produccion)+1;
        producciones.add(inp, new Produccion(newProduccion));
        unusedNoTerminales.remove(newProduccion);
        boolean fatorized=false;
        int r=0;
        for(int i=0;i<indices.size();i++){
            int index=indices.get(i)-r;
            String temp=(minIndex<produccion.getProduccion(index).length())? produccion.getProduccion(index).substring(minIndex, produccion.getProduccion(index).length()):epsilon;
            if(!fatorized){
                produccion.editProduccion(index,produccion.getProduccion(index).substring(0, minIndex)+newProduccion);
                fatorized=true;
            }else{  
                produccion.getProducciones().remove(index);
                r++;
            }
            producciones.get(inp).setProduccion(temp);
        }
    }
    
    
    
    //Elimina recursividad
    public void eliminarRecursividad(){
        for(int pi=0;pi<producciones.size();pi++){
            Produccion produccion=producciones.get(pi);
            boolean recursiva=isRecursiva(produccion);
            String newProduccion=produccion.getSymbol();
            int ipr=producciones.indexOf(produccion)+1;
            if (recursiva) {
                newProduccion=!unusedNoTerminales.contains(newProduccion)? unusedNoTerminales.get((int)(Math.random()*(unusedNoTerminales.size()-1))): newProduccion;
                producciones.add(ipr, new Produccion(newProduccion));   
                unusedNoTerminales.remove(newProduccion);
                for(int producei=0;producei<produccion.getProducciones().size();producei++){
                    String produce=produccion.getProduccion(producei);
                    if(produce.length()>=produccion.getSymbol().length()){
                        if (produce.substring(0,produccion.getSymbol().length()).equals(produccion.getSymbol())) {
                            producciones.get(ipr).setProduccion(produce.substring(produccion.getSymbol().length(),produce.length())+newProduccion);
                            produccion.getProducciones().remove(produce);
                            producei--;
                        }else{
                            if (!produce.equals(epsilon)) {
                                produccion.editProduccion(producei,produce+newProduccion);
                            }else{
                                produccion.editProduccion(producei,produce);
                            }
                        }
                    }
                }
                producciones.get(ipr).setProduccion(epsilon);
            }
        }
    }
    
    //Return true si la produccion es recursiva
    public boolean isRecursiva(Produccion p){
        for(int producei=0;producei<p.getProducciones().size();producei++){
            String produce=p.getProduccion(producei);
            if (produce.length()>=p.getSymbol().length()&& produce.substring(0, p.getSymbol().length()).equals(p.getSymbol())) {
                return true;
            }
        }
        return false;
    }
    
    
    
    //Establece conjunto de primeros
    public void primeros(){
        primeros=new ArrayList();
        //System.out.println("PRIMEROS");
        producciones.forEach((produccion)->{
            primeros.add(new Conjunto(produccion));
            for(int i=0;i<produccion.getProducciones().size();i++){
                alfaPrimero(producciones.indexOf(produccion),i,produccion.getProduccion(i));
            } 
            
        });
    }
    
    //Retorna el valor de alfa_i
    public String getAlfa(String produce, int j){
        return produce.substring(j, j+1);
    }
    
    //Subrutina que ahorra código buscando alfa
    public void alfaPrimero(int indexProduccion,int indexProduce,String produce){
        int indexAlfa=0;
        boolean derivaEpsilon=setPrimeros(indexProduccion,indexProduce,getAlfa(produce,indexAlfa));
        while(derivaEpsilon && indexAlfa<produce.length()){
            indexAlfa++;
            if (indexAlfa<produce.length()) {
                derivaEpsilon=setPrimeros(indexProduccion,indexProduce,getAlfa(produce,indexAlfa));
            }
        } 
    }
    
    //Busca recursivamente el terminal que corresponde al primer termino de la i-esimo produce, devuelve true si deriva a epsilon
    public boolean setPrimeros(int indexProduccion,int indexProduce, String alfa){
        if (isTerminal(alfa)) {
            if(!primeros.get(indexProduccion).getSimbolos().contains(alfa)){
                primeros.get(indexProduccion).setSimbolo(alfa,indexProduce);
            }
            return alfa.equals(epsilon);
        }else{
            Produccion produccion=producciones.get(noTerminales.indexOf(alfa));
            for(int i=0;i<produccion.getProducciones().size();i++){
                alfaPrimero(indexProduccion,indexProduce,produccion.getProduccion(i));
            } 
        }
        return false;
    }
    
    
    
    //Establece el conjunto de siguientes
    public void siguientes(){
        //System.out.println("SIGUIENTES");
        siguientes=new ArrayList();
        producciones.forEach((produccion)->{
            siguientes.add(new Conjunto(produccion));
            int indexSiguiente=producciones.indexOf(produccion);
            producciones.forEach((produccionI)->{
                if (!produccion.equals(produccionI)) {
                    for(int i=0;i<produccionI.getProducciones().size();i++){
                        String produce=produccionI.getProduccion(i); 
                        Conjunto B=siguientes.get(indexSiguiente);
                        if (produce.contains(B.getSimboloProduccion())) {
                            int j=produce.indexOf(B.getSimboloProduccion());
                            String Beta=produce.substring(j+B.getSimboloProduccion().length(),produce.length());
                            if (Beta.isEmpty()) {
                                regla3AB(produccionI.getSymbol(),B);
                            }else{
                                Beta=Beta.substring(0, 1);
                                regla23ABB(indexSiguiente,produccionI.getSymbol(),B,Beta);
                            }
                        }
                    }
                }
            });
            if (indexSiguiente==0) {
                siguientes.get(0).setSimbolo(fs, 0);
            }
        });
        
    }
    
    //Regla 3 produccion de la forma A->aB
    public void regla3AB(String A,Conjunto B){
        siguientes.forEach((siguiente)->{
            if (siguiente.getSimboloProduccion().equals(A)) {
                B.union(siguiente.getSimbolos());
            }
        });
    }
    
    //Regla 2 y 3 produccion de la forma A->aBBeta
    public void regla23ABB(int i,String A,Conjunto B, String Beta){       
        if (isTerminal(Beta)) {
            siguientes.get(i).setSimbolo(Beta);
        }else{
            primeros.forEach((primero)->{
                if (primero.getSimboloProduccion().equals(Beta)) {
                    siguientes.get(i).union(primero.getSimbolos());
                    if (primero.getSimbolos().contains(epsilon)) {
                        regla3AB(A,B);
                    }
                }
            });
        }
    }
    
    
    
    //Return true si es un terminal
    public boolean isTerminal(String symbol){
        char terminal=symbol.toCharArray()[0];
        return !Character.isUpperCase(terminal);
    }
    
    //Establece los terminales de la gramatica
    public void setTerminales(){
        terminales=new ArrayList();
        producciones.forEach((Produccion p)->{
            p.getProducciones().forEach((String produce)->{
                for(int i=0;i<produce.length();i++){
                    if (isTerminal(produce.substring(i, i+1)) && !terminales.contains(produce.substring(i, i+1)) ) {
                        terminales.add(produce.substring(i, i+1));
                    }
                }
            });
        });
        if (terminales.contains(epsilon)) {
            terminales.remove(epsilon);
            terminales.add(epsilon);
        }
    }
    
    //Establece los no terminales de la gramatica
    public void setNoTerminales(){
        noTerminales=new ArrayList();
        setUnusedNoTerminales();
        producciones.forEach((Produccion p)->{
            noTerminales.add(p.getSymbol());
            unusedNoTerminales.remove(p.getSymbol());
        });
    }

    //Inicializa los valores posibles para un No Terminal que no han sido usados en la gramatica
    public void setUnusedNoTerminales(){
        unusedNoTerminales=new ArrayList();
        unusedNoTerminales.add("A");
        unusedNoTerminales.add("B");
        unusedNoTerminales.add("C");
        unusedNoTerminales.add("D");
        unusedNoTerminales.add("E");
        unusedNoTerminales.add("F");
        unusedNoTerminales.add("G");
        unusedNoTerminales.add("H");
        unusedNoTerminales.add("I");
        unusedNoTerminales.add("J");
        unusedNoTerminales.add("K");
        unusedNoTerminales.add("L");
        unusedNoTerminales.add("M");
        unusedNoTerminales.add("N");
        unusedNoTerminales.add("O");
        unusedNoTerminales.add("P");
        unusedNoTerminales.add("Q");
        unusedNoTerminales.add("R");
        unusedNoTerminales.add("S");
        unusedNoTerminales.add("T");
        unusedNoTerminales.add("U");
        unusedNoTerminales.add("V");
        unusedNoTerminales.add("W");
    }
    
    
    
    //Obtener tabla M
    public void setTablaM(){
        //System.out.println("Tabla M");
        tablaM=new String[noTerminales.size()+1][terminales.size()+1];
        tablaM[0][0]="NT/T";
        for (int i = 0; i < noTerminales.size()+1; i++) {
            if (i>0) {
                tablaM[i][0]=noTerminales.get(i-1);
            }
            for (int j = 0; j < terminales.size()+1; j++) {
                if (j>0) {
                    if(i==0){
                        tablaM[0][j]=terminales.get(j-1);
                        if (j==terminales.size()) {
                            tablaM[0][j]=fs;
                        }
                    }else{
                        tablaM[i][j]=!primeros.get(i-1).getPruce(tablaM[0][j]).equals("") ? tablaM[i][0]+"->"+primeros.get(i-1).getPruce(tablaM[0][j]) : "";
                        if(primeros.get(i-1).getSimbolos().contains(epsilon)){
                            for (int k = 0; k < siguientes.get(i-1).getSimbolos().size(); k++) {
                                if (tablaM[0][j].equals(siguientes.get(i-1).getSimbolo(k))) {
                                    tablaM[i][j]=tablaM[i][0]+"->"+primeros.get(i-1).getPruce(epsilon);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    @SuppressWarnings("empty-statement")
    public void validarCadena(String cadena){
        pila=new ArrayList();
        entrada=new ArrayList();
        salida=new ArrayList();
        
        pila.add(fs+producciones.get(0).getSymbol());
        entrada.add(cadena+fs);
        salida.add(" ");
        int i=0;
        String X=pila.get(i).substring(pila.get(i).length()-1, pila.get(i).length());
        while(!X.equals(fs)){
            String a=entrada.get(i).substring(0, 1);
            X=pila.get(i).substring(pila.get(i).length()-1, pila.get(i).length());;
            if(isTerminal(X)){
                if (X.equals(a)) {
                    if (X.equals(fs)&& a.equals(fs)) {
                        salida.add("Aceptar");
                    }else{
                        pila.add(pila.get(i).substring(0, pila.get(i).length()-1));
                        entrada.add(entrada.get(i).substring(1, entrada.get(i).length()));
                        salida.add(" ");
                    }
                    i++;
                }else{
                    salida.add("Rechazar");
                    break;
                }
            }else{
                if (noTerminales.contains(X) && (a.equals(fs) || terminales.contains(a))) {
                    int f=(terminales.contains(a))? terminales.indexOf(a)+1: tablaM[0].length-1;
                    String produce=tablaM[noTerminales.indexOf(X)+1][f];
                    if (!produce.isEmpty() && produce.matches("^[A-Z]'?->(\\w|\\W)*$")) {
                        X=produce.split("->")[1];
                        String simbol=invertirProduce(X);
                        if (simbol.equals(epsilon)) {
                            pila.add(pila.get(i).substring(0, pila.get(i).length()-1));
                        }else{
                            pila.add(pila.get(i).substring(0, pila.get(i).length()-1)+invertirProduce(X));
                        }
                        entrada.add(entrada.get(i));
                        salida.add(produce);
                        i++;
                    }else{
                        salida.add("Rechazar");
                        break;
                    }
                }else{
                    salida.add("Rechazar");
                    break;
                }
            }
        }
    }
    
    
    private String invertirProduce(String cadena) {
        String invertida = "";
        for(int i=cadena.length();i>=1;i--){
            String simbolo=cadena.substring(i-1, i);
            invertida = invertida + simbolo;
        }
        return invertida;
    }
    
    
    
    public ArrayList<Produccion> getProducciones() {
        return producciones;
    }
    
    public ArrayList<Conjunto> getPrimeros() {
        return primeros;
    }

    public ArrayList<Conjunto> getSiguientes() {
        return siguientes;
    }
    
    public String[][] getTablaM() {
        return tablaM;
    }
    
    public ArrayList<String> getNoTerminales() {
        return noTerminales;
    }
    
    public ArrayList<String> getPila() {
        return pila;
    }

    public ArrayList<String> getEntrada() {
        return entrada;
    }

    public ArrayList<String> getSalida() {
        return salida;
    }
}

