package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest{
    private Graph graph;

    @BeforeEach
    void setUp() {
       graph = new Graph(5);
    }

    @Test
    void testConstructorVerticesCount() {
        Graph g = new Graph(3);
        assertEquals(3, g.getVerticesCount());
        assertEquals(0, g.getEdgesCount());
    }
    @Test
    void testConstructorZeroVertices() {
        assertThrows(IllegalArgumentException.class, () -> new Graph(0));
    }
    @Test
    void testConstructorNegativeVertices() {
        assertThrows(IllegalArgumentException.class, () -> new Graph(-5));
    }

    @Test
    void testAddEdge() {
        graph.addEdge(1, 2);
        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 1));
        assertEquals(1, graph.getEdgesCount());
    }
    @Test
    void testAddEdgeToAllVertices() {
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(1, 5);

        assertEquals(4, graph.getEdgesCount());
        assertEquals(new HashSet<>(Arrays.asList(2, 3, 4, 5)), graph.getNeighbors(1));
        assertEquals(new HashSet<>(Collections.singletonList(1)), graph.getNeighbors(2));
        assertEquals(new HashSet<>(Collections.singletonList(1)), graph.getNeighbors(3));
        assertEquals(new HashSet<>(Collections.singletonList(1)), graph.getNeighbors(4));
        assertEquals(new HashSet<>(Collections.singletonList(1)), graph.getNeighbors(5));
    }
    @Test
    void testAddEdgeChain() {
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);

        assertEquals(4, graph.getEdgesCount());
        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 3));
        assertTrue(graph.hasEdge(3, 4));
        assertTrue(graph.hasEdge(4, 5));
        assertFalse(graph.hasEdge(1, 5));
    }
    @Test
    void testAddEdgeSelfLoop() {
        assertEquals(5, graph.getVerticesCount());
        assertEquals(0, graph.getEdgesCount());
        assertTrue(graph.getNeighbors(1).isEmpty());

        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(1, 1));
    }

    @Test
    void testRemoveEdge() {
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        graph.removeEdge(1, 2);
        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(2, 1));
        assertTrue(graph.hasEdge(2, 3));
        assertEquals(1, graph.getEdgesCount());
    }
    @Test
    void testRemoveNonExistent() {
        graph.removeEdge(1, 2);
        graph.removeEdge(1, 2);
        graph.removeEdge(1, 2);

        assertFalse(graph.hasEdge(1, 2));
        assertEquals(0, graph.getEdgesCount());
    }

    @Test
    void testHasEdge() {
        graph.addEdge(1, 2);
        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 1));
    }
    @Test
    void testHasEdgeMultipleOperations() {
        assertFalse(graph.hasEdge(1, 2));

        graph.addEdge(1, 2);
        assertTrue(graph.hasEdge(1, 2));

        graph.removeEdge(1, 2);
        assertFalse(graph.hasEdge(1, 2));

        graph.addEdge(1, 2);
        assertTrue(graph.hasEdge(1, 2));
    }
    @Test
    void testHasEdgeForAllPossiblePairs() {
        Graph smallGraph = new Graph(3);

        assertFalse(smallGraph.hasEdge(1, 2));
        assertFalse(smallGraph.hasEdge(1, 3));
        assertFalse(smallGraph.hasEdge(2, 3));

        smallGraph.addEdge(1, 2);
        smallGraph.addEdge(3, 2);
        smallGraph.addEdge(3, 1);
        assertTrue(smallGraph.hasEdge(1, 2));
        assertTrue(smallGraph.hasEdge(1, 3));
        assertTrue(smallGraph.hasEdge(2, 3));
    }

    @Test
    void testGetNeighbors() {
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);

        Set<Integer> expected =  new HashSet<>(Arrays.asList(2, 3, 4));
        Set<Integer> neighbors = graph.getNeighbors(1);

        assertEquals(expected, neighbors);
    }
    @Test
    void testGetNeighborsAfterEdgeRemoval() {
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);

        Set<Integer> initialNeighbors = graph.getNeighbors(1);
        assertEquals(new HashSet<>(Arrays.asList(2, 3, 4)), initialNeighbors);

        graph.removeEdge(1, 3);
        Set<Integer> afterRemoval = graph.getNeighbors(1);
        assertEquals(new HashSet<>(Arrays.asList(2, 4)), afterRemoval);
    }
    @Test
    void testGetNeighborsForIsolatedVertex() {
        Set<Integer> neighbors = graph.getNeighbors(1);
        assertTrue(neighbors.isEmpty());

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.removeEdge(1, 2);
        graph.removeEdge(3, 1);

        Set<Integer> finalNeighbors = graph.getNeighbors(1);
        assertTrue(finalNeighbors.isEmpty());
    }

    @Test
    void testGetEdgesCountWithEdges() {
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        assertEquals(3, graph.getEdgesCount());
    }

    @Test
    void testGetVerticesCount() {
        assertEquals(5, graph.getVerticesCount());
    }

}