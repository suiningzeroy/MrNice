package com.example.mrnice;


import java.util.Date;

import com.example.mrnice.model.People;
import com.example.mrnice.model.SpecialDay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class PeopleSettingActivity extends Activity {
	
	private People people;
	private CheckBox male;
	private CheckBox female;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		final EditText age = (EditText) findViewById(R.id.age);
		final EditText fisrName = (EditText) findViewById(R.id.firstname);
		final EditText lastName = (EditText) findViewById(R.id.lastname);
		people = new People();
		people.setGender("male");
		
		male = (CheckBox) findViewById(R.id.malecheck);
		female = (CheckBox) findViewById(R.id.femalecheck);
		
		male.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				people.setGender("male");
				female.setChecked(false);
			}
			
		});
		
		female.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				people.setGender("female");
				male.setChecked(false);
			}
			
		});
		
		findViewById(R.id.next).setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(age.getText().toString().length() > 0){
					people.setAge(Integer.valueOf(age.getText().toString()));
				}
				Date createdate = new Date();
				people.setCreateDate(createdate.getTime());
				if(fisrName.getText().toString().length()  > 0 && lastName.getText().toString().length() > 0){
					people.setFirstName(fisrName.getText().toString());
					people.setLastName(lastName.getText().toString());
				}
				if(isDataPrepare(people)){
					DBUtil.addPeople(PeopleSettingActivity.this, people);
					int peopleID = DBUtil.findPeopleID(PeopleSettingActivity.this, people);
					if(peopleID == 99999){
						errorDisplay("people ID not found!");
					}
					
					Intent setting = new Intent(PeopleSettingActivity.this, SpecialDaySetting.class);
					setting.putExtra("PeopleID", peopleID);
					startActivity(setting);
				}
			}
			
		});
	}
	
	private void errorDisplay(String msg){
		Toast.makeText(this, "error " + msg + " happened anywhere! ", Toast.LENGTH_SHORT).show();
	}
	
	private boolean isDataPrepare(People pp ){
		
		if(pp.getFirstName() == null && pp.getLastName() == null){
			DBUtil.toastShow(this,"please enter full name!");
			return false;
		}
		
		return true;
	}

}
