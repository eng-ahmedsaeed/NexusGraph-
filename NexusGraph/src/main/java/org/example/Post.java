import java.util.ArrayList;
import java.util.List;

public class Post {
    private String body;
    private List<String> topics;

    public Post() {
        this.body = "";
        this.topics = new ArrayList<>();
    }

    public Post(String body) {
        this.body = body;
        this.topics = new ArrayList<>();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public void addTopic(String topic) {
        this.topics.add(topic);
    }
}
