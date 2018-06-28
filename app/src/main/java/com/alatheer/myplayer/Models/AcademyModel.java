package com.alatheer.myplayer.Models;

import java.io.Serializable;

/**
 * Created by elashry on 27/06/2018.
 */

public class AcademyModel implements Serializable {
    private String id;
    private int image;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String password;
    private String user_type;

    public AcademyModel() {
    }

    public AcademyModel(String id, int image, String name, String phone, String email, String address, String password, String user_type) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.password = password;
        this.user_type = user_type;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
