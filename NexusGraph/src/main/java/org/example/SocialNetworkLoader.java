package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SocialNetworkLoader {

    public List<User> loadFromFile(String filePath) throws IOException {
        String xml = readFile(filePath);
        return loadFromString(xml);
    }

    public List<User> loadFromString(String xml) {
        List<User> users = new ArrayList<>();
        
        int idx = 0;
        while (idx < xml.length()) {
            int userStart = xml.indexOf("<user>", idx);
            if (userStart == -1) break;
            
            int userEnd = xml.indexOf("</user>", userStart);
            if (userEnd == -1) break;
            
            String userBlock = xml.substring(userStart, userEnd + 7);
            User user = parseUser(userBlock);
            users.add(user);
            
            idx = userEnd + 7;
        }
        
        return users;
    }

    private User parseUser(String userBlock) {
        User user = new User();
        
        // Parse id
        String idStr = extractTagContent(userBlock, "id");
        if (idStr != null && !idStr.isEmpty()) {
            // Get first id (user's id, not follower's)
            int firstIdEnd = userBlock.indexOf("</id>");
            if (firstIdEnd != -1) {
                int firstIdStart = userBlock.lastIndexOf("<id>", firstIdEnd);
                if (firstIdStart != -1) {
                    String firstId = userBlock.substring(firstIdStart + 4, firstIdEnd).trim();
                    user.setId(Integer.parseInt(firstId));
                }
            }
        }
        
        // Parse name
        String name = extractTagContent(userBlock, "name");
        if (name != null) {
            user.setName(name.trim());
        }
        
        // Parse posts
        int postsStart = userBlock.indexOf("<posts>");
        int postsEnd = userBlock.indexOf("</posts>");
        if (postsStart != -1 && postsEnd != -1) {
            String postsBlock = userBlock.substring(postsStart, postsEnd);
            List<Post> posts = parsePosts(postsBlock);
            user.setPosts(posts);
        }
        
        // Parse followers
        int followersStart = userBlock.indexOf("<followers>");
        int followersEnd = userBlock.indexOf("</followers>");
        if (followersStart != -1 && followersEnd != -1) {
            String followersBlock = userBlock.substring(followersStart, followersEnd);
            List<Integer> followerIds = parseFollowers(followersBlock);
            user.setFollowers(followerIds);
        }
        
        return user;
    }

    private List<Post> parsePosts(String postsBlock) {
        List<Post> posts = new ArrayList<>();
        
        int idx = 0;
        while (idx < postsBlock.length()) {
            int postStart = postsBlock.indexOf("<post>", idx);
            if (postStart == -1) break;
            
            int postEnd = postsBlock.indexOf("</post>", postStart);
            if (postEnd == -1) break;
            
            String postBlock = postsBlock.substring(postStart, postEnd);
            Post post = parsePost(postBlock);
            posts.add(post);
            
            idx = postEnd + 7;
        }
        
        return posts;
    }

    private Post parsePost(String postBlock) {
        Post post = new Post();
        
        // Parse body
        String body = extractTagContent(postBlock, "body");
        if (body != null) {
            post.setBody(body.trim());
        }
        
        // Parse topics
        int topicsStart = postBlock.indexOf("<topics>");
        int topicsEnd = postBlock.indexOf("</topics>");
        if (topicsStart != -1 && topicsEnd != -1) {
            String topicsBlock = postBlock.substring(topicsStart, topicsEnd);
            int topicIdx = 0;
            while (topicIdx < topicsBlock.length()) {
                int topicStart = topicsBlock.indexOf("<topic>", topicIdx);
                if (topicStart == -1) break;
                
                int topicEnd = topicsBlock.indexOf("</topic>", topicStart);
                if (topicEnd == -1) break;
                
                String topic = topicsBlock.substring(topicStart + 7, topicEnd).trim();
                post.addTopic(topic);
                
                topicIdx = topicEnd + 8;
            }
        }
        
        return post;
    }

    private List<Integer> parseFollowers(String followersBlock) {
        List<Integer> followerIds = new ArrayList<>();
        
        int idx = 0;
        while (idx < followersBlock.length()) {
            int followerStart = followersBlock.indexOf("<follower>", idx);
            if (followerStart == -1) break;
            
            int followerEnd = followersBlock.indexOf("</follower>", followerStart);
            if (followerEnd == -1) break;
            
            String followerBlock = followersBlock.substring(followerStart, followerEnd);
            String idStr = extractTagContent(followerBlock, "id");
            if (idStr != null && !idStr.isEmpty()) {
                followerIds.add(Integer.parseInt(idStr.trim()));
            }
            
            idx = followerEnd + 11;
        }
        
        return followerIds;
    }

    private String extractTagContent(String xml, String tagName) {
        String openTag = "<" + tagName + ">";
        String closeTag = "</" + tagName + ">";
        
        int start = xml.indexOf(openTag);
        if (start == -1) return null;
        
        int end = xml.indexOf(closeTag, start);
        if (end == -1) return null;
        
        return xml.substring(start + openTag.length(), end);
    }

    private String readFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }
}
