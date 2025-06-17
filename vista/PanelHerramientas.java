package vista;

import controlador.EditorController;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class PanelHerramientas extends JPanel {
    private final EditorController controller;
    private final EditorFrame frame;
    private final Map<String, String> etiquetasDisponibles;
    
    public PanelHerramientas(EditorController controller, EditorFrame frame) {
        this.controller = controller;
        this.frame = frame;
        this.etiquetasDisponibles = crearMapaEtiquetas();
        
        configurarPanel();
    }
    
    private Map<String, String> crearMapaEtiquetas() {
        Map<String, String> mapa = new HashMap<>();
        mapa.put("IMG", "modelo.etiquetas.EtiquetaIMG");
        mapa.put("BR", "modelo.etiquetas.EtiquetaBR");
        mapa.put("HR", "modelo.etiquetas.EtiquetaHR");
        mapa.put("UL", "modelo.etiquetas.EtiquetaUL");
        mapa.put("LI", "modelo.etiquetas.EtiquetaLI");
        mapa.put("FONT", "modelo.etiquetas.EtiquetaFONT");
        mapa.put("TABLE", "modelo.etiquetas.EtiquetaTABLE");
        mapa.put("TR", "modelo.etiquetas.EtiquetaTR");
        mapa.put("TD", "modelo.etiquetas.EtiquetaTD");
        mapa.put("P", "modelo.etiquetas.EtiquetaP");
        mapa.put("A", "modelo.etiquetas.EtiquetaA");
        return mapa;
    }
    
    private void configurarPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Insertar Etiqueta"));
        
        for (String nombreEtiqueta : etiquetasDisponibles.keySet()) {
            JButton boton = crearBotonEtiqueta(nombreEtiqueta);
            add(boton);
        }
    }
    
    private JButton crearBotonEtiqueta(String nombreEtiqueta) {
        JButton boton = new JButton(nombreEtiqueta);
        boton.setToolTipText("Insertar etiqueta " + nombreEtiqueta);
        boton.addActionListener(e -> insertarEtiqueta(nombreEtiqueta));
        return boton;
    }
    
    private void insertarEtiqueta(String nombreEtiqueta) {
        String claseEtiqueta = etiquetasDisponibles.get(nombreEtiqueta);
        if (claseEtiqueta != null) {
            try {
                Class<?> clazz = Class.forName(claseEtiqueta);
                Object instance = clazz.getDeclaredConstructor().newInstance();
                boolean exito = controller.agregarElemento((modelo.EtiquetaHTML) instance);
                
                if (exito) {
                    frame.actualizarVista();
                } else {
                    javax.swing.JOptionPane.showMessageDialog(frame, 
                        "No se puede insertar " + nombreEtiqueta + " en el contexto actual", 
                        "Error de inserción", javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(frame, 
                    "Error al crear etiqueta: " + ex.getMessage(), 
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}