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

import com.alatheer.myplayer.Models.RegisterModel;
import com.alatheer.myplayer.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by elashry on 26/06/2018.
 */

public class Fragment_Image extends Fragment {
    private View view;
    private CircleImageView image;
    private Bitmap bitmap;
    private String encodeImage="";
    private final int IMG_REQ=1;
    private RegisterModel registerModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image,container,false);
        registerModel = RegisterModel.getInstance();
        initView(view);
        return view;
    }

    public static Fragment_Image getInstance()
    {
        Fragment_Image fragment_image = new Fragment_Image();
        return fragment_image;
    }
    private void initView(View view) {
        image = view.findViewById(R.id.image);

        image.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            getActivity().startActivityForResult(intent.createChooser(intent,"Chhose image"),IMG_REQ);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQ && resultCode == Activity.RESULT_OK&& data!=null)
        {
            Uri uri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                image.setImageBitmap(bitmap);
                encodeImage = EncodeImage(bitmap);
                registerModel.setImage(encodeImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String EncodeImage(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    public void setImageError()
    {
        Snackbar.make(view.getRootView(),"Image profile required",Snackbar.LENGTH_SHORT).show();
    }
}
