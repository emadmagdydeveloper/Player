package com.alatheer.myplayer.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alatheer.myplayer.R;

public class PlayerProfileActivity extends AppCompatActivity {
    private TextView tv_name,tv_age,tv_height,tv_weight,tv_code,tv_position,tv_speed,tv_attack,tv_defense,tv_finish,tv_kick;
    private ImageView image,back;
    private LinearLayout watvch_video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        initView();
        getDataFromIntent();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {

        }
    }

    private void initView() {
        tv_name = findViewById(R.id.tv_name);
        tv_age = findViewById(R.id.tv_age);
        tv_height = findViewById(R.id.tv_height);
        tv_weight = findViewById(R.id.tv_weight);
        tv_code = findViewById(R.id.tv_code);
        tv_position = findViewById(R.id.tv_position);
        tv_speed = findViewById(R.id.tv_speed);
        tv_attack = findViewById(R.id.tv_attack);
        tv_defense = findViewById(R.id.tv_defense);
        tv_finish = findViewById(R.id.tv_finish);
        tv_kick = findViewById(R.id.tv_kick);
        image = findViewById(R.id.image);
        back = findViewById(R.id.back);
        watvch_video = findViewById(R.id.watch_video);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
