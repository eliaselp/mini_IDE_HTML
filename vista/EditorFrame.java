package vista;

import controlador.EditorController;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import modelo.EtiquetaHTML;
import modelo.EtiquetaContenidoMixto;

public class EditorFrame extends JFrame implements ChangeListener {
    private final EditorController controller;
    private final JTextArea areaHTML;
    private final JLabel lblEstado;
    private final PanelAtributos panelAtributos;
    private final JTextArea txtContenido;
    private final JButton btnFinalizar;
    
    public EditorFrame() {
        this.controller = new EditorController();
        this.areaHTML = new JTextArea();
        this.lblEstado = new JLabel("", JLabel.CENTER);
        this.panelAtributos = new PanelAtributos(controller);
        this.txtContenido = new JTextArea();
        this.btnFinalizar = new JButton("Finalizar Etiqueta");
        
        configurarVentana();
        initComponentes();
        actualizarVista();
        
        controller.addChangeListener(this);
    }
    
    private void configurarVentana() {
        setTitle("Editor HTML");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void initComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        PanelHerramientas panelHerramientas = new PanelHerramientas(controller, this);
        panelPrincipal.add(panelHerramientas, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        JPanel panelEdicion = new JPanel(new BorderLayout());
        areaHTML.setEditable(false);
        areaHTML.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollHTML = new JScrollPane(areaHTML);
        panelEdicion.add(scrollHTML, BorderLayout.CENTER);
        
        JPanel panelDerecha = new JPanel(new GridLayout(2, 1));
        panelDerecha.add(panelAtributos);
        
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBorder(BorderFactory.createTitledBorder("Contenido"));
        txtContenido.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        panelContenido.add(scrollContenido, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnFinalizar.addActionListener(e -> {
            controller.finalizarEtiquetaActual();
            actualizarVista();
        });
        
        JButton btnAplicar = new JButton("Aplicar Contenido");
        btnAplicar.addActionListener(this::aplicarContenido);
        
        panelBotones.add(btnAplicar);
        panelBotones.add(btnFinalizar);
        panelContenido.add(panelBotones, BorderLayout.SOUTH);
        
        panelDerecha.add(panelContenido);
        splitPane.setLeftComponent(panelEdicion);
        splitPane.setRightComponent(panelDerecha);
        splitPane.setDividerLocation(600);
        
        panelPrincipal.add(splitPane, BorderLayout.CENTER);
        panelPrincipal.add(lblEstado, BorderLayout.SOUTH);
        
        JMenuBar menuBar = crearMenu();
        setJMenuBar(menuBar);
        
        add(panelPrincipal);
    }
    
    private JMenuBar crearMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuArchivo = new JMenu("Archivo");
        
        JMenuItem itemGuardar = new JMenuItem("Guardar");
        itemGuardar.addActionListener(this::guardarDocumento);
        
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        
        menuArchivo.add(itemGuardar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        
        menuBar.add(menuArchivo);
        return menuBar;
    }
    
    public void actualizarVista() {
        SwingUtilities.invokeLater(() -> {
            areaHTML.setText(controller.getHTML());
            lblEstado.setText("Etiqueta actual: " + controller.getNombreEtiquetaActual());
            
            EtiquetaHTML etiquetaActual = controller.getEtiquetaActual();
            if (etiquetaActual instanceof EtiquetaContenidoMixto) {
                txtContenido.setText(((EtiquetaContenidoMixto)etiquetaActual).getContenidoTexto());
            } else {
                txtContenido.setText("");
            }
            
            panelAtributos.actualizarAtributos(etiquetaActual);
            btnFinalizar.setEnabled(controller.puedeFinalizarEtiqueta());
            
            areaHTML.revalidate();
            areaHTML.repaint();
        });
    }
    
    private void aplicarContenido(ActionEvent e) {
        EtiquetaHTML etiqueta = controller.getEtiquetaActual();
        if (etiqueta instanceof EtiquetaContenidoMixto) {
            ((EtiquetaContenidoMixto)etiqueta).setContenidoTexto(txtContenido.getText());
            actualizarVista();
        }
    }
    
    private void guardarDocumento(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar documento HTML");
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            
            if (!filePath.toLowerCase().endsWith(".html")) {
                filePath += ".html";
            }
            String contenido = controller.getHTML();
            if (controller.guardarDocumento(filePath, contenido)) {
                JOptionPane.showMessageDialog(this, 
                    "Documento guardado exitosamente en:\n" + filePath, 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al guardar el documento", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        actualizarVista();
    }
}