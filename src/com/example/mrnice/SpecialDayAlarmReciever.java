package com.example.mrnice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SpecialDayAlarmReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle specialDayInfo = intent.getExtras();
		Log.d("MrNice","recieved the special day alarm");
		Intent alarmMangerService = 
				new Intent(context, SpecialDayAlarmMangerSerice.class);
		alarmMangerService.putExtras(specialDayInfo);
			context.startService(alarmMangerService);
	}

}
