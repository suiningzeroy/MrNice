package com.example.mrnice;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class SpecialDayNotificationGuidActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_day_notification_guid);
        
        TextView nd =  (TextView)findViewById(R.id.notificationdetails);
        nd.setText("wawawawawawa , ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_special_day_notification_guid, menu);
        return true;
    }
}
