package com.alatheer.myplayer.Models;

import java.io.Serializable;

/**
 * Created by elashry on 30/06/2018.
 */

public class ContactModel implements Serializable {
    private String phone;
    private String email;
    private String facebook;
    private String instagram;
    private String twitter;

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getTwitter() {
        return twitter;
    }
}
