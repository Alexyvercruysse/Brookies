package brookies.iut.com.brookies.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iem on 05/04/2017.
 */

public class RoomMessage {

    private List<Chat> chats;

    public RoomMessage() {
        chats = new ArrayList<>();
    }

    public RoomMessage(List<Chat> chats) {
        this.chats = chats;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void addChat(Chat chat){
        chats.add(chat);
    }

    public void removeChat(Chat chat){
        chats.remove(chat);
    }
}
