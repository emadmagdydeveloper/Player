package com.alatheer.myplayer.Models;

import java.io.Serializable;

/**
 * Created by elashry on 28/06/2018.
 */

public class PlayersModel implements Serializable {
    private String player_id;
    private String player_name;
    private String player_age;
    private String player_photo;
    private String player_position;
    private String player_tall;
    private String player_weight;
    private String player_vedio;
    private String player_vedio_comment;
    private String accademy_id_fk;
    private String player_vedio_like;
    private String player_vedio_dislike;
    private String player_vedio_view;
    private int  success;


    public String getPlayer_id() {
        return player_id;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public String getPlayer_age() {
        return player_age;
    }

    public String getPlayer_photo() {
        return player_photo;
    }

    public String getPlayer_position() {
        return player_position;
    }

    public String getPlayer_tall() {
        return player_tall;
    }

    public String getPlayer_weight() {
        return player_weight;
    }

    public String getPlayer_vedio() {
        return player_vedio;
    }

    public String getPlayer_vedio_comment() {
        return player_vedio_comment;
    }

    public String getAccademy_id_fk() {
        return accademy_id_fk;
    }

    public String getPlayer_vedio_like() {
        return player_vedio_like;
    }

    public String getPlayer_vedio_dislike() {
        return player_vedio_dislike;
    }

    public String getPlayer_vedio_view() {
        return player_vedio_view;
    }

    public int getSuccess() {
        return success;
    }
}
