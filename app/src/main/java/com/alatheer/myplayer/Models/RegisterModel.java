package com.alatheer.myplayer.Models;

import java.io.Serializable;

/**
 * Created by elashry on 26/06/2018.
 */

public class RegisterModel implements Serializable {
    private String image;
    private String name;
    private String email;
    private String phone;
    private String password;
    private static RegisterModel instance=null;

    private RegisterModel() {
    }

    public static synchronized RegisterModel getInstance ()
    {
        if (instance==null)
        {
            instance = new RegisterModel();
        }
        return instance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
