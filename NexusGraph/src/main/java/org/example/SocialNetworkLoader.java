package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SocialNetworkLoader {

    /* ===================== PUBLIC API ===================== */

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

    /* ===================== USER ===================== */

    private User parseUser(String userBlock) {
        User user = new User();

        // --- User ID (first <id> before <followers>) ---
        Integer userId = extractFirstIntTag(userBlock, "id");
        if (userId != null) {
            user.setId(userId);
        }

        // --- Name ---
        String name = extractFirstTag(userBlock, "name");
        if (name != null) {
            user.setName(name.trim());
        }

        // --- Posts ---
        String postsBlock = extractBlock(userBlock, "posts");
        if (postsBlock != null) {
            user.setPosts(parsePosts(postsBlock));
        }

        // --- Followers ---
        String followersBlock = extractBlock(userBlock, "followers");
        if (followersBlock != null) {
            user.setFollowers(parseFollowers(followersBlock));
        }

        return user;
    }

    /* ===================== POSTS ===================== */

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

        // --- Body ---
        String body = extractFirstTag(postBlock, "body");
        if (body != null) {
            post.setBody(body.trim());
        }

        // --- Topics ---
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

    /* ===================== FOLLOWERS ===================== */

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

            String followerBlock = followersBlock.substring(fStart + 10, fEnd);
            Integer id = extractFirstIntTag(followerBlock, "id");
            if (id != null)
                ids.add(id);

            idx = fEnd + 11;
        }
        return ids;
    }

    /* ===================== HELPERS ===================== */

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
                sb.append(line);
            }
        }
        return sb.toString();
    }
}
