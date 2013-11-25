package com.example.mrnice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.example.mrnice.model.SpecialDay;
import com.example.mrnice.model.TypeOfDay;
import com.j256.ormlite.field.DatabaseField;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SpecialDaySetting extends Activity implements Handler.Callback{
	private final static String STATE = "state";
	private final static int SET = 888;
	
	private int cycle;
	private Handler handler;
	private SpecialDay spd;
	private List<TypeOfDay> typeList;
	private CheckBox once;
	private CheckBox annually;
	private Button selectDate,another,done ,selecttype;
	private Context mContext;
	private Spinner typeSpinner;
	private TextView selecteddate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_day);
		typeSpinner = (Spinner) findViewById(R.id.typespinner);
		once = (CheckBox) findViewById(R.id.oncecheckbox);
		annually = (CheckBox) findViewById(R.id.peryearcheckbox);
		selectDate = (Button) findViewById(R.id.datesettingbutton);
		mContext = this;
		typeList = new ArrayList<TypeOfDay>();
		handler = new Handler(this);
		selecteddate = (TextView) findViewById(R.id.selecteddate);
		another = (Button) findViewById(R.id.another);
		done = (Button) findViewById(R.id.done);
		selecttype = (Button) findViewById(R.id.selecttype);
		
		
		typeList = DBUtil.getAllTypeOfDays(this);
		ArrayAdapter<TypeOfDay> typeAdapter = new ArrayAdapter<TypeOfDay>(this,android.R.layout.simple_spinner_item,typeList);
		//typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(typeAdapter);	
		
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(SpecialDaySetting.this, "ID:"+((TypeOfDay)typeSpinner.getSelectedItem()).get_id()+",position:" +arg2, Toast.LENGTH_SHORT).show();
				int typeID = ((TypeOfDay)typeSpinner.getSelectedItem()).get_id();
				spd.setTypeId(typeID);
				
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
			
		});
		
		initialUI();
		
		once.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(cycle == 1){
					cycle = 0;
				}else{
					cycle = 1;
				}
				annually.setChecked(false);
				DBUtil.toastShow(mContext,"cycle = " + String.valueOf(cycle));
			}
			
		});
		
		annually.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(cycle == 8){
					cycle = 0;
				}else{
					cycle = 8;
				}
				once.setChecked(false);
				DBUtil.toastShow(mContext,"cycle = " + String.valueOf(cycle));
			}
			
		});
		
		selectDate.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				showDatePickerDialog(v);
			}
			
		});
		
		another.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(isDataPrepare(spd)){
					DBUtil.addSpecialDay(SpecialDaySetting.this, spd);
					initialUI();
				}
			}
			
		});
		
		done.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(isDataPrepare(spd)){
					DBUtil.addSpecialDay(SpecialDaySetting.this, spd);
					Intent daylist = new Intent(SpecialDaySetting.this, SpecialDayList.class);
					startActivity(daylist);
				}
			}
			
		});
		
		selecttype.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				typeSpinner.setVisibility(View.VISIBLE);
				spd.setTypeId(typeList.get(0).get_id());
			}
			
		});
		
	}
	
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}
	
	private boolean isDataPrepare(SpecialDay spd ){
		if(spd.getCycle() == 0 ){
			DBUtil.toastShow(this,"please select cycle!");
			return false;
		}
		
		if(spd.getTypeId()== 0){
			DBUtil.toastShow(this,"please select day type first!");
			return false;
		}
		if(spd.getMonth() == null && spd.getDay() == null){
			DBUtil.toastShow(this,"please select date first!");
			return false;
		}
		
		return true;
	}
	
	private void initialUI(){
		if(getIntent().getIntExtra("PeopleID", 99999) !=  999999){
			spd = new SpecialDay();
			once.setChecked(false);
			annually.setChecked(true);
			spd.setPeople_id(getIntent().getIntExtra("PeopleID", 99999));
			spd.setCycle(8);}
		
		if(getIntent().getBooleanExtra("isedit", false)){
			spd = new SpecialDay();
			spd = DBUtil.getSpecialDayById(mContext,getIntent().getIntExtra("editday", 99999));
			
			if(spd == null){
				
			}else{
					once.setChecked(spd.getCycle()==1? true :false);
					annually.setChecked(spd.getCycle()==8? true :false);
					int po = typeList.indexOf(DBUtil.getDayTypeById(mContext,spd.getTypeId()));
					typeSpinner.setVisibility(View.VISIBLE);
					typeSpinner.setSelection(po,true);
					selecttype.setEnabled(false);
					selecteddate.setText("selected date is : "+ spd.getYear()+spd.getMonth()+spd.getDay());
					selectDate.setText("change date");
					another.setVisibility(View.INVISIBLE);
			}
		}

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
			
			spd.setYear(Integer.toString(year));
			if (Integer.toString(monthOfYear + 1).length() == 1){
				String month = "0" + Integer.toString(monthOfYear+1);
				DBUtil.toastShow(mContext,"fix month = " + month);
				spd.setMonth(month);
			}else{
				spd.setMonth(Integer.toString(monthOfYear+1));
			}
			
			if(Integer.toString(dayOfMonth).length() == 1){
				String day = "0"+Integer.toString(dayOfMonth);
				spd.setDay(day);
			}else{
				spd.setDay(Integer.toString(dayOfMonth));
			}
			sendMessage(SET);
					
		}

	}
	

	public boolean handleMessage(Message msg) {
		int state = msg.getData().getInt(STATE); 
		switch(state){
			case SET:
				
				Toast.makeText(getApplicationContext(), 
						"date selected", 
						Toast.LENGTH_LONG) 
						.show(); 
		}
		return false;
	}

}
