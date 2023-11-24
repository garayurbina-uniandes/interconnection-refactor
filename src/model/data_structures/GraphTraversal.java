package model.data_structures;

import java.util.LinkedList;
import java.util.Queue;

public class GraphTraversal {
    public static <K extends Comparable<K>, V extends Comparable<V>> void bfs(Vertex<K, V> startVertex) {
        Queue<Vertex<K, V>> queue = new LinkedList<>();
        startVertex.mark();
        queue.offer(startVertex);

        while (!queue.isEmpty()) {
            Vertex<K, V> current = queue.poll();
            System.out.println("Processing vertex: " + current.getId());

            for (Vertex<K, V> neighbor : current.vertices()) {
                if (!neighbor.getMark()) {
                    neighbor.mark();
                    queue.offer(neighbor);
                }
            }
        }
    }


    public static <K extends Comparable<K>, V extends Comparable<V>> void dfs(Vertex<K, V> startVertex, Edge<K, V> edgeTo) {
        startVertex.mark();
        System.out.println("Processing vertex: " + startVertex.getId());

        for (Vertex<K, V> neighbor : startVertex.vertices()) {
            if (!neighbor.getMark()) {
                neighbor.dfs(edgeTo);
            }
        }
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> void topologicalOrder(Vertex<K, V> startVertex, ColaEncadenada<Vertex<K, V>> pre, ColaEncadenada<Vertex<K, V>> post, PilaEncadenada<Vertex<K, V>> reversePost) {
        startVertex.mark();
        pre.enqueue(startVertex);

        for (Vertex<K, V> destination : startVertex.vertices()) {
            if (!destination.getMark()) {
                topologicalOrder(destination, pre, post, reversePost);
            }
        }

        post.enqueue(startVertex);
        reversePost.push(startVertex);
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> void getSCC(Vertex<K, V> startVertex, ITablaSimbolos<K, Integer> tabla, int idComponente) {
        startVertex.mark();
        tabla.put(startVertex.getId(), idComponente);

        for (int i = 1; i <= startVertex.edges().size(); i++) {
            try {
                Vertex<K, V> neighbor = startVertex.edges().getElement(i).getDestination();
                if (!neighbor.getMark()) {
                    getSCC(neighbor, tabla, idComponente);
                }
            } catch (PosException | VacioException e) {
                e.printStackTrace();
            }
        }
    }
}
