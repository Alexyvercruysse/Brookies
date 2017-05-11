package brookies.iut.com.brookies.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iem on 05/04/2017.
 */

public class UsersRoom {

    private List<String> usersChats;

    public UsersRoom() {
        usersChats = new ArrayList<>();
    }

    public UsersRoom(List<String> usersChats) {
        this.usersChats = usersChats;
    }

    public List<String> getUsersChats() {
        return usersChats;
    }
}
