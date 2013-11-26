package com.example.mrnice.model;

import java.text.ParseException;
import java.util.Date;

import com.example.mrnice.DBUtil;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "SpecialDay")
public class SpecialDay implements Comparable{

	@DatabaseField(generatedId = true)
	private int _id;
	
	@DatabaseField(canBeNull = false)
	private int people_id;
	@DatabaseField(canBeNull = false)
	private int type_id ;
	
	@DatabaseField(canBeNull = true)
	private int cycle ;
	
	@DatabaseField(canBeNull = false)
	private long when ;
	
	@DatabaseField(canBeNull = true)
	private String year ;
	
	@DatabaseField(canBeNull = false)
	private String month ;
	
	@DatabaseField(canBeNull = false)
	private String day ;
	
	@DatabaseField(canBeNull = false)
	private long ceate_date;

	public SpecialDay(){}
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getPeople_id() {
		return people_id;
	}

	public void setPeople_id(int people_id) {
		this.people_id = people_id;
	}

	public int getTypeId() {
		return type_id;
	}

	public void setTypeId(int type_id) {
		this.type_id = type_id;
	}

	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public long getWhen() {
		return when;
	}

	public void setWhen(long when) {
		this.when = when;
	}

	public long getCeate_date() {
		return ceate_date;
	}

	public void setCeate_date(long ceate_date) {
		this.ceate_date = ceate_date;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	
	public int compareTo(Object another) {
		String thisday = DBUtil.getAlarmDayBaseOnSpecialDay(this);
		String antoherday = DBUtil.getAlarmDayBaseOnSpecialDay((SpecialDay)another);
		Date day1 = null,day2 = null;
		try {
			day1 = DBUtil.df.parse(thisday);
			day2 = DBUtil.df.parse(antoherday);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(day1 != null && day2 != null){
			return day1.compareTo(day2);
		}
		return 0;
	}

	
}
