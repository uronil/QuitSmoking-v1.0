package com.almost_production.quitsmoking;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static TextView textView_descr,
            textView_timeLeft,
            textView_sig_cur,
            textView_waitingtime_cur,
            textView_sig_cur_next,
            textView_waitingtime_cur_next;
    static EditText sigarets_count;
    static Button button_start;
    static Button button_imsmoked;

    static CountDownTimer cd = null;
    static String timeleft;
    static int count, waitingtime;
    static long hoursleft, minleft, secleft;

    // Settings
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    public static final String APP_PREFERENCES_WAITINGTIME = "waitingtime";
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Setting initialization
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        openSettings();

        button_start = (Button) findViewById(R.id.button_start);
        button_imsmoked = (Button) findViewById(R.id.button_wait);

        textView_descr = (TextView) findViewById(R.id.textView_descr);
        textView_timeLeft = (TextView) findViewById(R.id.textView_timeleft);
        textView_sig_cur = (TextView) findViewById(R.id.textView_sig_curr);
        textView_waitingtime_cur = (TextView) findViewById(R.id.textView_waitingtime_cur);
        textView_sig_cur_next = (TextView) findViewById(R.id.textView_sig_curr_next);
        textView_waitingtime_cur_next = (TextView) findViewById(R.id.textView_waitingtime_cur_next);

        sigarets_count = (EditText) findViewById(R.id.sigarets_count);
        sigarets_count.setText(Integer.toString(count));

        if (count != 0) {
            double d = (double) waitingtime / 60;
            textView_waitingtime_cur.setText("Wait " + Double.toString(d) + " MIN");

            d = 16 * 60  / d;
            textView_sig_cur.setText(String.format("%.2f", d)  + " Sigs in day");

            button_imsmoked.setEnabled(true);
        }

        CustomNotification();

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickButton(true);

            }
        });

        button_imsmoked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickButton(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        openSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSettings();
        builder.setOngoing(false);
        notificationmanager.notify(0, builder.build());
    }

    public void openSettings() {
        if (mSettings.contains(APP_PREFERENCES_COUNTER)) {
            count = mSettings.getInt(APP_PREFERENCES_COUNTER, 0);
            waitingtime = mSettings.getInt(APP_PREFERENCES_WAITINGTIME, 0);
        }
    }

    public void saveSettings() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_COUNTER, count);
        editor.putInt(APP_PREFERENCES_WAITINGTIME, waitingtime);
        editor.apply();
    }

    static NotificationCompat.Builder builder;
    static NotificationManager notificationmanager;
    static PendingIntent pIntent;
    public void CustomNotification() {
        Intent intent = new Intent("Send");
        pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        Intent intentToMain = new Intent(this, MainActivity.class);
        PendingIntent pIntentToMain = PendingIntent.getActivity(this,0,intentToMain, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.smoke)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(false)
                .setContentTitle("Time left")
                .setOngoing(true) // always in notificationBar
                .setShowWhen(false)
                .setContentIntent(pIntentToMain)
                .setContentText("Launch timer for getting started");

        notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());
    }

    public static class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ClickButton(false);
        }
    }

    static public void UpdateNotification(boolean cansmoke) {
        builder.setContentText(timeleft);
        if (cansmoke) {
            builder.setSmallIcon(R.drawable.smoke);
            builder.addAction(R.drawable.smoke,"I'm smoked", pIntent);
        } else {
            builder.setSmallIcon(R.drawable.smoke_no);
            builder.mActions.clear();
        }
        notificationmanager.notify(0, builder.build());
    }

    static public void ClickButton(boolean start) {
        if (start) {
            count = Integer.parseInt(sigarets_count.getText().toString());
            waitingtime = 16 * 60 * 60 / count;
        }

        int day_modifier = 300; // 5 min in a day
        LaunchTimer(waitingtime);
        waitingtime += day_modifier / count;
        button_imsmoked.setEnabled(false);
        button_imsmoked.setText("Wait");

        double d = (double) waitingtime / 60;
        textView_waitingtime_cur.setText("Wait " + Double.toString(d) + " Min");

        d = 16 * 60 * 60 / d / 60;
        textView_sig_cur.setText(String.format("%.2f", d)  + " Sigs in day");
    }

    static void LaunchTimer(int sec) {

        if (cd != null)
            cd.cancel();
        cd = new CountDownTimer(sec * 1000, 500) {
            public void onTick(long millisUntilFinished) {
                hoursleft = millisUntilFinished / 1000 / 60 / 60;
                minleft = millisUntilFinished / 1000 / 60 - hoursleft * 60;
                secleft = (millisUntilFinished) / 1000 - (millisUntilFinished / 1000 / 60) * 60;
                timeleft = hoursleft + " h " +  minleft + " m " + secleft + " s";
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
