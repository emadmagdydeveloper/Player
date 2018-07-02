package com.alatheer.myplayer.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.myplayer.Models.UserModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Preference;
import com.alatheer.myplayer.Service.Tags;
import com.alatheer.myplayer.Service.UserSingleTone;
import com.google.gson.Gson;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView tv_signup,skip;
    private EditText edt_email,edt_password;
    private Button loginBtn;
    private RelativeLayout root;
    private android.app.AlertDialog dialog;
    private UserSingleTone userSingleTone;
    private Preference preference;
    private ShimmerTextView tv_shimmer;
    private Shimmer shimmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userSingleTone = UserSingleTone.getInstance();
        preference = new Preference(this);
        initView();


    }

    private void initView() {
        skip = findViewById(R.id.skip);
        tv_signup = findViewById(R.id.tv_signup);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        loginBtn = findViewById(R.id.loginBtn);
        root = findViewById(R.id.root);
        tv_shimmer = findViewById(R.id.tv_shimmer);
        shimmer = new Shimmer();
        shimmer.setDuration(500);
        shimmer.setStartDelay(300);
        shimmer.setDirection(Shimmer.ANIMATION_DIRECTION_RTL);
        tv_shimmer.setReflectionColor(ContextCompat.getColor(this,R.color.tvColor));
        shimmer.start(tv_shimmer);
        tv_signup.setOnClickListener(view ->
        {
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        loginBtn.setOnClickListener(view -> Signin());
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                intent.putExtra("user_type",Tags.user_type_skip);
                startActivity(intent);
            }
        });

        createAlertDialog();

        checkSession();


    }

    private void checkSession() {
        String session = preference.getSession();
        if (session!=null||!TextUtils.isEmpty(session))
        {
            if (session.equals(Tags.session_login))
            {
                UserModel userModel = preference.getUserData();
                userSingleTone.setUserData(userModel);

                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                intent.putExtra("user_type",userModel.getUser_type());
                startActivity(intent);
                finish();
            }
        }
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
                dialog.show();
                Login(email,password);



            }
    }

    private void Login(String email, String password) {
        Tags.getService().login(email,password).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {

                        userSingleTone.setUserData(response.body());
                        Gson gson = new Gson();
                        String userData = gson.toJson(response.body());
                        preference.CreateSharedPref(userData);
                        dialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        intent.putExtra("user_type",response.body().getUser_type());
                        startActivity(intent);
                        finish();
                    }else if (response.body().getSuccess()==0)
                    {
                        dialog.dismiss();

                        Toast.makeText(LoginActivity.this, "Failed try again later", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();

                Log.e("Error",t.getMessage());
            }
        });
    }
}
