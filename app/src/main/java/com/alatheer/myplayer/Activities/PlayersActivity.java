package com.alatheer.myplayer.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.myplayer.Adapters.PlayerAdapter;
import com.alatheer.myplayer.Models.PlayersModel;
import com.alatheer.myplayer.Models.UserModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Api;
import com.alatheer.myplayer.Service.Tags;
import com.alatheer.myplayer.Service.UserSingleTone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayersActivity extends AppCompatActivity {

    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ImageView back;
    private List<PlayersModel>playersModelList;
    private ProgressBar progressBar;
    private TextView no_data;
    private String id = "";
    private String whoVisit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        initView();
        getDataFromIntent();
        getPlayers();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            id = intent.getStringExtra("id");
            whoVisit = intent.getStringExtra("who_visit");
        }
    }

    private void getPlayers() {
        Tags.getService().getPlayers(id)
                .enqueue(new Callback<List<PlayersModel>>() {
                    @Override
                    public void onResponse(Call<List<PlayersModel>> call, Response<List<PlayersModel>> response) {
                        if (response.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);

                            if (response.body().size()>0)
                            {
                                no_data.setVisibility(View.GONE);
                                playersModelList.clear();
                                playersModelList.addAll(response.body());
                                adapter.notifyDataSetChanged();

                            }else
                                {
                                    no_data.setVisibility(View.VISIBLE);
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlayersModel>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PlayersActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void initView() {
        playersModelList = new ArrayList<>();
        back = findViewById(R.id.back);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        adapter = new PlayerAdapter(this,playersModelList);
        recView.setAdapter(adapter);
        progressBar = findViewById(R.id.progBar);
        no_data = findViewById(R.id.no_data);
        back.setOnClickListener(view -> finish());
    }




    public void setPos(int pos)
    {
        PlayersModel playersModel = playersModelList.get(pos);
        Intent intent = new Intent(this,PlayerProfileActivity.class);
        intent.putExtra("data",playersModel);
        intent.putExtra("who_visit",whoVisit);
        startActivity(intent);
    }

    public void DeletePlayer(int pos)
    {
        if (whoVisit.equals(Tags.me))
        {
            new AlertDialog.Builder(this)
                    .setMessage("Delete player ?")
                    .setPositiveButton("Delete", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        playersModelList.remove(pos);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();
        }


    }
}
