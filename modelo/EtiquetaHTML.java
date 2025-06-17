package modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class EtiquetaHTML {
    protected final String nombre;
    protected final List<Atributo> atributos;

    public EtiquetaHTML(String nombre) {
        this.nombre = nombre;
        this.atributos = new ArrayList<>();
    }

    public void agregarAtributo(String nombre, String valor) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            atributos.add(new Atributo(nombre, valor));
        }
    }

    public List<Atributo> getAtributos() {
        return new ArrayList<>(atributos);
    }

    public String getNombre() {
        return nombre;
    }

    protected String generarAtributos() {
        StringBuilder sb = new StringBuilder();
        for (Atributo attr : atributos) {
            sb.append(" ").append(attr.toString());
        }
        return sb.toString();
    }

    public abstract String generarHTML();
}