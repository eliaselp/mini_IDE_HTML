package modelo.etiquetas;

import modelo.Atributo;
import modelo.EtiquetaContenidoMixto;

public class EtiquetaA extends EtiquetaContenidoMixto {
    public EtiquetaA() {
        super("a");
        agregarAtributo("href", "#");
    }
}