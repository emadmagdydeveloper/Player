package com.alatheer.myplayer.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alatheer.myplayer.Models.AcademyModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;

/**
 * Created by elashry on 27/06/2018.
 */

public class FragmentAcademyContacts extends Fragment {
    public static String Tag = "TAG";
    private View view;
    private static FragmentAcademyContacts instance=null;
    private AcademyModel academyModel;
    private TextView tv_email,tv_phone,tv_address;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_contact,container,false);
        initView(view);
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            academyModel = (AcademyModel) bundle.getSerializable(Tag);
            updateUi(academyModel);

        }
        return view;
    }

    private void updateUi(AcademyModel academyModel) {
        tv_email.setText(academyModel.getEmail());
        tv_phone.setText(academyModel.getPhone());
        tv_address.setText(academyModel.getAddress());
    }

    private void initView(View view) {
        tv_email = view.findViewById(R.id.tv_email);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_address = view.findViewById(R.id.tv_address);
    }

    public static synchronized FragmentAcademyContacts getInstance(AcademyModel academyModel)
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
