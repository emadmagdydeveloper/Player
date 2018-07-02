package com.alatheer.myplayer.Fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alatheer.myplayer.Models.UserModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;

/**
 * Created by elashry on 27/06/2018.
 */

public class FragmentAcademyInfo extends Fragment {
    public static final String TAG="data";
    private View view;
    private static FragmentAcademyInfo instance=null;
    private ProgressBar progressBar;
    private ImageView playBtn;
    private VideoView videoView;
    private TextView tv_info;
    private String url="";
    private android.widget.MediaController mediaController;
    private UserModel userModel;
    private String comment="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_academy_info,container,false);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            userModel = (UserModel) bundle.getSerializable(TAG);
            if (userModel!=null)
            {
                url = userModel.getUser_vedio();
                comment = userModel.getUser_vedio_comment();
            }
        }
        initView(view);
        return view;
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.progBar);
        playBtn = view.findViewById(R.id.playBtn);
        videoView = view.findViewById(R.id.videoView);
        tv_info = view.findViewById(R.id.tv_info);
        mediaController = new android.widget.MediaController(getActivity());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        //url = "android.resource://"+getActivity().getPackageName()+"/"+R.raw.v;

        tv_info.setText(comment);
        Log.e("comm",url+"ssss");




        playBtn.setOnClickListener(view1 -> {
            if (!TextUtils.isEmpty(url)&& url!=null&&!url.equals("0"))
            {
                progressBar.setVisibility(View.VISIBLE);
                videoView.setVideoURI(Uri.parse(Tags.vedioUrl+url));
                playBtn.setVisibility(View.GONE);
                videoView.start();
            }else
                {
                    Toast.makeText(getActivity(), "No video available", Toast.LENGTH_SHORT).show();
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

    public static synchronized FragmentAcademyInfo getInstance(UserModel userModel)
    {
        if (instance==null)
        {
            instance = new FragmentAcademyInfo();
            Bundle bundle = new Bundle();
            bundle.putSerializable(TAG,userModel);
            instance.setArguments(bundle);
        }
        return instance;
    }
}
