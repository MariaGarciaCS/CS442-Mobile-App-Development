package com.marigarci.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceReceiver extends BroadcastReceiver {
    static final String ACTION_MSG_TO_SVC = "AMTS";

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ACTION_MSG_TO_SVC:
                if (intent.hasExtra("SOURCE_DATA")) {
                    NewsArticle Dataloader = new NewsArticle(intent.getStringExtra("SOURCE_DATA"));
                    new Thread(Dataloader).start();
                }
                break;
        }
    }
}
