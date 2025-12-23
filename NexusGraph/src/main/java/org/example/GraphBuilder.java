package org.example;

import java.util.ArrayList;
import java.util.List;

public class GraphBuilder {

    public Graph buildGraph(List<User> users) {
        Graph g = new Graph(users.size());

        ArrayList<Integer> ids = new ArrayList<>();

        // 1) Add vertices in same order as users list
        for (User u : users) {
            int id = u.getId();
            g.addVertex(new Vertex(id));
            ids.add(id);
        }

        // 2) Add edges: user -> followerId
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            List<Integer> followerIds = u.getFollowers();

            for (int followerId : followerIds) {
                int j = findIndexById(ids, followerId);
                // Skip if followerId not found or would create self-edge
                if (j != -1 && j != i) {
                    g.addFollow(i, j);
                }
            }
        }

        return g;
    }

    private int findIndexById(ArrayList<Integer> ids, int targetId) {
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == targetId) {
                return i;
            }
        }
        return -1;
    }
}
