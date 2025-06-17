package controlador;

import modelo.EtiquetaContenido;
import modelo.EtiquetaHTML;
import modelo.etiquetas.EtiquetaBODY;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class EditorController {
    private final EtiquetaContenido documento;
    private final Stack<EtiquetaContenido> pilaAnidamiento;
    
    public EditorController() {
        this.pilaAnidamiento = new Stack<>();
        this.documento = crearDocumentoBase();
    }
    
    private EtiquetaContenido crearDocumentoBase() {
        EtiquetaContenido html = new EtiquetaContenido("html") {
            @Override
            protected boolean validarInsercion(EtiquetaHTML etiqueta) {
                return true; // Acepta cualquier etiqueta como hijo
            }

            @Override
            public String generarHTML() {
                return "<html" + generarAtributos() + ">" + 
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
                return true;
            }
        }
        return false;
    }

    public void finalizarEtiquetaActual() {
        if (pilaAnidamiento.size() > 1) {
            pilaAnidamiento.pop();
        }
    }

    public boolean puedeFinalizarEtiqueta() {
        return pilaAnidamiento.size() > 1;
    }

    public String getNombreEtiquetaActual() {
        return !pilaAnidamiento.isEmpty() ? pilaAnidamiento.peek().getNombre() : "";
    }

    public EtiquetaContenido getEtiquetaActual() {
        return !pilaAnidamiento.isEmpty() ? pilaAnidamiento.peek() : null;
    }

    public boolean guardarDocumento(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            return false;
        }
        
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write("<!DOCTYPE html>\n");
            writer.write(documento.generarHTML());
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar el documento: " + e.getMessage());
            return false;
        }
    }

    public String getHTML() {
        return "<!DOCTYPE html>\n" + documento.generarHTML();
    }
}