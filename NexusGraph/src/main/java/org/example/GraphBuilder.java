package org.example;

import java.util.ArrayList;
import java.util.List;

public class GraphBuilder {

    public Graph<Vertex> buildGraph(List<User> users) {
        Graph<Vertex> g = new Graph<>(users.size());

        List<Vertex> vertexList = new ArrayList<>();

        // 1) Add vertices in same order as users list
        for (User u : users) {
            Vertex v = new Vertex(u);
            g.addVertex(v);
            vertexList.add(v);
        }

        // 2) Add edges: user -> followerId
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            List<Integer> followerIds = u.getFollowerIds();

            if (followerIds != null) {
                for (int followerId : followerIds) {
                    int j = findIndexById(users, followerId);
                    // Skip if followerId not found or would create self-edge
                    if (j != -1 && j != i) {
                        g.addEdge(vertexList.get(i), vertexList.get(j));
                    }
                }
            }
        }

        return g;
    }

    private int findIndexById(List<User> users, int targetId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == targetId) {
                return i;
            }
        }
        return -1;
    }
}
