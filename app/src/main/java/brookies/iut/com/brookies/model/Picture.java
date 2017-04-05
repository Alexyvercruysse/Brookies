package brookies.iut.com.brookies.model;

import java.net.URL;

/**
 * Created by iem on 29/03/2017.
 */

public class Picture {

    private String name;
    private URL url;

    public Picture() {
    }

    public Picture(String name, URL url) {
        this.name = name;
        this.url = url;
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
