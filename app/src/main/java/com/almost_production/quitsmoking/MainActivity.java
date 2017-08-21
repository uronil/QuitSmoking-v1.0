package com.almost_production.quitsmoking;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    TextView textView_descr;
    TextView textView_timeLeft;
    EditText sigarets_count;

    Button button_start;
    Button button_imsmoked;

    CountDownTimer cd = null;
    int count;

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

        // Buttons events
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickButton(true);
                CustomNotification();
            }
        });

        button_imsmoked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickButton(false);
            }
        });
    }

    RemoteViews remoteViews;
    NotificationCompat.Builder builder;
    NotificationManager notificationmanager;
    public void CustomNotification() {
        // Using RemoteViews to bind custom layouts into Notification
        remoteViews = new RemoteViews(getPackageName(),
                R.layout.notification);

        // Set Notification Title
        String strtitle = "notification title";
        // Set Notification Text
        String strtext = "notification text";

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, NotificationView.class);
        // Send data to NotificationView Class
        intent.putExtra("title", "Time left");
        intent.putExtra("text", timeleft);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.smoke)
                // Set Ticker Message
                .setTicker("Ticker message")
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setImageViewResource(R.id.image,R.drawable.smoke);

        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.title,"Time left");
        remoteViews.setTextViewText(R.id.text,timeleft);

        // Create Notification Manager
        notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());
    }

    public void UpdateNotification(boolean cansmoke) {
        remoteViews.setTextViewText(R.id.text,timeleft);
        if (cansmoke) {
            remoteViews.setImageViewResource(R.id.image,R.drawable.smoke);
            builder.setSmallIcon(R.drawable.smoke);
        } else {
            remoteViews.setImageViewResource(R.id.image,R.drawable.smoke_no);
            builder.setSmallIcon(R.drawable.smoke_no);
        }
        notificationmanager.notify(0, builder.build());
    }

    public void ClickButton(boolean start) {
        if (start)
            count = Integer.parseInt(sigarets_count.getText().toString());
        LaunchTimer(count);
        count += 5;
        button_imsmoked.setEnabled(false);
        button_imsmoked.setText("Wait");
    }

    long minleft, secleft;
    String timeleft;
    void LaunchTimer(int sec) {

        if (cd != null)
            cd.cancel();
        cd = new CountDownTimer(sec * 1000, 500) {
            public void onTick(long millisUntilFinished) {
                minleft = millisUntilFinished / 1000 / 60;
                secleft = (millisUntilFinished + 1000) / 1000;
                timeleft = minleft + " min " + secleft + " sec";
                textView_timeLeft.setText("Time left: "  +  timeleft);
                UpdateNotification(false);
            }
            public void onFinish() {
                textView_timeLeft.setText("You can go smoke");
                button_imsmoked.setEnabled(true);
                button_imsmoked.setText("I'm smoked");
                UpdateNotification(true);
            }

        };
        cd.start();
    }
}
