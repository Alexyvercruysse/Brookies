package brookies.iut.com.brookies.model;

import java.util.HashMap;

/**
 * Created by iem on 05/04/2017.
 */

public class UsersRoom {

    private HashMap<String, Boolean> usersChats;

    public UsersRoom() {
        usersChats = new HashMap<>();
    }

    public UsersRoom(HashMap<String, Boolean> usersChats) {
        this.usersChats = usersChats;
    }

    public HashMap<String, Boolean> getUsersChats() {
        return usersChats;
    }
}
