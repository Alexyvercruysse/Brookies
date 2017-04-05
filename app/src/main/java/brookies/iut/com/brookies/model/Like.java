package brookies.iut.com.brookies.model;

import java.util.HashMap;

/**
 * Created by iem on 05/04/2017.
 */

public class Like {

    private HashMap<String, Boolean> usersSeen;

    public Like() {
    }

    public Like(HashMap<String, Boolean> usersSeen) {
        this.usersSeen = usersSeen;
    }

    public HashMap<String, Boolean> getUsersSeen() {
        return usersSeen;
    }

    /**
     * Ajoute un utilisateur à la liste des utilisateurs vus (ou change son statut si déjà présent)
     * @param idUser l'id de l'user vu
     * @param like true si l'utilisateur a été liké, false sinon
     */
    public void addUserSeen(String idUser, Boolean like) {
        if (usersSeen.containsKey(idUser)) {
            usersSeen.remove(idUser);
        }
        usersSeen.put(idUser, like);
    }

    /**
     * Permet de savoir si un utilisateur a déjà vu un autre utilisateur
     * @param idUser l'utilisateur à tester
     * @return true si déjà vu, false sinon
     */
    public boolean hasSeen(String idUser){
        return usersSeen.containsKey(idUser);
    }

    /**
     * Permet de savoir si un utilisateur a liké un autre utilisateur
     * @param idUser l'utilisateur a tester
     * @return true si liké, false sinon
     */
    public boolean isLiked(String idUser){
        if (usersSeen.containsKey(idUser)){
            return usersSeen.get(idUser);
        }
        return false;
    }
}
