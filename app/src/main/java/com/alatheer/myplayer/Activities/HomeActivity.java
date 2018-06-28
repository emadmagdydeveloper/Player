package com.alatheer.myplayer.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.myplayer.Adapters.AcademyAdapter;
import com.alatheer.myplayer.Models.AcademyModel;
import com.alatheer.myplayer.Models.UserModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private RecyclerView recView;
    private AcademyAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private List<AcademyModel> academyModelList,academyModelList2,academyModelList_search;
    private ProgressBar progressBar;
    private AutoCompleteTextView edt_search;
    private AcademyModel academyModel;
    private UserModel userModel;
    private String user_type="";
    private CircleImageView image;
    private TextView tv_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        getDataFromIntent();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent !=null)
        {
            if (intent.hasExtra("user_data"))
            {
                user_type = Tags.user;
                userModel = (UserModel) intent.getSerializableExtra("user_data");
                Log.e("usertype",userModel.getUser_typ());

                updateUserUi(userModel);
            }else if (intent.hasExtra("ac_data"))
            {
                user_type = Tags.academy;
                academyModel = (AcademyModel) intent.getSerializableExtra("ac_data");
                Log.e("usertype",academyModel.getUser_type());

                updateAcademy_UI(academyModel);
            }

            Log.e("type",user_type);
        }
    }

    private void updateAcademy_UI(AcademyModel academyModel) {
        try
        {
            Picasso.with(this).load(academyModel.getImage()).into(image);
            tv_name.setText(academyModel.getName());
        }catch (NullPointerException e){}
        catch (Exception e){}
    }

    private void updateUserUi(UserModel userModel) {
        try
        {
            Picasso.with(this).load(userModel.getImage()).into(image);
            tv_name.setText(userModel.getName());
            Log.e("name1",userModel.getName());
            Log.e("email1",userModel.getEmail());
            Log.e("phone1",userModel.getPhone());

        }catch (NullPointerException e){}
        catch (Exception e){}
    }

    private void initView() 
    {

        academyModelList = new ArrayList<>();
        academyModelList2 = new ArrayList<>();

        academyModelList_search = new ArrayList<>();

        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this);
        recView.setLayoutManager(manager);
        adapter = new AcademyAdapter(academyModelList,this);
        recView.setAdapter(adapter);
        progressBar = findViewById(R.id.progBar);
        edt_search = findViewById(R.id.edt_search);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        //////////////////////////////////////////////////////
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer =  findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        image = view.findViewById(R.id.image);
        tv_name =view.findViewById(R.id.tv_name);
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH || i==EditorInfo.IME_ACTION_DONE)
                {
                    if (!TextUtils.isEmpty(edt_search.getText().toString()))
                    {
                        String query = edt_search.getText().toString();
                        Search(query);
                    }
                }
                return false;
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==0)
                {
                    //getData();
                    academyModelList.clear();
                    academyModelList.addAll(academyModelList2);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        /////////////////////////////////////////////////////////////
        
        
    }

    private void Search(String query) {


        academyModelList_search.clear();

        for (AcademyModel academyModel :academyModelList2)
        {
            if (academyModel.getName().toLowerCase().equals(query.toLowerCase()))
            {

                academyModelList_search.add(academyModel);
            }
        }

        if (academyModelList_search.size()>0)
        {
            adapter.Clear(academyModelList_search);
        }else
            {
                Toast.makeText(this, "No Results", Toast.LENGTH_SHORT).show();
            }


    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        progressBar.setVisibility(View.GONE);
        academyModelList.clear();

        academyModelList2.add(new AcademyModel("1",R.drawable.ac1,"Academy1","01012345678","ac1@gmail.com","Egypt shebin","111111", Tags.academy));
        academyModelList2.add(new AcademyModel("2",R.drawable.ac2,"Academy2","01102211221","ac2@gmail.com","Egypt shebin","222222", Tags.academy));
        academyModelList2.add(new AcademyModel("3",R.drawable.ac1,"Academy3","01211212123","ac3@gmail.com","Egypt shebin","333333", Tags.academy));
        academyModelList2.add(new AcademyModel("4",R.drawable.ac2,"Academy4","01000022525","ac4@gmail.com","Egypt shebin","444444", Tags.academy));
        academyModelList2.add(new AcademyModel("5",R.drawable.ac1,"Academy5","01033333222","ac5@gmail.com","Egypt shebin","555555", Tags.academy));
        academyModelList2.add(new AcademyModel("6",R.drawable.ac2,"Academy6","01011112222","ac6@gmail.com","Egypt shebin","666666", Tags.academy));
        academyModelList2.add(new AcademyModel("7",R.drawable.ac1,"Academy7","01122122222","ac7@gmail.com","Egypt shebin","777777", Tags.academy));

        if (user_type.equals(Tags.academy))
        {
            for (AcademyModel academyModel:academyModelList2)
            {
                if (academyModel.getId().equals(this.academyModel.getId()))
                {
                    continue;
                }else
                    {
                        academyModelList.add(academyModel);
                    }
            }
        }else
            {
                academyModelList.addAll(academyModelList2);
            }


        //academyModelList.addAll(academyModelList2);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
         drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            if (user_type.equals(Tags.user))
            {
                Log.e("name2",userModel.getName());
                Log.e("email2",userModel.getEmail());
                Log.e("phone2",userModel.getPhone());
                Intent intent = new Intent(this,UserProfileActivity.class);
                intent.putExtra("data",userModel);
                intent.putExtra("who_visit",Tags.me);
                startActivity(intent);
            }else if (user_type.equals(Tags.academy))
            {
                Intent intent = new Intent(this,AcademyProfileActivity.class);
                intent.putExtra("data",academyModel);
                intent.putExtra("who_visit",Tags.me);

                startActivity(intent);
            }

        } else if (id == R.id.about_app) {

        } else if (id == R.id.share) {

        } else if (id == R.id.contacts) {

        } else if (id == R.id.logout) {

        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setRecieveDataFromAdapter(AcademyModel academyModel)
    {
        Intent intent = new Intent(this,AcademyProfileActivity.class);
        intent.putExtra("data",academyModel);
        intent.putExtra("who_visit",Tags.visitor);

        startActivity(intent);
    }
}
