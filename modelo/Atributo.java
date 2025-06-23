package modelo;

public class Atributo {
    private String nombre;
    private String valor;

    public Atributo(String nombre, String valor) {
        this.nombre = nombre != null ? nombre : "";
        this.valor = valor != null ? valor : "";
    }

    public void setNombre(String nombre) {
        this.nombre = nombre != null ? nombre : "";
    }

    public void setValor(String valor) {
        this.valor = valor != null ? valor : "";
    }

    public String getNombre() {
        return nombre;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return nombre + "=\"" + valor.replace("\"", "&quot;") + "\"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Atributo atributo = (Atributo) o;
        return nombre.equals(atributo.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }
}