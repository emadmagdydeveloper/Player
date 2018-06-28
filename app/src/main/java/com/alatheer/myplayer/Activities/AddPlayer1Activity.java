package com.alatheer.myplayer.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alatheer.myplayer.Models.AddPlayerModel;
import com.alatheer.myplayer.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPlayer1Activity extends AppCompatActivity {

    private CircleImageView image;
    private ImageView back;
    private Bitmap bitmap;
    private String encodedImage="";
    private EditText edt_name,edt_age,edt_height,edt_weight,edt_code;
    private Button nextBtn;
    private final int IMG_REQ = 222;
    private  String name,age,height,weight,code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplayer1);
        initView();

    }

    private void initView() {
        image = findViewById(R.id.image);
        back = findViewById(R.id.back);
        edt_name = findViewById(R.id.edt_name);
        edt_age = findViewById(R.id.edt_age);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        edt_code = findViewById(R.id.edt_code);
        nextBtn = findViewById(R.id.nextBtn);

        image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent.createChooser(intent,"Choose Image"),IMG_REQ);

        });

        back.setOnClickListener(view -> finish());

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 name= edt_name.getText().toString();
                 age = edt_age.getText().toString();
                 height = edt_height.getText().toString();
                 weight = edt_weight.getText().toString();
                 code = edt_code.getText().toString();
                if (TextUtils.isEmpty(name))
                {
                    edt_name.setError("Name require");
                }else if (TextUtils.isEmpty(age))
                {
                    edt_age.setError("Age require");
                    edt_name.setError(null);

                }
                else if (TextUtils.isEmpty(height))
                {
                    edt_height.setError("Height require");
                    edt_name.setError(null);
                    edt_age.setError(null);

                }else if (TextUtils.isEmpty(weight))
                {
                    edt_name.setError(null);
                    edt_age.setError(null);
                    edt_height.setError(null);

                    edt_weight.setError("Weight require");
                }else if (TextUtils.isEmpty(code))
                {
                    edt_name.setError(null);
                    edt_age.setError(null);
                    edt_height.setError(null);
                    edt_weight.setError(null);
                    edt_code.setError("Code require");
                }else if (bitmap ==null)
                    {
                        edt_name.setError(null);
                        edt_age.setError(null);
                        edt_height.setError(null);
                        edt_weight.setError(null);
                        edt_code.setError(null);
                        Toast.makeText(AddPlayer1Activity.this, "Choose player image", Toast.LENGTH_SHORT).show();
                    }else

                        {
                            edt_name.setError(null);
                            edt_age.setError(null);
                            edt_height.setError(null);
                            edt_weight.setError(null);
                            edt_code.setError(null);
                            Intent intent = new Intent(AddPlayer1Activity.this,AddPlayer2Activity.class);
                            startActivityForResult(intent,121);



                        }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQ && resultCode==RESULT_OK && data!=null)
        {
            Uri  uri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                image.setImageBitmap(bitmap);
                encodedImage =EncodeImage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if (requestCode==121 && resultCode==RESULT_OK && data!=null)
        {
            AddPlayerModel playerModel = (AddPlayerModel) data.getSerializableExtra("data");
            playerModel.setImage(encodedImage);
            playerModel.setName(name);
            playerModel.setAge(age);
            playerModel.setHeight(height);
            playerModel.setWeight(weight);
            playerModel.setCode(code);

            AddPlayerData(playerModel);
        }
    }

    private void AddPlayerData(AddPlayerModel playerModel) {
        Log.e("data",playerModel.getImage());
        Log.e("data",playerModel.getAttack());

    }

    private String EncodeImage(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }}
