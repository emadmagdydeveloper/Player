package com.alatheer.myplayer.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alatheer.myplayer.Adapters.PlayerVideosAdapter;
import com.alatheer.myplayer.Models.PlayersModel;
import com.alatheer.myplayer.Models.ResponseModel;
import com.alatheer.myplayer.Models.UserModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerVideosActivity extends AppCompatActivity {
    private ImageView back,playBtn;
    private VideoView videoView;
    private ProgressBar video_progress,progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private String whoVisit="";
    private LinearLayout like,dislike,ll_view;
    private TextView tv_like,tv_dislike,tv_view;
    private UserModel userModel;
    private TextView no_data;
    private List<PlayersModel> playersModelList;
    private int currVideoPos=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_videos);
        initView();
        getDataFromIntent();
    }

    private void initView() {
        playersModelList = new ArrayList<>();
        back = findViewById(R.id.back);
        playBtn = findViewById(R.id.playBtn);
        videoView = findViewById(R.id.videoView);
        video_progress = findViewById(R.id.video_progBar);
        progBar = findViewById(R.id.progBar);
        recView = findViewById(R.id.recView);
        adapter = new PlayerVideosAdapter(this,playersModelList);
        manager = new LinearLayoutManager(this);
        recView.setLayoutManager(manager);
        recView.setAdapter(adapter);
        recView.setHasFixedSize(true);
        like = findViewById(R.id.like);
        ll_view = findViewById(R.id.ll_view);
        dislike = findViewById(R.id.dislike);
        tv_like = findViewById(R.id.tv_like);
        tv_dislike = findViewById(R.id.tv_dislike);
        tv_view = findViewById(R.id.tv_view);
        no_data = findViewById(R.id.no_data);
        back.setOnClickListener(view -> finish());

        videoView.setOnInfoListener((mediaPlayer, i, i1) -> {
            if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == i) {
                video_progress.setVisibility(View.GONE);
            }
            if (MediaPlayer.MEDIA_INFO_BUFFERING_START == i) {
                video_progress.setVisibility(View.VISIBLE);
            }
            if (MediaPlayer.MEDIA_INFO_BUFFERING_END == i) {
                video_progress.setVisibility(View.GONE);
            }
            return false;
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (playersModelList.size()>0)
                {
                    try {
                        updateVideoInteraction(playersModelList.get(currVideoPos).getPlayer_id(),Tags.like);

                    }catch (NullPointerException e){}
                    catch (ArrayIndexOutOfBoundsException e){}

                }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (playersModelList.size()>0)
                {
                    try {
                        updateVideoInteraction(playersModelList.get(currVideoPos).getPlayer_id(),Tags.dislike);

                    }catch (NullPointerException e){}
                    catch (ArrayIndexOutOfBoundsException e){}

                }
            }
        });



    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            userModel = (UserModel) intent.getSerializableExtra("data");
            whoVisit = intent.getStringExtra("who_visit");
            getPlayersVideo(userModel.getUser_id());
        }
    }

    private void getPlayersVideo(String id) {
        Tags.getService().getPlayers(id)
                .enqueue(new Callback<List<PlayersModel>>() {
                    @Override
                    public void onResponse(Call<List<PlayersModel>> call, Response<List<PlayersModel>> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body().size()>0)
                            {
                                no_data.setVisibility(View.GONE);
                                playersModelList.clear();
                                playersModelList.addAll(response.body());
                                adapter.notifyDataSetChanged();
                                playBtn.setEnabled(true);
                                //changeVideo(0);
                                tv_like.setText(playersModelList.get(0).getPlayer_vedio_like());
                                tv_dislike.setText(playersModelList.get(0).getPlayer_vedio_dislike());
                                tv_view.setText(playersModelList.get(0).getPlayer_vedio_view());
                                updateVideoInteraction(playersModelList.get(0).getPlayer_id(),Tags.viewer);
                                currVideoPos=0;
                                if (!TextUtils.isEmpty(playersModelList.get(0).getPlayer_vedio())&&!playersModelList.get(0).getPlayer_vedio().equals("0"))
                                {
                                    playBtn.setVisibility(View.GONE);
                                    video_progress.setVisibility(View.VISIBLE);
                                    videoView.setVideoURI(Uri.parse(Tags.vedioUrl+playersModelList.get(0).getPlayer_vedio()));
                                    videoView.start();
                                    tv_like.setVisibility(View.VISIBLE);
                                    tv_dislike.setVisibility(View.VISIBLE);
                                    tv_view.setVisibility(View.VISIBLE);
                                    like.setVisibility(View.VISIBLE);
                                    dislike.setVisibility(View.VISIBLE);
                                    ll_view.setVisibility(View.VISIBLE);
                                }else
                                    {
                                        playBtn.setVisibility(View.VISIBLE);
                                        video_progress.setVisibility(View.GONE);
                                        tv_like.setVisibility(View.GONE);
                                        tv_dislike.setVisibility(View.GONE);
                                        tv_view.setVisibility(View.GONE);
                                        like.setVisibility(View.GONE);
                                        dislike.setVisibility(View.GONE);
                                        ll_view.setVisibility(View.GONE);
                                    }


                            }else
                            {
                                playBtn.setEnabled(false);
                                no_data.setVisibility(View.VISIBLE);
                                tv_like.setVisibility(View.INVISIBLE);
                                tv_dislike.setVisibility(View.INVISIBLE);
                                tv_view.setVisibility(View.INVISIBLE);
                                like.setVisibility(View.INVISIBLE);
                                dislike.setVisibility(View.INVISIBLE);
                                ll_view.setVisibility(View.GONE);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlayersModel>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(PlayerVideosActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void changeVideo(int pos)
    {
        if (pos!=currVideoPos)
        {
            playBtn.setVisibility(View.GONE);
            currVideoPos = pos;
            tv_like.setText(playersModelList.get(pos).getPlayer_vedio_like());
            tv_dislike.setText(playersModelList.get(pos).getPlayer_vedio_dislike());
            tv_view.setText(playersModelList.get(pos).getPlayer_vedio_view());
            //playBtn.setEnabled(true);
            if (videoView.isPlaying())
            {
                videoView.stopPlayback();
                if (TextUtils.isEmpty(playersModelList.get(pos).getPlayer_vedio()))
                {
                    Toast.makeText(this, "This video not available", Toast.LENGTH_SHORT).show();
                }else
                {
                    video_progress.setVisibility(View.VISIBLE);
                    videoView.setVideoURI(Uri.parse(Tags.vedioUrl+playersModelList.get(pos).getPlayer_vedio()));
                    videoView.start();
                }
            }else
            {
                if (TextUtils.isEmpty(playersModelList.get(pos).getPlayer_vedio()))
                {
                    Toast.makeText(this, "This video not available", Toast.LENGTH_SHORT).show();
                }else
                {
                    videoView.setVideoURI(Uri.parse(Tags.vedioUrl+playersModelList.get(pos).getPlayer_vedio()));
                    videoView.start();

                }

            }

           /* tv_like.setVisibility(View.INVISIBLE);
            tv_dislike.setVisibility(View.INVISIBLE);
            tv_view.setVisibility(View.INVISIBLE);
            like.setVisibility(View.INVISIBLE);
            dislike.setVisibility(View.INVISIBLE);*/
        }

    }


    private void updateVideoInteraction(String id,String type)
    {
        Tags.getService().updateVideoInteraction(id,type)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess()==1)
                            {
                                if (type.equals(Tags.like))
                                {
                                    int likes  = Integer.parseInt(tv_like.getText().toString())+1;
                                    tv_like.setText(String.valueOf(likes));
                                }else if (type.equals(Tags.dislike))
                                {
                                    int dislikes  = Integer.parseInt(tv_dislike.getText().toString())+1;
                                    tv_dislike.setText(String.valueOf(dislikes));
                                }
                                else if (type.equals(Tags.viewer))
                                {
                                    int v  = Integer.parseInt(tv_view.getText().toString())+1;
                                    tv_view.setText(String.valueOf(v));
                                }
                            }else if (response.body().getSuccess()==0)
                            {
                                Toast.makeText(PlayerVideosActivity.this, "Failed try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                    }
                });
    }


}
