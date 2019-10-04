package com.example.explore;

import android.database.Observable;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("explore/insertUser")
    Call<User> register(@Body User user);

    @POST("explore/insertPost")
    Call<Posts> addpost(@Body Posts posts);

    @GET("explore/findUser")
    Call<User> login(@Query("username")String username, @Query("password")String password);
    @GET("explore/findFavorite")
    Call<Boolean> checkfav(@Query("placeId")String placeid, @Query("userId")int userid);
    @POST("explore/insertFavorite")
    Call<Favorite> insertFavorite(@Body Favorite favorite);
    @DELETE("explore/deleteFavorite")
    Call<Void> deleteFavorite(@Query("placeId")String placeId, @Query("userId") int userId);
    @GET("explore/posts")
    Call<List<Posts>>  getAllPosts();

    //asta era buna in backend am testat
    @GET("explore/getFavoriteUser")
    Call<List<Favorite>> getFavoriteUser(@Query("userId")int userId);
    //next
    @GET("explore/getUser")
    Call <User> getUser(@Query("id")int id);

   @GET("explore/findFavoriteUser")
   Call<List<Favorite>> showUserFav(@Query("userId")int userId);
//  @HTTP(method = "GET", path = "explore/deleteFavorite", hasBody = true)
  //Call<Favorite> deleteFavorite(@Body Favorite favorite);
//
   /* @GET("login.php")
    Call<User> performUserLogin(@Query("user_name") String Username,@Query("user_password") String Userpassword);*/

    @Multipart
    @POST("explore/upload")
    Call<Void> uploadImage(@Part MultipartBody.Part file);
}
