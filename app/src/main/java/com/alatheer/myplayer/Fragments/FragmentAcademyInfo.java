package com.alatheer.myplayer.Fragments;

import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.alatheer.myplayer.R;

/**
 * Created by elashry on 27/06/2018.
 */

public class FragmentAcademyInfo extends Fragment {
    private View view;
    private static FragmentAcademyInfo instance=null;
    private ProgressBar progressBar;
    private ImageView playBtn;
    private VideoView videoView;
    private String url="";
    private android.widget.MediaController mediaController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_academy_info,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.progBar);
        playBtn = view.findViewById(R.id.playBtn);
        videoView = view.findViewById(R.id.videoView);
        url = "android.resource://"+getActivity().getPackageName()+"/"+R.raw.v;
        videoView.setVideoURI(Uri.parse(url));
        mediaController = new android.widget.MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                progressBar.setVisibility(View.VISIBLE);
            }
            return false;
        });
        videoView.setOnCompletionListener(mediaPlayer -> playBtn.setVisibility(View.VISIBLE));

    }

    public static synchronized FragmentAcademyInfo getInstance()
    {
        if (instance==null)
        {
            instance = new FragmentAcademyInfo();
        }
        return instance;
    }
}
