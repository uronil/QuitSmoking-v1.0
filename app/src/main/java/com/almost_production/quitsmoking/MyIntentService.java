package com.almost_production.quitsmoking;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    public static final String ACTION1 = "ACTION1";
    public static final String ACTION2 = "ACTION2";

    @Override
    public void onHandleIntent(Intent intent) {
        final String action = intent.getAction();
        if (ACTION1.equals(action)) {
            Log.d("MSG","STRING");

        } else if (ACTION2.equals(action)) {
            // do some other stuff...
        } else {
            throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }
}
