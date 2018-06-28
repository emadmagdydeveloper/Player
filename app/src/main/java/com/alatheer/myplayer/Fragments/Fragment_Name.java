package com.alatheer.myplayer.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.alatheer.myplayer.Models.RegisterModel;
import com.alatheer.myplayer.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by elashry on 26/06/2018.
 */

public class Fragment_Name extends Fragment {
    private View view;
    private EditText edt_name;
    private String name;
    private RegisterModel registerModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_name,container,false);
        registerModel = RegisterModel.getInstance();
        initView(view);
        return view;
    }

    private void initView(View view) {
        edt_name = view.findViewById(R.id.edt_name);
        name = edt_name.getText().toString();
        registerModel.setName(name);

    }

    public static Fragment_Name getInstance()
    {
        Fragment_Name fragment_name= new Fragment_Name();
        return fragment_name;
    }

    public void getName()
    {
        name = edt_name.getText().toString();
        registerModel.setName(name);
    }

    public void setNameError()
    {
        edt_name.setError("Full name require");
        Snackbar.make(view.getRootView(),"Full name require",Snackbar.LENGTH_SHORT).show();

    }
    public void removeNameError()
    {
        edt_name.setError(null);

    }
}
