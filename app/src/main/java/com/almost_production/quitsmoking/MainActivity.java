package com.almost_production.quitsmoking;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    PendingIntent pIntent;
    public void CustomNotification() {
        //remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        // old intent
        //Intent intent = new Intent(this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
        //        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Intent iAction1 = new Intent(this, MyIntentService.class);
//        iAction1.setAction(MyIntentService.ACTION1);
//        pIntent = PendingIntent.getService(this, 0, iAction1, PendingIntent.FLAG_UPDATE_CURRENT);

        // INTENT LAST CHANCE
        Intent intent = new Intent("Download_Cancelled");
        pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.smoke)
                .setAutoCancel(false)
                .setContentTitle("Time left")
                .setTicker("Waiting")
                .setOngoing(true) // always in notificationBar
                .setContentText(timeleft);
         //       .setContent(remoteViews);

//        remoteViews.setImageViewResource(R.id.image,R.drawable.smoke);
//        remoteViews.setTextViewText(R.id.title,"Time left");
//        remoteViews.setTextViewText(R.id.text,timeleft);

        notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());
    }

    public static class DownloadCancelReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //ClickButton(false);
        }
    }

    public void UpdateNotification(boolean cansmoke) {
        //remoteViews.setTextViewText(R.id.text,timeleft);
        builder.setContentText(timeleft);
        if (cansmoke) {
            //remoteViews.setImageViewResource(R.id.image,R.drawable.smoke);
            builder.setSmallIcon(R.drawable.smoke);
            builder.addAction(R.drawable.smoke,"I'm smoked", pIntent);

        } else {
            //remoteViews.setImageViewResource(R.id.image,R.drawable.smoke_no);
            builder.setSmallIcon(R.drawable.smoke_no);
            builder.mActions.clear();
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
                timeleft = 0 + " min " + 0 + " sec";

                textView_timeLeft.setText("You can go smoke");
                button_imsmoked.setEnabled(true);
                button_imsmoked.setText("I'm smoked");
                UpdateNotification(true);
            }

        };
        cd.start();
    }
}
