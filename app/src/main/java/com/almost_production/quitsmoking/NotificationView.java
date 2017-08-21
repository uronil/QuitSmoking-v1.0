package com.almost_production.quitsmoking;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.app.Activity;

import static com.almost_production.quitsmoking.MainActivity.ALARM_MESSAGE;
import static com.almost_production.quitsmoking.MainActivity.WHERE_MY_CAT_ACTION;


public class NotificationView extends Activity {
    String title;
    String text;
    TextView txttitle;
    TextView txttext;
    Button button_smoked_not;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
//
//        // Create Notification Manager
//        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        // Dismiss Notification
//        notificationmanager.cancel(0);
//
//        // Retrive the data from MainActivity.java
//        Intent i = getIntent();
//
//        title = i.getStringExtra("title");
//        text = i.getStringExtra("text");
//
//        // Locate the TextView
//        txttitle = (TextView) findViewById(R.id.title);
//        txttext = (TextView) findViewById(R.id.text);
//
//        // Set the data into TextView
//        txttitle.setText(title);
//        txttext.setText(text);
//
//        button_smoked_not = (Button) findViewById(R.id.button_smoked_not);
//        button_smoked_not.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
    }

}
