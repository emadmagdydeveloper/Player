package com.alatheer.myplayer.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alatheer.myplayer.Adapters.PlayerAdapter;
import com.alatheer.myplayer.Models.PlayersModel;
import com.alatheer.myplayer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayersActivity extends AppCompatActivity {

    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ImageView back;
    private List<PlayersModel>playersModelList;
    private Map<String,List<PlayersModel>> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        initView();
        CreatePlayers();

    }



    private void CreatePlayers() {
        playersModelList.add(new PlayersModel("1",R.drawable.p1,"Messi","26 year","155 cm","60 kg","100","","","","","",""));
        playersModelList.add(new PlayersModel("2",R.drawable.p2,"Neymar","25 year","170 cm","70 kg","200","","","","","",""));
        playersModelList.add(new PlayersModel("3",R.drawable.p3,"Cruis ","26 year","155 cm","60 kg","300","","","","","",""));
        playersModelList.add(new PlayersModel("4",R.drawable.p4,"Tiago costa ","26 year","155 cm","60 kg","400","","","","","",""));
        playersModelList.add(new PlayersModel("5",R.drawable.p5,"Roben ","28 year","155 cm","60 kg","500","","","","","",""));
        playersModelList.add(new PlayersModel("6",R.drawable.p6,"Salah ","28 year","155 cm","60 kg","600","","","","","",""));
        playersModelList.add(new PlayersModel("7",R.drawable.p7,"Marcelo ","28 year","155 cm","60 kg","700","","","","","",""));

        adapter.notifyDataSetChanged();

    }

    private void initView() {
        playersModelList = new ArrayList<>();

        map = new HashMap<>();
        back = findViewById(R.id.back);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        adapter = new PlayerAdapter(this,playersModelList);
        recView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
