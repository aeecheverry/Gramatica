/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gramatica;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author andre
 */
public class Render extends DefaultTableCellRenderer{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component c=super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
        if (row==table.getModel().getRowCount()-1) {
            TableModel tb=table.getModel();
            if (tb.getValueAt(tb.getRowCount()-1, 2).equals("Aceptar")){
                this.setOpaque(true);
                c.setBackground(Color.GREEN);
            }else{
                c.setBackground(Color.RED);
            }
        }else{
            c.setBackground(Color.WHITE);
        }
        c.setForeground(Color.BLACK);
        if (column==1) {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }else{
            setHorizontalAlignment(SwingConstants.LEFT);
        }
        return c;
   }
}
