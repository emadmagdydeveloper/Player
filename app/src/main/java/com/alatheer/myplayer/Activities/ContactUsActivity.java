package com.alatheer.myplayer.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.myplayer.Models.ContactModel;
import com.alatheer.myplayer.R;
import com.alatheer.myplayer.Service.Tags;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity {

    private EditText edt_name,edt_msg;
    private ImageView back,tw,fb,in;
    private Button submitBtn;
    private TextView tv_phone,tv_email;
    private String tw_url="",fb_url="",in_url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        initView();
        getContacts();
    }
    private void initView()
    {
        edt_name = findViewById(R.id.edt_name);
        edt_msg = findViewById(R.id.edt_msg);
        back = findViewById(R.id.back);
        tw = findViewById(R.id.tw);
        fb = findViewById(R.id.fb);
        in = findViewById(R.id.in);
        submitBtn = findViewById(R.id.submitBtn);
        tv_phone = findViewById(R.id.tv_phone);
        tv_email = findViewById(R.id.tv_email);

        back.setOnClickListener(view -> finish());

        submitBtn.setOnClickListener(view -> {
            String name = edt_name.getText().toString();
            String msg = edt_msg.getText().toString();
            String email = tv_email.getText().toString();

            if (TextUtils.isEmpty(email))
            {
                Toast.makeText(ContactUsActivity.this, "Sorry! There is No email to send message", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(msg))
            {
                edt_msg.setError("Message require");
            }else
            {
                edt_msg.setError(null);

                sendMsg(name,msg,email);

            }
        });
        tw.setOnClickListener(view -> {
            Intent intent = new Intent(ContactUsActivity.this,WebViewActivity.class);
            intent.putExtra("url",tw_url);
            startActivity(intent);
        });
        fb.setOnClickListener(view -> {
            Intent intent = new Intent(ContactUsActivity.this,WebViewActivity.class);
            intent.putExtra("url",fb_url);
            startActivity(intent);
        });
        in.setOnClickListener(view -> {
            Intent intent = new Intent(ContactUsActivity.this,WebViewActivity.class);
            intent.putExtra("url",in_url);
            startActivity(intent);
        });
    }
    private void getContacts()
    {
        Tags.getService().getContacts().enqueue(new Callback<ContactModel>() {
            @Override
            public void onResponse(Call<ContactModel> call, Response<ContactModel> response) {
                if (response.isSuccessful())
                {
                    tw_url = response.body().getTwitter();
                    fb_url = response.body().getFacebook();
                    in_url = response.body().getInstagram();
                    UpdateUI(response.body());
                }
            }

            @Override
            public void onFailure(Call<ContactModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                Toast.makeText(ContactUsActivity.this, "Something went haywire", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void UpdateUI(ContactModel contactModel)
    {
        tv_phone.setText(contactModel.getPhone());
        tv_email.setText(contactModel.getEmail());

    }
    private void sendMsg(String name, String msg, String email)
    {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT,name);
        intent.putExtra(Intent.EXTRA_TEXT,msg);
        PackageManager pm =getPackageManager();
        List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
        ResolveInfo best = null;
        for(ResolveInfo info : matches)
        {
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
            {
                best = info;

            }
        }

        if (best != null)
        {
            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

        }

        startActivity(intent);

    }
}
