package com.alatheer.myplayer.Models;

import java.io.Serializable;

/**
 * Created by elashry on 28/06/2018.
 */

public class PlayersModel implements Serializable {
    private String id;
    private int    image;
    private String name;
    private String age;
    private String height;
    private String weight;
    private String code;
    private String position;
    private String speed;
    private String attack;
    private String defense;
    private String finish;
    private String kick;

    public PlayersModel() {
    }

    public PlayersModel(String id, int image, String name, String age, String height, String weight, String code, String position, String speed, String attack, String defense, String finish, String kick) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.code = code;
        this.position = position;
        this.speed = speed;
        this.attack = attack;
        this.defense = defense;
        this.finish = finish;
        this.kick = kick;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getAttack() {
        return attack;
    }

    public void setAttack(String attack) {
        this.attack = attack;
    }

    public String getDefense() {
        return defense;
    }

    public void setDefense(String defense) {
        this.defense = defense;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getKick() {
        return kick;
    }

    public void setKick(String kick) {
        this.kick = kick;
    }
}
