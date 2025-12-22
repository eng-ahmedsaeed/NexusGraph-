import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private int userId;
    private List<Vertex> neighbors;

    public Vertex(int userId) {
        this.userId = userId;
        this.neighbors = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Vertex> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Vertex neighbor) {
        this.neighbors.add(neighbor);
    }
}
