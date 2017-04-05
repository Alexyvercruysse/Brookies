package brookies.iut.com.brookies.model;

import java.util.HashMap;

/**
 * Created by iem on 05/04/2017.
 */

public class UserMatchs {

    HashMap<String, Boolean> userMatches;

    public UserMatchs() {
        userMatches = new HashMap<>();
    }

    public UserMatchs(HashMap<String, Boolean> userMatches) {
        this.userMatches = userMatches;
    }

    public HashMap<String, Boolean> getUserMatches() {
        return userMatches;
    }

    /**
     * Ajoute un utilisateur à la liste des utilisateurs matchés
     * @param idUser l'id de l'user matché
     */
    public void addUserSeen(String idUser) {
        this.userMatches.put(idUser, true);
    }

    /**
     * Permet de savoir si un utilisateur est matché avec un autre utilisateur
     * @param idUser l'utilisateur à tester
     * @return true si déjà vu, false sinon
     */
    public boolean isMatched(String idUser){
        return userMatches.containsKey(idUser);
    }
}
