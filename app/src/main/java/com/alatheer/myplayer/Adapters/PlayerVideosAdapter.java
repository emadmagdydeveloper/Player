package com.alatheer.myplayer.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.myplayer.Activities.PlayerVideosActivity;
import com.alatheer.myplayer.Activities.PlayersActivity;
import com.alatheer.myplayer.Models.PlayersModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by elashry on 28/06/2018.
 */

public class PlayerVideosAdapter extends RecyclerView.Adapter<PlayerVideosAdapter.MyHolder> {
    private Context context;
    private List<PlayersModel> playersModelList;
    private PlayerVideosActivity activity;

    public PlayerVideosAdapter(Context context, List<PlayersModel> playersModelList) {
        this.context = context;
        this.playersModelList = playersModelList;
        this.activity = (PlayerVideosActivity) context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.player_video_item_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        PlayersModel playersModel = playersModelList.get(position);
        holder.BindData(playersModel);
        holder.openVideo.setOnClickListener(view -> activity.changeVideo(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return playersModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private FrameLayout openVideo;
        private TextView tv_comment;
        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            openVideo = itemView.findViewById(R.id.openVideo);
            tv_comment = itemView.findViewById(R.id.tv_comments);



        }
        private void BindData(PlayersModel playersModel)
        {
            try {
                Picasso.with(context).load(Uri.parse(Tags.imageUrl+playersModel.getPlayer_photo())).into(image);
                tv_comment.setText(playersModel.getPlayer_age());

            }catch (NullPointerException e){}
            catch (Exception e){}
        }
    }

}
