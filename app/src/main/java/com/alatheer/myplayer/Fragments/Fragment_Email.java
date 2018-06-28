package com.alatheer.myplayer.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.alatheer.myplayer.Models.RegisterModel;
import com.alatheer.myplayer.R;

/**
 * Created by elashry on 26/06/2018.
 */

public class Fragment_Email extends Fragment {
    private View view;
    private EditText edt_email,edt_password;
    private String email="",password="";
    private RegisterModel registerModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_email,container,false);
        registerModel = RegisterModel.getInstance();
        initView(view);
        return view;
    }

    private void initView(View view) {
        edt_email = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);
        email = edt_email.getText().toString();
        password =edt_password.getText().toString();
        registerModel.setEmail(email);
        registerModel.setPassword(password);


    }

    public static Fragment_Email getInstance()
    {
        Fragment_Email fragment_email= new Fragment_Email();
        return fragment_email;
    }

    public void getEmail_Password()
    {
        email = edt_email.getText().toString();
        password =edt_password.getText().toString();
        registerModel.setEmail(email);
        registerModel.setPassword(password);
    }

    public void setEmailError()
    {
        edt_email.setError("Email required");
        Snackbar.make(view.getRootView(),"Email required",Snackbar.LENGTH_SHORT).show();

    }
    public void setInvalidEmailError()
    {
        edt_email.setError("Invalid email");
        Snackbar.make(view.getRootView(),"Invalid email",Snackbar.LENGTH_SHORT).show();
    }
    public void setPasswordError()
    {
        edt_password.setError("Password required");
        Snackbar.make(view.getRootView(),"Password required",Snackbar.LENGTH_SHORT).show();
    }
    public void setShortPasswordError()
    {

        edt_password.setError("Password is too short");
        Snackbar.make(view.getRootView(),"Password is too short",Snackbar.LENGTH_SHORT).show();
    }
    public void removeEmailError()
    {
        edt_email.setError(null);

    }
    public void removePasswordError()
    {
        edt_password.setError(null);

    }


}
