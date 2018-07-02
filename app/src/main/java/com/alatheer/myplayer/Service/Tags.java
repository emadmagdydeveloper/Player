package com.alatheer.myplayer.Service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import com.alatheer.myplayer.R;

import retrofit2.Retrofit;

/**
 * Created by elashry on 26/06/2018.
 */

public class Tags {
    public static final String baseUrl = "http://engaz.semicolonsoft.com/";
    public static final String imageUrl="http://engaz.semicolonsoft.com/uploads/images/";
    public static final String vedioUrl = "http://engaz.semicolonsoft.com/uploads/vedios/";
    public static final String file = "http://engaz.semicolonsoft.com/uploads/files/";

    public static String baseUrlDir="https://maps.googleapis.com/";
    public static final String user_type_academy="1";
    public static final String user_type_user="2";
    public static final String user_type_skip="0";
    public static final String session_login="1";
    public static final String session_logout="0";
    public static final String like ="1";
    public static final String dislike="3";
    public static final String viewer="2";



    public static final String me="1";
    public static final String visitor="0";



    public static Services getService()
    {
        Retrofit retrofit = Api.getInstance(baseUrl);
        Services services = retrofit.create(Services.class);
        return services;
    }

    public static Services getServiceDir()
    {
        Retrofit retrofit = Api.getInstance(baseUrlDir);
        Services services = retrofit.create(Services.class);
        return services;
    }


    public static AlertDialog CreateAlertDialog(Context context)
    {
        AlertDialog dialog  = new AlertDialog.Builder(context)
                .setMessage("This service not available to visitors")
                .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss()).create();

        return dialog;


    }



    public static ProgressDialog CreateProgressDialog(Context context , String msg)
    {
        ProgressBar progressBar = new ProgressBar(context);
        Drawable drawable = progressBar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setIndeterminateDrawable(drawable);
        return dialog;
    }
}
