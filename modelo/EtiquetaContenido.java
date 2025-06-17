package modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class EtiquetaContenido extends EtiquetaHTML {
    protected final List<EtiquetaHTML> etiquetasHijas;

    public EtiquetaContenido(String nombre) {
        super(nombre);
        this.etiquetasHijas = new ArrayList<>();
    }

    public boolean agregarEtiquetaHija(EtiquetaHTML etiqueta) {
        if (etiqueta != null && validarInsercion(etiqueta)) {
            etiquetasHijas.add(etiqueta);
            return true;
        }
        return false;
    }

    public List<EtiquetaHTML> getEtiquetasHijas() {
        return new ArrayList<>(etiquetasHijas);
    }

    protected abstract boolean validarInsercion(EtiquetaHTML etiqueta);

    protected String generarContenido() {
        StringBuilder sb = new StringBuilder();
        for (EtiquetaHTML etiqueta : etiquetasHijas) {
            sb.append(etiqueta.generarHTML());
        }
        return sb.toString();
    }
}