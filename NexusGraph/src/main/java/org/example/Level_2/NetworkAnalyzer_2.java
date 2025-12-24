package org.example.Level_2;

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
        int maxOutDegree = 0;

        for (int i = 0; i < n; i++) {
            int outDegree = 0;
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    outDegree++;
                }
            }
            
            if (outDegree > maxOutDegree) {
                maxOutDegree = outDegree;
                mostActiveUsersIds.clear();
                mostActiveUsersIds.add(i);
            } else if (outDegree == maxOutDegree && maxOutDegree != 0) {
                mostActiveUsersIds.add(i);
            }
        }
        return mostActiveUsersIds;
    }

    
    public String mostInfluencerUser() {
        int[][] graph = graphObj.getAdjacencyMatrix();
        List<Vertex> vertices = graphObj.getVertices();
        
        if (n == 0) {
            return "No users in graph.";
        }
        
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

        StringBuilder result = new StringBuilder();
        result.append("Most influential user(s) with ").append(max).append(" follower(s):\n\n");
        
        for (int j = 0; j < n; j++) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                sum += graph[i][j];
            }
            if (sum == max) {
                User user = vertices.get(j).getUser();
                result.append("- ").append(user.getName())
                      .append(" (ID: ").append(user.getId()).append(")\n");
            }
        }
        return result.toString();
    }
    
    
    public String mutualFollowers(List<Integer> ids) {
        int[][] graph = graphObj.getAdjacencyMatrix();
        List<Vertex> vertices = graphObj.getVertices();
        
        if (ids == null || ids.isEmpty()) {
            return "No user IDs provided.";
        }
        for (int id : ids) {
            if (!idToIndex.containsKey(id)) {
                return "Error: User ID " + id + " not found in graph.";
            }
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

        StringBuilder result = new StringBuilder();
        boolean found = false;
        int cnt = 1;
        
        for (int j = 0; j < n; j++) {
            if (intersection[j]) {
                if (!found) {
                    result.append("Mutual follower(s) found:\n\n");
                }
                User user = vertices.get(j).getUser();
                result.append(cnt).append(") ").append(user.getName())
                      .append(" (ID: ").append(user.getId()).append(")\n");
                found = true;
                cnt++;
            }
        }

        if (!found) {
            result.append("No mutual followers found for the selected IDs.");
        }
        return result.toString();
    }
    
    
    public List<Integer> suggestUsers(int userId) {
        List<Integer> suggestionsIds = new ArrayList<>();
        if (!idToIndex.containsKey(userId)) {
            return suggestionsIds; // Empty list if user not found
        }
        
        int userIndex = idToIndex.get(userId);
        int[][] matrix = graphObj.getAdjacencyMatrix();
        boolean[] alreadyFollowers = new boolean[n];
        for (int j = 0; j < n; j++) {
            if (matrix[j][userIndex] == 1) {
                alreadyFollowers[j] = true;
            }
        }
        for (int followerIdx = 0; followerIdx < n; followerIdx++) {
            if (matrix[followerIdx][userIndex] == 1) {
                for (int i = 0; i < n; i++) {
                    if (matrix[i][followerIdx] == 1) {
                        if (i != userIndex && !alreadyFollowers[i] && !suggestionsIds.contains(i)) {
                            suggestionsIds.add(i);
                        }
                    }
                }
            }
        }
        return suggestionsIds;
    }
    
    
    public User getUserByIndex(int index) {
        if (index >= 0 && index < n) {
            return graphObj.getVertices().get(index).getUser();
        }
        return null;
    }
    
    
    public int getIndexForUserId(int userId) {
        return idToIndex.getOrDefault(userId, -1);
    }
}
