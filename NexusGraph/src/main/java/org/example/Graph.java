import java.util.ArrayList;
import java.util.List;

class Graph<VertexType> {
    private int[][] edges; // 0 = no edge, 1 = edge exists
    private List<VertexType> vertices;
    private boolean[] marks;
    private int maxVertices;

    public Graph(int maxVertices) {
        this.maxVertices = maxVertices;
        edges = new int[maxVertices][maxVertices];
        vertices = new ArrayList<>();
        marks = new boolean[maxVertices];
    }

    // Add a new vertex
    public void addVertex(VertexType vertex) {
        if (vertices.size() < maxVertices) {
            vertices.add(vertex);
            marks[vertices.size() - 1] = false;
        } else {
            System.out.println("Graph is full!");
        }
    }

    // Add an edge (unweighted)
    public void addEdge(VertexType fromVertex, VertexType toVertex) {
        int row = indexIs(fromVertex);
        int col = indexIs(toVertex);
        if (row != -1 && col != -1) {
            edges[row][col] = 1; // edge exists
        }
    }

    // Check if there is an edge
    public boolean isEdge(VertexType fromVertex, VertexType toVertex) {
        int row = indexIs(fromVertex);
        int col = indexIs(toVertex);
        if (row != -1 && col != -1)
            return edges[row][col] == 1;
        return false;
    }

    // Clear all vertices
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

    public void markVertex(VertexType vertex) {
        int index = indexIs(vertex);
        if (index != -1)
            marks[index] = true;
    }

    public boolean isMarked(VertexType vertex) {
        int index = indexIs(vertex);
        if (index != -1)
            return marks[index];
        return false;
    }

    // Get all adjacent vertices
    public List<VertexType> getToVertices(VertexType vertex) {
        List<VertexType> adjVertices = new ArrayList<>();
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

    // Helper: get index of a vertex
    private int indexIs(VertexType vertex) {
        return vertices.indexOf(vertex);
    }
    public List<VertexType> getVertices() {
        return vertices;
    }

    public int[][] getAdjacencyMatrix() {
        return edges;
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


