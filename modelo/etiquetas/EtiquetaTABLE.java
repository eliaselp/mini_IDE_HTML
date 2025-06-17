package modelo.etiquetas;

import modelo.Atributo;
import modelo.EtiquetaContenidoPuro;

public class EtiquetaTABLE extends EtiquetaContenidoPuro {
    public EtiquetaTABLE() {
        super("table");
        agregarAtributo("border", "1");
    }
}