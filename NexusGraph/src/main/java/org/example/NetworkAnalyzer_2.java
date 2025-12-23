/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsa_project_2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class NetworkAnalyzer_2 {
    private Graph graphObj;
    private int n;
    private Map<Integer, Integer> idToIndex;
    private Map<Integer, Integer> indexToId;
    public NetworkAnalyzer_2(Graph graphObj) {
        this.graphObj = graphObj;
        this.n = graphObj.getVertices().size();
        idToIndex = new HashMap<>();
        indexToId = new HashMap<>();
        for (int i = 0; i < n; i++) {
            idToIndex.put(graphObj.getVertices().get(i).id, i);
            indexToId.put(i, graphObj.getVertices().get(i).id);
        }
    }
    public void mostInfluencerUser() {
        int[][] graph = graphObj.getAdjacencyMatrix();
        ArrayList<Vertex> vertices = graphObj.getVertices();
        int max = -1;
        for (int j = 0; j < n; j++) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                sum += graph[i][j];
            }
            if (sum > max) {
                max = sum;
            }
        }

        System.out.println("Most influencer user(s) with " + max + " follower(s):");
        for (int j = 0; j < n; j++) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                sum += graph[i][j];
            }
            if (sum == max) {
                System.out.println("User name: " + vertices.get(j).name +
                                   " | User id: " + vertices.get(j).id);
            }
        }
    }
    public void mutualFollowers(List<Integer> ids) {
        int[][] graph = graphObj.getAdjacencyMatrix();
        ArrayList<Vertex> vertices = graphObj.getVertices();
        if (ids == null || ids.isEmpty()) {
            System.out.println("No user IDs provided.");
            return;
        }
        boolean[] intersection = new boolean[n];
        int firstIndex = idToIndex.get(ids.get(0));
        for (int j = 0; j < n; j++) {
    intersection[j] = graph[j][firstIndex] == 1; 
    }

    for (int i = 1; i < ids.size(); i++) {
        int userIndex = idToIndex.get(ids.get(i));
        for (int j = 0; j < n; j++) {
            intersection[j] = intersection[j] && (graph[j][userIndex] == 1); 
        }
    }

        boolean found = false;
        int cnt = 1;
        for (int j = 0; j < n; j++) {
            if (intersection[j]) {
                if (!found)
                    System.out.println("mutual follower(s) found in your selected ids:");
                System.out.println(cnt + ") User name: " + vertices.get(j).name +
                                   " | User id: " + vertices.get(j).id);
                found = true;
                cnt++;
            }
        }

        if (!found) {
            System.out.println("No mutual followers found in your selected ids.");
        }
    }
}
