package com.example.mrnice;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements Handler.Callback {
	
	private final static int REFRESH = 1;
	private final static String STATE = "state";
	private String selectedDate;
	private TextView Info;
	
	private AlarmManager alarms;
	private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        handler = new Handler(this);
        alarms = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        
        Info = (TextView) findViewById(R.id.textView1);
        Info.setText("i am info. waiting for date setting!");
        
        findViewById(R.id.addday).setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDatePickerDialog(v);
			}
        	
        });
    }
    
	public boolean handleMessage(Message msg) {
		int state = msg.getData().getInt(STATE); 
		switch(state){
			case REFRESH:
				refreshUI();
				setSpecialDayAlarm();
				Toast.makeText(getApplicationContext(), 
                        "refreshUI and setAlarm", 
                        Toast.LENGTH_LONG) 
                       .show(); 
		}
		return false;
	}
    
    private void refreshUI(){
		if(selectedDate == null)
			Info.setText("i am info. waiting for date setting!");
		else
			Info.setText("the date was set in " + selectedDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}
    
    public class DatePickerFragment  extends DialogFragment implements DatePickerDialog.OnDateSetListener  {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
				// Use the current time as the default values for the picker
				final Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				 
				 return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		
		private void sendMessage(int what) {
			Bundle bundle = new Bundle();
			bundle.putInt(STATE, what);
			Message message = new Message();
			message.setData(bundle);
			handler.sendMessage(message);
		}
		 
		public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
			selectedDate = getSelectedDateString(year,monthOfYear,dayOfMonth);
			sendMessage(REFRESH);
					
		}
		
		private String getSelectedDateString(int year, int monthOfYear,
			int dayOfMonth){
				String dateString;
				if (Integer.toString(monthOfYear + 1).length() == 1)
					dateString = Integer.toString(year) + "0" +Integer.toString(monthOfYear+1) + Integer.toString(dayOfMonth);
					else
						dateString = Integer.toString(year) + Integer.toString(monthOfYear+1) + Integer.toString(dayOfMonth);
					
					return dateString; 
		}

    }
    
    private void setSpecialDayAlarm(){
    	int alarmType = AlarmManager.RTC_WAKEUP;
    	String[] specialDayInfo = {"name","type","notification","tips"};
    	String ALARM_ACTION = "ALARM_ACTION";
    	Intent intentToFire = new Intent(this, SpecialDayAlarmReciever.class);
    	Bundle specialDay =  new Bundle();
    	specialDay.putStringArray("info", specialDayInfo);
    	intentToFire.putExtras(specialDay);
    	PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentToFire, 0);
    	//setting time before now to fire the alarm immediately
    	alarms.set(alarmType, System.currentTimeMillis()-10000, pendingIntent);
    }


}
