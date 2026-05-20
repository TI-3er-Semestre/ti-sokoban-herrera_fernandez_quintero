package com.icesi.sokoban.structure.graph;

/**
 * Vertice generico de un grafo.
 *
 * Guarda el contenido del vertice y los atributos que necesitan los
 * recorridos BFS y DFS vistos en clase:
 *
 *   color   - estado durante el recorrido (BLANCO, GRIS, NEGRO).
 *   distancia (d) - distancia desde el origen en numero de aristas (BFS).
 *   predecesor (pi) - vertice padre en el arbol del recorrido.
 *   tiempoDescubrimiento (d) - marca de tiempo al pasar a GRIS (DFS).
 *   tiempoFinalizacion (f)   - marca de tiempo al pasar a NEGRO (DFS).
 *
 * NOTA: esta clase pertenece a la estructura base del grafo (Persona A).
 * Se incluye aqui para que el codigo de Persona C compile de forma
 * independiente. Al integrar, debe quedar una sola version compartida.
 *
 * @param <T> tipo del contenido almacenado en el vertice
 */
public class Vertice<T> {

    private T contenido;
    private Color color;
    private int distancia;
    private Vertice<T> predecesor;
    private int tiempoDescubrimiento;
    private int tiempoFinalizacion;

    /**
     * Crea un vertice con el contenido dado.
     * El vertice empieza BLANCO, sin distancia y sin predecesor.
     *
     * @param contenido valor que almacena el vertice
     */
    public Vertice(T contenido) {
        this.contenido = contenido;
        this.color = Color.BLANCO;
        this.distancia = Integer.MAX_VALUE;
        this.predecesor = null;
        this.tiempoDescubrimiento = 0;
        this.tiempoFinalizacion = 0;
    }

    public T getContenido() {
        return contenido;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public Vertice<T> getPredecesor() {
        return predecesor;
    }

    public void setPredecesor(Vertice<T> predecesor) {
        this.predecesor = predecesor;
    }

    public int getTiempoDescubrimiento() {
        return tiempoDescubrimiento;
    }

    public void setTiempoDescubrimiento(int tiempoDescubrimiento) {
        this.tiempoDescubrimiento = tiempoDescubrimiento;
    }

    public int getTiempoFinalizacion() {
        return tiempoFinalizacion;
    }

    public void setTiempoFinalizacion(int tiempoFinalizacion) {
        this.tiempoFinalizacion = tiempoFinalizacion;
    }

    /**
     * Reinicia los atributos de recorrido del vertice.
     * Se llama antes de cada BFS o DFS para empezar limpio.
     */
    public void reiniciar() {
        this.color = Color.BLANCO;
        this.distancia = Integer.MAX_VALUE;
        this.predecesor = null;
        this.tiempoDescubrimiento = 0;
        this.tiempoFinalizacion = 0;
    }

    @Override
    public String toString() {
        return String.valueOf(contenido);
    }
}
