import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private List<Post> posts;
    private List<Integer> followers;

    public User() {
        this.id = 0;
        this.name = "";
        this.posts = new ArrayList<>();
        this.followers = new ArrayList<>();
    }

    public User(int id) {
        this.id = id;
        this.name = "";
        this.posts = new ArrayList<>();
        this.followers = new ArrayList<>();
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.posts = new ArrayList<>();
        this.followers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public List<Integer> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Integer> followers) {
        this.followers = followers;
    }

    public void addFollower(int followerId) {
        this.followers.add(followerId);
    }
}
