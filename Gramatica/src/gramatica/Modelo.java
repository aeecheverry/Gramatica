/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gramatica;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author andre
 */
public class Modelo extends AbstractTableModel{
    private final String[] columnNames={"Pila","Entrada","Salida"};
    String[][] table;
    
    public Modelo(){
        super();
    }
    
    public void setData(String[][] data){
        table=data;
    }
    
    @Override
    public int getRowCount() {
        return table.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return table[rowIndex][columnIndex];
    }
    
}
