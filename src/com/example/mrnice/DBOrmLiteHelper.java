package com.example.mrnice;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mrnice.model.People;
import com.example.mrnice.model.SpecialDay;
import com.example.mrnice.model.TypeOfDay;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBOrmLiteHelper  extends OrmLiteSqliteOpenHelper {
	
	private Dao<People, Integer> peopleDao = null;
	private Dao<SpecialDay, Integer> specialDayDao = null;
	private Dao<TypeOfDay, Integer> typeOfDayDao = null;
	private RuntimeExceptionDao<People, Integer> peopleRuntimeDao = null;
	
	public static final String DB_NAME = "MrNice";
	public static final int DB_VERSION = 1;
	
	public DBOrmLiteHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			Log.i(People.class.getName(), "onCreating Bone db");
			TableUtils.createTable(connectionSource, People.class);
			TableUtils.createTable(connectionSource, SpecialDay.class);
			TableUtils.createTable(connectionSource, TypeOfDay.class);
		} catch (SQLException e) {
			Log.e(DBOrmLiteHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		try {
			Log.i(DBOrmLiteHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, People.class, true);
			TableUtils.dropTable(connectionSource, SpecialDay.class, true);
			TableUtils.dropTable(connectionSource, TypeOfDay.class, true);

			onCreate(arg0, connectionSource);
		} catch (SQLException e) {
			Log.e(DBOrmLiteHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
		
	}
	
	public Dao<People, Integer> getPeopleDao() throws SQLException {
		if (peopleDao == null) {
			peopleDao = getDao(People.class);
		}
		return peopleDao;
	}
	
	public Dao<SpecialDay, Integer> getSpecialDayDao() throws SQLException {
		if (specialDayDao == null) {
			specialDayDao = getDao(SpecialDay.class);
		}
		return specialDayDao;
	}
	
	public Dao<TypeOfDay, Integer> getTypeOfDayDao() throws SQLException {
		if (typeOfDayDao == null) {
			typeOfDayDao = getDao(TypeOfDay.class);
		}
		return typeOfDayDao;
	}
	
	public RuntimeExceptionDao<People, Integer> getSimpleDataDao() {
		if (peopleRuntimeDao == null) {
			peopleRuntimeDao = getRuntimeExceptionDao(People.class);
		}
		return peopleRuntimeDao;
	}

	/**
	* Close the database connections and clear any cached DAOs.
	*/
	@Override
	public void close() {			
		super.close();
		peopleRuntimeDao = null;
	}

}
