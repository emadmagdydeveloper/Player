package com.alatheer.myplayer.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.myplayer.Models.AcademyModel;
import com.alatheer.myplayer.Models.UserModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    private TextView tv_signup;
    private EditText edt_email,edt_password;
    private Button loginBtn;
    private RelativeLayout root;
    private android.app.AlertDialog dialog;
    private List<AcademyModel> academyModelList,accademy_result;
    private List<UserModel> userModelList,userResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        academyModelList = new ArrayList<>();
        accademy_result = new ArrayList<>();
        userResult = new ArrayList<>();

        userModelList = new ArrayList<>();
        tv_signup = findViewById(R.id.tv_signup);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        loginBtn = findViewById(R.id.loginBtn);
        root = findViewById(R.id.root);

        tv_signup.setOnClickListener(view ->
        {
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        });

        loginBtn.setOnClickListener(view -> Signin());

        createAlertDialog();
        createAcademy();
        createUsers();

    }


    private void createUsers() {

        userModelList.add(new UserModel("1",R.drawable.u1,"u1","u1@gmail.com","011111111111","111111",Tags.user));
        userModelList.add(new UserModel("2",R.drawable.u2,"u2","u2@gmail.com","022222222222","222222",Tags.user));
        userModelList.add(new UserModel("3",R.drawable.u2,"u3","u3@gmail.com","033333333333","333333",Tags.user));
    }

    private void createAcademy() {

        academyModelList.add(new AcademyModel("1",R.drawable.ac1,"Academy1","01012345678","ac1@gmail.com","Egypt shebin","111111", Tags.academy));
        academyModelList.add(new AcademyModel("2",R.drawable.ac2,"Academy2","01102211221","ac2@gmail.com","Egypt shebin","222222", Tags.academy));
        academyModelList.add(new AcademyModel("3",R.drawable.ac1,"Academy3","01211212123","ac3@gmail.com","Egypt shebin","333333", Tags.academy));
        academyModelList.add(new AcademyModel("4",R.drawable.ac2,"Academy4","01000022525","ac4@gmail.com","Egypt shebin","444444", Tags.academy));
        academyModelList.add(new AcademyModel("5",R.drawable.ac1,"Academy5","01033333222","ac5@gmail.com","Egypt shebin","555555", Tags.academy));
        academyModelList.add(new AcademyModel("6",R.drawable.ac2,"Academy6","01011112222","ac6@gmail.com","Egypt shebin","666666", Tags.academy));
        academyModelList.add(new AcademyModel("7",R.drawable.ac1,"Academy7","01122122222","ac7@gmail.com","Egypt shebin","777777", Tags.academy));

    }

    private void createAlertDialog() {
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(true)
                .setMessage("Login...")
                .build();
    }

    private void Signin() {
        String email = edt_email.getText().toString();
        String password = edt_password.getText().toString();
        if (TextUtils.isEmpty(email))
        {
            edt_email.setError("Email require");
            Snackbar.make(root,"Email require",Snackbar.LENGTH_SHORT).show();

        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            edt_email.setError("Invalid email");
            Snackbar.make(root,"Invalid email",Snackbar.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password))
        {
            edt_email.setError(null);
            edt_password.setError("Password require");
            Snackbar.make(root,"Password require",Snackbar.LENGTH_SHORT).show();
        }else if (password.length()<5)
        {
            edt_email.setError(null);

            edt_password.setError("Password is too short");
            Snackbar.make(root,"Password is too short",Snackbar.LENGTH_SHORT).show();
        }else
            {
                edt_email.setError(null);
                edt_password.setError(null);

                if (email.toLowerCase().startsWith("ac"))
                {
                    for (AcademyModel academyModel:academyModelList)
                    {
                        if (academyModel.getEmail().equals(email)&&academyModel.getPassword().equals(password))
                        {
                            accademy_result.add(academyModel);
                            break;
                        }
                    }
                    if (accademy_result.size()==1)
                    {

                        dialog.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                intent.putExtra("ac_data",accademy_result.get(0));
                                startActivity(intent);
                                finish();
                            }
                        },5000);
                    }else if (accademy_result.size()==0)
                    {
                        Toast.makeText(this, "Check username or password", Toast.LENGTH_SHORT).show();
                    }
                }else if (email.toLowerCase().startsWith("u"))
                {
                    for (UserModel userModel:userModelList)
                    {
                        if (userModel.getEmail().equals(email)&& userModel.getPassword().equals(password))
                        {
                            userResult.add(userModel);
                            break;
                        }
                    }

                    if (userResult.size()==1)
                    {
                        dialog.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                intent.putExtra("user_data",userResult.get(0));
                                startActivity(intent);
                                finish();
                            }
                        },5000);
                    }
                    else if (userResult.size()==0)
                    {
                        Toast.makeText(this, "Check username or password", Toast.LENGTH_SHORT).show();

                    }
                }else
                    {
                        Toast.makeText(this, "Check username or password", Toast.LENGTH_SHORT).show();

                    }




            }
    }
}
