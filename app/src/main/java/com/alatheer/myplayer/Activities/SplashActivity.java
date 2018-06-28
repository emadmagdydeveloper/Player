package com.alatheer.myplayer.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.alatheer.myplayer.R;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

public class SplashActivity extends AppCompatActivity {

    private ShimmerTextView tv_shimmer;
    private Shimmer shimmer;
    private LinearLayout logo;
    private Animation zoom_in,rotate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        tv_shimmer = findViewById(R.id.tv_shimmer);
        logo = findViewById(R.id.logo);
        shimmer = new Shimmer();
        shimmer.setDuration(500);
        shimmer.setStartDelay(300);
        shimmer.setDirection(Shimmer.ANIMATION_DIRECTION_RTL);
        tv_shimmer.setReflectionColor(ContextCompat.getColor(this,R.color.tvColor));
        shimmer.start(tv_shimmer);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);*/


        zoom_in = AnimationUtils.loadAnimation(this,R.anim.zoom_in);
        rotate = AnimationUtils.loadAnimation(this,R.anim.rotate);
        logo.startAnimation(rotate);

        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.startAnimation(zoom_in);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        zoom_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
