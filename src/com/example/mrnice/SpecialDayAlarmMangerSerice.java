package com.example.mrnice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class SpecialDayAlarmMangerSerice extends Service {
	private static final String TAG = "SpecialDayAlarmMangerSerice ";
	Bundle specialDayInfo;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, TAG + "onCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, TAG + "onStartCommand");
		specialDayInfo = intent.getExtras();
		String[] specialDay =  specialDayInfo.getStringArray("info");
		Log.d(TAG ,"special day name : " + specialDay[0] + "special day type : " + specialDay[1] +
				"special day notification : " + specialDay[2] + "special day tips : " + specialDay[3]);
		createSpecialDayNotification(specialDay);
		return Service.START_STICKY;
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, TAG + "onDestroy");
	}
	
	private void createSpecialDayNotification(String[] specialDay) {
		NotificationManager nmgr = (NotificationManager) 
				getSystemService(Context.NOTIFICATION_SERVICE);
		String shortMsg = "special day arrived , name is : " + specialDay[0];
		long time = System.currentTimeMillis();
		NotificationCompat.Builder builder = 
				new NotificationCompat.Builder(SpecialDayAlarmMangerSerice.this)
				.setSmallIcon(R.drawable.fligh_icon)
				.setWhen(time)
				.setContentTitle(shortMsg)
				.setContentText("this is a test notification!")
				.setAutoCancel(true);
		Intent specialDayIntent = new Intent(this, SpecialDayNotificationGuidActivity.class);
		specialDayIntent.putExtras(specialDayInfo);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, specialDayIntent , 0);
		builder.setContentIntent(contentIntent);
		nmgr.notify(1, builder.build());

	}

}
