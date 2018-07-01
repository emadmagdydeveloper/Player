package com.alatheer.myplayer.Activities;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.myplayer.Fragments.FragmentAcademyContacts;
import com.alatheer.myplayer.Fragments.FragmentAcademyInfo;
import com.alatheer.myplayer.Models.ResponseModel;
import com.alatheer.myplayer.Models.UserModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Preference;
import com.alatheer.myplayer.Service.Tags;
import com.alatheer.myplayer.Service.UserSingleTone;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.gson.Gson;
import com.santalu.diagonalimageview.DiagonalImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcademyProfileActivity extends AppCompatActivity {
    private Button contactBtn,infoBtn;
    private FabSpeedDial fab;
    private DialogProperties properties;
    private FilePickerDialog dialog;
    private UserModel academyModel;
    private DiagonalImageView image_profile;
    private CircleImageView image;
    private TextView tv_name;
    private LinearLayout viewPlayer;
    private String whoVisit ="";
    private LinearLayout watch_video;
    private String encodedCv="";
    private ImageView uploadImage,map;
    private final int IMG_REQ = 523;
    private LinearLayout edit_name;
    private UserSingleTone userSingleTone;
    private Preference preference;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_profile);
        initView();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {

            academyModel = (UserModel) intent.getSerializableExtra("data");
            whoVisit = intent.getStringExtra("who_visit");
            UpdateUi(academyModel);
            getSupportFragmentManager().beginTransaction().add(R.id.academy_fragment_container, FragmentAcademyContacts.getInstance(academyModel,whoVisit)).commit();


        }
    }

    private void UpdateUi(UserModel academyModel)
    {
        try {
            Picasso.with(this).load(Uri.parse(Tags.imageUrl+academyModel.getUser_photo())).into(image);
            Picasso.with(this).load(Uri.parse(Tags.imageUrl+academyModel.getUser_photo())).into(image_profile);
            tv_name.setText(academyModel.getUser_name());
            if (whoVisit.equals(Tags.me))
            {
                fab.setVisibility(View.VISIBLE);
                uploadImage.setVisibility(View.VISIBLE);
                edit_name.setVisibility(View.VISIBLE);
                map.setVisibility(View.GONE);
            }else if (whoVisit.equals(Tags.visitor))
            {
                fab.setVisibility(View.GONE);
                uploadImage.setVisibility(View.GONE);
                edit_name.setVisibility(View.INVISIBLE);
                map.setVisibility(View.VISIBLE);


            }

            watch_video.setOnClickListener(view -> {
                Intent intent = new Intent(AcademyProfileActivity.this,PlayerVideosActivity.class);
                intent.putExtra("data",academyModel);
                intent.putExtra("who_visit",whoVisit);
                startActivity(intent);
            });
        }catch (NullPointerException e){}
        catch (Exception e){}
    }

    private void initView() {

        image_profile = findViewById(R.id.image_profile);
        image = findViewById(R.id.image);
        tv_name = findViewById(R.id.tv_name);
        edit_name = findViewById(R.id.edit_name);
        map = findViewById(R.id.map);
        viewPlayer = findViewById(R.id.viewPlayer);
        watch_video = findViewById(R.id.watch_video);
        uploadImage = findViewById(R.id.uploadImage);
        properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = new String[]{"pdf"};

        dialog = new FilePickerDialog(AcademyProfileActivity.this,properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(files -> {
            if (files.length>1)
            {
                Toast.makeText(AcademyProfileActivity.this, "Only one CV allowed", Toast.LENGTH_SHORT).show();

            }else if (files.length==1)
            {

                String path = files[0];
                encodedCv = EncodeFile(path);
                UploadCV(encodedCv);
                Log.e("path", path);
            }

        });
        contactBtn = findViewById(R.id.contBtn);
        infoBtn = findViewById(R.id.infoBtn);
        fab = (FabSpeedDial)findViewById(R.id.fabsd);

        fab.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id =menuItem.getItemId();
                if (id== R.id.add)
                {
                    Intent intent = new Intent(AcademyProfileActivity.this,AddPlayerActivity.class);
                    startActivity(intent);
                }else if (id == R.id.upload)
                {
                    dialog.show();
                }
                return false;
            }
        });

        contactBtn.setOnClickListener(view -> {
            contactBtn.setTextColor(ContextCompat.getColor(AcademyProfileActivity.this,R.color.white));
            contactBtn.setBackgroundResource(R.drawable.academy_selected_btn);
            getSupportFragmentManager().beginTransaction().replace(R.id.academy_fragment_container, FragmentAcademyContacts.getInstance(academyModel,whoVisit)).commit();
            infoBtn.setTextColor(ContextCompat.getColor(AcademyProfileActivity.this,R.color.colorPrimary));
            infoBtn.setBackgroundResource(R.drawable.academy_unselected_btn);
        });
        infoBtn.setOnClickListener(view -> {
            infoBtn.setTextColor(ContextCompat.getColor(AcademyProfileActivity.this,R.color.white));
            infoBtn.setBackgroundResource(R.drawable.academy_selected_btn);

            getSupportFragmentManager().beginTransaction().replace(R.id.academy_fragment_container, FragmentAcademyInfo.getInstance()).commit();

            contactBtn.setTextColor(ContextCompat.getColor(AcademyProfileActivity.this,R.color.colorPrimary));
            contactBtn.setBackgroundResource(R.drawable.academy_unselected_btn);
        });

        viewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcademyProfileActivity.this,PlayersActivity.class);
                intent.putExtra("id",academyModel.getUser_id());
                intent.putExtra("who_visit",whoVisit);
                startActivity(intent);
            }
        });

        uploadImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent.createChooser(intent,"Choose Image"),IMG_REQ);
        });

        edit_name.setOnClickListener(view -> {

            View view1 = LayoutInflater.from(AcademyProfileActivity.this).inflate(R.layout.edit_layout,null);
            TextView title = view1.findViewById(R.id.tv_title);
            EditText edt_name = view1.findViewById(R.id.edt_update);
            edt_name.setHint("Name");
            edt_name.setText(academyModel.getUser_name());
            Button updateBtn = view1.findViewById(R.id.updateBtn);
            Button cancelBtn = view1.findViewById(R.id.cancelBtn);
            title.setText("Edit Name");
            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(edt_name.getText().toString()))
                    {
                        edt_name.setError("name require");
                    }
                    else if (edt_name.getText().toString().equals(academyModel.getUser_name()))
                    {
                        edt_name.setError(null);
                        Toast.makeText(AcademyProfileActivity.this, "No changes occur", Toast.LENGTH_SHORT).show();
                    }else
                        {
                            edt_name.setError(null);
                            alertDialog.dismiss();
                            UpdateName(edt_name.getText().toString());
                        }
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog = new AlertDialog.Builder(AcademyProfileActivity.this)
                    .setCancelable(true)
                    .setView(view1)
                    .create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        });

        map.setOnClickListener(view -> {
            Intent intent = new Intent(AcademyProfileActivity.this,MapsActivity.class);
            intent.putExtra("lat",academyModel.getUser_google_lat());
            intent.putExtra("lng",academyModel.getUser_google_long());
            startActivity(intent);
        });
    }


    private void UpdateName(String name) {
        ProgressDialog dialog = Tags.CreateProgressDialog(this, "Updating name....");
        dialog.show();
        Tags.getService().updateProfile(academyModel.getUser_id(),name,academyModel.getUser_pass(),academyModel.getUser_phone(),academyModel.getUser_email(),"")
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess()==1)
                            {
                                dialog.dismiss();
                                Gson gson = new Gson();
                                String data  = gson.toJson(response.body());
                                userSingleTone = UserSingleTone.getInstance();
                                userSingleTone.setUserData(response.body());
                                preference = new Preference(AcademyProfileActivity.this);
                                preference.UpdateSharedPref(data);
                                academyModel = response.body();
                                UpdateUi(response.body());
                                Toast.makeText(AcademyProfileActivity.this, "Name successfully updated", Toast.LENGTH_SHORT).show();
                            }else if (response.body().getSuccess()==0)
                            {
                                dialog.dismiss();

                                Toast.makeText(AcademyProfileActivity.this, "Failed try again later", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(AcademyProfileActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();
                        Log.e("Error",t.getMessage());
                    }
                });
    }

    private void UploadCV(String encodedCv) {
        ProgressDialog dialog = Tags.CreateProgressDialog(AcademyProfileActivity.this, "Uploading CV...");
        dialog.show();

        Tags.getService().UploadCV(academyModel.getUser_id(),encodedCv)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess()==1)
                            {
                                dialog.dismiss();
                                Toast.makeText(AcademyProfileActivity.this, "Cv Successfully uploaded", Toast.LENGTH_SHORT).show();
                            }else if (response.body().getSuccess()==0)
                            {
                                dialog.dismiss();

                                Toast.makeText(AcademyProfileActivity.this, "Failed try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        dialog.dismiss();

                        Toast.makeText(AcademyProfileActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String EncodeFile(String path) {
        String cv = "";
        File file = new File(path);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte [] bytes = new byte[(int)file.length()];
            inputStream.read(bytes);
            cv = Base64.encodeToString(bytes,Base64.DEFAULT);
            Log.e("file",cv);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cv;
    }
    private String EncodeImage(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQ && resultCode==RESULT_OK&&data!=null)
        {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                String encodedImage = EncodeImage(bitmap);
                UpdateImage(encodedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void UpdateImage(String encodedImage) {
        ProgressDialog dialog = Tags.CreateProgressDialog(this, "Updating image...");
        dialog.show();

        Tags.getService().updateProfile(academyModel.getUser_id(),academyModel.getUser_name(),academyModel.getUser_pass(),academyModel.getUser_phone(),academyModel.getUser_email(),encodedImage)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess()==1)
                            {
                                dialog.dismiss();
                                academyModel = response.body();
                                userSingleTone = UserSingleTone.getInstance();
                                userSingleTone.setUserData(response.body());
                                Gson gson= new Gson();
                                String data = gson.toJson(response.body());
                                preference = new Preference(AcademyProfileActivity.this);
                                preference.UpdateSharedPref(data);
                                UpdateUi(response.body());
                                Toast.makeText(AcademyProfileActivity.this, "Image successfully updated", Toast.LENGTH_SHORT).show();


                            }else if (response.body().getSuccess()==0)
                            {
                                dialog.dismiss();
                                Toast.makeText(AcademyProfileActivity.this, "Failed try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        dialog.dismiss();
                        Toast.makeText(AcademyProfileActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(dialog!=null)
                    {   //Show dialog if the read permission has been granted.
                        dialog.show();
                    }
                }
                else {
                    //Permission has not been granted. Notify the user.
                    Toast.makeText(AcademyProfileActivity.this,"Permission is Required for getting list of files",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
