package modelo;

public class EtiquetaSimple extends EtiquetaHTML {
    public EtiquetaSimple(String nombre) {
        super(nombre);
    }

    @Override
    public String generarHTML() {
        return "<" + nombre + generarAtributos() + ">";
    }
}