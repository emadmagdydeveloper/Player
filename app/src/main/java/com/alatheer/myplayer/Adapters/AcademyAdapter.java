package com.alatheer.myplayer.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.myplayer.Activities.HomeActivity;
import com.alatheer.myplayer.Models.AcademyModel;
import com.alatheer.myplayer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by elashry on 27/06/2018.
 */

public class AcademyAdapter extends RecyclerView.Adapter<AcademyAdapter.MyHolder> {
    private List<AcademyModel>  academyModelList;
    private Context context;
    private HomeActivity activity;

    public AcademyAdapter(List<AcademyModel> academyModelList, Context context) {
        this.academyModelList = academyModelList;
        this.context = context;
        this.activity = (HomeActivity) context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.academy_item_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        AcademyModel academyModel = academyModelList.get(position);
        setAnimation(holder.itemView);
        holder.BindData(academyModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setRecieveDataFromAdapter(academyModelList.get(holder.getAdapterPosition()));
            }
        });
    }

    private void setAnimation(View view)
    {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f,0f,1.0f,1.0f);
        view.startAnimation(scaleAnimation);
    }
    @Override
    public int getItemCount() {
        return academyModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tv_name,tv_phone;
        public MyHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_phone);
        }
        private void BindData(AcademyModel academyModel)
        {
            Picasso.with(context).load(academyModel.getImage()).into(image);
            tv_name.setText(academyModel.getName());
            tv_phone.setText(academyModel.getPhone());
        }
    }

    public void Clear(List<AcademyModel> academyModelList)
    {
        this.academyModelList.clear();
        this.academyModelList.addAll(academyModelList);
        notifyDataSetChanged();
    }
}
