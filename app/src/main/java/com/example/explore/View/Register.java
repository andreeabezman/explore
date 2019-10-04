package com.example.explore.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.explore.APIClient;
import com.example.explore.ApiInterface;
import com.example.explore.R;
import com.example.explore.User;

import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Register extends AppCompatActivity {

    private TextView txtSignIn;
    private ConstraintLayout clContainerReg;
    private Button Reg;
    private EditText etUsername, etPassword, etCity, etBirthday, etEmail;
    String currentUsername;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String date;
    //int dateint;

    Calendar c = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUsername = findViewById(R.id.etUser);

        etPassword = findViewById(R.id.etPassword);
        etCity = findViewById(R.id.etCity);
        etBirthday = findViewById(R.id.etBirth);
        etBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal  = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                  Register.this,
                  android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                  mDateSetListener,
                  year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                c.set(year, month, day);
                month = month + 1;

                String dated = month+"/"+day+"/"+year;
              //  String date = ""+ month + day + year;
              //  dateint = Integer.parseInt(date);
                etBirthday.setText(dated);

            }
        };

        etEmail = findViewById(R.id.etMail);



        txtSignIn = findViewById(R.id.txtSignIn);
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToSignIn = new Intent(Register.this, MainActivity.class);
                startActivity(moveToSignIn);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("explore", MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");

        clContainerReg = findViewById(R.id.clContainerReg);
        TextView tv =  findViewById(R.id.tvWelcomeReg);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/YARDSALE.TTF");
        tv.setTypeface(face);
        Reg =  findViewById(R.id.btReg);
        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etUsername.getText().toString())
                    || TextUtils.isEmpty(etPassword.getText().toString())
                    || TextUtils.isEmpty(etEmail.getText().toString())
                    || TextUtils.isEmpty(etBirthday.getText().toString())
                    || TextUtils.isEmpty(etCity.getText().toString())){
                    Toast.makeText(Register.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                    return; }
                User user = new User();
                user.setUsername(etUsername.getText().toString());
                user.setPassword(etPassword.getText().toString());
                user.setEmail(etEmail.getText().toString());
                //aici
              //  user.setBirthday(Integer.valueOf(etBirthday.getText().toString()));
               // user.setBirthday(dateint);
                user.setBirthday(c.getTimeInMillis());
                user.setCity(etCity.getText().toString());
                callRegister(user);

            }
        }); }
    private void callRegister(User user){
        APIClient apiClient = new APIClient();
        Retrofit retrofit = apiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<User> call = apiInterface.register(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();
                Intent moveToHome = new Intent(Register.this, HomeActivity.class);
                if (response.body() != null){
                    SharedPreferences sharedPreferences = getSharedPreferences("explore", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("userid", response.body().getId());
                    editor.putString("username", response.body().getUsername());
                    editor.apply();
                }

                startActivity(moveToHome);
            //aici
                etUsername.setText("");
                etPassword.setText("");
                etCity.setText("");
                etBirthday.setText("");
                etEmail.setText("");
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Register.this, "Failed to register", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
