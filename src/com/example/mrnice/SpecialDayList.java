package com.example.mrnice;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.mrnice.model.SpecialDay;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class SpecialDayList extends Activity 
					implements Handler.Callback{
	private Context mContext;
	private List<SpecialDay> dayList;
	private int selectedId;
	private boolean isSelected ;
	private SpecialDay selectedDay;
	private Button addNew;
	private Button edit;
	private Button delete;
	private ListView dayListView;
	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_special_day_list);
		
		mContext = SpecialDayList.this;
		isSelected = false;
		handler = new Handler();
		
		dayListView = (ListView) findViewById(R.id.daylistview);
		addNew = (Button) findViewById(R.id.addnew);
		edit = (Button) findViewById(R.id.editday);
		delete = (Button) findViewById(R.id.deleteday);
		
		dayList = getSpecialDaysForAdapter();
		Collections.sort(dayList);
		final SpecialDayAdapter dayAdapter = new SpecialDayAdapter(dayList);
		dayListView.setAdapter(dayAdapter);
		
		dayListView.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selectedId = arg2;
				isSelected = true;
				selectedDay = dayList.get(arg2);
				edit.setEnabled(true);
				delete.setEnabled(true);
				dayAdapter.notifyDataSetChanged();
				
		}});
		
		dayListView.setOnItemLongClickListener(new OnItemLongClickListener(){

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				DBUtil.toastShow(mContext, "long clicked");
				return false;
			}
			
		});
		
		addNew.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent peopleList = new Intent(mContext, PeopleList.class);
				startActivity(peopleList);
			}
			
		});
		
		edit.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(selectedDay == null){
					DBUtil.toastShow(mContext, "no selected day for edit!");
				}else{
					Intent editday = new Intent(mContext, SpecialDaySetting.class);
					editday.putExtra(MrNiceConstant.PEOPLE_ID, selectedDay.get_id());
					editday.putExtra(MrNiceConstant.ISEDIT, true);
					startActivity(editday);
				}
			}
			
		});
		
		delete.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(selectedDay == null){
					DBUtil.toastShow(mContext, "no selected day for delete!");
				}else{
					DBUtil.DeleteSpecialDay(mContext,selectedDay);
					if(dayList.contains(selectedDay)){
						dayList.remove(selectedDay);
					}else{
						DBUtil.toastShow(mContext, "selected day error!");
					}
					isSelected = false;
					selectedId = 0;
					dayAdapter.notifyDataSetChanged();
				}
			}
			
		});
		
	}

	private List<SpecialDay> getSpecialDaysForAdapter(){

		List<SpecialDay> dayList = DBUtil.getAllSpecialDays(mContext);
		
		return dayList;
	}

	public class SpecialDayAdapter extends ArrayAdapter<SpecialDay> {
		
		Calendar now = 	Calendar.getInstance();
		String today = String.valueOf(now.get(Calendar.YEAR)) + String.valueOf(now.get(Calendar.MONTH)+1) + String.valueOf(now.get(Calendar.DAY_OF_MONTH));
		
		public SpecialDayAdapter(List<SpecialDay> dayList) {
			super(SpecialDayList.this, R.layout.day_list_item, dayList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.day_list_item, parent, false);
			}
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView date = (TextView) convertView.findViewById(R.id.date);
			TextView type = (TextView) convertView.findViewById(R.id.type);
			TextView howmuchtime = (TextView) convertView.findViewById(R.id.howmuchtime);
			CheckBox selectCheck = (CheckBox) convertView.findViewById(R.id.dayselectcheck);
			SpecialDay day = getItem(position);
	
			if (day != null) {
				name.setText(DBUtil.getPeopleNameById(mContext, day.getPeople_id()));
				date.setText(getNewFormateDateString(DBUtil.getAlarmDateBaseOnSpecialDay(day)));
				type.setText(DBUtil.getSpecialDayType(mContext, day.getTypeId()));
				long days = DBUtil.GetNumberOfDaysBetweenToDateStrings(today, DBUtil.getAlarmDateBaseOnSpecialDay(day));
				howmuchtime.setText(getHowMuchTimeMsg(days));
				if(selectedId == position && isSelected){
					selectCheck.setChecked(true);
				}else{
					selectCheck.setChecked(false);
				}
			}	
			return convertView;
		}	
		
		private String getHowMuchTimeMsg(long days){
			String dayString = String.valueOf(days);
			String msg = dayString + "days left ";
			return msg;
		}
		
		private String getNewFormateDateString(String date){
			//String msg = date.substring(0, 4)+ " - " + date.substring(4,6) + " - " + date.substring(6);
			return date;
		}
	}
	
	private String getHowMuchTimeMsg(long days){
		String dayString = String.valueOf(days);
		String msg = dayString + "days left ";
		return msg;
	}

	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent main = new Intent(this, MainActivity.class);
			main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(main);
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
