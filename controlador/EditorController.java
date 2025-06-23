package controlador;

import modelo.EtiquetaContenido;
import modelo.EtiquetaHTML;
import modelo.etiquetas.EtiquetaBODY;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import modelo.Atributo;

public class EditorController {
    private final EtiquetaContenido documento;
    private final Stack<EtiquetaContenido> pilaAnidamiento;
    private final List<ChangeListener> listeners;
    
    public EditorController() {
        this.pilaAnidamiento = new Stack<>();
        this.documento = crearDocumentoBase();
        this.listeners = new ArrayList<>();
    }
    
    private EtiquetaContenido crearDocumentoBase() {
        EtiquetaContenido html = new EtiquetaContenido("html") {
            @Override
            protected boolean validarInsercion(EtiquetaHTML etiqueta) {
                return true;
            }

            @Override
            public String generarHTML() {
                return "<html" + generarAtributos() + ">\n" + 
                       generarContenido() + 
                       "</html>";
            }
        };
        
        EtiquetaBODY body = new EtiquetaBODY();
        html.agregarEtiquetaHija(body);
        pilaAnidamiento.push(body);
        return html;
    }

    public boolean agregarElemento(EtiquetaHTML elemento) {
        if (!pilaAnidamiento.isEmpty() && elemento != null) {
            EtiquetaContenido padre = pilaAnidamiento.peek();
            if (padre.agregarEtiquetaHija(elemento)) {
                if (elemento instanceof EtiquetaContenido) {
                    pilaAnidamiento.push((EtiquetaContenido) elemento);
                }
                notificarCambios();
                return true;
            }
        }
        return false;
    }

    public void finalizarEtiquetaActual() {
        if (pilaAnidamiento.size() > 1) {
            pilaAnidamiento.pop();
            notificarCambios();
        }
    }

    public boolean puedeFinalizarEtiqueta() {
        return pilaAnidamiento.size() > 1;
    }

    public String getNombreEtiquetaActual() {
        return !pilaAnidamiento.isEmpty() ? pilaAnidamiento.peek().getNombre() : "";
    }

    public EtiquetaHTML getEtiquetaActual() {
        return !pilaAnidamiento.isEmpty() ? pilaAnidamiento.peek() : null;
    }

    public boolean guardarDocumento(String nombreArchivo, String contenido) {
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return false;
        }
        
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write(contenido);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar el documento: " + e.getMessage());
            return false;
        }
    }

    public String getHTML() {
        return "<!DOCTYPE html>\n" + documento.generarHTML();
    }

    public void notificarCambios() {
        ChangeEvent evt = new ChangeEvent(this);
        for (ChangeListener listener : listeners) {
            listener.stateChanged(evt);
        }
    }

    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    public void modificarAtributoActual(Atributo atributo) {
        EtiquetaHTML actual = getEtiquetaActual();
        if (actual != null && atributo != null) {
            actual.modificarAtributo(atributo);
            notificarCambios();
        }
    }

    public void eliminarAtributoActual(String nombre) {
        EtiquetaHTML actual = getEtiquetaActual();
        if (actual != null && nombre != null) {
            actual.eliminarAtributo(nombre);
            notificarCambios();
        }
    }

    public void agregarAtributoActual(Atributo atributo) {
        EtiquetaHTML actual = getEtiquetaActual();
        if (actual != null && atributo != null) {
            actual.agregarAtributo(atributo);
            notificarCambios();
        }
    }
}