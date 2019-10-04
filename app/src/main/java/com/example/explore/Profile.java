package com.example.explore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.explore.APIClient;
import com.example.explore.ApiInterface;
import com.example.explore.BuildConfig;
import com.example.explore.R;
import com.example.explore.User;
import com.example.explore.View.HomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Profile extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 101;
    final int GALLERY_REQUEST_CODE=100;
    private String cameraFilePath;

    int currentUserId;
    User user;
    String usern, passw, cityp, emailp;
    long birthp;
    EditText username, password, city, birthday, email;
    FloatingActionButton addphoto;
    CircleImageView imgprofile;
    FloatingActionButton okprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //userid = 4;
        addphoto = findViewById(R.id.add_photo);
        username = findViewById(R.id.etUserProfile);
        password = findViewById(R.id.etPasswordProfile);
        city = findViewById(R.id.etCityProfile);
        birthday = findViewById(R.id.etBirthProfile);
        email = findViewById(R.id.etMailProfile);
        imgprofile = findViewById(R.id.imageView3);
        okprofile= findViewById(R.id.okprofile);

        SharedPreferences sharedPreferences = getSharedPreferences("explore", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("userid", 0);

        callUser();

        okprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToHome = new Intent(com.example.explore.Profile.this, HomeActivity.class);
                startActivity(moveToHome);
                Toast.makeText(com.example.explore.Profile.this, "Profile saved",Toast.LENGTH_SHORT).show();

            }
        });
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhotoDialog();
            }
        });

    }
    private void showPhotoDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_photo, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);

        if (ContextCompat.checkSelfPermission(com.example.explore.Profile.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(com.example.explore.Profile.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(com.example.explore.Profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(com.example.explore.Profile.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GALLERY_REQUEST_CODE);
        }else{
            deleteDialog.show();
        }
        deleteDialogView.findViewById(R.id.tvCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                deleteDialog.dismiss();

                // Permission has already been granted
                captureFromCamera();
            }
        });
        deleteDialogView.findViewById(R.id.tvGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
                // Permission has already been granted
                pickFromGallery();
            }
        });


    }
    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }
    private void captureFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
        if (!storageDir.exists()){
            storageDir.mkdir();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    if (Uri.parse(getRealPathFromURI(data.getData())) != null){
                      /*  File file = new File(Objects.requireNonNull
                                (Uri.parse(getRealPathFromURI(data.getData())).getPath()));
                                */
                        //se specifica locatia din telefon unde se afla fisierul img
                //        uploadToServer(Objects.requireNonNull
                  //              (Uri.parse(getRealPathFromURI(data.getData())).getPath()));
                        imgprofile.setImageURI(Uri.parse(getRealPathFromURI(data.getData())));
                    }

                    break;
                case CAMERA_REQUEST_CODE:
                   imgprofile.setImageURI(Uri.parse(cameraFilePath));
                    break;
            }
    }
    private void uploadToServer(String filePath) {
        //aici se formeaza request pt trimiterea imaginii la backend
        APIClient apiClient = new APIClient();
        Retrofit retrofit = apiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        // Call<User> call = apiInterface.getUser(currentUserId);

        //  Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        //    UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        //  RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        Call call = apiInterface.uploadImage(part);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Toast.makeText(com.example.explore.Profile.this, "worked",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(com.example.explore.Profile.this, "didn't work",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void callUser() {
        APIClient apiClient = new APIClient();
        Retrofit retrofit = apiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<User> call = apiInterface.getUser(currentUserId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null && response.isSuccessful()) {
                    user = response.body();
                    usern = user.getUsername();
                    passw = user.getPassword();
                    cityp = user.getCity();
                    birthp = user.getBirthday();
                    int x = ((int) birthp);
                    emailp = user.getEmail();

                    username.setText(usern);
                    password.setText(passw);
                    city.setText(cityp);
                    //aici luna ziua anul

                    Date date = new Date(user.getBirthday());
                    // dat = user.getBirthday();
                    DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
                    birthday.setText(dateFormat.format(date));
                    //birthday.setText(x);
                    email.setText(emailp);

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
