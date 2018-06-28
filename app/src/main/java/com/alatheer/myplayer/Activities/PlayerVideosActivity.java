package com.alatheer.myplayer.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.alatheer.myplayer.R;

public class PlayerVideosActivity extends AppCompatActivity {
    private ImageView back,playBtn;
    private VideoView videoView;
    private ProgressBar video_progress,progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_videos);
        initView();
    }

    private void initView() {
        back = findViewById(R.id.back);
        playBtn = findViewById(R.id.playBtn);
        videoView = findViewById(R.id.videoView);
        video_progress = findViewById(R.id.video_progBar);
        progBar = findViewById(R.id.progBar);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);


    }
}
