package com.example.mrnice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MrNiceSetServiceReciever extends BroadcastReceiver {

	private static final String LOGGING_TAG = "MrNiceSetServiceReciever";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(LOGGING_TAG," MrNiceSetServiceReciever onReceive");
		Intent service = new Intent(context, SpecialDaySetService.class);
		if(intent.getIntExtra("remind", 99999) != 99999){
			service.putExtra("remind", intent.getIntExtra("remind", 99999));
		}
		context.startService(service);

	}

}
