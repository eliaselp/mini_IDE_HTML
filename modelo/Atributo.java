package modelo;

public class Atributo {
    private final String nombre;
    private final String valor;

    public Atributo(String nombre, String valor) {
        this.nombre = nombre != null ? nombre : "";
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
        return nombre + "=\"" + valor + "\"";
    }
}