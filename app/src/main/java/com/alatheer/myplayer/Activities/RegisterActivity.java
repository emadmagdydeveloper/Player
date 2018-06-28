package com.alatheer.myplayer.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alatheer.myplayer.Adapters.PagerAdapter;
import com.alatheer.myplayer.Fragments.Fragment_Email;
import com.alatheer.myplayer.Fragments.Fragment_Image;
import com.alatheer.myplayer.Fragments.Fragment_Name;
import com.alatheer.myplayer.Fragments.Fragment_Phone;
import com.alatheer.myplayer.Models.RegisterModel;
import com.alatheer.myplayer.R;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private ViewPager pager;
    private TabLayout tab;
    private Button nextBtn,backBtn;
    private TextView title;
    private List<Fragment> fragmentList;
    private RegisterModel registerModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerModel = RegisterModel.getInstance();
        initView();
    }

    private void initView() {
        fragmentList = new ArrayList<>();
        pager = findViewById(R.id.pager);
        tab = findViewById(R.id.tab);
        nextBtn = findViewById(R.id.nextBtn);
        backBtn = findViewById(R.id.backBtn);
        title = findViewById(R.id.title);
        tab.setupWithViewPager(pager);
        ////////////////////////////////////////////////////////////
        fragmentList.add(Fragment_Image.getInstance());
        fragmentList.add(Fragment_Name.getInstance());
        fragmentList.add(Fragment_Phone.getInstance());
        fragmentList.add(Fragment_Email.getInstance());

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(fragmentList);
        pager.setAdapter(adapter);
        pager.beginFakeDrag();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==fragmentList.size()-1)
                {


                    nextBtn.setTextColor(ContextCompat.getColor(RegisterActivity.this,R.color.colorPrimary));
                    nextBtn.setText("get started");
                    nextBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                   /* String m_image = registerModel.getImage();
                    String m_name = registerModel.getName();
                    String m_phone = registerModel.getPhone();
                    String m_email = registerModel.getEmail();
                    String m_pass = registerModel.getPassword();

                    if (TextUtils.isEmpty(m_image))
                    {
                        Fragment_Image fragment_image = (Fragment_Image) fragmentList.get(0);
                        fragment_image.setImageError();
                    }else if (TextUtils.isEmpty(m_name))
                    {
                        Fragment_Name fragment_name = (Fragment_Name) fragmentList.get(1);
                        fragment_name.setNameError();
                    }else if (TextUtils.isEmpty(m_phone))
                    {
                        Fragment_Phone fragment_phone = (Fragment_Phone) fragmentList.get(2);
                        fragment_phone.setPhoneError();
                    }else if (TextUtils.isEmpty(m_email))
                    {
                        Fragment_Email fragment_email2 = (Fragment_Email) fragmentList.get(3);
                        fragment_email2.setEmailError();
                    }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                    {
                        Fragment_Email fragment_email2 = (Fragment_Email) fragmentList.get(3);
                        fragment_email2.setInvalidEmailError();
                    }else if (TextUtils.isEmpty(m_pass))
                    {
                        Fragment_Email fragment_email2 = (Fragment_Email) fragmentList.get(3);
                        fragment_email2.setPasswordError();
                    }else if (m_pass.length()<5)
                    {
                        Fragment_Email fragment_email2 = (Fragment_Email) fragmentList.get(3);
                        fragment_email2.setShortPasswordError();
                    }else
                    {
                        Log.e("image",registerModel.getImage());
                        Log.e("name",registerModel.getName());
                        Log.e("phone",registerModel.getPhone());
                        Log.e("email",registerModel.getEmail());
                        Log.e("password",registerModel.getPassword());

                    }
*/
                }else
                    {
                        nextBtn.setTextColor(ContextCompat.getColor(RegisterActivity.this,R.color.gray1));
                        nextBtn.setText("next");
                        nextBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.next_arrow,0);

                        if (position==0)
                        {
                            backBtn.setVisibility(View.INVISIBLE);

                        }else
                            {
                                backBtn.setVisibility(View.VISIBLE);

                            }

                    }


                    if (position==0)
                    {
                        title.setText("Image profile");
                    }else if (position==1)
                    {
                        title.setText("Name");

                    }
                    else if (position==2)
                    {
                        title.setText("Phone");

                    }
                    else if (position==3)
                    {
                        title.setText("Authentication");

                    }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pager.getCurrentItem()<fragmentList.size())
                {


                    if (pager.getCurrentItem()==0)
                    {
                        String image = registerModel.getImage();
                        Fragment_Image fragment_image = (Fragment_Image) fragmentList.get(0);

                        if (TextUtils.isEmpty(image))
                        {
                            fragment_image.setImageError();
                        }else
                            {
                                pager.setCurrentItem(pager.getCurrentItem()+1);

                            }
                    }else if (pager.getCurrentItem()==1)
                    {
                        Fragment_Name fragment_name = (Fragment_Name) fragmentList.get(1);
                        fragment_name.getName();
                        String name =registerModel.getName();
                        if (TextUtils.isEmpty(name))
                        {
                            fragment_name.setNameError();
                        }else
                            {
                                pager.setCurrentItem(pager.getCurrentItem()+1);
                                fragment_name.removeNameError();
                            }
                    }
                    else if (pager.getCurrentItem()==2)
                    {
                        Fragment_Phone fragment_phone = (Fragment_Phone) fragmentList.get(2);
                        fragment_phone.getPhone();
                        String phone = registerModel.getPhone();
                        if (TextUtils.isEmpty(phone))
                        {
                            fragment_phone.setPhoneError();

                        }else
                            {
                                pager.setCurrentItem(pager.getCurrentItem()+1);
                                fragment_phone.removeError();
                            }
                    }
                    else if (pager.getCurrentItem()==3)
                    {
                        Fragment_Email fragment_email = (Fragment_Email) fragmentList.get(3);
                        fragment_email.getEmail_Password();
                        String email = registerModel.getEmail();
                        String password = registerModel.getPassword();

                        Log.e("sdsada","sadasda");
                        if (TextUtils.isEmpty(email))
                        {
                            fragment_email.setEmailError();
                        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                        {
                            fragment_email.setInvalidEmailError();
                        }else if (TextUtils.isEmpty(password)){
                            fragment_email.removeEmailError();
                            fragment_email.setPasswordError();
                        }else if (password.length()<5)
                        {
                            fragment_email.removeEmailError();
                            fragment_email.setShortPasswordError();
                        }else
                            {
                                fragment_email.removeEmailError();
                                fragment_email.removePasswordError();
                                pager.setCurrentItem(pager.getCurrentItem()+1);

                                String m_image = registerModel.getImage();
                                String m_name = registerModel.getName();
                                String m_phone = registerModel.getPhone();
                                String m_email = registerModel.getEmail();
                                String m_pass = registerModel.getPassword();

                                if (TextUtils.isEmpty(m_image))
                                {
                                    Fragment_Image fragment_image = (Fragment_Image) fragmentList.get(0);
                                    fragment_image.setImageError();
                                }else if (TextUtils.isEmpty(m_name))
                                {
                                    Fragment_Name fragment_name = (Fragment_Name) fragmentList.get(1);
                                    fragment_name.setNameError();
                                }else if (TextUtils.isEmpty(m_phone))
                                {
                                    Fragment_Phone fragment_phone = (Fragment_Phone) fragmentList.get(2);
                                    fragment_phone.setPhoneError();
                                }else if (TextUtils.isEmpty(m_email))
                                {
                                    Fragment_Email fragment_email2 = (Fragment_Email) fragmentList.get(3);
                                    fragment_email2.setEmailError();
                                }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                                {
                                    Fragment_Email fragment_email2 = (Fragment_Email) fragmentList.get(3);
                                    fragment_email2.setInvalidEmailError();
                                }else if (TextUtils.isEmpty(m_pass))
                                {
                                    Fragment_Email fragment_email2 = (Fragment_Email) fragmentList.get(3);
                                    fragment_email2.setPasswordError();
                                }else if (m_pass.length()<5)
                                {
                                    Fragment_Email fragment_email2 = (Fragment_Email) fragmentList.get(3);
                                    fragment_email2.setShortPasswordError();
                                }else
                                    {
                                        SignUp(registerModel);
                                        Log.e("image",registerModel.getImage());
                                        Log.e("name",registerModel.getName());
                                        Log.e("phone",registerModel.getPhone());
                                        Log.e("email",registerModel.getEmail());
                                        Log.e("password",registerModel.getPassword());

                                    }

                            }
                    }
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pager.getCurrentItem()>0)
                {
                    pager.setCurrentItem(pager.getCurrentItem()-1);
                }
            }
        });
    }

    private void SignUp(RegisterModel registerModel) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment :fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
