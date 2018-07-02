package com.alatheer.myplayer.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alatheer.myplayer.Models.PlayersModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;
import com.squareup.picasso.Picasso;

public class PlayerProfileActivity extends AppCompatActivity {
    private TextView tv_name,tv_age,tv_height,tv_weight,tv_code,tv_position,tv_comments;
    private ImageView image,back;
    private PlayersModel playersModel;
    private ImageView playBtn;
    private ProgressBar progressBar;
    private VideoView videoView;
    private String url="";
    private MediaController mediaController;
    private Button updateBtn;
    private String whoVisit="";

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
            playersModel = (PlayersModel) intent.getSerializableExtra("data");
            whoVisit = intent.getStringExtra("who_visit");
            updateUi(playersModel);
        }
    }

    private void updateUi(PlayersModel playersModel) {

            Log.e("who",whoVisit);
            Log.e("img",playersModel.getPlayer_photo());
            Log.e("name",playersModel.getPlayer_name());
            Log.e("tall",playersModel.getPlayer_tall());

            Picasso.with(this).load(Uri.parse(Tags.imageUrl+playersModel.getPlayer_photo())).into(image);
            tv_name.setText(playersModel.getPlayer_name());
            tv_age.setText(playersModel.getPlayer_age()+" year");
            tv_height.setText(playersModel.getPlayer_tall()+" cm");
            tv_weight.setText(playersModel.getPlayer_weight()+" kg");
            tv_position.setText(playersModel.getPlayer_position());
            tv_code.setText(playersModel.getPlayer_id());
            tv_comments.setText(playersModel.getPlayer_vedio_comment());

            if (whoVisit.equals(Tags.me))
            {
                updateBtn.setVisibility(View.VISIBLE);
            }else if (whoVisit.equals(Tags.visitor))
            {
                updateBtn.setVisibility(View.INVISIBLE);

            }






            ////////////////////////////////////////
            url = playersModel.getPlayer_vedio();
            if (!url.isEmpty()&&!url.equals("0"))
            {
                videoView.setVideoURI(Uri.parse(Tags.vedioUrl+url));
                mediaController = new android.widget.MediaController(this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
            }


            playBtn.setOnClickListener(view -> {
                if (TextUtils.isEmpty(url))
                {
                    Toast.makeText(PlayerProfileActivity.this, "No video available", Toast.LENGTH_SHORT).show();
                }else
                    {
                        playBtn.setVisibility(View.GONE);
                        videoView.start();
                    }

            });
            videoView.setOnInfoListener((mediaPlayer, i, i1) -> {
                if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == i) {
                    progressBar.setVisibility(View.GONE);
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_START == i) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_END == i) {
                    progressBar.setVisibility(View.GONE);
                }
                return false;
            });
            videoView.setOnCompletionListener(mediaPlayer -> playBtn.setVisibility(View.VISIBLE));


    }

    private void initView() {
        tv_name = findViewById(R.id.tv_name);
        tv_age = findViewById(R.id.tv_age);
        tv_height = findViewById(R.id.tv_height);
        tv_weight = findViewById(R.id.tv_weight);
        tv_code = findViewById(R.id.tv_code);
        tv_position = findViewById(R.id.tv_position);
        tv_comments = findViewById(R.id.tv_comments);
        image = findViewById(R.id.image);
        back = findViewById(R.id.back);
        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progBar);
        playBtn = findViewById(R.id.playBtn);
        updateBtn = findViewById(R.id.updateBtn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayerProfileActivity.this,UpdatePlayerProfileActivity.class);
                intent.putExtra("data",playersModel);
                startActivityForResult(intent,202);
            }
        });
        back.setOnClickListener(view -> finish());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==202&& resultCode == RESULT_OK && data!=null)
        {
            playersModel = (PlayersModel) data.getSerializableExtra("data");
            updateUi(playersModel);
        }
    }
}
