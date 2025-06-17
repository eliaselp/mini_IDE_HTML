package modelo.etiquetas;

import modelo.Atributo;
import modelo.EtiquetaSimple;

public class EtiquetaHR extends EtiquetaSimple {
    public EtiquetaHR() {
        super("hr");
        agregarAtributo("width", "100%");
        agregarAtributo("color", "black");
    }
}