package  org.example;

import java.util.ArrayList;
import java.util.List;

public class Vertex {

    private User user;
    private List<Vertex> neighbors;    

    public Vertex(User user) {
        this.user = user;
        this.neighbors = new ArrayList<>();
    }

    
    public User getUser() {
        return user;
    }

    public List<Vertex> getNeighbors() {
        return neighbors;
    }

    
    public void addNeighbor(Vertex vertex) {
        neighbors.add(vertex);
    }
}