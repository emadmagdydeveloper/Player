package com.alatheer.myplayer.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.myplayer.Models.PlayersModel;
import com.alatheer.myplayer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by elashry on 28/06/2018.
 */

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.MyHolder> {
    private Context context;
    private List<PlayersModel> playersModelList;

    public PlayerAdapter(Context context, List<PlayersModel> playersModelList) {
        this.context = context;
        this.playersModelList = playersModelList;
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
                Picasso.with(context).load(playersModel.getImage()).into(image);
                tv_name.setText(playersModel.getName());
                tv_age.setText(playersModel.getAge());
                tv_height.setText(playersModel.getHeight());
                tv_weight.setText(playersModel.getWeight());
                tv_code.setText(playersModel.getCode());
            }catch (NullPointerException e){}
            catch (Exception e){}
        }
    }

}
