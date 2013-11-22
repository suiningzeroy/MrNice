package com.example.mrnice.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "Pepole")
public class People {
	@DatabaseField(generatedId = true)
	private int _id;
	@DatabaseField(canBeNull = false)
	private String first_name;
	@DatabaseField(canBeNull = false)
	private String last_name;
	@DatabaseField(canBeNull = true)
	private String middle_name;
	@DatabaseField(canBeNull = true)
	private String gender;
	
	@DatabaseField(canBeNull = false)
	private int relation_id;
	
	@DatabaseField(canBeNull = true)
	private int age;
	
	@DatabaseField(canBeNull = false)
	private long create_date;
	
	public People() {}
	
	public String getFirstName(){
		return first_name;
	}
	
	public void setFirstName(String firstname){
		this.first_name = firstname;
	}
	
	
	public String getLastName(){
		return last_name;
	}
	
	public void setLastName(String lastname){
		this.last_name = lastname;
	}
	
	public String getMiddleName(){
		return middle_name;
	}
	
	public void setMiddleName(String middlename){
		this.middle_name = middlename;
	}
	
	public int getRelationId(){
		return relation_id;
	}
	
	public void setRelationId(int relation_id){
		this.relation_id = relation_id;
	}
	
	public long getCreateDate(){
		return create_date;
	}
	
	public void setCreateDate(long create_date){
		this.create_date = create_date;
	}
	
	public int getAge(){
		return age;
	}

	public void setAge(int age){
		this.age = age;
	}

	public int getid() {
		return _id;
	}

	public void setid(int _id) {
		this._id = _id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}
