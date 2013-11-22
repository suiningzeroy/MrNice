package com.example.mrnice;

import java.util.Calendar;
import java.util.Date;

import com.example.mrnice.model.TypeOfDay;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
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
		
		MrNiceApp app = (MrNiceApp) this.getApplication();
		if(app.getPrefs().getBoolean("init", true)){
			initialTypeTable();
			app.setBooleanValueToSharedPreferences("init",false);
		}
		GridView gridview = (GridView) findViewById(R.id.gridView1);
		gridview.setAdapter(new ImageAdapter(this));
		
		handler = new Handler(this);
		alarms = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		
		Info = (TextView) findViewById(R.id.textView1);
		Info.setText("i am info. waiting for date setting!");
		
		findViewById(R.id.addday).setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				showDatePickerDialog(v);
			}
			
		});
		
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id){
				Toast.makeText(MainActivity.this, "grid " + (position + 1) + " was clicked! ", Toast.LENGTH_SHORT).show();
				Intent setting = new Intent(MainActivity.this, PeopleSettingActivity.class);
				startActivity(setting);
			}
		});
	}
	
	public boolean handleMessage(Message msg) {
		int state = msg.getData().getInt(STATE); 
		switch(state){
			case REFRESH:
				refreshUI();
				//setSpecialDayAlarm();
				Toast.makeText(getApplicationContext(), 
						"refreshUI and add days to Db", 
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

	public class ImageAdapter extends BaseAdapter{
		private Context context;
		private Integer[] mImageIds = {
				R.drawable.pic1,
				R.drawable.pic2,
				R.drawable.pic3,
				R.drawable.pic4,
				R.drawable.pic5
		};
		
		public ImageAdapter(Context c){
			context = c;
		}
		
		public int getCount() {
			return mImageIds.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			 ImageView imageView;
			 if (convertView == null){
				 imageView = new ImageView(context);
				 imageView.setLayoutParams(new GridView.LayoutParams(125, 125));
				 imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			 }else{
				 imageView = (ImageView) convertView;
			 }
			 imageView.setImageResource(mImageIds[position]);
			 return imageView;
		}
		
	}
	
	private void initialTypeTable(){
		Date dt = new Date();
		TypeOfDay td = new TypeOfDay();
		td.setName("Birthday");
		td.setUri("www.aaa.com");
		td.setUpdate_date(dt.getTime());
		
		TypeOfDay td1 = new TypeOfDay();
		td1.setName("Wedding");
		td1.setUri("www.aaa.com");
		td1.setUpdate_date(dt.getTime());
		
		TypeOfDay td2 = new TypeOfDay();
		td2.setName("others");
		td2.setUri("www.aaa.com");
		td2.setUpdate_date(dt.getTime());
		
		DBUtil.addTypeOfDay(this, td);
		DBUtil.addTypeOfDay(this, td1);
		DBUtil.addTypeOfDay(this, td2);
	}

}
