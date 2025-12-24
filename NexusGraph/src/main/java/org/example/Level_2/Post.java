package org.example.Level_2;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private String text;
    private List<String> topics;
    public Post() {
        this.topics = new ArrayList<>();
    }

    public Post(String text, List<String> topics) {
        this.text = text;
        this.topics = topics;
    }
    public Post(String text) {
        this.text = text;
        this.topics = new ArrayList<>();
    }
    
    public void setText(String text) {
        this.text = text;
    }
    public void setBody(String body) {
        this.text = body;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
    public void addTopic(String topic) {
        if (this.topics == null) {
            this.topics = new ArrayList<>();
        }
        this.topics.add(topic);
    }

    public List<String> getTopics() {
        return topics;
    }

    public String getText() {
        return text;
    }
    
    public String getBody() {
        return text;
    }
}
