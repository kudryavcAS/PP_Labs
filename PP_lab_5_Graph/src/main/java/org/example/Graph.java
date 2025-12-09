package org.example;

import java.util.*;

public class Graph {

    private final int verticesCount;
    private final List<Set<Integer>> adjacencyList;

    public Graph(int verticesCount) {
        if (verticesCount <= 0) {
            throw new IllegalArgumentException("Number of vertices must be positive");
        }
        this.verticesCount = verticesCount;
        this.adjacencyList = new ArrayList<>(verticesCount + 1);

        for (int i = 0; i <= verticesCount; i++) {
            adjacencyList.add(new HashSet<>());
        }
    }

    public void addEdge(int v1, int v2) {
        validateVertex(v1);
        validateVertex(v2);

        if (v1 == v2) {
            throw new IllegalArgumentException("Self-loops are not allowed");
        }

        adjacencyList.get(v1).add(v2);
        adjacencyList.get(v2).add(v1);
    }

    public void removeEdge(int v1, int v2) {
        validateVertex(v1);
        validateVertex(v2);

        adjacencyList.get(v1).remove(v2);
        adjacencyList.get(v2).remove(v1);
    }

    public boolean hasEdge(int v1, int v2) {
        validateVertex(v1);
        validateVertex(v2);
        return adjacencyList.get(v1).contains(v2);
    }

    public int getVerticesCount() {
        return verticesCount;
    }

    public Set<Integer> getNeighbors(int vertex) {
        validateVertex(vertex);
        return new HashSet<>(adjacencyList.get(vertex));
    }

    public int getEdgesCount() {
        int count = 0;
        for (Set<Integer> neighbors : adjacencyList) {
            count += neighbors.size();
        }
        return count / 2;
    }

    private void validateVertex(int vertex) {
        if (vertex < 1 || vertex > verticesCount) {
            throw new IllegalArgumentException("Vertex " + vertex + " is out of range [1, " + verticesCount + "]");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph Visualization:\n");
        sb.append("Vertices: ").append(verticesCount).append(", Edges: ").append(getEdgesCount()).append("\n\n");

        for (int i = 1; i <= verticesCount; i++) {
            Set<Integer> neighbors = adjacencyList.get(i);
            if (!neighbors.isEmpty()) {
                sb.append(i).append(" - ");

                List<Integer> sorted = new ArrayList<>(neighbors);
                Collections.sort(sorted);
                for (Integer integer : sorted) {
                    sb.append(integer);
                    sb.append(" ");
                }
                sb.append("\n");
            } else {
                sb.append(i).append(" - isolated\n");
            }
        }
        return sb.toString();
    }
}
