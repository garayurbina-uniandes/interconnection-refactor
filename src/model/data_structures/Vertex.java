package model.data_structures;

import java.util.Comparator;

public class Vertex<K extends Comparable<K>, V extends Comparable<V>> implements Comparable<Vertex<K, V>> {
    private K key;
    private V value;
    private ILista<Edge<K, V>> arcos;
    private boolean marked;

    public Vertex(K id, V value) {
        this.key = id;
        this.value = value;
        this.arcos = new ArregloDinamico<Edge<K, V>>(1);
    }

    public K getId() {
        return key;
    }

    public ILista<Edge<K, V>> edges() {
        return arcos;
    }

    public Edge<K, V> getEdge(K vertex) {
        Edge<K, V> retorno = null;
        for (int i = 1; i <= arcos.size(); i++) {
            try {
                if (arcos.getElement(i).getDestination().getId().compareTo(vertex) == 0) {
                    retorno = arcos.getElement(i);
                }
            } catch (PosException | VacioException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return retorno;

    }

	public int indegree() {
		// Assuming each edge represents an incoming connection
		return arcos.size();
	}

	public int outdegree() {
		// Assuming each edge represents an outgoing connection
		return arcos.size();
	}


    public V getInfo() {
        return value;
    }

    public boolean getMark() {
        return marked;
    }

    public void mark() {
        marked = true;
    }

    public void unmark() {
        marked = false;
    }

    public void addEdge(Edge<K, V> edge) {
        try {
            arcos.insertElement(edge, arcos.size() + 1);
        } catch (PosException | NullException e) {
            e.printStackTrace();
        }
    }

    public ILista<Vertex<K, V>> vertices() {
        ILista<Vertex<K, V>> retorno = new ArregloDinamico<>(1);
        for (int i = 1; i <= arcos.size(); i++) {
            try {
                retorno.insertElement(arcos.getElement(i).getDestination(), retorno.size() + 1);
            } catch (PosException | NullException | VacioException e) {
                e.printStackTrace();
            }
        }
        return retorno;
    }

    public void bfs() {
        GraphTraversal.bfs(this);
    }

    public void dfs(Edge<K, V> edgeTo) {
        GraphTraversal.dfs(this, edgeTo);
    }

    public void topologicalOrder(ColaEncadenada<Vertex<K, V>> pre, ColaEncadenada<Vertex<K, V>> post, PilaEncadenada<Vertex<K, V>> reversePost) {
        GraphTraversal.topologicalOrder(this, pre, post, reversePost);
    }

    public void getSCC(ITablaSimbolos<K, Integer> tabla, int idComponente) {
        GraphTraversal.getSCC(this, tabla, idComponente);
    }

    @Override
    public int compareTo(Vertex<K, V> o) {
        return key.compareTo(o.getId());
    }
}

