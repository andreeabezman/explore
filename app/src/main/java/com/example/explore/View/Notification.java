package com.example.explore.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.example.explore.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.util.Calendar;

public class Notification extends AppCompatActivity
    implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    EditText notificationTitle;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        text = findViewById(R.id.notif);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/YARDSALE.TTF");
        text.setTypeface(face);

        Button notif = findViewById(R.id.buttonNotif);
        notificationTitle = findViewById(R.id.editTextNotif);

        notificationTitle.setText("");

        dpd = DatePickerDialog.newInstance(
                Notification.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        tpd = TimePickerDialog.newInstance(Notification.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),false);
    /*
    btnCancel.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View v){
        NotifyMe.cancel(getApplicationContext(),"test")
    }
    });
     */
    notif.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dpd.show(getFragmentManager(),"Datepickerdialog");
            notificationTitle.setText(notificationTitle.getText().toString());



            // notificationTitle.setText("");
        }
    });


    }



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR,year);
        now.set(Calendar.MONTH,monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        tpd.show(getFragmentManager(),"Timekeeping");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
        now.set(Calendar.MINUTE,minute);
        now.set(Calendar.SECOND,second);

        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                .title("Explore")
                .content(notificationTitle.getText().toString())
                .color(255,0,0,255)
                .led_color(255,255,255,255)
                .time(now)
                .addAction(new Intent(),"Snooze", false)
                .key("test")
                .addAction(new Intent(), "Dismiss",true,false)
                .addAction(new Intent(),"Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();
       // Intent movetoHome = new Intent(Notification.this, HomeActivity.class);
       // startActivity(movetoHome);
        notificationTitle.setText("");

        Toast.makeText(Notification.this, "Notification saved!",Toast.LENGTH_SHORT).show();
    }
}
