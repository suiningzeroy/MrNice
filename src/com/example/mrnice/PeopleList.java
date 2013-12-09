package com.example.mrnice;

import java.util.Calendar;
import java.util.List;
import com.example.mrnice.model.People;

import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PeopleList extends Activity implements Callback{
	
	private Context mContext;
	private List<People> peopleList;
	private ListView peopleListView ;
	private Button add;
	private int selectedPeopleIndex;
	private PeopleAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_list);
		mContext = this;
		peopleListView = (ListView) findViewById(R.id.peoplelist);
		peopleList = getPeopleForAdapter();
		adapter = new PeopleAdapter(peopleList);
		peopleListView.setAdapter(adapter);
		
		peopleListView.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent setting = new Intent(mContext, SpecialDaySetting.class);
				setting.putExtra(MrNiceConstant.PEOPLE_ID, peopleList.get(arg2).getid());
				startActivity(setting);
				
			}
			
		});
		
		peopleListView.setOnItemLongClickListener(new OnItemLongClickListener(){

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {;
				selectedPeopleIndex = arg2;
				showEditDialog();
				return true;
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
	
	private void showEditDialog(){
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle("What to do ");
		dialog.setPositiveButton(getString(R.string.edit),new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent editpeople = new Intent(mContext, PeopleSettingActivity.class);
				editpeople.putExtra(MrNiceConstant.ISEDIT, true);
				editpeople.putExtra(MrNiceConstant.PEOPLE_ID, peopleList.get(selectedPeopleIndex).getid());
				startActivity(editpeople);
			}
		});
		
		dialog.setNegativeButton(getString(R.string.cancel),new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		dialog.setNeutralButton(getString(R.string.delete),new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				People delete = peopleList.get(selectedPeopleIndex);
				//if(DBUtil.getAllPeople(mContext).contains(delete)){
					DBUtil.DeletePeople(mContext, delete);					
				//}
				peopleList.remove(selectedPeopleIndex);
				adapter.notifyDataSetChanged();
			}
		});
		dialog.show();
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

public boolean handleMessage(Message arg0) {
	// TODO Auto-generated method stub
	return false;
}
}
