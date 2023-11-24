package model.data_structures;

public class GrafoListaAdyacencia<K extends Comparable<K>, V extends Comparable<V>> {
	private ITablaSimbolos<K, Vertex<K, V>> vertices;
	private ILista<Edge<K, V>> arcos;
	private ILista<Vertex<K, V>> verticesLista;
	private int numEdges;

	public GrafoListaAdyacencia(int numVertices) {
		vertices = new TablaHashLinearProbing<K, Vertex<K, V>>(numVertices);
		numEdges = 0;
		arcos = new ArregloDinamico<>(1);
		verticesLista = new ArregloDinamico<>(1);
	}

	public boolean containsVertex(K id) {
		return vertices.contains(id);
	}

	public int numVertices() {
		return vertices.size();
	}

	public int numEdges() {
		return numEdges;
	}

	public void insertVertex(K id, V value) {
		vertices.put(id, new Vertex<K, V>(id, value));
		try {
			Vertex<K, V> vertice = getVertex(id);
			verticesLista.insertElement(vertice, verticesLista.size() + 1);
		} catch (PosException | NullException e) {
			e.printStackTrace();
		}
	}

	public void addEdge(K source, K dest, float weight) {
		Edge<K, V> existe = getEdge(source, dest);

		if (existe == null) {
			Vertex<K, V> origin = getVertex(source);
			Vertex<K, V> destination = getVertex(dest);
			Edge<K, V> arco1 = new Edge<K, V>(origin, destination, weight);
			origin.addEdge(arco1);

			Edge<K, V> arco2 = new Edge<K, V>(destination, origin, weight);
			destination.addEdge(arco2);
			numEdges++;
			try {
				arcos.insertElement(arco1, arcos.size() + 1);
			} catch (PosException | NullException e) {
				e.printStackTrace();
			}
		}
	}

	public Vertex<K, V> getVertex(K id) {
		return vertices.get(id);
	}

	public Edge<K, V> getEdge(K idS, K idD) {
		Vertex<K, V> origen = vertices.get(idS);

		if (origen == null) {
			return null;
		} else {
			return origen.getEdge(idD);
		}
	}

	public ILista<Edge<K, V>> adjacentEdges(K id) {
		Vertex<K, V> origen = vertices.get(id);
		return origen.edges();
	}

	public ILista<Vertex<K, V>> adjacentVertex(K id) {
		Vertex<K, V> origen = vertices.get(id);
		return origen.vertices();
	}

	public int indegree(K vertex) {
		Vertex<K, V> origen = vertices.get(vertex);
		return origen.indegree();
	}

	public int outdegree(K vertex) {
		Vertex<K, V> origen = vertices.get(vertex);
		return origen.outdegree();
	}

	public ILista<Edge<K, V>> edges() {
		return arcos;
	}

	public ILista<Vertex<K, V>> vertices() {
		return verticesLista;
	}

	public void unmark() {
		ILista<Vertex<K, V>> vertices = vertices();
		for (int i = 1; i <= vertices.size(); i++) {
			try {
				vertices.getElement(i).unmark();
			} catch (PosException | VacioException e) {
				e.printStackTrace();
			}
		}
	}

	public void dfs(K id) {
		Vertex<K, V> inicio = getVertex(id);
		inicio.dfs(null);
		unmark();
	}

	public void bfs(K id) {
		Vertex<K, V> inicio = getVertex(id);
		inicio.bfs();
		unmark();
	}

	public Edge<K, V> arcoMin() {
		return GraphOperations.arcoMin(this);
	}

	public Edge<K, V> arcoMax() {
		return GraphOperations.arcoMax(this);
	}

	public GrafoListaAdyacencia<K, V> reverse() {
		return GraphOperations.reverse(this);
	}

	public ITablaSimbolos<K, Integer> getSSC() {
		return GraphOperations.getSSC(this);
	}

	public PilaEncadenada<Vertex<K, V>> topologicalOrder() {
		return GraphOperations.topologicalOrder(this);
	}

	public ILista<Edge<K, V>> mstPrimLazy(K idOrigen) {
		return GraphOperations.mstPrimLazy(this, idOrigen);
	}

	public ITablaSimbolos<K, NodoTS<Float, Edge<K, V>>> minPathTree(K idOrigen) {
		return GraphOperations.minPathTree(this, idOrigen);
	}

	public PilaEncadenada<Edge<K, V>> minPath(K idOrigen, K idDestino) {
		return GraphOperations.minPath(this, idOrigen, idDestino);
	}
}
