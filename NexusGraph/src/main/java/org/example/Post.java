import java.util.List;

public class Post {
    private String text;
   private  List<String> topics;


    public Post(String text, List<String> topics) {
        this.text = text;
        this.topics = topics;
    }

    // Constructor with text only
    public Post(String text) {
        this.text = text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getTopics() {
        return topics;
    }

    public String getText() {
        return text;
    }
}
