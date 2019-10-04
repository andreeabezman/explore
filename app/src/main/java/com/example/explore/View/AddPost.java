package com.example.explore.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.explore.APIClient;
import com.example.explore.ApiInterface;
import com.example.explore.BuildConfig;
import com.example.explore.CustomMultipart;
import com.example.explore.Posts;
import com.example.explore.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;

public class AddPost extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 101;
    private Button addpost, btnbk;
    EditText title, content;
    final int GALLERY_REQUEST_CODE=100;
    ImageView photo;
    private String cameraFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        title = findViewById(R.id.editTextTitle);
        content = findViewById(R.id.editTextContent);
        photo = findViewById(R.id.photo);


        btnbk = findViewById(R.id.btn_back);
        btnbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToHome = new Intent(AddPost.this, HomeActivity.class);
                startActivity(moveToHome);
            }
        });
        addpost = findViewById(R.id.btn_add_posttobd);
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(title.getText().toString())
                        || TextUtils.isEmpty(content.getText().toString())
                        ){
                  Toast.makeText(AddPost.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                    return; }
                Posts posts = new Posts();
                posts.setTitle(title.getText().toString());
                posts.setContent(content.getText().toString());
                posts.setDate(System.currentTimeMillis());
                //img
                callPosts(posts);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
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

        if (ContextCompat.checkSelfPermission(AddPost.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(AddPost.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(AddPost.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
            ActivityCompat.requestPermissions(AddPost.this,
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
                        cameraFilePath =getRealPathFromURI(data.getData());
                        /*uploadToServer(Objects.requireNonNull
                                (Uri.parse(getRealPathFromURI(data.getData())).getPath()));*/
                        photo.setImageURI(Uri.parse(getRealPathFromURI(data.getData())));
                    }

                    break;
                case CAMERA_REQUEST_CODE:
                    photo.setImageURI(Uri.parse(cameraFilePath));
                    break;
            }
    }

    private void uploadToServer(String filePath, String title) {
        //aici se formeaza request pt trimiterea imaginii la backend
        APIClient apiClient = new APIClient();
        Retrofit retrofit = apiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        File file = new File(filePath);

        MultipartBody.Part filePart =
                MultipartBody.Part.createFormData("file", title+".jpg", //file.getName(),
                        RequestBody.create(MediaType.parse("image/*"), file));


        Call call = apiInterface.uploadImage(filePart);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Toast.makeText(AddPost.this, "Worked",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AddPost.this, "Didn't work",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callPosts(Posts posts) {
        APIClient apiClient = new APIClient();
        Retrofit retrofit = apiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<Posts> call = apiInterface.addpost(posts);
        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                    Toast.makeText(AddPost.this, "Success adding post", Toast.LENGTH_SHORT).show();
                    Intent moveToPosts = new Intent(AddPost.this, PostsActivity.class);
                    startActivity(moveToPosts);

            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                    Toast.makeText(AddPost.this, "Failed to add post", Toast.LENGTH_SHORT).show();
            }
        });

        uploadToServer(Uri.parse(cameraFilePath).getPath(), posts.getTitle());
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

    private void captureFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    captureFromCamera();
                }
                break;
            case GALLERY_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickFromGallery();
                }
                break;
        }
    }*/

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
}
