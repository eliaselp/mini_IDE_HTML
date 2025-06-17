package modelo.etiquetas;

import modelo.Atributo;
import modelo.EtiquetaSimple;

public class EtiquetaIMG extends EtiquetaSimple {
    public EtiquetaIMG() {
        super("img");
        agregarAtributo("src", "");
        agregarAtributo("width", "100");
        agregarAtributo("height", "100");
        agregarAtributo("alt", "imagen");
    }
}