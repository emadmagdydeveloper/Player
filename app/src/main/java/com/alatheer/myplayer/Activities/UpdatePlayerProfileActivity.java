package com.alatheer.myplayer.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.myplayer.Models.PlayersModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePlayerProfileActivity extends AppCompatActivity {
    private CircleImageView image;
    private ImageView back;
    private Bitmap bitmap;
    private String encodedImage="";
    private EditText edt_name,edt_age,edt_height,edt_weight,edt_comment,edt_position;
    private Button updateBtn;
    private final int IMG_REQ = 222;
    private  String name,age,height,weight,position,comment;
    private DialogProperties properties;
    private FilePickerDialog dialog;
    private TextView uploadBtn,tv_video_path;
    private String encoded_video="";
    private PlayersModel playersModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_player_profile);
        initView();
        getDataFromIntent();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            playersModel = (PlayersModel) intent.getSerializableExtra("data");
            updateUi(playersModel);
        }
    }

    private void updateUi(PlayersModel playersModel) {
        Picasso.with(this).load(Uri.parse(Tags.imageUrl+playersModel.getPlayer_photo())).into(image);
        edt_name.setText(playersModel.getPlayer_name());
        edt_age.setText(playersModel.getPlayer_age());
        edt_height.setText(playersModel.getPlayer_tall());
        edt_weight.setText(playersModel.getPlayer_weight());
        edt_position.setText(playersModel.getPlayer_position());
        edt_comment.setText(playersModel.getPlayer_vedio_comment());
    }

    private void initView() {
        image = findViewById(R.id.image);
        back = findViewById(R.id.back);
        edt_name = findViewById(R.id.edt_name);
        edt_age = findViewById(R.id.edt_age);
        edt_height = findViewById(R.id.edt_height);
        edt_weight = findViewById(R.id.edt_weight);
        edt_comment = findViewById(R.id.edt_comment);
        edt_position = findViewById(R.id.edt_position);
        updateBtn = findViewById(R.id.updateBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        tv_video_path = findViewById(R.id.tv_video_path);
        image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent.createChooser(intent,"Choose Image"),IMG_REQ);

        });

        back.setOnClickListener(view -> finish());

        updateBtn.setOnClickListener(view -> {
            name= edt_name.getText().toString();
            age = edt_age.getText().toString();
            height = edt_height.getText().toString();
            weight = edt_weight.getText().toString();
            position = edt_position.getText().toString();
            comment = edt_comment.getText().toString();

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
            }else if (TextUtils.isEmpty(position))
            {
                edt_name.setError(null);
                edt_age.setError(null);
                edt_height.setError(null);
                edt_weight.setError(null);
                edt_position.setError("Position required");
            }

            else if (TextUtils.isEmpty(comment))
            {
                edt_name.setError(null);
                edt_age.setError(null);
                edt_height.setError(null);
                edt_weight.setError(null);
                edt_position.setError(null);
                edt_comment.setError("Comment require");
            }else{
                edt_name.setError(null);
                edt_age.setError(null);
                edt_height.setError(null);
                edt_weight.setError(null);
                edt_comment.setError(null);
                if (bitmap==null)
                {
                    encodedImage = "";
                }
                UpdatePlayer(encodedImage,name,age,weight,height,position,comment,encoded_video);
            }
        });

        properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = new String[]{"mp4"};

        dialog = new FilePickerDialog(UpdatePlayerProfileActivity.this,properties);
        dialog.setTitle("Select a Video");
        dialog.setDialogSelectionListener(files -> {
            if (files.length>1)
            {
                Toast.makeText(UpdatePlayerProfileActivity.this, "Only one video allowed", Toast.LENGTH_SHORT).show();
            }else if (files.length==1)
            {
                String path = files[0];
                String file_endPoint = path.substring(path.lastIndexOf("/"+1));
                tv_video_path.setText(file_endPoint);
                ProgressDialog dialog = Tags.CreateProgressDialog(UpdatePlayerProfileActivity.this, "Uploading image....");
                dialog.show();
                encoded_video = EncodeFile(path);
                dialog.dismiss();

                Log.e("path", path);
            }

        });
        uploadBtn.setOnClickListener(view -> dialog.show());
    }

    private void UpdatePlayer(String encodedImage, String name, String age, String weight, String height, String position, String comment, String encoded_video)
    {
        ProgressDialog dialog = Tags.CreateProgressDialog(this, "Adding player...");
        dialog.show();
        Tags.getService().updatePlayer(playersModel.getPlayer_id(),name,age,position,height,encodedImage,weight,encoded_video,comment)
                .enqueue(new Callback<PlayersModel>() {
                    @Override
                    public void onResponse(Call<PlayersModel> call, Response<PlayersModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess()==1)
                            {
                                dialog.dismiss();
                                Toast.makeText(UpdatePlayerProfileActivity.this, "successfully updated", Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                intent.putExtra("data",response.body());
                                setResult(RESULT_OK,intent);
                                finish();
                            }else if (response.body().getSuccess()==0)
                            {
                                dialog.dismiss();
                                Toast.makeText(UpdatePlayerProfileActivity.this, "Failed try again later", Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PlayersModel> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(UpdatePlayerProfileActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();
                        Log.e("Error",t.getMessage());
                    }
                });
    }

    private String EncodeFile(String path) {
        File file = new File(path);
        String enc_vid = "";
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte [] bytes = new byte[(int)file.length()];
            inputStream.read(bytes);
            enc_vid= Base64.encodeToString(bytes,Base64.DEFAULT);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return enc_vid;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQ && resultCode==RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                image.setImageBitmap(bitmap);
                ProgressDialog dialog = Tags.CreateProgressDialog(UpdatePlayerProfileActivity.this, "Uploading image....");
                dialog.show();
                encodedImage =EncodeImage(bitmap);
                dialog.dismiss();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



    private String EncodeImage(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
}
