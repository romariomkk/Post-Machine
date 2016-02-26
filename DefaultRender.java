package main;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class DefaultRender extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent( JTable table, Object value,
    boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected,
        hasFocus, row, column);
        cell.setBackground(Color.WHITE);
        return cell; 
    }
}
