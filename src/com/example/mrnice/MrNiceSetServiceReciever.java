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
		if(intent.getIntExtra(MrNiceConstant.REMIND, MrNiceConstant.ERROR_CODE) != MrNiceConstant.ERROR_CODE){
			service.putExtra(MrNiceConstant.REMIND, intent.getIntExtra(MrNiceConstant.REMIND, MrNiceConstant.ERROR_CODE));
		}
		context.startService(service);

	}

}
