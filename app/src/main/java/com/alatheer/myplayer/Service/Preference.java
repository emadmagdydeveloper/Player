package com.alatheer.myplayer.Service;

import android.content.Context;
import android.content.SharedPreferences;

import com.alatheer.myplayer.Models.UserModel;
import com.google.gson.Gson;

/**
 * Created by elashry on 30/06/2018.
 */

public class Preference {

    private Context context;

    public Preference(Context context) {
        this.context = context;
    }

    public void CreateSharedPref(String userData)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("data",userData);
        createSession(Tags.session_login);
        editor.apply();
    }

    public void UpdateSharedPref(String userData)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("data",userData);
        editor.apply();
    }

    public void CleareSharedPref()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("data","");
        createSession(Tags.session_logout);
        editor.apply();
    }

    public UserModel getUserData()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("data","");
        Gson gson = new Gson();
        return gson.fromJson(data,UserModel.class);

    }
    private void createSession(String session)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("session",session);
        editor.apply();

    }

    public String getSession()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        String session =sharedPreferences.getString("session","");
        return session;

    }


}
