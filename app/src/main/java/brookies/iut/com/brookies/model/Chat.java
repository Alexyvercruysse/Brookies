package brookies.iut.com.brookies.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iem on 05/04/2017.
 */

public class Chat {

    private List<Message> messages;

    public Chat() {
        messages = new ArrayList<>();
    }

    public Chat(List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message){
        messages.add(message);
    }

    public void removeMessage(Message message){
        messages.remove(message);
    }
}
