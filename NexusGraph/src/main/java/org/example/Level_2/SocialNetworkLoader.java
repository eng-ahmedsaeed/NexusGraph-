package org.example.Level_2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SocialNetworkLoader {

    

    public List<User> loadFromFile(String filePath) throws IOException {
        return loadFromString(readFile(filePath));
    }

    public List<User> loadFromString(String xml) {
        List<User> users = new ArrayList<>();

        int idx = 0;
        while (true) {
            int userStart = xml.indexOf("<user>", idx);
            if (userStart == -1)
                break;

            int userEnd = xml.indexOf("</user>", userStart);
            if (userEnd == -1)
                break;

            String userBlock = xml.substring(userStart + 6, userEnd);
            users.add(parseUser(userBlock));

            idx = userEnd + 7;
        }
        return users;
    }

    

    private User parseUser(String userBlock) {
        User user = new User();
        Integer userId = extractFirstIntTag(userBlock, "id");
        if (userId != null) {
            user.setId(userId);
        }
        String name = extractFirstTag(userBlock, "name");
        if (name != null) {
            user.setName(name.trim());
        }
        String postsBlock = extractBlock(userBlock, "posts");
        if (postsBlock != null) {
            user.setPosts(parsePosts(postsBlock));
        }
        String followersBlock = extractBlock(userBlock, "followers");
        if (followersBlock != null) {
            user.setFollowers(parseFollowers(followersBlock));
        }

        return user;
    }

    

    private List<Post> parsePosts(String postsBlock) {
        List<Post> posts = new ArrayList<>();

        int idx = 0;
        while (true) {
            int postStart = postsBlock.indexOf("<post>", idx);
            if (postStart == -1)
                break;

            int postEnd = postsBlock.indexOf("</post>", postStart);
            if (postEnd == -1)
                break;

            String postBlock = postsBlock.substring(postStart + 6, postEnd);
            posts.add(parsePost(postBlock));

            idx = postEnd + 7;
        }
        return posts;
    }

    private Post parsePost(String postBlock) {
        Post post = new Post();
        String body = extractFirstTag(postBlock, "body");
        if (body != null) {
            post.setBody(body.trim());
        }
        String topicsBlock = extractBlock(postBlock, "topics");
        if (topicsBlock != null) {
            int idx = 0;
            while (true) {
                int tStart = topicsBlock.indexOf("<topic>", idx);
                if (tStart == -1)
                    break;

                int tEnd = topicsBlock.indexOf("</topic>", tStart);
                if (tEnd == -1)
                    break;

                post.addTopic(topicsBlock.substring(tStart + 7, tEnd).trim());
                idx = tEnd + 8;
            }
        }
        return post;
    }

    

    
    private List<Integer> parseFollowers(String followersBlock) {
        List<Integer> ids = new ArrayList<>();
        int idx = 0;
        while (true) {
            int fStart = followersBlock.indexOf("<follower>", idx);
            if (fStart == -1)
                break;

            int fEnd = followersBlock.indexOf("</follower>", fStart);
            if (fEnd == -1)
                break;

            String content = followersBlock.substring(fStart + 10, fEnd).trim();
            if (!content.contains("<")) {
                try {
                    ids.add(Integer.parseInt(content));
                } catch (NumberFormatException e) {
                }
            } else {
                Integer nestedId = extractFirstIntTag(content, "id");
                if (nestedId != null) {
                    ids.add(nestedId);
                }
            }

            idx = fEnd + 11;
        }
        if (ids.isEmpty()) {
            idx = 0;
            while (true) {
                int fStart = followersBlock.indexOf("<follower_id>", idx);
                if (fStart == -1)
                    break;

                int fEnd = followersBlock.indexOf("</follower_id>", fStart);
                if (fEnd == -1)
                    break;

                String idStr = followersBlock.substring(fStart + 13, fEnd).trim();
                try {
                    ids.add(Integer.parseInt(idStr));
                } catch (NumberFormatException e) {
                }

                idx = fEnd + 14;
            }
        }
        
        return ids;
    }

    

    private String extractFirstTag(String xml, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";

        int start = xml.indexOf(open);
        if (start == -1)
            return null;

        int end = xml.indexOf(close, start);
        if (end == -1)
            return null;

        return xml.substring(start + open.length(), end);
    }

    private Integer extractFirstIntTag(String xml, String tag) {
        try {
            String value = extractFirstTag(xml, tag);
            return value == null ? null : Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String extractBlock(String xml, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";

        int start = xml.indexOf(open);
        if (start == -1)
            return null;

        int end = xml.indexOf(close, start);
        if (end == -1)
            return null;

        return xml.substring(start + open.length(), end);
    }

    private String readFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}
