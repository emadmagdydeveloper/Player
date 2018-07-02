package com.alatheer.myplayer.Service;

import com.alatheer.myplayer.Models.AboutAppModel;
import com.alatheer.myplayer.Models.ContactModel;
import com.alatheer.myplayer.Models.DirectionModel;
import com.alatheer.myplayer.Models.PlayersModel;
import com.alatheer.myplayer.Models.ResponseModel;
import com.alatheer.myplayer.Models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface Services {

    @FormUrlEncoded
    @POST("ApiP/Registration")
    Call<UserModel> registeration(@Field("user_name") String user_name,
                                  @Field("user_pass") String user_pass,
                                  @Field("user_phone") String user_phone,
                                  @Field("user_email") String user_email,
                                  @Field("user_photo") String user_photo);


    @FormUrlEncoded
    @POST("ApiP/Login")
    Call<UserModel> login(@Field("user_email") String user_email,@Field("user_pass") String user_pass);

    @GET("ApiP/ContactUs")
    Call<ContactModel> getContacts();

    @GET("ApiP/AboutUs")
    Call<AboutAppModel> aboutApp();

    @GET("ApiP/AllAccademy/{user_id}")
    Call<List<UserModel>> getAcademies(@Path("user_id")String user_id);
    @GET("ApiP/Logout/{user_id}")
    Call<ResponseModel> logout();

    @FormUrlEncoded
    @POST("ApiP/AllAccademy/{user_id}")
    Call<List<UserModel>> search(@Path("user_id") String user_id,@Field("search_name") String query);

    @GET("ApiP/AllPlayer/{user_id}")
    Call<List<PlayersModel>> getPlayers(@Path("user_id") String user_id);

    @FormUrlEncoded
    @POST("ApiP/AddPlayer/{user_id}")
    Call<ResponseModel> AddPlayer(@Path("user_id")String user_id,
                                  @Field("player_name")String player_name,
                                  @Field("player_age")String player_age,
                                  @Field("player_position")String player_position,
                                  @Field("player_tall")String player_tall,
                                  @Field("player_photo")String player_photo,
                                  @Field("player_weight")String player_weight,
                                  @Field("player_vedio")String player_video
                                  );

    @FormUrlEncoded
    @POST("ApiP/AccademyCv/{user_id}")
    Call<ResponseModel> UploadCV(@Path("user_id")String user_id,@Field("user_cv")String user_cv);

    @FormUrlEncoded
    @POST("ApiP/UpdateProfile/{user_id}")
    Call<UserModel> updateProfile(@Path("user_id")String user_id,
                                  @Field("user_name")String user_name,
                                  @Field("user_pass")String user_pass,
                                  @Field("user_phone")String user_phone,
                                  @Field("user_email")String user_email,
                                  @Field("user_photo")String user_photo
                                  );


    @FormUrlEncoded
    @POST("ApiP/LikeVedio/{player_id}")
    Call<ResponseModel> updateVideoInteraction(@Path("player_id")String player_id,@Field("count")String count);

    @FormUrlEncoded
    @POST("ApiP/UpdatePlayer/{player_id}")
    Call<PlayersModel> updatePlayer(@Path("player_id") String player_id,
                                    @Field("player_name") String player_name,
                                    @Field("player_age") String player_age,
                                    @Field("player_position") String player_position,
                                    @Field("player_tall") String player_tall,
                                    @Field("player_photo") String player_photo,
                                    @Field("player_weight") String player_weight,
                                    @Field("player_vedio") String player_vedio,
                                    @Field("player_vedio_comment") String player_vedio_comment


                                    );

    @GET()
    Call<DirectionModel> getDirection(@Url String url);

    @Multipart
    @POST("/uploads/vedios/")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);

    @GET("ApiP/ShowCv/{user_id}")
    Call<UserModel> showCV(@Path("user_id")String user_id);
}
