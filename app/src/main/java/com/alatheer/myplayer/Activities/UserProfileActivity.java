package com.alatheer.myplayer.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alatheer.myplayer.Models.UserModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;
import com.santalu.diagonalimageview.DiagonalImageView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private DiagonalImageView image_profile;
    private CircleImageView image;
    private TextView tv_name,tv_email,tv_phone;
    private UserModel userModel;
    private LinearLayout edit_name,edit_email,edit_phone;
    private String whoVisit="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initView();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            userModel = (UserModel) intent.getSerializableExtra("data");
            whoVisit = intent.getStringExtra("who_visit");
            UpdateUi(userModel);
        }
    }

    private void UpdateUi(UserModel userModel) {
        try {
            Picasso.with(this).load(Uri.parse(Tags.imageUrl+userModel.getUser_photo())).into(image_profile);
            Picasso.with(this).load(Uri.parse(Tags.imageUrl+userModel.getUser_photo())).into(image);
            tv_name.setText(userModel.getUser_name());
            tv_email.setText(userModel.getUser_email());
            tv_phone.setText(userModel.getUser_phone());


            if (whoVisit.equals(Tags.me))
            {
                edit_name.setVisibility(View.VISIBLE);
                edit_email.setVisibility(View.VISIBLE);
                edit_phone.setVisibility(View.VISIBLE);

            }else if (whoVisit.equals(Tags.visitor))
            {
                edit_name.setVisibility(View.INVISIBLE);
                edit_email.setVisibility(View.INVISIBLE);
                edit_phone.setVisibility(View.INVISIBLE);

            }

        }catch (NullPointerException e){}
        catch (Exception e){}
    }

    private void initView() {
        image_profile = findViewById(R.id.image_profile);
        image = findViewById(R.id.image);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_phone = findViewById(R.id.tv_phone);
        edit_name = findViewById(R.id.ll_edit_name);
        edit_email = findViewById(R.id.ll_edit_email);
        edit_phone = findViewById(R.id.ll_edit_phone);

    }
}
