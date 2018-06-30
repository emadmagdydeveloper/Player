package com.alatheer.myplayer.Service;

import com.alatheer.myplayer.Models.UserModel;

/**
 * Created by elashry on 30/06/2018.
 */

public class UserSingleTone {
    private static UserSingleTone instance=null;
    private UserModel userModel;

    private UserSingleTone() {
    }

    public static synchronized UserSingleTone getInstance()
    {
        if (instance==null)
        {
            instance = new UserSingleTone();
        }
        return instance;
    }

    public interface Listener
    {
        void onSuccess(UserModel userModel);
    }
    public void setUserData(UserModel userData){
        this.userModel = userData;

    }

    public void getUserData(UserSingleTone.Listener listener)
    {
        listener.onSuccess(userModel);
    }


}
