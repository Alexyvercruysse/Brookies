package brookies.iut.com.brookies.model;

import java.net.URL;

/**
 * Created by iem on 29/03/2017.
 */

public class Picture {

    private int ID;
    private String name;
    private URL url;

    public Picture() {
    }

    public Picture(int ID, String name, URL url) {
        this.ID = ID;
        this.name = name;
        this.url = url;
    }

    public int getID() {
        return ID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

}
