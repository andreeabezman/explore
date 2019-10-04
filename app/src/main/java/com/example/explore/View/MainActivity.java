package com.example.explore.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.explore.APIClient;
import com.example.explore.ApiInterface;
import com.example.explore.R;
import com.example.explore.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout clContainer;
    private Button login;
    private TextView txtLog;
    private EditText usernameLogin , passwordLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        usernameLogin = findViewById(R.id.etUserLogin);
        passwordLogin = findViewById(R.id.etPasswordLogin);
        usernameLogin.setText("");
        passwordLogin.setText("");

        login =  (Button) findViewById(R.id.btLogin);
        clContainer = findViewById(R.id.clContainer);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(usernameLogin.getText().toString())
                        || TextUtils.isEmpty(passwordLogin.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = new User();
          //      user.getUsername();
           //     user.getPassword();
           //     if(user.getUsername()==usernameLogin.getText().toString() && user.getPassword()== passwordLogin.getText().toString()) {
                    callLogin(usernameLogin.getText().toString(), passwordLogin.getText().toString());
            //    }
            }
        });

        TextView tv =  findViewById(R.id.tvWelcome);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/YARDSALE.TTF");
        tv.setTypeface(face);

        txtLog = (TextView) findViewById(R.id.txtSignIn);
        txtLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToRegister = new Intent(MainActivity.this, Register.class);
                startActivity(moveToRegister);
            }
        });
    }

  private void callLogin(String username, String password){
      APIClient apiClient = new APIClient();
      Retrofit retrofit = apiClient.getApiClient();
      ApiInterface apiInterface = retrofit.create(ApiInterface.class);
      Call<User> call = apiInterface.login(username,password);
      call.enqueue(new Callback<User>() {
          @Override
          public void onResponse(Call<User> call, Response<User> response) {
              if(response.body()==null || response.body().getUsername()==null ){
                  Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
              }else {
                  Toast.makeText(MainActivity.this, "Success Login", Toast.LENGTH_SHORT).show();

                  SharedPreferences sharedPreferences = getSharedPreferences("explore", MODE_PRIVATE);
                  SharedPreferences.Editor editor = sharedPreferences.edit();
                  editor.putInt("userid", response.body().getId());
                  editor.apply();
                  editor.putString("username", response.body().getUsername());
                  editor.apply();

                  Intent moveToHome = new Intent(MainActivity.this, HomeActivity.class);
                  startActivity(moveToHome);
                  usernameLogin.setText("");
                  passwordLogin.setText("");
              }
          }

          @Override
          public void onFailure(Call<User> call, Throwable t) {
              Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
          }
      });

  }

}
