package com.alatheer.myplayer.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alatheer.myplayer.Models.AddPlayerModel;
import com.alatheer.myplayer.R;

public class AddPlayer2Activity extends AppCompatActivity {
    private EditText edt_position,edt_speed,edt_attack,edt_defense,edt_finish,edt_kick;
    private Button doneBtn;
    private ImageView back;
    private String image="",name="",age="",height="",weight="",code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player2);
        initView();

    }


    private void initView() {
        back = findViewById(R.id.back);
        edt_position = findViewById(R.id.edt_position);
        edt_speed = findViewById(R.id.edt_speed);
        edt_attack = findViewById(R.id.edt_attack);
        edt_defense = findViewById(R.id.edt_defense);
        edt_finish = findViewById(R.id.edt_finish);
        edt_kick = findViewById(R.id.edt_kick);

        doneBtn = findViewById(R.id.doneBtn);



        back.setOnClickListener(view -> finish());

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String positon= edt_position.getText().toString();
                String speed = edt_speed.getText().toString();
                String attack = edt_attack.getText().toString();
                String defense = edt_defense.getText().toString();
                String finish = edt_finish.getText().toString();
                String kick = edt_kick.getText().toString();

                if (TextUtils.isEmpty(positon))
                {
                    edt_position.setError("Position require");
                }else if (TextUtils.isEmpty(speed))
                {
                    edt_speed.setError("Speed require");
                    edt_position.setError(null);

                }
                else if (TextUtils.isEmpty(attack))
                {
                    edt_attack.setError("Attack require");
                    edt_position.setError(null);
                    edt_speed.setError(null);

                }else if (TextUtils.isEmpty(defense))
                {
                    edt_attack.setError(null);
                    edt_position.setError(null);
                    edt_speed.setError(null);

                    edt_defense.setError("Defense require");
                }else if (TextUtils.isEmpty(finish))
                {
                    edt_attack.setError(null);
                    edt_position.setError(null);
                    edt_speed.setError(null);
                    edt_defense.setError(null);
                    edt_finish.setError("Finish require");
                }else if (TextUtils.isEmpty(kick))
                {
                    edt_attack.setError(null);
                    edt_position.setError(null);
                    edt_speed.setError(null);
                    edt_defense.setError(null);
                    edt_finish.setError(null);
                    edt_kick.setError("Kick require");

                }
                else

                {
                    edt_attack.setError(null);
                    edt_position.setError(null);
                    edt_speed.setError(null);
                    edt_defense.setError(null);
                    edt_finish.setError(null);
                    edt_kick.setError(null);

                    Intent intent = getIntent();
                    AddPlayerModel playerModel = new AddPlayerModel();
                    playerModel.setAttack(attack);
                    playerModel.setPosition(positon);
                    playerModel.setSpeed(speed);
                    playerModel.setDefense(defense);
                    playerModel.setFinish(finish);
                    playerModel.setKick(kick);
                    intent.putExtra("data",playerModel);
                    setResult(RESULT_OK,intent);
                    finish();

                }
            }
        });

    }



}
