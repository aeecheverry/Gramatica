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
public class Analisis {
    ArrayList<Produccion> producciones;

    
    ArrayList<String> terminales;
    ArrayList<String> noTerminales;

    ArrayList<String> unusedNoTerminales;
    ArrayList<String> simbolosGramaticales;
    ArrayList<Conjunto> primeros;
    ArrayList<Conjunto> siguientes;
    String[][] tablaM;

    final String epsilon="&";
    final String fs="$";
    
    public Analisis(ArrayList<Produccion> producciones){
        this.producciones=producciones;
    }
    
    /*public static void main(String[] args) {
        Analisis analisis=new Analisis();
        
        analisis.gramaticaSample();
        
        analisis.setNoTerminales();
        analisis.setTerminales();
        analisis.printGramatica();
        
        analisis.eliminarRecursividad();
        analisis.factorizar();
        analisis.setNoTerminales();
        analisis.setTerminales();
        analisis.printGramatica();
        analisis.setTablaM();
    }*/
    
    
    
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
        String newProduccion=produccion.getSymbol()+"'";
        if (!unusedNoTerminales.contains(newProduccion)) {
            newProduccion=unusedNoTerminales.get((int)(Math.random()*(unusedNoTerminales.size()-6)));
        }
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
            String newProduccion=produccion.getSymbol()+"'";
            int ipr=producciones.indexOf(produccion)+1;
            if (recursiva) {
                newProduccion=!unusedNoTerminales.contains(newProduccion)? unusedNoTerminales.get((int)(Math.random()*(unusedNoTerminales.size()-6))): newProduccion;
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
        
        //Muestra en consola el conjunto PRIMEROS
        /*primeros.forEach((primero)->{
            System.out.printf("%-2s = [ ",primero.getSimboloProduccion());
            for(int i=0;i<primero.getSimbolos().size();i++){
                String terminal=primero.getSimbolo(i);
                String produce=primero.getPruce(terminal);
                System.out.printf(" %-1s : %-1s,",terminal,produce);
            }
            System.out.print(" ]");
            System.out.println();
        });*/
    }
    
    //Retorna el valor de alfa_i
    public String getAlfa(String produce, int j){
        return (j+2<produce.length() && noTerminales.contains(produce.substring(j, j+2))) ? produce.substring(j, j+2) : produce.substring(j,j+1);
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
                                if (Beta.length()>1) {
                                   Beta=noTerminales.contains(Beta.substring(0, 2)) ? Beta.substring(0, 2) : Beta.substring(0, 1);
                                }
                                if (!Beta.equals("'")) {
                                    regla23ABB(indexSiguiente,produccionI.getSymbol(),B,Beta);
                                }
                            }
                        }
                    }
                }
            });
            if (indexSiguiente==0) {
                siguientes.get(0).setSimbolo(fs, 0);
            }
        });
        
        //Escribe en consola el conjunto SIGUIENTES
        /*siguientes.forEach((siguiente)->{
            System.out.print(siguiente.getSimboloProduccion()+" = ");
            System.out.print(siguiente.getSimbolos().toString());
            System.out.println();
        });*/
        
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
                    if (isTerminal(produce.substring(i, i+1)) && !produce.substring(i, i+1).equals("'") && !terminales.contains(produce.substring(i, i+1)) ) {
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
        unusedNoTerminales.add("A'");
        unusedNoTerminales.add("B'");
        unusedNoTerminales.add("C'");
        unusedNoTerminales.add("D'");
        unusedNoTerminales.add("E'");
        unusedNoTerminales.add("F'");
        unusedNoTerminales.add("G'");
        unusedNoTerminales.add("H'");
        unusedNoTerminales.add("I'");
        unusedNoTerminales.add("J'");
        unusedNoTerminales.add("K'");
        unusedNoTerminales.add("L'");
        unusedNoTerminales.add("M'");
        unusedNoTerminales.add("N'");
        unusedNoTerminales.add("O'");
        unusedNoTerminales.add("P'");
        unusedNoTerminales.add("Q'");
        unusedNoTerminales.add("R'");
        unusedNoTerminales.add("S'");
        unusedNoTerminales.add("T'");
        unusedNoTerminales.add("U'");
        unusedNoTerminales.add("V'");
        unusedNoTerminales.add("W'");
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
                //System.out.printf("%-13s", tablaM[i][j]);
            }
            //System.out.println();
        }
    }
    
    
    public void validarCadena(String cadena){
        String desp="desplazar";
        String red="reducir";
        String[][] tablaV=new String[100][3];
        int i=0;
        /*while(){
            for (int j = 0; j < 3; j++) {
                if (i==0) {
                    tablaV[i][0]=fs;
                    tablaV[i][1]=cadena;
                    tablaV[i][2]=desp;
                }else{
                    tablaV[i-1][0]=tablaV[i-1][0]+tablaV[i-1][1].substring(0, 1);
                        
                    
                }
            }
        }*/
         
    }
    
    //Gramatica de prueba
    public void gramaticaSample(){
        /*producciones=new ArrayList<>();
        producciones.add(new Produccion("E"));
        producciones.get(0).setProduccion("E+T");
        producciones.get(0).setProduccion("E-T");
        producciones.get(0).setProduccion("T");
        producciones.add(new Produccion("T"));
        producciones.get(1).setProduccion("T*F");
        producciones.get(1).setProduccion("T/F");
        producciones.get(1).setProduccion("F");
        //producciones.get(1).setProduccion("&");
        producciones.add(new Produccion("F"));
        producciones.get(2).setProduccion("i");
        producciones.get(2).setProduccion("(E)");*/
        
       /* producciones.add(new Produccion("E"));
        producciones.get(0).setProduccion("E+T");
        producciones.get(0).setProduccion("E+F");
        producciones.get(0).setProduccion("Ta");
        producciones.get(0).setProduccion("Tb");*/
    }
    
       
    //Imprime gramatica en consola
    /*public void printGramatica(){
        producciones.forEach((Produccion p)->{
            p.getProducciones().forEach((produce)->{
                System.out.println(p.getSymbol()+"->"+produce);
            });
        });
        System.out.println("Terminales: "+terminales.toString());
        System.out.println("No terminales: "+noTerminales.toString());
    }*/
    
    
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
}
