package brookies.iut.com.brookies.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iem on 29/03/2017.
 *
 * Informations relatives à un chat
 */

public class RoomMetadata {

    private List<String> users;
    private String lastmessage;
    private boolean unseen;

    public RoomMetadata() {
        users = new ArrayList<>();
    }

    public RoomMetadata(List<String> users, String lastMessage, boolean unseen) {
        this.users = users;
        this.lastmessage = lastMessage;
        this.unseen = unseen;
    }

    public List<String> getUsers() {
        return users;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public boolean isUnseen() {
        return unseen;
    }

    public void setUnseen(boolean unseen) {
        this.unseen = unseen;
    }
}
