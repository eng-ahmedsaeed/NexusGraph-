package org.example.Level_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphBuilder {

    public Graph buildGraph(List<User> users) {
        Graph g = new Graph(users.size());
        Map<Integer, Integer> idToIndex = new HashMap<>();
        List<Vertex> vertexList = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            Vertex v = new Vertex(u);
            g.addVertex(v);
            vertexList.add(v);
            idToIndex.put(u.getId(), i);
        }
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            List<Integer> followerIds = u.getFollowerIds();

            if (followerIds != null) {
                for (int followerId : followerIds) {
                    Integer j = idToIndex.get(followerId);
                    if (j != null && j != i) {
                        g.addEdge(vertexList.get(j), vertexList.get(i));
                    }
                }
            }
        }

        return g;
    }
}
