package vista;

import controlador.EditorController;
import modelo.Atributo;
import modelo.EtiquetaHTML;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PanelAtributos extends JPanel {
    private final EditorController controller;
    private final JTable tablaAtributos;
    private final AtributosTableModel modeloTabla;
    
    public PanelAtributos(EditorController controller) {
        this.controller = controller;
        this.modeloTabla = new AtributosTableModel(new ArrayList<>());
        this.tablaAtributos = new JTable(modeloTabla);
        
        configurarPanel();
    }
    
    private void configurarPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Atributos"));
        
        tablaAtributos.setPreferredScrollableViewportSize(new Dimension(250, 150));
        JScrollPane scroll = new JScrollPane(tablaAtributos);
        add(scroll, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(this::agregarAtributo);
        
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(this::eliminarAtributo);
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void agregarAtributo(ActionEvent e) {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del atributo:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            String valor = JOptionPane.showInputDialog(this, "Valor del atributo:");
            modeloTabla.agregarAtributo(new Atributo(nombre, valor));
        }
    }
    
    private void eliminarAtributo(ActionEvent e) {
        int fila = tablaAtributos.getSelectedRow();
        if (fila >= 0) {
            modeloTabla.eliminarAtributo(fila);
        }
    }
    
    public void actualizarAtributos(EtiquetaHTML etiqueta) {
        if (etiqueta != null) {
            modeloTabla.setAtributos(new ArrayList<>(etiqueta.getAtributos()));
        } else {
            modeloTabla.setAtributos(new ArrayList<>());
        }
    }
    
    private class AtributosTableModel extends AbstractTableModel {
        private List<Atributo> atributos;
        
        public AtributosTableModel(List<Atributo> atributosIniciales) {
            this.atributos = new ArrayList<>(atributosIniciales);
        }
        
        public void setAtributos(List<Atributo> nuevosAtributos) {
            this.atributos = new ArrayList<>(nuevosAtributos);
            fireTableDataChanged();
        }
        
        public void agregarAtributo(Atributo atributo) {
            atributos.add(atributo);
            fireTableRowsInserted(atributos.size()-1, atributos.size()-1);
        }
        
        public void eliminarAtributo(int fila) {
            if (fila >= 0 && fila < atributos.size()) {
                atributos.remove(fila);
                fireTableRowsDeleted(fila, fila);
            }
        }
        
        @Override
        public int getRowCount() {
            return atributos.size();
        }
        
        @Override
        public int getColumnCount() {
            return 2;
        }
        
        @Override
        public String getColumnName(int column) {
            return column == 0 ? "Nombre" : "Valor";
        }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= 0 && rowIndex < atributos.size()) {
                Atributo attr = atributos.get(rowIndex);
                return columnIndex == 0 ? attr.getNombre() : attr.getValor();
            }
            return null;
        }
        
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
        
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (rowIndex >= 0 && rowIndex < atributos.size() && aValue != null) {
                Atributo attr = atributos.get(rowIndex);
                if (columnIndex == 0) {
                    attr = new Atributo(aValue.toString(), attr.getValor());
                } else {
                    attr = new Atributo(attr.getNombre(), aValue.toString());
                }
                atributos.set(rowIndex, attr);
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }
    }
}