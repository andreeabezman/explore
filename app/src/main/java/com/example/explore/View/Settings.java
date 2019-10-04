package com.example.explore.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.explore.R;

public class Settings extends AppCompatActivity {

    TextView contact, about, notifications, help, version, sett;
    ImageView contactimg, aboutimg, notifimg, helpimg, verisonimg;

    Dialog contactDialog, appVersionDialog, aboutDialog;
    ImageView closeContactIMG, closeAppVersionImg , closeAboutImg;
    Button closeContactBtn, closeAppVersionBtn, closeAboutBtn;
    TextView titlucont, continutCont;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        contactDialog = new Dialog(this);
        appVersionDialog =new Dialog(this);
        aboutDialog = new Dialog(this);



        sett = findViewById(R.id.settings);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/YARDSALE.TTF");
        sett.setTypeface(face);
        contact = findViewById(R.id.etCon);
        about = findViewById(R.id.etAbout);
        notifications = findViewById(R.id.etN);
        help = findViewById(R.id.etHow);
        version = findViewById(R.id.etAppV);

        contactimg = findViewById(R.id.ivContact);
        aboutimg = findViewById(R.id.ivAbout);
        notifimg = findViewById(R.id.ivNot);
        helpimg = findViewById(R.id.ivHowto);
        verisonimg = findViewById(R.id.ivAndro);

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToNotif = new Intent(Settings.this, Notification.class);
                startActivity(moveToNotif);
            }
        });
        notifimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToNotif = new Intent(Settings.this, Notification.class);
                startActivity(moveToNotif);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactDialog();
            }
        });
        contactimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactDialog();
            }
        });
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAppVersionDialog();
            }
        });
        verisonimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAppVersionDialog();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openAboutDialog();
                showAboutDialog();
            }
        });
        aboutimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  openAboutDialog();
                showAboutDialog();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToHelp = new Intent(Settings.this, HowTo.class);
                startActivity(moveToHelp);
            }
        });
        helpimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToHelp = new Intent(Settings.this, HowTo.class);
                startActivity(moveToHelp);
            }
        });



    }

    private void showAboutDialog() {
        aboutDialog.setContentView(R.layout.about_popup);
        closeAboutBtn = (Button)aboutDialog.findViewById(R.id.okAbout);
        closeAboutImg = (ImageView) aboutDialog.findViewById(R.id.closeAbout);
        closeAboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutDialog.dismiss();
            }
        });
        closeAboutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutDialog.dismiss();
            }
        });
        aboutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aboutDialog.show();

    }

    private void showAppVersionDialog() {
        appVersionDialog.setContentView(R.layout.appversion_popup);
        closeAppVersionBtn = (Button)appVersionDialog.findViewById(R.id.okAppVersion);
        closeAppVersionImg = (ImageView)appVersionDialog.findViewById(R.id.closeAppVersion);
        closeAppVersionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appVersionDialog.dismiss();
            }
        });
        closeAppVersionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appVersionDialog.dismiss();
            }
        });
        appVersionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        appVersionDialog.show();
    }

    private void showContactDialog() {
        contactDialog.setContentView(R.layout.contact_popup);
        closeContactIMG = (ImageView)contactDialog.findViewById(R.id.closeContact);
        closeContactBtn = (Button) contactDialog.findViewById(R.id.okContact);
        closeContactIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactDialog.dismiss();
            }
        });
        closeContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactDialog.dismiss();
            }
        });
        contactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        contactDialog.show();
    }


}
