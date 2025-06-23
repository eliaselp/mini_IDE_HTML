package modelo;

public class EtiquetaContenidoMixto extends EtiquetaContenido {
    private String contenidoTexto;

    public EtiquetaContenidoMixto(String nombre) {
        super(nombre);
        this.contenidoTexto = "";
    }

    public void setContenidoTexto(String texto) {
        this.contenidoTexto = texto != null ? texto : "";
    }

    public String getContenidoTexto() {
        return contenidoTexto;
    }

    @Override
    protected boolean validarInsercion(EtiquetaHTML etiqueta) {
        return true;
    }

    @Override
    public String generarHTML() {
        return "<" + nombre + generarAtributos() + ">\n" + 
               contenidoTexto + generarContenido() + 
               "</" + nombre + ">\n";
    }
}