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
import javax.swing.SwingUtilities;

public class PanelAtributos extends JPanel {
    private final EditorController controller;
    private final JTable tablaAtributos;
    private final AtributosTableModel modeloTabla;
    private EtiquetaHTML etiquetaActual;
    
    public PanelAtributos(EditorController controller) {
        this.controller = controller;
        this.modeloTabla = new AtributosTableModel(new ArrayList<>());
        this.tablaAtributos = new JTable(modeloTabla);
        configurarPanel();
        actualizarEtiquetaActual();
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
            if (etiquetaActual != null && etiquetaActual.tieneAtributo(nombre)) {
                JOptionPane.showMessageDialog(this, 
                    "El atributo ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String valor = JOptionPane.showInputDialog(this, "Valor del atributo:");
            Atributo nuevo = new Atributo(nombre, valor != null ? valor : "");
            controller.agregarAtributoActual(nuevo);
            modeloTabla.agregarAtributo(nuevo);
        }
    }
    
    private void eliminarAtributo(ActionEvent e) {
        int fila = tablaAtributos.getSelectedRow();
        if (fila >= 0) {
            Atributo attr = modeloTabla.getAtributo(fila);
            controller.eliminarAtributoActual(attr.getNombre());
            modeloTabla.eliminarAtributo(fila);
        }
    }
    
    public void actualizarAtributos(EtiquetaHTML etiqueta) {
        this.etiquetaActual = etiqueta;
        if (etiqueta != null) {
            modeloTabla.setAtributos(new ArrayList<>(etiqueta.getAtributos()));
        } else {
            modeloTabla.setAtributos(new ArrayList<>());
        }
    }
    
    public void etiquetaSeleccionadaCambiada() {
        SwingUtilities.invokeLater(() -> {
            actualizarEtiquetaActual();
            tablaAtributos.revalidate();
            tablaAtributos.repaint();
        });
    }
    
    private void actualizarEtiquetaActual() {
        this.etiquetaActual = controller.getEtiquetaActual();
        actualizarAtributos(etiquetaActual);
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
        
        public Atributo getAtributo(int fila) {
            if (fila >= 0 && fila < atributos.size()) {
                return atributos.get(fila);
            }
            return null;
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
                    String nuevoNombre = aValue.toString();
                    if (!attr.getNombre().equals(nuevoNombre)) {
                        controller.eliminarAtributoActual(attr.getNombre());
                        attr.setNombre(nuevoNombre);
                        controller.agregarAtributoActual(attr);
                    }
                } else {
                    String nuevoValor = aValue.toString();
                    if (!attr.getValor().equals(nuevoValor)) {
                        attr.setValor(nuevoValor);
                        controller.modificarAtributoActual(attr);
                    }
                }
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }
    }
}