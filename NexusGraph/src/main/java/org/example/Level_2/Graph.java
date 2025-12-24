package org.example.Level_2;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private int[][] edges; // 0 = no edge, 1 = edge exists
    private List<Vertex> vertices;
    private boolean[] marks;
    private int maxVertices;

    public Graph(int maxVertices) {
        this.maxVertices = maxVertices;
        edges = new int[maxVertices][maxVertices];
        vertices = new ArrayList<>();
        marks = new boolean[maxVertices];
    }
    public void addVertex(Vertex vertex) {
        if (vertices.size() < maxVertices) {
            vertices.add(vertex);
            marks[vertices.size() - 1] = false;
        }
    }
    public void addEdge(Vertex fromVertex, Vertex toVertex) {
        int row = indexIs(fromVertex);
        int col = indexIs(toVertex);
        if (row != -1 && col != -1) {
            edges[row][col] = 1; // edge exists
        }
    }
    public boolean isEdge(Vertex fromVertex, Vertex toVertex) {
        int row = indexIs(fromVertex);
        int col = indexIs(toVertex);
        if (row != -1 && col != -1)
            return edges[row][col] == 1;
        return false;
    }
    public void makeEmpty() {
        vertices.clear();
        for (int i = 0; i < maxVertices; i++)
            for (int j = 0; j < maxVertices; j++)
                edges[i][j] = 0;
    }

    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    public boolean isFull() {
        return vertices.size() == maxVertices;
    }

    public void clearMarks() {
        for (int i = 0; i < marks.length; i++)
            marks[i] = false;
    }

    public void markVertex(Vertex vertex) {
        int index = indexIs(vertex);
        if (index != -1)
            marks[index] = true;
    }

    public boolean isMarked(Vertex vertex) {
        int index = indexIs(vertex);
        if (index != -1)
            return marks[index];
        return false;
    }
    public List<Vertex> getToVertices(Vertex vertex) {
        List<Vertex> adjVertices = new ArrayList<>();
        int row = indexIs(vertex);
        if (row != -1) {
            for (int col = 0; col < vertices.size(); col++) {
                if (edges[row][col] == 1) {
                    adjVertices.add(vertices.get(col));
                }
            }
        }
        return adjVertices;
    }
    private int indexIs(Vertex vertex) {
        return vertices.indexOf(vertex);
    }
    
    public List<Vertex> getVertices() {
        return vertices;
    }

    public int[][] getAdjacencyMatrix() {
        return edges;
    }
    
    
    public ArrayList<Pair<String, String>> getEdges() {
        ArrayList<Pair<String, String>> edgeList = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if (edges[i][j] == 1) {
                    String fromName = vertices.get(i).getUser().getName();
                    String toName = vertices.get(j).getUser().getName();
                    edgeList.add(new Pair<>(fromName, toName));
                }
            }
        }

        return edgeList;
    }
    
    
    public int getEdgeCount() {
        int count = 0;
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if (edges[i][j] == 1) {
                    count++;
                }
            }
        }
        return count;
    }
}
