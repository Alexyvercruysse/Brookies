package brookies.iut.com.brookies.model;

import java.util.Date;
import java.util.List;

/**
 * Created by iem on 29/03/2017.
 */

public class User {

    private enum Sexe {
        MALE,
        FEMALE
    }

    private String ID;
    private String firstname;
    private String lastname;
    private Sexe sexe;
    private int age;
    private Date BirthDay;
    private String description;
    private String hobbies;
    private List<Picture> pictures;


    public User() {
    }

    public User(String ID, String firstname, String lastname, Sexe sexe, int age, Date birthDay, String description, String hobbies, List<Picture> pictures) {
        this.ID = ID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.sexe = sexe;
        this.age = age;
        BirthDay = birthDay;
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

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public int getAge() {
        return age;
    }

    public Date getBirthDay() {
        return BirthDay;
    }

    public void setBirthDay(Date birthDay) {
        BirthDay = birthDay;
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
