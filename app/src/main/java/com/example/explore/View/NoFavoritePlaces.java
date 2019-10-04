package com.example.explore.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.explore.R;

public class NoFavoritePlaces extends AppCompatActivity {

    private TextView text;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_favorite_places);

        text = findViewById(R.id.nofav);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/YARDSALE.TTF");
        text.setTypeface(face);
        btn = findViewById(R.id.btHome);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToHome = new Intent(NoFavoritePlaces.this, HomeActivity.class);
                startActivity(moveToHome);
            }
        });



    }
}
