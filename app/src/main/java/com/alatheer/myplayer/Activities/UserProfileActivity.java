package com.alatheer.myplayer.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.myplayer.Models.UserModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Preference;
import com.alatheer.myplayer.Service.Tags;
import com.alatheer.myplayer.Service.UserSingleTone;
import com.google.gson.Gson;
import com.lamudi.phonefield.PhoneInputLayout;
import com.santalu.diagonalimageview.DiagonalImageView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    private DiagonalImageView image_profile;
    private CircleImageView image;
    private TextView tv_name,tv_email,tv_phone;
    private UserModel userModel;
    private LinearLayout edit_name,edit_email,edit_phone;
    private String whoVisit="";
    private UserSingleTone userSingleTone;
    private Preference preference;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initView();
        userSingleTone = UserSingleTone.getInstance();
        preference = new Preference(this);

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

        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View view1 = LayoutInflater.from(UserProfileActivity.this).inflate(R.layout.edit_layout,null);
                TextView title = view1.findViewById(R.id.tv_title);
                EditText edt_name = view1.findViewById(R.id.edt_update);
                edt_name.setHint("Name");
                edt_name.setText(userModel.getUser_name());
                Button updateBtn = view1.findViewById(R.id.updateBtn);
                Button cancelBtn = view1.findViewById(R.id.cancelBtn);
                title.setText("Edit Name");
                String name = edt_name.getText().toString();
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(name))
                        {
                            edt_name.setError("name require");
                        }
                        else if (name.equals(userModel.getUser_name()))
                        {
                            edt_name.setError(null);
                            Toast.makeText(UserProfileActivity.this, "No changes occur", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            edt_name.setError(null);
                            alertDialog.dismiss();
                            UpdateName(name);
                        }
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog = new AlertDialog.Builder(UserProfileActivity.this)
                        .setCancelable(true)
                        .create();
                alertDialog.setCanceledOnTouchOutside(false);

            }
        });

        edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(UserProfileActivity.this).inflate(R.layout.edit_layout,null);
                TextView title = view1.findViewById(R.id.tv_title);
                EditText edt_email = view1.findViewById(R.id.edt_update);
                edt_email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                edt_email.setHint("Email");
                edt_email.setText(userModel.getUser_name());
                Button updateBtn = view1.findViewById(R.id.updateBtn);
                Button cancelBtn = view1.findViewById(R.id.cancelBtn);
                title.setText("Edit Email");
                String email = edt_email.getText().toString();
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(email))
                        {
                            edt_email.setError("email require");
                        }
                        else if (email.equals(userModel.getUser_email()))
                        {
                            edt_email.setError(null);
                            Toast.makeText(UserProfileActivity.this, "No changes occur", Toast.LENGTH_SHORT).show();
                        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                        {
                            edt_email.setError("invalid email");

                        }
                        else
                        {
                            edt_email.setError(null);
                            alertDialog.dismiss();
                            UpdateEmail(email);
                        }
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog = new AlertDialog.Builder(UserProfileActivity.this)
                        .setCancelable(true)
                        .create();
                alertDialog.setCanceledOnTouchOutside(false);

            }
        });

        edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(UserProfileActivity.this).inflate(R.layout.edit_phone_layout,null);
                TextView title = view1.findViewById(R.id.tv_title);
                PhoneInputLayout edt_phone = view1.findViewById(R.id.edt_phone);
                edt_phone.getTextInputLayout().getEditText().setHint("Phone number");
                edt_phone.getTextInputLayout().getEditText().setTextSize(TypedValue.COMPLEX_UNIT_SP,12f);
                edt_phone.setDefaultCountry("eg");
                Button updateBtn = view1.findViewById(R.id.updateBtn);
                Button cancelBtn = view1.findViewById(R.id.cancelBtn);
                title.setText("Edit Phone");
                String phone = edt_phone.getPhoneNumber();
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(phone))
                        {
                            edt_phone.getTextInputLayout().getEditText().setError("phone number require");
                        }
                        else if (!edt_phone.isValid())
                        {
                            edt_phone.getTextInputLayout().getEditText().setError(null);

                            edt_phone.getTextInputLayout().getEditText().setError(" invalid phone number");
                        }else if (phone.equals(userModel.getUser_phone()))
                        {
                            edt_phone.getTextInputLayout().getEditText().setError(null);

                            Toast.makeText(UserProfileActivity.this, "No changes occur", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            edt_phone.getTextInputLayout().getEditText().setError(null);
                            alertDialog.dismiss();
                            UpdatePhone(phone);
                        }
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog = new AlertDialog.Builder(UserProfileActivity.this)
                        .setCancelable(true)
                        .create();
                alertDialog.setCanceledOnTouchOutside(false);

            }
        });
    }





    private void UpdateName(String name) {
        ProgressDialog dialog = Tags.CreateProgressDialog(this, "Updating name....");
        dialog.show();
        Tags.getService().updateProfile(userModel.getUser_id(),name,userModel.getUser_pass(),userModel.getUser_phone(),userModel.getUser_email(),"")
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess()==1)
                            {
                                dialog.dismiss();
                                Gson gson = new Gson();
                                String data  = gson.toJson(response.body());
                                userSingleTone.setUserData(response.body());
                                preference.UpdateSharedPref(data);
                                userModel = response.body();
                                UpdateUi(response.body());
                                Toast.makeText(UserProfileActivity.this, "Name successfully updated", Toast.LENGTH_SHORT).show();
                            }else if (response.body().getSuccess()==0)
                            {
                                dialog.dismiss();

                                Toast.makeText(UserProfileActivity.this, "Failed try again later", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(UserProfileActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();
                        Log.e("Error",t.getMessage());
                    }
                });
    }
    private void UpdatePhone(String phone) {
        ProgressDialog dialog = Tags.CreateProgressDialog(UserProfileActivity.this, "Updating phone....");
        dialog.show();
        Tags.getService().updateProfile(userModel.getUser_id(),userModel.getUser_name(),userModel.getUser_pass(),phone,userModel.getUser_email(),"")
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess()==1)
                            {
                                dialog.dismiss();
                                Gson gson = new Gson();
                                String data  = gson.toJson(response.body());
                                userSingleTone.setUserData(response.body());
                                preference.UpdateSharedPref(data);
                                userModel = response.body();
                                UpdateUi(response.body());
                                Toast.makeText(UserProfileActivity.this, "phone successfully updated", Toast.LENGTH_SHORT).show();
                            }else if (response.body().getSuccess()==0)
                            {
                                dialog.dismiss();

                                Toast.makeText(UserProfileActivity.this, "Failed try again later", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(UserProfileActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();
                        Log.e("Error",t.getMessage());
                    }
                });
    }

    private void UpdateEmail(String email) {

        ProgressDialog dialog = Tags.CreateProgressDialog(UserProfileActivity.this, "Updating email....");
        dialog.show();
        Tags.getService().updateProfile(userModel.getUser_id(),userModel.getUser_name(),userModel.getUser_pass(),userModel.getUser_phone(),email,"")
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess()==1)
                            {
                                dialog.dismiss();
                                Gson gson = new Gson();
                                String data  = gson.toJson(response.body());
                                userSingleTone.setUserData(response.body());
                                preference.UpdateSharedPref(data);
                                userModel = response.body();
                                UpdateUi(response.body());
                                Toast.makeText(UserProfileActivity.this, "Email successfully updated", Toast.LENGTH_SHORT).show();
                            }else if (response.body().getSuccess()==0)
                            {
                                dialog.dismiss();

                                Toast.makeText(UserProfileActivity.this, "Failed try again later", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(UserProfileActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();
                        Log.e("Error",t.getMessage());
                    }
                });
    }


}
