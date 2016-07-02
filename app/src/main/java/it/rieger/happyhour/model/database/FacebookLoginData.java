package it.rieger.happyhour.model.database;

/**
 * Created by admin on 07.05.16.
 */
public class FacebookLoginData {
    private long id;
    private String facebookID;
    private String facebookToken;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }
}

