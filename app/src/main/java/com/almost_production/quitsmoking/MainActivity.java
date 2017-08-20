package com.almost_production.quitsmoking;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    TextView textView_descr;
    TextView textView_timeLeft;
    EditText sigarets_count;

    Button button_start;
    Button button_imsmoked;

    Notification notification;
    RemoteViews contentView;
    NotificationManager mNotificationManager;
    Button button_smoke_not;

    CountDownTimer cd = null;
    int count;
    int temp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_start = (Button) findViewById(R.id.button_start);
        button_imsmoked = (Button) findViewById(R.id.button_wait);

        textView_descr = (TextView) findViewById(R.id.textView_descr);
        textView_timeLeft = (TextView) findViewById(R.id.textView_timeleft);

        sigarets_count = (EditText) findViewById(R.id.sigarets_count);
        count = Integer.parseInt(sigarets_count.getText().toString());

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = 0;
                count = Integer.parseInt(sigarets_count.getText().toString());
                LaunchTimer(count);
                count += 5;
                button_imsmoked.setEnabled(false);
                button_imsmoked.setText("Wait");
            }
        });

        button_imsmoked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = 0;
                LaunchTimer(count);
                count += 5;
                button_imsmoked.setEnabled(false);
                button_imsmoked.setText("Wait");
            }
        });


        // NOTIFICATION CODE
//        button_smoke_not = (Button) findViewById(R.id.button_smoked_not);
//
//        int icon = R.mipmap.ic_launcher;
//        long when = System.currentTimeMillis();
//        notification = new Notification(icon, "Custom Notification", when);
//
//        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//
//        contentView = new RemoteViews(getPackageName(), R.layout.notification);
//        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
//        contentView.setTextViewText(R.id.title, "Quit smoking");
//        contentView.setTextViewText(R.id.text, "0 h 0 min");
//        notification.contentView = contentView;
//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        notification.contentIntent = contentIntent;
//
//        notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
//
//        mNotificationManager.notify(1, notification);
//
//        System.out.print("!!!" + button_smoke_not);
    }

//    private static final int NOTIFY_ID = 101;
//
//    public void UpdateNotification(boolean canSmoke) {
//        contentView.setTextViewText(R.id.text, textView.getText().toString());
//
//        if (canSmoke) {
//            button_smoke_not.setEnabled(true);
//        } else {
//            button_smoke_not.setEnabled(false);
//        }
//
//        mNotificationManager.notify(1, notification);
//    }

    void LaunchTimer(int sec) {

        if (cd != null)
            cd.cancel();
        cd = new CountDownTimer(sec * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                textView_timeLeft.setText("Time left: "  + millisUntilFinished / 1000 / 60 +
                        " min " + millisUntilFinished / 1000 + " sec");
                temp++;
                textView_descr.setText(temp + "");
                //UpdateNotification(false);
            }
            public void onFinish() {
                textView_timeLeft.setText("You can go smoke");
                button_imsmoked.setEnabled(true);
                button_imsmoked.setText("I'm smoked");
                temp++;
                textView_descr.setText(temp + "");
                //UpdateNotification(true);
            }

        };
        cd.start();
    }
}
