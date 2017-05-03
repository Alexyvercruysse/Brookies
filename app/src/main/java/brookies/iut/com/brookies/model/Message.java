package brookies.iut.com.brookies.model;

/**
 * Created by iem on 05/04/2017.
 */

public class Message {

    private String author;
    private String content;
    private String date;

    public Message() {
    }

    public Message(String author, String content, String date) {
        this.author = author;
        this.content = content;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}
