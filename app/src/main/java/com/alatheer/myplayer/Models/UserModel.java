package com.alatheer.myplayer.Models;

import java.io.Serializable;

/**
 * Created by elashry on 28/06/2018.
 */

public class UserModel implements Serializable {
    private String user_id;
    private String user_name;
    private String user_pass;
    private String user_type;
    private String user_address;
    private String user_phone;
    private String user_email;
    private String user_photo;
    private String user_token_id;
    private String user_google_lat;
    private String user_google_long;
    private String user_info;
    private String user_facebook;
    private String user_twitter;
    private String user_instagram;
    private int success;


    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getUser_address() {
        return user_address;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public String getUser_token_id() {
        return user_token_id;
    }

    public String getUser_google_lat() {
        return user_google_lat;
    }

    public String getUser_google_long() {
        return user_google_long;
    }

    public String getUser_info() {
        return user_info;
    }

    public String getUser_facebook() {
        return user_facebook;
    }

    public String getUser_twitter() {
        return user_twitter;
    }

    public String getUser_instagram() {
        return user_instagram;
    }

    public int getSuccess() {
        return success;
    }
}
