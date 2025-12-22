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

    public List<User> loadFromString(String x

        
        int idx = 0;
        while (true) {
            int userStart = xml.
                

            
            
                

                ndexOf

            

            users.add(parseU


        }

        

    /* ===================== US

        ate User parseUser(Stri

        
        // --- User ID (first <id> before <followers>) ---
        Integer userId = extractFirstIntTag(userBlock, "id");
        if (userId != null) {
            user.setId(userId);
        }

        // --- Name ---
        String name = extractFirstTag(userBlock, "name");
        if (name != null) {
         

        

        String postsBlock = extractBlock(userBlock, "posts");
        if (postsBlock != null) {
         

        

        String followersBlock = extractBlock(userBlock, "followers");
        if (followersBlock != null) {
            user.setFollowers(parseFollowers(followersBlock));
        }

        r

        

        
    private List<Post> parsePosts(String postsBlock) {
        List<Post> posts = new ArrayList<>();

        int idx = 0;
        w

         

            int postEnd = postsBlock.indexOf("</post>", postStart);
            if (postEnd == -1) break;


            posts.add(parsePost(postBlock));

            idx = postEnd + 7;
        }
                

            
                
                

            Post parsePost(String postBlock) {
        Post post = new Post()
                

            -- Body ---
        S

        
            


        String topicsBlock = ex

            int idx = 0;
            while (true) {
                int tStart = to

        

                if (tEnd == -1) break;

         

            }
        }
        return post;
    }

                    

                
    private List<Integer> parseFoll
                    wers(S

                    ist<>(


                rue) {
                    


        

                fEnd == -1) break;

         

            if (id != null) ids.add(id);

            idx = fEnd + 11;
        }
                

            
        
                

            
    private String extractFirstTag(S
                ring x

            n

                


        
        int end = xml.indexOf(close, start);
        if (end == -1) return null;


            

        
        try {
            

            return value == null ? null : Integer.parseInt(value.trim());
        } catch (Numbe
            FormatExcept

        }

        
    private String extra
            tBlock(Strin

        String close = "</" + tag + ">";

            

        if (start == -1) return null;

        int end = xml.indexOf(close, start);
        if (end == -1) return null;

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
