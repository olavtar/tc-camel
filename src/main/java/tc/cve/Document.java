package tc.cve;

import java.util.ArrayList;

public class Document {

    public String category;
    public String csaf_version;
    public String lang;
    public ArrayList<Note> notes;
    public Publisher publisher;
    public String title;
    public Tracking tracking;

    public Document() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCsaf_version() {
        return csaf_version;
    }

    public void setCsaf_version(String csaf_version) {
        this.csaf_version = csaf_version;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Tracking getTracking() {
        return tracking;
    }

    public void setTracking(Tracking tracking) {
        this.tracking = tracking;
    }
}
