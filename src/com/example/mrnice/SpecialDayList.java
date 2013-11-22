package com.example.mrnice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.mrnice.model.SpecialDay;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SpecialDayList extends Activity {
	private Context mContext;
	private List<SpecialDay> dayList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_special_day_list);
		mContext = SpecialDayList.this;
		ListView dayListView = (ListView) findViewById(R.id.daylistview);
		dayList = getSpecialDaysForAdapter();
		dayListView.setAdapter(new SpecialDayAdapter(dayList));
	}

	private List<SpecialDay> getSpecialDaysForAdapter(){

		List<SpecialDay> dayList = DBUtil.getAllSpecialDays(mContext);
		
		return dayList;
	}

	private class SpecialDayAdapter extends ArrayAdapter<SpecialDay> {
		
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
			SpecialDay day = getItem(position);
	
			if (day != null) {
				name.setText(DBUtil.getPeopleNameById(mContext, day.getPeople_id()));
				date.setText(getNewFormateDateString(DBUtil.getAlarmDayBaseOnSpecialDay(day)));
				type.setText(DBUtil.getSpecialDayType(mContext, day.getTypeId()));
				long days = DBUtil.getDaySub(today, DBUtil.getAlarmDayBaseOnSpecialDay(day));
				howmuchtime.setText(getHowMuchTimeMsg(days));
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
	

}
