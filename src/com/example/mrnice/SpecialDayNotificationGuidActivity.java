package com.example.mrnice;


import java.util.Calendar;

import com.example.mrnice.model.SpecialDay;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SpecialDayNotificationGuidActivity extends Activity {
	private SpecialDay spd;
	private Context mContext;
	private Button remindMeLater, dealButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_special_day_notification_guid);
		remindMeLater = (Button) findViewById(R.id.delay);
		dealButton = (Button) findViewById(R.id.dealbutton);
		mContext = SpecialDayNotificationGuidActivity.this;
		
		
		spd = DBUtil.getSpecialDayById(mContext, getIntent().getIntExtra("dayId", 99999));
		TextView nd =  (TextView)findViewById(R.id.notificationdetails);
		nd.setText("blalbalba,");
		
		remindMeLater.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				setReminderAlarm();
				finish();
			}
		});
		
		dealButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				//
			}
		});
	}

	private void setReminderAlarm(){
		AlarmManager alarmMgr = 
				(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(mContext, MrNiceSetServiceReciever.class);
		i.putExtra("remind", spd.get_id());
		PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, 
				i, PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, 30);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), sender);
//		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, 
//				now.getTimeInMillis(), SAMPLING_INTERVAL_IN_MILLIS, sender);
//		Log.d(LOGGING_TAG,"setAlarm");
	}
}
