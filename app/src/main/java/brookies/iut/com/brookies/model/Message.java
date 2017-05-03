package brookies.iut.com.brookies.model;

/**
 * Created by iem on 05/04/2017.
 */

public class Message {

    private String idauthor;
    private String author;
    private String content;
    private Long date;

    public Message() {
    }

    public Message(String idauthor, String author ,String content, Long date) {
        this.idauthor = idauthor;
        this.author = author;
        this.content = content;
        this.date = date;
    }

    public String getIdauthor(){ return  idauthor;}

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Long getDate() {
        return date;
    }
}
