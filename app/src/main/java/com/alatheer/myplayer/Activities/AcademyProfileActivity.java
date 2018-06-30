package com.alatheer.myplayer.Activities;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.myplayer.Fragments.FragmentAcademyContacts;
import com.alatheer.myplayer.Fragments.FragmentAcademyInfo;
import com.alatheer.myplayer.Models.UserModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.santalu.diagonalimageview.DiagonalImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

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
            getSupportFragmentManager().beginTransaction().add(R.id.academy_fragment_container, FragmentAcademyContacts.getInstance(academyModel)).commit();


        }
    }

    private void UpdateUi(UserModel academyModel) {
        try {
            Picasso.with(this).load(Uri.parse(Tags.imageUrl+academyModel.getUser_photo())).into(image);
            Picasso.with(this).load(Uri.parse(Tags.imageUrl+academyModel.getUser_photo())).into(image_profile);
            tv_name.setText(academyModel.getUser_name());
            if (whoVisit.equals(Tags.me))
            {
                fab.setVisibility(View.VISIBLE);
            }else if (whoVisit.equals(Tags.visitor))
            {
                fab.setVisibility(View.GONE);

            }

            watch_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AcademyProfileActivity.this,PlayerVideosActivity.class);
                    intent.putExtra("id",academyModel.getUser_id());
                    startActivity(intent);
                }
            });
        }catch (NullPointerException e){}
        catch (Exception e){}
    }

    private void initView() {

        image_profile = findViewById(R.id.image_profile);
        image = findViewById(R.id.image);
        tv_name = findViewById(R.id.tv_name);
        viewPlayer = findViewById(R.id.viewPlayer);
        watch_video = findViewById(R.id.watch_video);

        properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = new String[]{"pdf","xls","xlsx","doc"};

        dialog = new FilePickerDialog(AcademyProfileActivity.this,properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(files -> {
            if (files.length>1)
            {
                Toast.makeText(AcademyProfileActivity.this, "Only one CV allowed", Toast.LENGTH_SHORT).show();

            }else if (files.length==1)
            {
                String path = files[0];
                EncodeFile(path);
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
            getSupportFragmentManager().beginTransaction().replace(R.id.academy_fragment_container, FragmentAcademyContacts.getInstance(academyModel)).commit();
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
    }

    private void EncodeFile(String path) {
        File file = new File(path);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte [] bytes = new byte[(int)file.length()];
            inputStream.read(bytes);
            String encode_file = Base64.encodeToString(bytes,Base64.DEFAULT);
            Log.e("file",encode_file);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
