package com.example.mrnice;

import java.util.Calendar;
import java.util.List;
import com.example.mrnice.model.People;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PeopleList extends Activity {
	
	private Context mContext;
	private List<People> peopleList;
	private ListView peopleListView ;
	private Button add;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_list);
		mContext = this;
		peopleListView = (ListView) findViewById(R.id.peoplelist);
		peopleList = getPeopleForAdapter();
		peopleListView.setAdapter(new PeopleAdapter(peopleList));
		
		peopleListView.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent setting = new Intent(mContext, SpecialDaySetting.class);
				setting.putExtra("PeopleID", peopleList.get(arg2).getid());
				startActivity(setting);
				
			}
			
		});
		
		add = (Button) findViewById(R.id.addpeople);
		
		add.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent newpeople = new Intent(mContext, PeopleSettingActivity.class);
				startActivity(newpeople);
			}
			
		});
	}

	private List<People> getPeopleForAdapter(){

		List<People> allPeople = DBUtil.getAllPeople(mContext);
		
		return allPeople;
	}

private class PeopleAdapter extends ArrayAdapter<People> {
		
		public PeopleAdapter(List<People> peopleList) {
			super(mContext, R.layout.day_list_item, peopleList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.people_list_item, parent, false);
			}
			TextView name = (TextView) convertView.findViewById(R.id.peoplename);
			TextView gender = (TextView) convertView.findViewById(R.id.gender);
			People people = getItem(position);
	
			if (people != null) {
				name.setText(people.getFullName());
				gender.setText(people.getGender());
			}	
			return convertView;
		}	
		
	}
}
