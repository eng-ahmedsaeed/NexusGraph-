/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example;
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
            Vertex v = graphObj.getVertices().get(i);
            idToIndex.put(v.getUser().getId(), i);
            indexToId.put(i, v.getUser().getId());
        }
    }
    public List<Integer> mostActiveUsers() {
        List<Integer> mostActiveUsersIds = new ArrayList<>();
        int[][] matrix = graphObj.getAdjacencyMatrix();
        int maxTotalConnections = 0;

        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            int outDegree = 0;
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    outDegree++;
                }
            }
            int totalConnections = outDegree ;
            if (totalConnections > maxTotalConnections) {
                maxTotalConnections = totalConnections;
                mostActiveUsersIds.clear();
                mostActiveUsersIds.add(i);
            }
            else if (totalConnections == maxTotalConnections && maxTotalConnections != 0) {
                mostActiveUsersIds.add(i);
            }
        }
        return mostActiveUsersIds;
    }

    public void mostInfluencerUser() {
        int[][] graph = graphObj.getAdjacencyMatrix();
        List<Vertex> vertices = graphObj.getVertices();
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
                System.out.println("User name: " + vertices.get(j).getUser().getName() +
                        " | User id: " + vertices.get(j).getUser().getId());
            }
        }
    }
    public void mutualFollowers(List<Integer> ids) {
        int[][] graph = graphObj.getAdjacencyMatrix();
        List<Vertex> vertices = graphObj.getVertices();
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
                System.out.println(cnt + ") User name: " + vertices.get(j).getUser().getName() +
                        " | User id: " + vertices.get(j).getUser().getId());
                found = true;
                cnt++;
            }
        }

        if (!found) {
            System.out.println("No mutual followers found in your selected ids.");
        }
    }
    public List<Integer> suggestUsers(int userId) {
        int userIndex = -1;
        int[][] matrix = graphObj.getAdjacencyMatrix();

        if (userId < 0 || userId >= matrix.length) {
            return new ArrayList<>();
        }
        List<Integer> suggestionsIds = new ArrayList<>();
        for (int followedIds = 0; followedIds < matrix.length; followedIds++) {
            if (matrix[userIndex][followedIds] == 1) {
                for (int i = 0; i < matrix.length; i++) {
                    if (matrix[followedIds][i] == 1) {
                        // Not recommending the user to themselves
                        // User doesn't already follow this person
                        // Person isn't already in the suggestions list
                        if (i != userIndex && matrix[userIndex][i] == 0 && !suggestionsIds.contains(i)) {
                            suggestionsIds.add(i);
                        }
                    }
                }
            }
        }
        return suggestionsIds;
    }
}