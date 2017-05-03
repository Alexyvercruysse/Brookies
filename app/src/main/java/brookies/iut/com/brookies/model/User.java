package brookies.iut.com.brookies.model;


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
    private Boolean likeMen;
    private Boolean likeWomen;
    private Boolean isPremium;


    public User() {
    }

    public User(String firstname, String lastname, String email,
                String sexe, int age, String birthdate, String description, String hobbies, List<Picture> pictures,Boolean isPremium,Boolean likeMen,Boolean likeWomen) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.sexe = sexe;
        this.birthdate = birthdate;
        this.description = description;
        this.hobbies = hobbies;
        this.pictures = pictures;
        this.isPremium = isPremium;
        this.likeMen = likeMen;
        this.likeWomen = likeWomen;
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

    public void setPicturesAtIndex(int index, Picture picture) {
        this.pictures.set(index,picture);
    }

    public void deletePicture(Picture picture) {
        this.pictures.remove(picture);
    }

    public Boolean getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }

    public Boolean getLikeMen() {
        return likeMen;
    }

    public void setLikeMen(Boolean likeMen) {
        this.likeMen = likeMen;
    }

    public Boolean getLikeWomen() {
        return likeWomen;
    }

    public void setLikeWomen(Boolean likeWomen) {
        this.likeWomen = likeWomen;
    }
}
