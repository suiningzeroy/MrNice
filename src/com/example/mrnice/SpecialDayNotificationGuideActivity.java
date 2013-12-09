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

public class SpecialDayNotificationGuideActivity extends Activity {
	private SpecialDay spd;
	private Context mContext;
	private Button remindMeLater, dealButton,skip;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_special_day_notification_guid);
		remindMeLater = (Button) findViewById(R.id.delay);
		dealButton = (Button) findViewById(R.id.dealbutton);
		skip = (Button) findViewById(R.id.Button01);
		mContext = SpecialDayNotificationGuideActivity.this;
		
		
		spd = DBUtil.getSpecialDayById(mContext, getIntent().getIntExtra(MrNiceConstant.PEOPLE_ID, MrNiceConstant.ERROR_CODE));
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
		
		skip.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				deleteDay(mContext);
			}
		});
	}
	
	private void deleteDay(Context mContext){
		DBUtil.DeleteSpecialDay(mContext, spd);
	}

	private void setReminderAlarm(){
		AlarmManager alarmMgr = 
				(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(mContext, MrNiceSetServiceReciever.class);
		i.putExtra(MrNiceConstant.REMIND, spd.get_id());
		PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, 
				i, PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, 30);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), sender);
	}
}
