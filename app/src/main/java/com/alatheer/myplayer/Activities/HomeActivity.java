package com.alatheer.myplayer.Activities;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.myplayer.Adapters.AcademyAdapter;
import com.alatheer.myplayer.Models.AcademyModel;
import com.alatheer.myplayer.Models.ResponseModel;
import com.alatheer.myplayer.Models.UserModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Preference;
import com.alatheer.myplayer.Service.Tags;
import com.alatheer.myplayer.Service.UserSingleTone;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,UserSingleTone.Listener{

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private RecyclerView recView;
    private AcademyAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private List<UserModel> academyModelList,main_academyModelList;
    private ProgressBar progressBar,progSearch;
    private AutoCompleteTextView edt_search;
    private UserModel userModel;
    private String user_type="";
    private CircleImageView image;
    private TextView tv_name,no_data;
    private Preference preference;
    private UserSingleTone singleTone;
    private String user_id="";
    private LinearLayout no_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preference = new Preference(this);
        initView();
        getDataFromIntent();

        if (user_type.equals(Tags.user_type_user)||user_type.equals(Tags.user_type_academy))
        {
            getData(userModel.getUser_id());

        }else if (user_type.equals(Tags.user_type_skip))
        {
            getData("0");

        }

    }
    private void initView()
    {

        main_academyModelList = new ArrayList<>();
        academyModelList = new ArrayList<>();
        no_result = findViewById(R.id.no_result);
        no_data = findViewById(R.id.no_data);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this);
        recView.setLayoutManager(manager);
        adapter = new AcademyAdapter(academyModelList,this);
        recView.setAdapter(adapter);
        progressBar = findViewById(R.id.progBar);
        progSearch = findViewById(R.id.progSearch);
        edt_search = findViewById(R.id.edt_search);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        progSearch.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

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
                        HideKeyBoard();

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
                    academyModelList.clear();
                    academyModelList.addAll(main_academyModelList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        /////////////////////////////////////////////////////////////


    }
    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent !=null)
        {
            if (intent.getStringExtra("user_type").equals(Tags.user_type_user)||intent.getStringExtra("user_type").equals(Tags.user_type_academy))
            {

                singleTone = UserSingleTone.getInstance();
                singleTone.getUserData(this);
            }
            else if (intent.getStringExtra("user_type").equals(Tags.user_type_skip))
            {
                user_type=Tags.user_type_skip;
                updateSkipUi();
            }

            Log.e("type",user_type);
        }
    }
    private void getData(String id)
    {
        progressBar.setVisibility(View.GONE);
        Tags.getService().getAcademies(id)
                .enqueue(new Callback<List<UserModel>>() {
                    @Override
                    public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                        if (response.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);

                            if (response.body().size()>0)
                            {
                                main_academyModelList.clear();
                                academyModelList.clear();
                                main_academyModelList.addAll(response.body());
                                no_data.setVisibility(View.GONE);
                                academyModelList.addAll(response.body());
                                adapter.notifyDataSetChanged();
                                no_result.setVisibility(View.GONE);
                                progSearch.setVisibility(View.GONE);

                            }else
                            {
                                no_data.setVisibility(View.VISIBLE);
                                no_result.setVisibility(View.GONE);
                                progSearch.setVisibility(View.GONE);


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserModel>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(HomeActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();
                        Log.e("Error",t.getMessage());
                    }
                });

    }
    private void updateUserUi(UserModel userModel)
    {
        try
        {
            user_id = userModel.getUser_id();
            user_type = userModel.getUser_type();
            Picasso.with(this).load(userModel.getUser_photo()).into(image);
            tv_name.setText(userModel.getUser_name());


        }catch (NullPointerException e){}
        catch (Exception e){}
    }
    private void updateSkipUi()
    {
        user_id = "0";
        Picasso.with(this).load(R.drawable.user_profile).into(image);
        tv_name.setText("Visitor");
    }
    private void Search(String query)
    {
        progSearch.setVisibility(View.VISIBLE);

        Tags.getService().search(user_id,query).enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful())
                {
                    if (response.body().size()>0)
                    {
                        academyModelList.clear();
                        academyModelList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        progSearch.setVisibility(View.GONE);
                        no_result.setVisibility(View.GONE);

                    }else
                        {
                            progSearch.setVisibility(View.GONE);
                            no_result.setVisibility(View.VISIBLE);

                        }
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Log.e("Error",t.getMessage());
            }
        });

    }
    private void HideKeyBoard()
    {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromInputMethod(edt_search.getWindowToken(),0);
    }
    @Override
    public void onBackPressed()
    {
         drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {

            if (user_type.equals(Tags.user_type_user))
            {
                Intent intent = new Intent(this,UserProfileActivity.class);
                intent.putExtra("data",userModel);
                intent.putExtra("who_visit",Tags.me);
                startActivity(intent);
            }else if (user_type.equals(Tags.user_type_academy))
            {
                Intent intent = new Intent(this,AcademyProfileActivity.class);
                intent.putExtra("data",userModel);
                intent.putExtra("who_visit",Tags.me);
                startActivity(intent);
            }else if (user_type.equals(Tags.user_type_skip))
            {
                Tags.CreateAlertDialog(this).show();
            }

            

        } else if (id == R.id.about_app) {

            Intent intent = new Intent(this,AboutAppActivity.class);
            startActivity(intent);
        } else if (id == R.id.share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,"");
            intent.putExtra(Intent.EXTRA_TITLE,"");
            intent.putExtra(Intent.EXTRA_STREAM,R.drawable.logo);
            startActivity(intent);

        } else if (id == R.id.contacts) {
            Intent intent = new Intent(this,ContactUsActivity.class);
            startActivity(intent);

        } else if (id == R.id.logout) {
            Logout();

        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void Logout()
    {
        ProgressDialog dialog = Tags.CreateProgressDialog(this, "Logout...");
        dialog.show();
        Tags.getService().logout().enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        dialog.dismiss();

                        preference.CleareSharedPref();
                        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else
                        {
                            dialog.dismiss();

                            Toast.makeText(HomeActivity.this, "Failed try again later", Toast.LENGTH_SHORT).show();

                        }

                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                dialog.dismiss();

                Log.e("Error",t.getMessage());
                Toast.makeText(HomeActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void setRecieveDataFromAdapter(UserModel academyModel)
    {
        Intent intent = new Intent(this,AcademyProfileActivity.class);
        intent.putExtra("data",academyModel);
        intent.putExtra("who_visit",Tags.visitor);
        startActivity(intent);
    }

    @Override
    public void onSuccess(UserModel userModel) {
        this.userModel  = userModel;
        updateUserUi(userModel);

    }
}
