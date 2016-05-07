package it.rieger.happyhour.model;

/**
 * Created by admin on 07.05.16.
 */
public class Locationtab {
    private long id;
    private String comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setLocationtab(String locationtab) {
        this.comment = locationtab;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return comment;
    }
}

