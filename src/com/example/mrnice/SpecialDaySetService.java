package com.example.mrnice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.example.mrnice.model.SpecialDay;
import com.j256.ormlite.dao.Dao;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class SpecialDaySetService extends IntentService {
	private static long ONE_DAY_IN_MILL_SECOND = 24 * 60 * 60 * 1000;
	private String LOGGING_TAG = "SpecialDaySetService Intent service";
	private static final String LOCK_TAG = "SpecialDaySetService";
	
	private static PowerManager.WakeLock wakeLock = null;
	private MrNiceApp app;
	private Context mContext;
	private static DateFormat df = new SimpleDateFormat("yyyyMMdd");
	
	
	public SpecialDaySetService() {
		super("SpecialDaySetService");
	}
	
	public static synchronized void acquireLock(Context ctx){
		if (wakeLock == null){
			PowerManager pMgr = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
			wakeLock = pMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_TAG);
			wakeLock.setReferenceCounted(false);
		}
		wakeLock.acquire();
	}
	
	public static synchronized void releaseLock(){
		if (wakeLock != null){
			wakeLock.release();
		}
	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		app =(MrNiceApp) getApplication();
		mContext = this;
		Log.d(LOGGING_TAG, LOGGING_TAG + "onCreate");
	}
	
	@Override  
	public void onStart(Intent intent, int startId) {  
		Log.d(LOGGING_TAG, LOGGING_TAG + "onStart");  
		acquireLock(this);
		super.onStart(intent, startId);  
	}  
	

	@Override  
	public int onStartCommand(Intent intent, int flags, int startId) {  
		Log.d(LOGGING_TAG, LOGGING_TAG + "onStartCommand");  
		return super.onStartCommand(intent, flags, startId);  
	}  
	
	@Override  
	public void setIntentRedelivery(boolean enabled) {  
		super.setIntentRedelivery(enabled);  
		Log.d(LOGGING_TAG, LOGGING_TAG + "setIntentRedelivery");  
	}  

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(LOGGING_TAG, LOGGING_TAG + "onHandleIntent");  
		int spdIdInIntent = intent.getIntExtra("remind", 99999);
		if(spdIdInIntent == 99999){
			checkAlarmPointsListForNotification(mContext);
		}else{
			SpecialDay remindSpd = DBUtil.getSpecialDayById(mContext, spdIdInIntent);
			createSpecialDayNotification(remindSpd,"happy day! remind agian!");
		}

	}
	
	@Override  
	public void onDestroy() {  
		Log.d(LOGGING_TAG, LOGGING_TAG + "onDestroy");  
		releaseLock();
		super.onDestroy();  
	}
	
	public void checkAlarmPointsListForNotification(Context context){
		Dao<SpecialDay, Integer> specialDayDao = null;
		Log.d(LOGGING_TAG,  "checkAlarmPointsListForNotification");  
		try {
			specialDayDao = DBUtil.getOrmLiteHelper(context).getSpecialDayDao();
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
		Log.d(LOGGING_TAG,  "start iterate specialdays");  
		for(SpecialDay spd : specialDayDao){
			Calendar now = 	Calendar.getInstance();
			String today;
			if(Integer.toString((now.get(Calendar.MONTH)+1)).length()==1){
				today = String.valueOf(now.get(Calendar.YEAR)) + "0" + String.valueOf(now.get(Calendar.MONTH)+1) + String.valueOf(now.get(Calendar.DAY_OF_MONTH));
			}
			today = String.valueOf(now.get(Calendar.YEAR)) + String.valueOf(now.get(Calendar.MONTH)+1) + String.valueOf(now.get(Calendar.DAY_OF_MONTH));
			long days = DBUtil.getDaySub(today, DBUtil.getAlarmDayBaseOnSpecialDay(spd));
			Log.d(LOGGING_TAG, "today is :" + today + "specialday is " + spd.getYear()+spd.getMonth()+spd.getDay()+
					". alarmday is: " + DBUtil.getAlarmDayBaseOnSpecialDay(spd) + ". "
					+ String.valueOf(days) + "days left .");  
			if(days >= 0){
				if(days == 0){
					makeNotificationForSpecialDay(days,spd);
				}else{
					if(days == 1){
						makeNotificationForSpecialDay(days,spd);
					}
				}
			}
		}
	}
	
	public void makeNotificationForSpecialDay(long days, SpecialDay spd) {
		String notice;
		if(days == 1){
			notice = "happy tomorrow! ";
		}else{
			notice = "happy today!";
		}
		
		createSpecialDayNotification(spd, notice);
		
	}
	
	public void createSpecialDayNotification(SpecialDay spd, String notice) {
		NotificationManager nmgr = (NotificationManager) 
				getSystemService(Context.NOTIFICATION_SERVICE);
		String shortMsg = makeNotificationMessage(notice, spd);
		long time = System.currentTimeMillis();
		NotificationCompat.Builder builder = 
				new NotificationCompat.Builder(mContext)
				.setSmallIcon(R.drawable.fligh_icon)
				.setWhen(time)
				.setContentTitle(shortMsg)
				.setContentText("this is a test notification!")
				.setAutoCancel(true);
		Intent specialDayIntent = new Intent(mContext, SpecialDayNotificationGuidActivity.class);
		specialDayIntent.putExtra("dayId",spd.get_id());
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, specialDayIntent , 0);
		builder.setContentIntent(contentIntent);
		nmgr.notify(spd.get_id(), builder.build());

	}
	
	public String makeNotificationMessage(String notice , SpecialDay spd){
		String result = "";
		String peopleName = DBUtil.getPeopleNameById(mContext,spd.getPeople_id());
		result = "Hi, " + peopleName + "," + notice;
		
		return result;
	}
	
	// date1 later than date2 then return 1, equal return 0, otherwise return -1
	public int compare_date(String date1, String date2) {
		
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

}
