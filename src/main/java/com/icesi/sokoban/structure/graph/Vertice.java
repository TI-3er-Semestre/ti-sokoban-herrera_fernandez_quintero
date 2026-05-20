package com.icesi.sokoban.structure.graph;

/**
 * Representa un vértice del grafo genérico.
 * Almacena el contenido y los atributos usados por DFS y BFS.
 *
 * @param <T> tipo del contenido almacenado en el vértice
 */
public class Vertice<T> {

    public enum Color {
        BLANCO,  // no visitado
        GRIS,    // descubierto, en proceso
        NEGRO    // finalizado
    }

    private T contenido;
    private Color color;
    private int distancia;
    private Vertice<T> predecesor;
    private int tiempoDescubrimiento;
    private int tiempoFinalizacion;

    public Vertice(T contenido) {
        this.contenido = contenido;
        this.color = Color.BLANCO;
        this.distancia = 0;
        this.predecesor = null;
        this.tiempoDescubrimiento = 0;
        this.tiempoFinalizacion = 0;
    }

    public T getContenido() { return contenido; }
    public void setContenido(T contenido) { this.contenido = contenido; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public int getDistancia() { return distancia; }
    public void setDistancia(int distancia) { this.distancia = distancia; }

    public Vertice<T> getPredecesor() { return predecesor; }
    public void setPredecesor(Vertice<T> predecesor) { this.predecesor = predecesor; }

    public int getTiempoDescubrimiento() { return tiempoDescubrimiento; }
    public void setTiempoDescubrimiento(int t) { this.tiempoDescubrimiento = t; }

    public int getTiempoFinalizacion() { return tiempoFinalizacion; }
    public void setTiempoFinalizacion(int t) { this.tiempoFinalizacion = t; }

    @Override
    public String toString() {
        return "Vertice{contenido=" + contenido + ", color=" + color
                + ", d=" + tiempoDescubrimiento + ", f=" + tiempoFinalizacion + "}";
    }
}
