package com.alatheer.myplayer.Activities;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutAppActivity extends AppCompatActivity {

    private TextView tv_aboutApp;
    private ProgressBar progressBar;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        initView();
    }

    private void initView() {
        tv_aboutApp = findViewById(R.id.tv_aboutApp);
        progressBar = findViewById(R.id.progBar);
        back = findViewById(R.id.back);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        back.setOnClickListener(view -> finish());

        getAppData();
    }

    private void getAppData() {
        Tags.getService().aboutApp().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful())
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        String data = jsonObject.getString("about_app");
                        tv_aboutApp.setText(data);
                        progressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Error",t.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AboutAppActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
