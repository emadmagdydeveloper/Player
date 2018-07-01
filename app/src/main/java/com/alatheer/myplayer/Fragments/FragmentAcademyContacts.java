package com.alatheer.myplayer.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.myplayer.Activities.WebViewActivity;
import com.alatheer.myplayer.Models.UserModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Preference;
import com.alatheer.myplayer.Service.Tags;
import com.alatheer.myplayer.Service.UserSingleTone;
import com.google.gson.Gson;
import com.lamudi.phonefield.PhoneInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elashry on 27/06/2018.
 */

public class FragmentAcademyContacts extends Fragment {
    public static String Tag = "TAG";
    public static String visit_tage="who_visit";
    private String who_visit="";
    private View view;
    private static FragmentAcademyContacts instance=null;
    private UserModel academyModel;
    private TextView tv_email,tv_phone,tv_address;
    private ImageView tw,fb,in;
    private LinearLayout ll_edit_email,ll_edit_phone,ll_edit_address;
    private AlertDialog alertDialog;
    private UserSingleTone userSingleTone;
    private Preference preference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_contact,container,false);
        initView(view);
        userSingleTone = UserSingleTone.getInstance();
        preference = new Preference(getActivity());
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            academyModel = (UserModel) bundle.getSerializable(Tag);
            who_visit = bundle.getString(visit_tage);
            updateUi(academyModel);

        }
        return view;
    }

    private void updateUi(UserModel academyModel) {
        tv_email.setText(academyModel.getUser_email());
        tv_phone.setText(academyModel.getUser_phone());
        tv_address.setText(academyModel.getUser_address());
        if (who_visit.equals(Tags.me))
        {
            ll_edit_phone.setVisibility(View.VISIBLE);
            ll_edit_email.setVisibility(View.VISIBLE);
            ll_edit_address.setVisibility(View.VISIBLE);

        }else if (who_visit.equals(Tags.visitor))
        {
            ll_edit_phone.setVisibility(View.INVISIBLE);
            ll_edit_email.setVisibility(View.INVISIBLE);
            ll_edit_address.setVisibility(View.INVISIBLE);
        }
    }

    private void initView(View view) {
        tv_email = view.findViewById(R.id.tv_email);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_address = view.findViewById(R.id.tv_address);
        tw = view.findViewById(R.id.tw);
        fb = view.findViewById(R.id.fb);
        in = view.findViewById(R.id.in);
        ll_edit_email = view.findViewById(R.id.ll_edit_email);
        ll_edit_phone = view.findViewById(R.id.ll_edit_phone);
        ll_edit_address = view.findViewById(R.id.ll_edit_address);

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

        ll_edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.edit_layout,null);
                TextView title = view1.findViewById(R.id.tv_title);
                EditText edt_email = view1.findViewById(R.id.edt_update);
                edt_email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                edt_email.setHint("Email");
                edt_email.setText(academyModel.getUser_name());
                Button updateBtn = view1.findViewById(R.id.updateBtn);
                Button cancelBtn = view1.findViewById(R.id.cancelBtn);
                title.setText("Edit Email");
                String email = edt_email.getText().toString();
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(email))
                        {
                            edt_email.setError("email require");
                        }
                        else if (email.equals(academyModel.getUser_email()))
                        {
                            edt_email.setError(null);
                            Toast.makeText(getActivity(), "No changes occur", Toast.LENGTH_SHORT).show();
                        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                        {
                            edt_email.setError("invalid email");

                        }
                        else
                        {
                            edt_email.setError(null);
                            alertDialog.dismiss();
                            UpdateEmail(email);
                        }
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog = new AlertDialog.Builder(getActivity())
                        .setCancelable(true)
                        .create();
                alertDialog.setCanceledOnTouchOutside(false);

            }
        });

        ll_edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.edit_phone_layout,null);
                TextView title = view1.findViewById(R.id.tv_title);
                PhoneInputLayout edt_phone = view1.findViewById(R.id.edt_phone);
                edt_phone.getTextInputLayout().getEditText().setHint("Phone number");
                edt_phone.getTextInputLayout().getEditText().setTextSize(TypedValue.COMPLEX_UNIT_SP,12f);
                edt_phone.setDefaultCountry("eg");
                Button updateBtn = view1.findViewById(R.id.updateBtn);
                Button cancelBtn = view1.findViewById(R.id.cancelBtn);
                title.setText("Edit Phone");
                String phone = edt_phone.getPhoneNumber();
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(phone))
                        {
                            edt_phone.getTextInputLayout().getEditText().setError("phone number require");
                        }
                        else if (!edt_phone.isValid())
                        {
                            edt_phone.getTextInputLayout().getEditText().setError(null);

                            edt_phone.getTextInputLayout().getEditText().setError(" invalid phone number");
                        }else if (phone.equals(academyModel.getUser_phone()))
                        {
                            edt_phone.getTextInputLayout().getEditText().setError(null);

                            Toast.makeText(getActivity(), "No changes occur", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            edt_phone.getTextInputLayout().getEditText().setError(null);
                            alertDialog.dismiss();
                            UpdatePhone(phone);
                        }
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog = new AlertDialog.Builder(getActivity())
                        .setCancelable(true)
                        .create();
                alertDialog.setCanceledOnTouchOutside(false);

            }
        });
/*
        ll_edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.edit_layout,null);
                TextView title = view1.findViewById(R.id.tv_title);
                EditText edt_address = view1.findViewById(R.id.edt_update);
                edt_address.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                edt_address.setHint("Address");
                edt_address.setText(academyModel.getUser_name());
                Button updateBtn = view1.findViewById(R.id.updateBtn);
                Button cancelBtn = view1.findViewById(R.id.cancelBtn);
                title.setText("Edit Address");
                String address = edt_address.getText().toString();
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(address))
                        {
                            edt_address.setError("address require");
                        }
                        else if (address.equals(academyModel.getUser_address()))
                        {
                            edt_address.setError(null);
                            Toast.makeText(getActivity(), "No changes occur", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            edt_address.setError(null);
                            alertDialog.dismiss();
                            //UpdateAddress(address);
                        }
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog = new AlertDialog.Builder(getActivity())
                        .setCancelable(true)
                        .create();
                alertDialog.setCanceledOnTouchOutside(false);

            }
        });
*/

    }

    private void UpdatePhone(String phone) {
        ProgressDialog dialog = Tags.CreateProgressDialog(getActivity(), "Updating phone....");
        dialog.show();
        Tags.getService().updateProfile(academyModel.getUser_id(),academyModel.getUser_name(),academyModel.getUser_pass(),phone,academyModel.getUser_email(),"")
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
                                userSingleTone.setUserData(response.body());
                                preference.UpdateSharedPref(data);
                                academyModel = response.body();
                                updateUi(response.body());
                                Toast.makeText(getActivity(), "phone successfully updated", Toast.LENGTH_SHORT).show();
                            }else if (response.body().getSuccess()==0)
                            {
                                dialog.dismiss();

                                Toast.makeText(getActivity(), "Failed try again later", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Something went haywire", Toast.LENGTH_SHORT).show();
                        Log.e("Error",t.getMessage());
                    }
                });
    }

/*
    private void UpdateAddress(String address) {
        ProgressDialog dialog = Tags.CreateProgressDialog(getActivity(), "Updating address....");
        dialog.show();
        Tags.getService().updateProfile(academyModel.getUser_id(),academyModel.getUser_name(),academyModel.getUser_pass(),academyModel.getUser_phone(),email,"")
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
                                userSingleTone.setUserData(response.body());
                                preference.UpdateSharedPref(data);
                                academyModel = response.body();
                                updateUi(response.body());
                                Toast.makeText(getActivity(), "Email successfully updated", Toast.LENGTH_SHORT).show();
                            }else if (response.body().getSuccess()==0)
                            {
                                dialog.dismiss();

                                Toast.makeText(getActivity(), "Failed try again later", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Something went haywire", Toast.LENGTH_SHORT).show();
                        Log.e("Error",t.getMessage());
                    }
                });
    }
*/


    private void UpdateEmail(String email) {

        ProgressDialog dialog = Tags.CreateProgressDialog(getActivity(), "Updating email....");
        dialog.show();
        Tags.getService().updateProfile(academyModel.getUser_id(),academyModel.getUser_name(),academyModel.getUser_pass(),academyModel.getUser_phone(),email,"")
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
                                userSingleTone.setUserData(response.body());
                                preference.UpdateSharedPref(data);
                                academyModel = response.body();
                                updateUi(response.body());
                                Toast.makeText(getActivity(), "Email successfully updated", Toast.LENGTH_SHORT).show();
                            }else if (response.body().getSuccess()==0)
                            {
                                dialog.dismiss();

                                Toast.makeText(getActivity(), "Failed try again later", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Something went haywire", Toast.LENGTH_SHORT).show();
                        Log.e("Error",t.getMessage());
                    }
                });
    }

    public static synchronized FragmentAcademyContacts getInstance(UserModel academyModel,String who_visit)
    {
        if (instance==null)
        {
            instance = new FragmentAcademyContacts();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Tag,academyModel);
            bundle.putString(visit_tage,who_visit);
            instance.setArguments(bundle);

        }
        return instance;
    }
}
