package org.example;

import java.util.ArrayList;

public class Graph {


    private ArrayList<Vertex> vertices;


    private int[][] adjMatrix;

    private int size;

    public Graph(int maxUsers) {
        vertices = new ArrayList<>();
        adjMatrix = new int[maxUsers][maxUsers];
        size = 0;
    }


    public void addVertex(Vertex v) {
        vertices.add(v);
        size++;
    }


    public void addFollow(int fromIndex, int toIndex) {
        adjMatrix[fromIndex][toIndex] = 1;
    }


    public boolean isFollowing(int fromIndex, int toIndex) {
        return adjMatrix[fromIndex][toIndex] == 1;
    }

    public int[][] getAdjacencyMatrix() {
        return adjMatrix;
    }


    public ArrayList<Vertex> getVertices() {
        return vertices;
    }


    public Vertex getVertex(int index) {
        return vertices.get(index);
    }
    public int getFollow(int fromIndex, int toIndex) {
        return adjMatrix[fromIndex][toIndex];
    }

    public ArrayList<Pair<Integer, Integer>> getEdges() {
        ArrayList<Pair<Integer, Integer>> edges = new ArrayList<>();
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < adjMatrix[i].length; j++) {
                if (adjMatrix[i][j] == 1) {
                    edges.add(new Pair<>(i, j));
                }
            }
        }
        return edges;
    }
}