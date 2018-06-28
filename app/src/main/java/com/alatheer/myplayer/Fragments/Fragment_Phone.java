package com.alatheer.myplayer.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alatheer.myplayer.Models.RegisterModel;
import com.alatheer.myplayer.R;
import com.lamudi.phonefield.PhoneInputLayout;

/**
 * Created by elashry on 26/06/2018.
 */

public class Fragment_Phone extends Fragment {
    private View view;
    private PhoneInputLayout edt_phone;
    private String phone;
    private RegisterModel registerModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_phone,container,false);
        registerModel = RegisterModel.getInstance();
        initView(view);
        return view;
    }

    private void initView(View view) {

        edt_phone = view.findViewById(R.id.edt_phone);
        edt_phone.getTextInputLayout().getEditText().setHint("phone number");
        edt_phone.getTextInputLayout().getEditText().setTextSize(TypedValue.COMPLEX_UNIT_SP,13f);
        edt_phone.setDefaultCountry("eg");
        phone = edt_phone.getPhoneNumber();
        registerModel.setPhone(phone);



    }

    public static Fragment_Phone getInstance()
    {
        Fragment_Phone fragment_phone= new Fragment_Phone();
        return fragment_phone;
    }

    public void getPhone()
    {
        phone = edt_phone.getPhoneNumber();
        if (!TextUtils.isEmpty(phone)&&edt_phone.isValid())
        {
            registerModel.setPhone(phone);

        }else
            {
                registerModel.setPhone("");

            }
    }

    public void setPhoneError()
    {
        edt_phone.getTextInputLayout().getEditText().setError("Invalid phone number");
        Snackbar.make(view.getRootView(),"Invalid phone number",Snackbar.LENGTH_SHORT).show();

    }
    public void removeError()
    {
        edt_phone.getTextInputLayout().getEditText().setError(null);

    }
}
