package com.marigarci.civicadvocacy;

import java.io.Serializable;

public class Socials implements Serializable {
    private String facebook;
    private String twitter;
    private String youtube;

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getYoutube() {
        return youtube;
    }


    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }
}
