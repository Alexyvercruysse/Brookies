package brookies.iut.com.brookies.model;

import android.media.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by iem on 29/03/2017.
 */

public class User {


    private String firstname;
    private String lastname;
    private String email;
    private String sexe;
    private String birthdate;
    private String description;
    private String hobbies;
    private List<Picture> pictures;
    private Boolean likesMen;
    private Boolean likesWomen;


    public User() {
    }

    public User(String firstname, String lastname, String email, String sexe,String birthdate, String description, String hobbies, List<Picture> pictures, Boolean likesMen, Boolean likesWomen) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.sexe = sexe;
        this.birthdate = birthdate;
        this.description = description;
        this.hobbies = hobbies;
        this.pictures = pictures;
        this.likesMen = likesMen;
        this.likesWomen = likesWomen;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void addPicture(Picture picture) {
        if (pictures == null){
            pictures = new ArrayList<>();
        }
        this.pictures.add(picture);
    }

    public void deletePicture(Picture picture) {
        this.pictures.remove(picture);
    }


    public Boolean getLikesMen() {
        return likesMen;
    }

    public void setLikesMen(Boolean likesMen) {
        this.likesMen = likesMen;
    }

    public Boolean getLikesWomen() {
        return likesWomen;
    }

    public void setLikesWomen(Boolean likesWomen) {
        this.likesWomen = likesWomen;
    }
}
