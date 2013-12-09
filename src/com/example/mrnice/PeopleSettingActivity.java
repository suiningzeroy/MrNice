package com.example.mrnice;


import java.util.Date;

import com.example.mrnice.model.People;
import com.example.mrnice.model.SpecialDay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class PeopleSettingActivity extends Activity {
	
	private People people;
	private CheckBox male;
	private CheckBox female;
	private Button next;
	private EditText age,firstName,lastName;
	private Context mContext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		age = (EditText) findViewById(R.id.age);
		firstName = (EditText) findViewById(R.id.firstname);
		lastName = (EditText) findViewById(R.id.lastname);
		next = (Button) findViewById(R.id.next);
		male = (CheckBox) findViewById(R.id.malecheck);
		female = (CheckBox) findViewById(R.id.femalecheck);
		mContext = PeopleSettingActivity.this;
		
		people = new People();
		
		initialUI();
		
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
		
		next.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(age.getText().toString().length() > 0){
					people.setAge(Integer.valueOf(age.getText().toString()));
				}
				Date createdate = new Date();
				people.setCreateDate(createdate.getTime());
				if(firstName.getText().toString().length()  > 0 && lastName.getText().toString().length() > 0){
					people.setFirstName(firstName.getText().toString());
					people.setLastName(lastName.getText().toString());
				}
				if(isDataPrepare(people)){
					DBUtil.addPeople(PeopleSettingActivity.this, people);
					int peopleID = DBUtil.findPeopleID(PeopleSettingActivity.this, people);
					if(peopleID == MrNiceConstant.ERROR_CODE){
						errorDisplay("people ID not found!");
					}
					
					Intent setting = new Intent(PeopleSettingActivity.this, SpecialDaySetting.class);
					setting.putExtra(MrNiceConstant.PEOPLE_ID, peopleID);
					startActivity(setting);
				}
			}
			
		});
	}
	
	private void initialUI(){
		if(getIntent().getBooleanExtra(MrNiceConstant.ISEDIT, false)){
			int peopleId = getIntent().getIntExtra(MrNiceConstant.PEOPLE_ID, MrNiceConstant.ERROR_CODE);
			if(peopleId != MrNiceConstant.ERROR_CODE)
			people = DBUtil.getPeopleById(mContext, peopleId);
			male.setChecked(people.getGender().equals("male")?true:false);
			DBUtil.toastShow(mContext,people.getGender());
			female.setChecked(people.getGender().equals("female")?true:false);			
			firstName.setText(people.getFirstName());
			lastName.setText(people.getLastName());
			age.setText(String.valueOf(people.getAge()));
			next.setText("Add Special Day");
		}
		else{
			people.setGender("male");
		}
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
