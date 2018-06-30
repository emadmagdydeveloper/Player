package com.alatheer.myplayer.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.myplayer.Activities.PlayersActivity;
import com.alatheer.myplayer.Models.PlayersModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by elashry on 28/06/2018.
 */

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.MyHolder> {
    private Context context;
    private List<PlayersModel> playersModelList;
    private PlayersActivity activity;

    public PlayerAdapter(Context context, List<PlayersModel> playersModelList) {
        this.context = context;
        this.playersModelList = playersModelList;
        this.activity = (PlayersActivity) context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.player_item_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        PlayersModel playersModel = playersModelList.get(position);
        holder.BindData(playersModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setPos(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return playersModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tv_name,tv_age,tv_height,tv_weight,tv_code;
        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_age = itemView.findViewById(R.id.tv_age);
            tv_height = itemView.findViewById(R.id.tv_height);
            tv_weight = itemView.findViewById(R.id.tv_weight);
            tv_code = itemView.findViewById(R.id.tv_code);
        }
        private void BindData(PlayersModel playersModel)
        {
            try {
                Picasso.with(context).load(Uri.parse(Tags.imageUrl+playersModel.getPlayer_photo())).into(image);
                tv_name.setText(playersModel.getPlayer_name());
                tv_age.setText(playersModel.getPlayer_age());
                tv_height.setText(playersModel.getPlayer_tall());
                tv_weight.setText(playersModel.getPlayer_weight());
                tv_code.setText(playersModel.getPlayer_id());
            }catch (NullPointerException e){}
            catch (Exception e){}
        }
    }

}
