package model.data_structures;

import java.util.ArrayList;
import java.util.List;

public class GraphOperations {


    public static <K extends Comparable<K>, V extends Comparable<V>> void dfs(GrafoListaAdyacencia<K, V> graph, K id) {
        Vertex<K, V> startVertex = graph.getVertex(id);
        if (startVertex != null) {
            startVertex.dfs(null);
            graph.unmark();
        } else {
            System.out.println("Vertex with ID " + id + " not found in the graph.");
        }
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> void bfs(GrafoListaAdyacencia<K, V> graph, K id) {
        Vertex<K, V> startVertex = graph.getVertex(id);
        if (startVertex != null) {
            startVertex.bfs();
            graph.unmark();
        } else {
            System.out.println("Vertex with ID " + id + " not found in the graph.");
        }
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> Edge<K, V> arcoMin(GrafoListaAdyacencia<K, V> graph) {
        ILista<Edge<K, V>> edges = graph.edges();
        if (!edges.isEmpty()) {
            try {
                Edge<K, V> minEdge = edges.getElement(1);
                float minWeight = minEdge.getWeight();

                for (int i = 2; i <= edges.size(); i++) {
                    Edge<K, V> currentEdge = edges.getElement(i);
                    if (currentEdge.getWeight() < minWeight) {
                        minEdge = currentEdge;
                        minWeight = currentEdge.getWeight();
                    }
                }

                return minEdge;
            } catch (PosException | VacioException e) {
                e.printStackTrace();
            }
        }

        System.out.println("No edges in the graph.");
        return null;
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> Edge<K, V> arcoMax(GrafoListaAdyacencia<K, V> graph) {
        ILista<Edge<K, V>> edges = graph.edges();
        if (!edges.isEmpty()) {
            try {
                Edge<K, V> maxEdge = edges.getElement(1);
                float maxWeight = maxEdge.getWeight();

                for (int i = 2; i <= edges.size(); i++) {
                    Edge<K, V> currentEdge = edges.getElement(i);
                    if (currentEdge.getWeight() > maxWeight) {
                        maxEdge = currentEdge;
                        maxWeight = currentEdge.getWeight();
                    }
                }

                return maxEdge;
            } catch (PosException | VacioException e) {
                e.printStackTrace();
            }
        }

        System.out.println("No edges in the graph.");
        return null;
    }


    public static <K extends Comparable<K>, V extends Comparable<V>> ILista<Edge<K, V>> mstPrimLazy(GrafoListaAdyacencia<K, V> graph, K idOrigen) {
        ILista<Edge<K, V>> mstEdges = new ArregloDinamico<>(1);
        MinPQ<Float, Edge<K, V>> minPQ = new MinPQ<>(1);

        Vertex<K, V> startVertex = graph.getVertex(idOrigen);
        addEdgesToMinPQ(minPQ, startVertex);

        while (!minPQ.isEmpty()) {
            Edge<K, V> currentEdge = minPQ.delMin().getValue();
            Vertex<K, V> destVertex = currentEdge.getDestination();

            if (!destVertex.getMark()) {
                try {
                    mstEdges.insertElement(currentEdge, mstEdges.size() + 1);
                } catch (PosException | NullException e) {
                    e.printStackTrace();
                }
                addEdgesToMinPQ(minPQ, destVertex);
            }
        }

        return mstEdges;
    }

    private static <K extends Comparable<K>, V extends Comparable<V>> void addEdgesToMinPQ(MinPQ<Float, Edge<K, V>> minPQ, Vertex<K, V> startVertex) {
        startVertex.mark();

        for (int i = 1; i <= startVertex.edges().size(); i++) {
            try {
                Edge<K, V> currentEdge = startVertex.edges().getElement(i);
                minPQ.insert(currentEdge.getWeight(), currentEdge);
            } catch (PosException | VacioException e) {
                e.printStackTrace();
            }
        }
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> ITablaSimbolos<K, NodoTS<Float, Edge<K, V>>> minPathTree(GrafoListaAdyacencia<K, V> graph, K idOrigen) {
        ITablaSimbolos<K, NodoTS<Float, Edge<K, V>>> pathTreeTable = new TablaHashLinearProbing<>(2);
        MinPQIndexada<Float, K, Edge<K, V>> minPQIndex = new MinPQIndexada<>(20);

        pathTreeTable.put(idOrigen, new NodoTS<>(0f, null));
        relaxDijkstra(pathTreeTable, minPQIndex, graph.getVertex(idOrigen), 0);

        while (!minPQIndex.isEmpty()) {
            NodoTS<Float, Edge<K, V>> current = minPQIndex.delMin();
            Edge<K, V> currentEdge = current.getValue();
            float currentWeight = current.getKey();
            relaxDijkstra(pathTreeTable, minPQIndex, currentEdge.getDestination(), currentWeight);
        }

        return pathTreeTable;
    }

    private static <K extends Comparable<K>, V extends Comparable<V>> void relaxDijkstra(ITablaSimbolos<K, NodoTS<Float, Edge<K, V>>> pathTreeTable, MinPQIndexada<Float, K, Edge<K, V>> minPQIndex, Vertex<K, V> startVertex, float weight) {
        startVertex.mark();

        for (int i = 1; i <= startVertex.edges().size(); i++) {
            try {
                Edge<K, V> currentEdge = startVertex.edges().getElement(i);
                Vertex<K, V> destination = currentEdge.getDestination();
                float edgeWeight = currentEdge.getWeight();

                if (!destination.getMark()) {
                    NodoTS<Float, Edge<K, V>> arrivalDestination = pathTreeTable.get(destination.getId());

                    if (arrivalDestination == null) {
                        pathTreeTable.put(destination.getId(), new NodoTS<>(weight + edgeWeight, currentEdge));
                        minPQIndex.insert(weight + edgeWeight, destination.getId(), currentEdge);
                    } else if (arrivalDestination.getKey() > (weight + edgeWeight)) {
                        arrivalDestination.setKey(weight + edgeWeight);
                        arrivalDestination.setValue(currentEdge);
                        minPQIndex.changePriority(destination.getId(), weight + edgeWeight, currentEdge);
                    }
                }
            } catch (PosException | VacioException e) {
                e.printStackTrace();
            }
        }
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> PilaEncadenada<Edge<K, V>> minPath(GrafoListaAdyacencia<K, V> graph, K idOrigen, K idDestino) {
        ITablaSimbolos<K, NodoTS<Float, Edge<K, V>>> tree = minPathTree(graph, idOrigen);

        PilaEncadenada<Edge<K, V>> path = new PilaEncadenada<>();
        K searchId = idDestino;
        NodoTS<Float, Edge<K, V>> current;

        while ((current = tree.get(searchId)) != null && current.getValue() != null) {
            path.push(current.getValue());
            searchId = current.getValue().getSource().getId();
        }

        return path;
    }


    public static <K extends Comparable<K>, V extends Comparable<V>> GrafoListaAdyacencia<K, V> reverse(GrafoListaAdyacencia<K, V> graph) {
        GrafoListaAdyacencia<K, V> reversedGraph = new GrafoListaAdyacencia<>(graph.numVertices());

        ILista<Vertex<K, V>> vertices = graph.vertices();
        for (int i = 1; i <= vertices.size(); i++) {
            try {
                Vertex<K, V> vertex = vertices.getElement(i);
                reversedGraph.insertVertex(vertex.getId(), vertex.getInfo());
            } catch (PosException | NullPointerException | VacioException e) {
                e.printStackTrace();
            }
        }

        ILista<Edge<K, V>> edges = graph.edges();
        for (int i = 1; i <= edges.size(); i++) {
            try {
                Edge<K, V> edge = edges.getElement(i);
                reversedGraph.addEdge(edge.getDestination().getId(), edge.getSource().getId(), edge.getWeight());
            } catch (PosException | NullPointerException | VacioException e) {
                e.printStackTrace();
            }
        }

        return reversedGraph;
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> ITablaSimbolos<K, Integer> getSSC(GrafoListaAdyacencia<K, V> graph) {
        PilaEncadenada<Vertex<K, V>> reverseTopological = topologicalOrder(reverse(graph));
        ITablaSimbolos<K, Integer> tabla = new TablaHashLinearProbing<>(graph.numVertices());
        int idComponente = 1;

        while (reverseTopological.top() != null) {
            Vertex<K, V> current = reverseTopological.pop();
            if (!current.getMark()) {
                current.getSCC(tabla, idComponente);
                idComponente++;
            }
        }

        graph.unmark();
        return tabla;
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> PilaEncadenada<Vertex<K, V>> topologicalOrder(GrafoListaAdyacencia<K, V> graph) {
        ColaEncadenada<Vertex<K, V>> pre = new ColaEncadenada<>();
        ColaEncadenada<Vertex<K, V>> post = new ColaEncadenada<>();
        PilaEncadenada<Vertex<K, V>> reversePost = new PilaEncadenada<>();

        ILista<Vertex<K, V>> vertices = graph.vertices();

        for (int i = 1; i <= vertices.size(); i++) {
            try {
                if (!vertices.getElement(i).getMark()) {
                    vertices.getElement(i).topologicalOrder(pre, post, reversePost);
                }
            } catch (PosException | VacioException e) {
                e.printStackTrace();
            }
        }

        graph.unmark();
        return reversePost;
    }

}
