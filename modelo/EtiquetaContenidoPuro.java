package modelo;

public class EtiquetaContenidoPuro extends EtiquetaContenido {
    public EtiquetaContenidoPuro(String nombre) {
        super(nombre);
    }

    @Override
    protected boolean validarInsercion(EtiquetaHTML etiqueta) {
        switch (this.nombre.toLowerCase()) {
            case "ul":
                return etiqueta.getNombre().equalsIgnoreCase("li");
            case "table":
                return etiqueta.getNombre().equalsIgnoreCase("tr");
            case "tr":
                return etiqueta.getNombre().equalsIgnoreCase("td");
            default:
                return true;
        }
    }

    @Override
    public String generarHTML() {
        return "<" + nombre + generarAtributos() + ">\n" + 
               generarContenido() + 
               "</" + nombre + ">\n";
    }
}