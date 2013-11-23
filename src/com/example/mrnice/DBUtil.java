package com.example.mrnice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;

import com.example.mrnice.model.*;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;

public class DBUtil {
	
public static final long ONE_DAY = 24 * 60 * 60 * 1000;
	
	private static DateFormat df = new SimpleDateFormat("yyyyMMdd");
	private static String LOGGING_TAG = "MrNiceUtil";
	private static DBOrmLiteHelper ormLiteHelper;
	private static GenericRawResults<String[]> contacts = null;
	
	public static DBOrmLiteHelper getOrmLiteHelper(Context context) {
		if (ormLiteHelper == null) {
			ormLiteHelper = OpenHelperManager.getHelper(context, DBOrmLiteHelper.class);
			Log.i("getOrmLiteHelper", ormLiteHelper.getReadableDatabase().getPath());
			Log.d(LOGGING_TAG, "getOrmLiteHelper success!");
		}
		return ormLiteHelper;
		
	}
	
	public static void releaseOrmLiteHelper() {
		if (ormLiteHelper != null) {
			OpenHelperManager.releaseHelper();
			ormLiteHelper = null;
			Log.d(LOGGING_TAG, "releaseOrmLiteHelper success!");
		}
	}
	
	public static void clearSpecialDayTable(Context context) {
		Log.d(LOGGING_TAG, "clearSpecialDayTable  ");
		try {
			getOrmLiteHelper(context).getSpecialDayDao().delete(getOrmLiteHelper(context).getSpecialDayDao().queryForAll());
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addSpecialDay(Context context, SpecialDay spd){
		Log.d(LOGGING_TAG, "add a special day");
		try {
			getOrmLiteHelper(context).getSpecialDayDao().create(spd);
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void DeleteSpecialDay(Context context, SpecialDay spd){
		Log.d(LOGGING_TAG, "delete a special day");
		try {
			getOrmLiteHelper(context).getSpecialDayDao().delete(spd);
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void addPeople(Context context, People people){
		Log.d(LOGGING_TAG, "add a man/women");
		try {
			getOrmLiteHelper(context).getPeopleDao().create(people);
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static int findPeopleID(Context context, People people){
		Log.d(LOGGING_TAG, "get people id");
		List<People> result = null;

		try {
			result = getOrmLiteHelper(context).getPeopleDao().queryForMatching(people);
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
		
		if(result == null){
			return 99999;
		}else{
			return result.get(0).getid();
		}
	}
	
	public static void DeletePeople(Context context, People people){
		Log.d(LOGGING_TAG, "delete a man/women");
		try {
			getOrmLiteHelper(context).getPeopleDao().delete(people);
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addTypeOfDay(Context context, TypeOfDay type){
		Log.d(LOGGING_TAG, "add a type of special day");
		try {
			getOrmLiteHelper(context).getTypeOfDayDao().create(type);
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void DeleteTypeOfDay(Context context, TypeOfDay type){
		Log.d(LOGGING_TAG, "delete a type of special day");
		try {
			getOrmLiteHelper(context).getTypeOfDayDao().delete(type);
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<SpecialDay> getAllSpecialDays(Context context){
		List<SpecialDay> result = new ArrayList<SpecialDay>();
		try {
			result = getOrmLiteHelper(context).getSpecialDayDao().queryForAll();
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<People> getAllPeople(Context context){
		List<People> result = new ArrayList<People>();
		try {
			result = getOrmLiteHelper(context).getPeopleDao().queryForAll();
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<TypeOfDay> getAllTypeOfDays(Context context){
		List<TypeOfDay> result = new ArrayList<TypeOfDay>();
		try {
			result = getOrmLiteHelper(context).getTypeOfDayDao().queryForAll();
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static int getCountsOfAllSpecialDays(Context context){
		int result = 0;
		try {
			result = getOrmLiteHelper(context).getSpecialDayDao().queryForAll().size();
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getSpecialDayType(Context context,int _id){
		TypeOfDay type = null ;	
		try {
			type = getOrmLiteHelper(context).getTypeOfDayDao().queryForId(_id);
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
		if(type == null){
			return "no name found";
		}else{
			return type.getName();
		}
	}

	public static String getPeopleNameById(Context context,int _id){
		People people = null ;	
		try {
			people = getOrmLiteHelper(context).getPeopleDao().queryForId(_id);
		}catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
		return people.getFirstName() + "." + people.getLastName();
	}
	
	public static String getAlarmDayBaseOnSpecialDay(SpecialDay spd) {
		String alarmDay = "";
		String orignDate = spd.getYear() + spd.getMonth() + spd.getDay();
		switch (spd.getCycle()){
			case 1:
				alarmDay = orignDate;
				break;
			case 8:
				Calendar now = 	Calendar.getInstance();
				Calendar cal = Calendar.getInstance();
				String alarmDayInThisYear = String.valueOf(cal.get(Calendar.YEAR)) + spd.getMonth() + spd.getDay();
				
			Date alarmDateInThisYear = null;
			try {
				alarmDateInThisYear = df.parse(alarmDayInThisYear);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				cal.setTime(alarmDateInThisYear); 
				if(cal.after(now)){
					alarmDay = alarmDayInThisYear;
				}else{
					alarmDay = String.valueOf(cal.get(Calendar.YEAR) + 1) + spd.getMonth() + spd.getDay();
				}
				
				break;
			case 2:
				break;
		}
		
		return alarmDay;
	}
	
	public static long getDaySub(String beginDateStr,String endDateStr)
	{
		long ONE_DAY_IN_MILL_SECOND = 24 * 60 * 60 * 1000;
		long day=0;
		Date beginDate;
		Date endDate;
		try
		{
			beginDate = df.parse(beginDateStr);
			endDate= df.parse(endDateStr);   
			day= (endDate.getTime()-beginDate.getTime())/ONE_DAY_IN_MILL_SECOND;   
		} catch (ParseException e)
		{
			e.printStackTrace();
		}  
		return day;
	}
	
	public static void toastShow(Context ctx,String text) {  
		Toast.makeText(ctx, text, 1000).show();  
	}

}
