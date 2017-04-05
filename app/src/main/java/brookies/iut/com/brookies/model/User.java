package brookies.iut.com.brookies.model;

import java.util.List;

import static android.os.Build.ID;

/**
 * Created by iem on 29/03/2017.
 */

public class User {


    private String firstname;
    private String lastname;
    private String email;
    private String sexe;
    private int age;
    private String birthdate;
    private String description;
    private String hobbies;
    private List<Picture> pictures;


    public User() {
    }

    public User(String firstname, String lastname, String email, String sexe, int age, String birthdate, String description, String hobbies, List<Picture> pictures) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.sexe = sexe;
        this.age = age;
        this.birthdate = birthdate;
        this.description = description;
        this.hobbies = hobbies;
        this.pictures = pictures;
    }


    public User(String firstname, String lastname, String description) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.description = description;
    }

    public String getID() {
        return ID;
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

    public int getAge() {
        return age;
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
        this.pictures.add(picture);
    }

    public void deletePicture(Picture picture) {
        this.pictures.remove(picture);
    }
}
