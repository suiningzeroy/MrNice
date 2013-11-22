package com.example.mrnice.model;

import java.util.List;

import android.view.Menu;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "TypeOfDay")
public class TypeOfDay {

	@DatabaseField(generatedId = true)
	private int _id;	
	@DatabaseField(canBeNull = false)
	private String name ;
	
	@DatabaseField(canBeNull = false)
	private String uri ;
	
	@DatabaseField(canBeNull = false)
	private long update_date;

	
	public TypeOfDay(){}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public long getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(long update_date) {
		this.update_date = update_date;
	}
	

	@Override
	public String toString() {
		return this.name;
	}
}
