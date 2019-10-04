package com.example.explore.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.explore.R;

public class SplashActivity extends AppCompatActivity {

    Button button;
    ImageView img;
    Animation frombottom, fromtop;
    TextView explore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        button = findViewById(R.id.btStart);
        img = findViewById(R.id.img);
        explore = findViewById(R.id.explore);


        button.setAnimation(frombottom);
        img.setAnimation(fromtop);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/YARDSALE.TTF");
        explore.setTypeface(face);

        button.setAnimation(frombottom);
        img.setAnimation(fromtop);
        explore.setAnimation(fromtop);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToHome = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(moveToHome);
            }
        });
    }
}
