package modelo.etiquetas;

import modelo.Atributo;
import modelo.EtiquetaContenidoMixto;

public class EtiquetaFONT extends EtiquetaContenidoMixto {
    public EtiquetaFONT() {
        super("font");
        agregarAtributo("size", "3");
        agregarAtributo("color", "black");
    }
}