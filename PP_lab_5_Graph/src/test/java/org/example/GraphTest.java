package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
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
    void testRemoveEdge() {
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        graph.removeEdge(1, 2);
        assertFalse(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 3));
        assertEquals(1, graph.getEdgesCount());
    }



    @Test
    void testHasEdge() {
        graph.addEdge(1, 2);
        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 1));
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