package org.example.Level_2;
import java.util.List;

public class User {
    int id ;
    String name;
    List<Integer> followerIds;
    List<Post>posts;
    public User() {
    }

    public User(int id, String name, List<Integer> followerIds, List<Post> posts) {
        this.id = id;
        this.name = name;
        this.followerIds = followerIds;
        this.posts = posts;
    }
    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public void setFollowerIds(List<Integer> followerIds) {
        this.followerIds = followerIds;
    }
    public void setFollowers(List<Integer> followers) {
        this.followerIds = followers;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getFollowerIds() {
        return followerIds;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public String getName() {
        return name;
    }
}
