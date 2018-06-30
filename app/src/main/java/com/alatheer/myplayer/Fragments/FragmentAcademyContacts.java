package com.alatheer.myplayer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.myplayer.Activities.ContactUsActivity;
import com.alatheer.myplayer.Activities.WebViewActivity;
import com.alatheer.myplayer.Models.AcademyModel;
import com.alatheer.myplayer.Models.UserModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;

/**
 * Created by elashry on 27/06/2018.
 */

public class FragmentAcademyContacts extends Fragment {
    public static String Tag = "TAG";
    private View view;
    private static FragmentAcademyContacts instance=null;
    private UserModel academyModel;
    private TextView tv_email,tv_phone,tv_address;
    private ImageView tw,fb,in;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_contact,container,false);
        initView(view);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            academyModel = (UserModel) bundle.getSerializable(Tag);
            updateUi(academyModel);

        }
        return view;
    }

    private void updateUi(UserModel academyModel) {
        tv_email.setText(academyModel.getUser_email());
        tv_phone.setText(academyModel.getUser_phone());
        tv_address.setText(academyModel.getUser_address());
    }

    private void initView(View view) {
        tv_email = view.findViewById(R.id.tv_email);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_address = view.findViewById(R.id.tv_address);
        tw = view.findViewById(R.id.tw);
        fb = view.findViewById(R.id.fb);
        in = view.findViewById(R.id.in);

        tw.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(),WebViewActivity.class);
            intent.putExtra("url",academyModel.getUser_twitter());
            startActivity(intent);
        });
        fb.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(),WebViewActivity.class);
            intent.putExtra("url",academyModel.getUser_facebook());
            startActivity(intent);
        });
        in.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(),WebViewActivity.class);
            intent.putExtra("url",academyModel.getUser_instagram());
            startActivity(intent);
        });

    }

    public static synchronized FragmentAcademyContacts getInstance(UserModel academyModel)
    {
        if (instance==null)
        {
            instance = new FragmentAcademyContacts();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Tag,academyModel);
            instance.setArguments(bundle);

        }
        return instance;
    }
}
