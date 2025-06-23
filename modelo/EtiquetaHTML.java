package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class EtiquetaHTML {
    protected final String nombre;
    protected final List<Atributo> atributos;

    public EtiquetaHTML(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre de la etiqueta no puede ser nulo");
        this.atributos = new ArrayList<>();
    }

    public void agregarAtributo(Atributo atributo) {
        if (atributo != null) {
            eliminarAtributo(atributo.getNombre());
            atributos.add(atributo);
        }
    }

    public void agregarAtributo(String nombre, String valor) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            agregarAtributo(new Atributo(nombre, valor));
        }
    }

    public boolean tieneAtributo(String nombre) {
        return atributos.stream().anyMatch(a -> a.getNombre().equals(nombre));
    }

    public void eliminarAtributo(String nombre) {
        if (nombre != null) {
            atributos.removeIf(a -> a.getNombre().equals(nombre));
        }
    }

    public void modificarAtributo(Atributo atributo) {
        if (atributo != null) {
            eliminarAtributo(atributo.getNombre());
            agregarAtributo(atributo);
        }
    }

    public Atributo getAtributo(String nombre) {
        return atributos.stream()
                .filter(a -> a.getNombre().equals(nombre))
                .findFirst()
                .orElse(null);
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

    @Override
    public String toString() {
        return nombre + generarAtributos();
    }
}