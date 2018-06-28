package com.alatheer.myplayer.Models;

import java.io.Serializable;

/**
 * Created by elashry on 28/06/2018.
 */

public class UserModel implements Serializable {
    private String id;
    private int image;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String user_typ;

    public UserModel() {
    }

    public UserModel(String id, int image, String name, String email, String phone, String password, String user_typ) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.user_typ = user_typ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
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

    public String getUser_typ() {
        return user_typ;
    }

    public void setUser_typ(String user_typ) {
        this.user_typ = user_typ;
    }
}
